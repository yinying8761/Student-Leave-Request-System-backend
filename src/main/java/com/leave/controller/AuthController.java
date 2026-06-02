package com.leave.controller;

import com.leave.common.Result;
import com.leave.dto.LoginRequest;
import com.leave.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody LoginRequest request) {
        return Result.ok(userService.login(request));
    }

    @GetMapping("/me")
    public Result<?> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        com.leave.entity.User user = (com.leave.entity.User) auth.getPrincipal();
        return Result.ok(userService.getCurrentUser(user.getId()));
    }
}
