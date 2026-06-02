package com.leave.controller;

import com.leave.common.Result;
import com.leave.dto.ApprovalRequest;
import com.leave.entity.User;
import com.leave.service.ApprovalService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @PostMapping
    public Result<?> approve(@Valid @RequestBody ApprovalRequest request) {
        User user = getCurrentUser();
        if (!"ADVISOR".equals(user.getRole()) && !"COUNSELOR".equals(user.getRole())) {
            return Result.fail(403, "无审批权限");
        }
        approvalService.approve(request, user.getId());
        return Result.ok();
    }

    @GetMapping("/pending")
    public Result<?> pending() {
        User user = getCurrentUser();
        return Result.ok(approvalService.listPending(user.getId()));
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }
}
