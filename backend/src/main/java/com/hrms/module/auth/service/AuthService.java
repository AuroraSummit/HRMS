package com.hrms.module.auth.service;

import com.hrms.module.auth.dto.LoginDto;
import com.hrms.module.auth.dto.LoginResult;
import com.hrms.module.auth.dto.RegisterDto;
import com.hrms.module.auth.entity.User;

public interface AuthService {
    LoginResult login(LoginDto loginDto);
    User register(RegisterDto registerDto);
    LoginResult refreshToken(String refreshToken);
    void logout();
    void changePassword(Long userId, String oldPassword, String newPassword);
}
