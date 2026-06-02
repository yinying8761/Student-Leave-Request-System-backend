package com.leave.mapper;

import com.leave.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    User findById(@Param("id") Long id);
    User findByUsername(@Param("username") String username);
    List<User> findAll();
    List<User> findByRole(@Param("role") String role);
    int insert(User user);
    int update(User user);
    int deleteById(@Param("id") Long id);
}
