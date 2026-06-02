package com.leave.service;

import com.leave.dto.LoginRequest;
import com.leave.dto.UserCreateRequest;
import com.leave.dto.UserVO;
import com.leave.entity.User;

import java.util.Map;

public interface UserService {
    Map<String, Object> login(LoginRequest request);
    UserVO getCurrentUser(Long userId);
    UserVO createUser(UserCreateRequest request);
    UserVO updateUser(Long id, UserCreateRequest request);
    void deleteUser(Long id);
    java.util.List<UserVO> listUsers();
    User findById(Long id);
}
