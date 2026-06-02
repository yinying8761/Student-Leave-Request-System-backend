package com.leave.service.impl;

import com.leave.common.BusinessException;
import com.leave.common.JwtUtils;
import com.leave.dto.LoginRequest;
import com.leave.dto.UserCreateRequest;
import com.leave.dto.UserVO;
import com.leave.entity.User;
import com.leave.mapper.UserMapper;
import com.leave.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Map<String, Object> login(LoginRequest request) {
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null || user.getEnabled() == 0) {
            throw new BusinessException("用户名或密码错误");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", UserVO.fromUser(user));
        return result;
    }

    @Override
    public UserVO getCurrentUser(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) throw new BusinessException("用户不存在");
        return UserVO.fromUser(user);
    }

    @Override
    public UserVO createUser(UserCreateRequest request) {
        if (request.getPassword() == null || request.getPassword().isBlank())
            throw new BusinessException("密码不能为空");
        User exist = userMapper.findByUsername(request.getUsername());
        if (exist != null) throw new BusinessException("用户名已存在");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setRole(request.getRole());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setDepartment(request.getDepartment());
        user.setClassName(request.getClassName());
        user.setAdvisorId(request.getAdvisorId());
        user.setCounselorId(request.getCounselorId());
        userMapper.insert(user);
        return UserVO.fromUser(user);
    }

    @Override
    public UserVO updateUser(Long id, UserCreateRequest request) {
        User user = userMapper.findById(id);
        if (user == null) throw new BusinessException("用户不存在");

        user.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setRealName(request.getRealName());
        user.setRole(request.getRole());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setDepartment(request.getDepartment());
        user.setClassName(request.getClassName());
        user.setAdvisorId(request.getAdvisorId());
        user.setCounselorId(request.getCounselorId());
        userMapper.update(user);
        return UserVO.fromUser(userMapper.findById(id));
    }

    @Override
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public List<UserVO> listUsers() {
        return userMapper.findAll().stream()
                .map(UserVO::fromUser)
                .collect(Collectors.toList());
    }

    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }
}
