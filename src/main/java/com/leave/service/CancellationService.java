package com.leave.service;

import com.leave.dto.CancellationRequest;
import com.leave.entity.LeaveCancellation;

import java.util.List;

public interface CancellationService {
    LeaveCancellation create(CancellationRequest request, Long studentId);
    void approve(Long id, Long approverId);
    void counselorCancel(CancellationRequest request, Long counselorId);
    List<LeaveCancellation> listPending();
}
