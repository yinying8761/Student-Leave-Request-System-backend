package com.leave.service.impl;

import com.leave.common.BusinessException;
import com.leave.common.PageResult;
import com.leave.dto.ApplicationCreateRequest;
import com.leave.entity.ApprovalRecord;
import com.leave.entity.LeaveApplication;
import com.leave.entity.User;
import com.leave.mapper.ApprovalRecordMapper;
import com.leave.mapper.LeaveApplicationMapper;
import com.leave.service.LeaveApplicationService;
import com.leave.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LeaveApplicationServiceImpl implements LeaveApplicationService {

    private final LeaveApplicationMapper appMapper;
    private final ApprovalRecordMapper recordMapper;
    private final UserService userService;

    public LeaveApplicationServiceImpl(LeaveApplicationMapper appMapper,
                                        ApprovalRecordMapper recordMapper,
                                        UserService userService) {
        this.appMapper = appMapper;
        this.recordMapper = recordMapper;
        this.userService = userService;
    }

    @Override
    @Transactional
    public LeaveApplication create(ApplicationCreateRequest request, Long studentId) {
        LeaveApplication app = new LeaveApplication();
        app.setStudentId(studentId);
        app.setLeaveType(request.getLeaveType());
        app.setStartTime(Timestamp.valueOf(request.getStartTime()));
        app.setEndTime(Timestamp.valueOf(request.getEndTime()));
        long diff = app.getEndTime().getTime() - app.getStartTime().getTime();
        app.setDurationDays(Math.ceil(diff / (1000.0 * 3600 * 24) * 10) / 10);
        app.setReason(request.getReason());
        app.setIsLeaveCampus(request.getIsLeaveCampus());
        app.setDestinationProvince(request.getDestinationProvince());
        app.setDestinationCity(request.getDestinationCity());
        app.setDestinationDistrict(request.getDestinationDistrict());
        app.setDestinationDetail(request.getDestinationDetail());
        app.setContactPhone(request.getContactPhone());
        app.setEmergencyContactName(request.getEmergencyContactName());
        app.setEmergencyContactPhone(request.getEmergencyContactPhone());
        app.setStatus("PENDING");
        appMapper.insert(app);
        return app;
    }

    @Override
    public PageResult<LeaveApplication> listByStudent(Long studentId, int current, int size) {
        int offset = (current - 1) * size;
        List<LeaveApplication> list = appMapper.findByStudentId(studentId, offset, size);
        long total = appMapper.countByStudentId(studentId);
        return PageResult.of(list, total, current, size);
    }

    @Override
    @Transactional
    public Map<String, Object> getDetail(Long id) {
        LeaveApplication app = appMapper.findById(id);
        if (app == null) throw new BusinessException("请假申请不存在");
        List<ApprovalRecord> records = recordMapper.findByApplicationId(id);
        Map<String, Object> result = new HashMap<>();
        result.put("application", app);
        result.put("records", records);
        return result;
    }

    @Override
    public void cancel(Long id, Long studentId) {
        int rows = appMapper.cancel(id, studentId);
        if (rows == 0) throw new BusinessException("申请不存在或状态不允许撤销");
    }

    @Override
    public PageResult<LeaveApplication> listPending(int current, int size) {
        // Overridden in controller with current user context
        return PageResult.of(java.util.Collections.emptyList(), 0, current, size);
    }
}
