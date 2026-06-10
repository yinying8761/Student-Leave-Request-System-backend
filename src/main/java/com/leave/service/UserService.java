package com.leave.service;

import com.leave.dto.LoginRequest;
import com.leave.dto.PasswordChangeRequest;
import com.leave.dto.ProfileUpdateRequest;
import com.leave.dto.UserCreateRequest;
import com.leave.dto.UserVO;
import com.leave.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String, Object> login(LoginRequest request);
    UserVO getCurrentUser(Long userId);
    UserVO createUser(UserCreateRequest request);
    UserVO updateUser(Long id, UserCreateRequest request);
    void deleteUser(Long id);
    java.util.List<UserVO> listUsers();
    User findById(Long id);
    UserVO updateProfile(Long userId, ProfileUpdateRequest request);
    void changePassword(Long userId, PasswordChangeRequest request);
    List<UserVO> listCounselors();
}
