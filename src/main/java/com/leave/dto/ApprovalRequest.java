package com.leave.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ApprovalRequest {

    @NotNull(message = "申请ID不能为空")
    private Long applicationId;

    @NotBlank(message = "审批动作不能为空")
    private String action;

    private String comment;

    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
