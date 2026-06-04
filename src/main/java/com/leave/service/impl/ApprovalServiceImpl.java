package com.leave.service.impl;

import com.leave.common.BusinessException;
import com.leave.dto.ApprovalRequest;
import com.leave.entity.ApprovalRecord;
import com.leave.entity.LeaveApplication;
import com.leave.entity.User;
import com.leave.mapper.ApprovalRecordMapper;
import com.leave.mapper.LeaveApplicationMapper;
import com.leave.service.ApprovalService;
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
        if (!"COUNSELOR".equals(approver.getRole())) {
            throw new BusinessException("无审批权限，仅辅导员可审批");
        }
        if (!"PENDING".equals(app.getStatus())) {
            throw new BusinessException("当前状态不允许审批");
        }

        String nextStatus = "APPROVE".equals(request.getAction()) ? "APPROVED" : "REJECTED";

        ApprovalRecord record = new ApprovalRecord();
        record.setApplicationId(app.getId());
        record.setApproverId(approverId);
        record.setStep(1);
        record.setAction(request.getAction());
        record.setComment(request.getComment());
        recordMapper.insert(record);

        appMapper.updateStatus(app.getId(), nextStatus);
    }

    @Override
    public List<LeaveApplication> listPending(Long approverId) {
        User approver = userService.findById(approverId);
        if (!"COUNSELOR".equals(approver.getRole())) {
            throw new BusinessException("无审批权限");
        }
        return appMapper.findByApproverRole("COUNSELOR", approverId, "PENDING", 0, 100);
    }
}
