package com.leave.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CancellationRequest {

    @NotNull(message = "请假申请ID不能为空")
    private Long applicationId;

    @NotBlank(message = "返校时间不能为空")
    private String returnTime;

    private String comment;

    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public String getReturnTime() { return returnTime; }
    public void setReturnTime(String returnTime) { this.returnTime = returnTime; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
