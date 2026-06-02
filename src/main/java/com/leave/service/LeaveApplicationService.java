package com.leave.service;

import com.leave.dto.ApplicationCreateRequest;
import com.leave.common.PageResult;
import com.leave.entity.LeaveApplication;

import java.util.Map;

public interface LeaveApplicationService {
    LeaveApplication create(ApplicationCreateRequest request, Long studentId);
    PageResult<LeaveApplication> listByStudent(Long studentId, int current, int size);
    Map<String, Object> getDetail(Long id);
    void cancel(Long id, Long studentId);
    PageResult<LeaveApplication> listPending(int current, int size);
}
