package com.leave.service.impl;

import com.leave.common.BusinessException;
import com.leave.dto.CancellationRequest;
import com.leave.entity.LeaveApplication;
import com.leave.entity.LeaveCancellation;
import com.leave.entity.User;
import com.leave.mapper.LeaveApplicationMapper;
import com.leave.mapper.LeaveCancellationMapper;
import com.leave.mapper.NotificationMapper;
import com.leave.service.CancellationService;
import com.leave.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class CancellationServiceImpl implements CancellationService {

    private final LeaveCancellationMapper cxlMapper;
    private final LeaveApplicationMapper appMapper;
    private final UserService userService;

    public CancellationServiceImpl(LeaveCancellationMapper cxlMapper,
                                    LeaveApplicationMapper appMapper,
                                    UserService userService) {
        this.cxlMapper = cxlMapper;
        this.appMapper = appMapper;
        this.userService = userService;
    }

    @Override
    @Transactional
    public LeaveCancellation create(CancellationRequest request, Long studentId) {
        LeaveApplication app = appMapper.findById(request.getApplicationId());
        if (app == null) throw new BusinessException("请假申请不存在");
        if (!"APPROVED".equals(app.getStatus()) && !"CANCELLING".equals(app.getStatus())) {
            throw new BusinessException("当前状态不允许销假");
        }
        // Check for existing pending cancellation
        List<LeaveCancellation> existing = cxlMapper.findByApplicationId(request.getApplicationId());
        if (existing.stream().anyMatch(c -> "PENDING".equals(c.getStatus()))) {
            throw new BusinessException("已有待处理的销假申请，请等待审批");
        }
        if (!app.getStudentId().equals(studentId)) {
            throw new BusinessException("只能对自己的请假发起销假");
        }

        LeaveCancellation cxl = new LeaveCancellation();
        cxl.setApplicationId(request.getApplicationId());
        cxl.setStudentId(studentId);
        cxl.setReturnTime(Timestamp.valueOf(request.getReturnTime()));
        cxl.setComment(request.getComment());
        cxl.setStatus("PENDING");
        cxlMapper.insert(cxl);

        appMapper.updateStatus(app.getId(), "CANCELLING");
        return cxl;
    }

    @Override
    @Transactional
    public void approve(Long id, Long approverId) {
        LeaveCancellation cxl = cxlMapper.findById(id);
        if (cxl == null) throw new BusinessException("销假记录不存在");
        if (!"PENDING".equals(cxl.getStatus())) {
            throw new BusinessException("该销假已处理");
        }
        cxlMapper.updateStatus(id, "APPROVED", approverId);
        appMapper.updateStatus(cxl.getApplicationId(), "CANCELLED");
    }

    @Override
    @Transactional
    public void counselorCancel(CancellationRequest request, Long counselorId) {
        LeaveApplication app = appMapper.findById(request.getApplicationId());
        if (app == null) throw new BusinessException("请假申请不存在");
        String status = app.getStatus();
        if (!"CANCELLING".equals(status) && !"APPROVED".equals(status)) {
            throw new BusinessException("当前状态不允许销假");
        }

        LeaveCancellation cxl;
        if ("CANCELLING".equals(status)) {
            List<LeaveCancellation> list = cxlMapper.findByApplicationId(request.getApplicationId());
            cxl = list.stream().filter(c -> "PENDING".equals(c.getStatus())).findFirst().orElse(null);
            if (cxl == null) {
                // auto-expired, no student-initiated record yet — create one
                cxl = new LeaveCancellation();
                cxl.setApplicationId(request.getApplicationId());
                cxl.setStudentId(app.getStudentId());
                cxl.setReturnTime(Timestamp.valueOf(request.getReturnTime()));
                cxl.setComment(request.getComment());
                cxl.setStatus("PENDING");
                cxlMapper.insert(cxl);
            }
        } else {
            // counselor directly cancels on behalf of student
            cxl = new LeaveCancellation();
            cxl.setApplicationId(request.getApplicationId());
            cxl.setStudentId(app.getStudentId());
            cxl.setReturnTime(Timestamp.valueOf(request.getReturnTime()));
            cxl.setComment(request.getComment());
            cxl.setStatus("PENDING");
            cxlMapper.insert(cxl);
        }

        cxlMapper.updateStatus(cxl.getId(), "APPROVED", counselorId);
        appMapper.updateStatus(app.getId(), "CANCELLED");
    }

    @Override
    public List<LeaveCancellation> listPending() {
        return cxlMapper.findByStatus("PENDING");
    }
}
