package com.leave.service;

import com.leave.dto.ApprovalRequest;

public interface ApprovalService {
    void approve(ApprovalRequest request, Long approverId);
    java.util.List<com.leave.entity.LeaveApplication> listPending(Long approverId);
}
