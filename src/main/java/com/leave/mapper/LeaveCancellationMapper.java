package com.leave.mapper;

import com.leave.entity.LeaveCancellation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LeaveCancellationMapper {
    int insert(LeaveCancellation cancellation);
    LeaveCancellation findById(@Param("id") Long id);
    List<LeaveCancellation> findByStatus(@Param("status") String status);
    int updateStatus(@Param("id") Long id, @Param("status") String status,
                     @Param("approverId") Long approverId);
}
