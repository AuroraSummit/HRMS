package com.hrms.module.auth.service.impl;

import com.hrms.common.util.JwtUtil;
import com.hrms.module.auth.dao.UserDao;
import com.hrms.module.auth.dto.LoginDto;
import com.hrms.module.auth.dto.LoginResult;
import com.hrms.module.auth.dto.RegisterDto;
import com.hrms.module.auth.entity.User;
import com.hrms.module.auth.service.AuthService;
import com.hrms.common.exception.BusinessException;
import com.hrms.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    /** In-memory refresh token store (userId -> refreshToken) */
    private final ConcurrentHashMap<Long, String> refreshTokenStore = new ConcurrentHashMap<>();

    @Override
    public LoginResult login(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );

        User user = userDao.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                        .eq(User::getUsername, loginDto.getUsername())
        );

        if (user == null || user.getStatus() != 0) {
            throw new BusinessException("用户名或密码错误");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        // Store refresh token in memory
        refreshTokenStore.put(user.getId(), refreshToken);

        return LoginResult.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(7200)
                .build();
    }

    @Override
    @Transactional
    public User register(RegisterDto registerDto) {
        Long count = userDao.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                        .eq(User::getUsername, registerDto.getUsername())
        );
        if (count != null && count > 0) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRealName(registerDto.getRealName());
        user.setEmail(registerDto.getEmail());
        user.setStatus(0);
        user.setType(1);
        userDao.insert(user);

        user.setPassword(null);
        return user;
    }

    @Override
    public LoginResult refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
            throw new BusinessException("刷新令牌无效");
        }

        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        String storedToken = refreshTokenStore.get(userId);

        if (storedToken == null || !storedToken.equals(refreshToken)) {
            throw new BusinessException("刷新令牌已过期");
        }

        User user = userDao.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        String newAccessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername());
        return LoginResult.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(7200)
                .build();
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userDao.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("当前密码不正确");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.updateById(user);
    }

    @Override
    public void logout() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId != null) {
            refreshTokenStore.remove(userId);
        }
    }
}
