package com.leave.controller;

import com.leave.common.Result;
import com.leave.dto.CancellationRequest;
import com.leave.entity.User;
import com.leave.service.CancellationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cancellations")
public class CancellationController {

    private final CancellationService cxlService;

    public CancellationController(CancellationService cxlService) {
        this.cxlService = cxlService;
    }

    @PostMapping
    public Result<?> create(@Valid @RequestBody CancellationRequest request) {
        User user = getCurrentUser();
        if (!"STUDENT".equals(user.getRole())) {
            return Result.fail(403, "仅学生可发起销假");
        }
        return Result.ok(cxlService.create(request, user.getId()));
    }

    @GetMapping("/pending")
    public Result<?> pending() {
        return Result.ok(cxlService.listPending());
    }

    @PutMapping("/{id}/approve")
    public Result<?> approve(@PathVariable Long id) {
        User user = getCurrentUser();
        cxlService.approve(id, user.getId());
        return Result.ok();
    }

    @PostMapping("/counselor")
    public Result<?> counselorCancel(@Valid @RequestBody CancellationRequest request) {
        User user = getCurrentUser();
        if (!"COUNSELOR".equals(user.getRole())) {
            return Result.fail(403, "仅辅导员可代替销假");
        }
        cxlService.counselorCancel(request, user.getId());
        return Result.ok();
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }
}
