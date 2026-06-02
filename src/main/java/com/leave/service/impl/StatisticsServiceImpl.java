package com.leave.service.impl;

import com.leave.entity.User;
import com.leave.mapper.LeaveApplicationMapper;
import com.leave.mapper.NotificationMapper;
import com.leave.service.StatisticsService;
import com.leave.service.UserService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final LeaveApplicationMapper appMapper;
    private final UserService userService;

    public StatisticsServiceImpl(LeaveApplicationMapper appMapper,
                                  UserService userService) {
        this.appMapper = appMapper;
        this.userService = userService;
    }

    @Override
    public Map<String, Object> dashboard(Long userId) {
        User user = userService.findById(userId);
        Map<String, Object> result = new HashMap<>();
        long pendingCount = 0;
        if ("ADVISOR".equals(user.getRole())) {
            pendingCount = appMapper.countByApproverIdAndStep(userId, "PENDING_ADVISOR", 1);
        } else if ("COUNSELOR".equals(user.getRole())) {
            pendingCount = appMapper.countByApproverIdAndStep(userId, "PENDING_COUNSELOR", 2);
        }
        result.put("pendingCount", (int) pendingCount);
        result.put("totalCount", 0); // simplified
        return result;
    }

    @Override
    public List<Map<String, Object>> studentStats(Long studentId) {
        return Collections.emptyList(); // simplified
    }

    @Override
    public List<Map<String, Object>> classStats(Long userId) {
        User user = userService.findById(userId);
        List<com.leave.entity.LeaveApplication> apps = appMapper.findByClassAndDepartment(
                user.getDepartment(), user.getClassName());
        Map<Long, Map<String, Object>> grouped = new LinkedHashMap<>();
        for (com.leave.entity.LeaveApplication app : apps) {
            Long sid = app.getStudentId();
            grouped.computeIfAbsent(sid, k -> {
                Map<String, Object> m = new HashMap<>();
                m.put("studentName", app.getStudentName());
                m.put("className", app.getClassName());
                m.put("totalCount", 0);
                m.put("totalDays", 0.0);
                return m;
            });
            Map<String, Object> m = grouped.get(sid);
            m.put("totalCount", (int) m.get("totalCount") + 1);
            m.put("totalDays", (double) m.get("totalDays") + (app.getDurationDays() != null ? app.getDurationDays() : 0));
        }
        return new ArrayList<>(grouped.values());
    }
}
