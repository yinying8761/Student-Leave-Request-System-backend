package com.leave.controller;

import com.leave.common.Result;
import com.leave.common.PageResult;
import com.leave.dto.ApplicationCreateRequest;
import com.leave.entity.LeaveApplication;
import com.leave.entity.User;
import com.leave.service.LeaveApplicationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/applications")
public class LeaveApplicationController {

    private final LeaveApplicationService appService;

    public LeaveApplicationController(LeaveApplicationService appService) {
        this.appService = appService;
    }

    @PostMapping
    public Result<?> create(@Valid @RequestBody ApplicationCreateRequest request) {
        User user = getCurrentUser();
        if (!"STUDENT".equals(user.getRole())) {
            return Result.fail(403, "仅学生可提交请假申请");
        }
        LeaveApplication app = appService.create(request, user.getId());
        return Result.ok(app);
    }

    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "1") int current,
                          @RequestParam(defaultValue = "10") int size) {
        User user = getCurrentUser();
        PageResult<LeaveApplication> page;
        if ("STUDENT".equals(user.getRole())) {
            page = appService.listByStudent(user.getId(), current, size);
        } else if ("COUNSELOR".equals(user.getRole())) {
            page = appService.listByCounselorId(user.getId(), current, size);
        } else {
            page = appService.listAll(current, size);
        }
        return Result.ok(page);
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.ok(appService.getDetail(id));
    }

    @PutMapping("/{id}/cancel")
    public Result<?> cancel(@PathVariable Long id) {
        User user = getCurrentUser();
        appService.cancel(id, user.getId());
        return Result.ok();
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }
}
