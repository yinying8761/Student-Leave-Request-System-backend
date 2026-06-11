package com.leave.mapper;

import com.leave.entity.LeaveApplication;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LeaveApplicationMapper {
    int insert(LeaveApplication app);
    LeaveApplication findById(@Param("id") Long id);
    List<LeaveApplication> findByStudentId(@Param("studentId") Long studentId,
                                           @Param("offset") int offset,
                                           @Param("limit") int limit);
    long countByStudentId(@Param("studentId") Long studentId);
    List<LeaveApplication> findByApproverRole(@Param("role") String role,
                                               @Param("approverId") Long approverId,
                                               @Param("status") String status,
                                               @Param("offset") int offset,
                                               @Param("limit") int limit);
    long countByApproverRole(@Param("role") String role,
                             @Param("approverId") Long approverId,
                             @Param("status") String status);
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    int cancel(@Param("id") Long id, @Param("studentId") Long studentId);
    List<LeaveApplication> findAll(@Param("offset") int offset, @Param("limit") int limit);
    long countAll();
    List<LeaveApplication> findByCounselorId(@Param("counselorId") Long counselorId,
                                              @Param("offset") int offset,
                                              @Param("limit") int limit);
    long countByCounselorId(@Param("counselorId") Long counselorId);
    long countByStatus(@Param("status") String status);
    long countPendingByCounselorId(@Param("approverId") Long approverId,
                                   @Param("status") String status);
    List<LeaveApplication> findByClassAndDepartment(@Param("department") String department,
                                                     @Param("className") String className);
    int updateExpiredToCancelling();
}
