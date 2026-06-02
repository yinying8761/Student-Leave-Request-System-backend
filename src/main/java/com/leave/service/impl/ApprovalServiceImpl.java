package com.leave.service.impl;

import com.leave.common.BusinessException;
import com.leave.dto.ApprovalRequest;
import com.leave.entity.ApprovalRecord;
import com.leave.entity.LeaveApplication;
import com.leave.entity.User;
import com.leave.mapper.ApprovalRecordMapper;
import com.leave.mapper.LeaveApplicationMapper;
import com.leave.service.ApprovalService;
import com.leave.service.LeaveApplicationService;
import com.leave.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    private final LeaveApplicationMapper appMapper;
    private final ApprovalRecordMapper recordMapper;
    private final UserService userService;

    public ApprovalServiceImpl(LeaveApplicationMapper appMapper,
                                ApprovalRecordMapper recordMapper,
                                UserService userService) {
        this.appMapper = appMapper;
        this.recordMapper = recordMapper;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void approve(ApprovalRequest request, Long approverId) {
        LeaveApplication app = appMapper.findById(request.getApplicationId());
        if (app == null) throw new BusinessException("请假申请不存在");

        User approver = userService.findById(approverId);
        String role = approver.getRole();
        int step;
        String nextStatus;

        if ("ADVISOR".equals(role)) {
            if (!"PENDING_ADVISOR".equals(app.getStatus())) {
                throw new BusinessException("当前状态不允许导师审批");
            }
            step = 1;
            nextStatus = "APPROVE".equals(request.getAction()) ? "PENDING_COUNSELOR" : "REJECTED";
        } else if ("COUNSELOR".equals(role)) {
            if (!"PENDING_COUNSELOR".equals(app.getStatus())) {
                throw new BusinessException("当前状态不允许辅导员审批");
            }
            step = 2;
            nextStatus = "APPROVE".equals(request.getAction()) ? "APPROVED" : "REJECTED";
        } else {
            throw new BusinessException("无审批权限");
        }

        // 记录审批
        ApprovalRecord record = new ApprovalRecord();
        record.setApplicationId(app.getId());
        record.setApproverId(approverId);
        record.setStep(step);
        record.setAction(request.getAction());
        record.setComment(request.getComment());
        recordMapper.insert(record);

        // 更新状态
        appMapper.updateStatus(app.getId(), nextStatus);
    }

    @Override
    public List<LeaveApplication> listPending(Long approverId) {
        User approver = userService.findById(approverId);
        String role = approver.getRole();
        String status;
        if ("ADVISOR".equals(role)) {
            status = "PENDING_ADVISOR";
        } else if ("COUNSELOR".equals(role)) {
            status = "PENDING_COUNSELOR";
        } else {
            throw new BusinessException("无审批权限");
        }
        return appMapper.findByApproverRole(role, approverId, status, 0, 100);
    }
}
