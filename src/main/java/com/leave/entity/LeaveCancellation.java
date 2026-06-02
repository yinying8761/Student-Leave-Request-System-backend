package com.leave.entity;

public class LeaveCancellation {
    private Long id;
    private Long applicationId;
    private Long studentId;
    private java.sql.Timestamp returnTime;
    private String status;
    private String comment;
    private Long approverId;
    private java.sql.Timestamp approveTime;
    private java.sql.Timestamp createTime;

    private String studentName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public java.sql.Timestamp getReturnTime() { return returnTime; }
    public void setReturnTime(java.sql.Timestamp returnTime) { this.returnTime = returnTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Long getApproverId() { return approverId; }
    public void setApproverId(Long approverId) { this.approverId = approverId; }
    public java.sql.Timestamp getApproveTime() { return approveTime; }
    public void setApproveTime(java.sql.Timestamp approveTime) { this.approveTime = approveTime; }
    public java.sql.Timestamp getCreateTime() { return createTime; }
    public void setCreateTime(java.sql.Timestamp createTime) { this.createTime = createTime; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
}
