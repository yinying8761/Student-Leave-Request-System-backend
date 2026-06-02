package com.leave.controller;

import com.leave.common.Result;
import com.leave.entity.User;
import com.leave.service.StatisticsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/dashboard")
    public Result<?> dashboard() {
        User user = getCurrentUser();
        return Result.ok(statisticsService.dashboard(user.getId()));
    }

    @GetMapping("/student/{id}")
    public Result<?> studentStats(@PathVariable Long id) {
        return Result.ok(statisticsService.studentStats(id));
    }

    @GetMapping("/class")
    public Result<?> classStats() {
        User user = getCurrentUser();
        return Result.ok(statisticsService.classStats(user.getId()));
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }
}
