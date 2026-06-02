package com.leave.controller;

import com.leave.common.Result;
import com.leave.dto.UserCreateRequest;
import com.leave.service.UserService;
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
}
