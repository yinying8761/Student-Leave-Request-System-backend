package com.leave.controller;

import com.leave.common.Result;
import com.leave.dto.PasswordChangeRequest;
import com.leave.dto.ProfileUpdateRequest;
import com.leave.dto.UserCreateRequest;
import com.leave.entity.User;
import com.leave.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Result<?> list() {
        return Result.ok(userService.listUsers());
    }

    @GetMapping("/{id}")
    public Result<?> get(@PathVariable Long id) {
        return Result.ok(userService.getCurrentUser(id));
    }

    @PostMapping
    public Result<?> create(@Valid @RequestBody UserCreateRequest request) {
        return Result.ok(userService.createUser(request));
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @Valid @RequestBody UserCreateRequest request) {
        return Result.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.ok();
    }

    @PutMapping("/profile")
    public Result<?> updateProfile(@Valid @RequestBody ProfileUpdateRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.ok(userService.updateProfile(user.getId(), request));
    }

    @PutMapping("/password")
    public Result<?> changePassword(@Valid @RequestBody PasswordChangeRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.changePassword(user.getId(), request);
        return Result.ok();
    }

    @GetMapping("/counselors")
    public Result<?> listCounselors() {
        return Result.ok(userService.listCounselors());
    }
}
