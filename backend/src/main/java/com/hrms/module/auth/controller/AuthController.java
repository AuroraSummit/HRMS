package com.hrms.module.auth.controller;

import com.hrms.common.result.Result;
import com.hrms.module.auth.dao.UserDao;
import com.hrms.module.auth.dto.LoginDto;
import com.hrms.module.auth.dto.LoginResult;
import com.hrms.module.auth.dto.RegisterDto;
import com.hrms.module.auth.entity.User;
import com.hrms.module.auth.service.AuthService;
import com.hrms.module.system.entity.Role;
import com.hrms.module.system.service.RoleService;
import com.hrms.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/api/v1/hr/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserDao userDao;
    private final RoleService roleService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<LoginResult> login(@Valid @RequestBody LoginDto loginDto) {
        return Result.success(authService.login(loginDto));
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<User> register(@Valid @RequestBody RegisterDto registerDto) {
        return Result.success(authService.register(registerDto));
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新令牌")
    public Result<LoginResult> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        return Result.success(authService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public Result<Void> logout() {
        authService.logout();
        return Result.success();
    }

    @GetMapping("/userinfo")
    @Operation(summary = "获取当前用户信息")
    public Result<Map<String, Object>> userinfo() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return Result.error("未登录");
        }
        User user = userDao.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setPassword(null);
        Map<String, Object> info = new HashMap<>();
        info.put("id", user.getId());
        info.put("username", user.getUsername());
        info.put("realName", user.getRealName());
        info.put("email", user.getEmail());
        info.put("phone", user.getPhone());
        info.put("avatar", user.getAvatar());
        info.put("empId", user.getEmpId());

        // Look up role codes
        List<Long> roleIds = roleService.getRoleIdsByUserId(userId);
        List<Role> roles = roleIds.isEmpty() ? List.of() : roleService.listByIds(roleIds);
        List<String> roleCodes = roles.stream().map(Role::getCode).collect(Collectors.toList());
        info.put("roles", roleCodes);

        // Determine permissions based on roles
        boolean isAdmin = roleCodes.contains("admin");
        if (isAdmin) {
            info.put("permissions", List.of("*:*:*"));
        } else {
            info.put("permissions", List.of());
        }

        return Result.success(info);
    }
}
