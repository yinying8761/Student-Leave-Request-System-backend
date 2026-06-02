package com.leave.mapper;

import com.leave.entity.ApprovalRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApprovalRecordMapper {
    int insert(ApprovalRecord record);
    List<ApprovalRecord> findByApplicationId(@Param("applicationId") Long applicationId);
    List<ApprovalRecord> findByApproverId(@Param("approverId") Long approverId,
                                           @Param("offset") int offset,
                                           @Param("limit") int limit);
}
