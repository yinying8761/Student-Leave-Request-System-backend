package com.leave.entity;

public class LeaveApplication {
    private Long id;
    private Long studentId;
    private String leaveType;
    private java.sql.Timestamp startTime;
    private java.sql.Timestamp endTime;
    private Double durationDays;
    private String reason;
    private Boolean isLeaveCampus;
    private String destinationProvince;
    private String destinationCity;
    private String destinationDistrict;
    private String destinationDetail;
    private String contactPhone;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String status;
    private java.sql.Timestamp createTime;
    private java.sql.Timestamp updateTime;

    // 联表字段
    private String studentName;
    private String className;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }
    public java.sql.Timestamp getStartTime() { return startTime; }
    public void setStartTime(java.sql.Timestamp startTime) { this.startTime = startTime; }
    public java.sql.Timestamp getEndTime() { return endTime; }
    public void setEndTime(java.sql.Timestamp endTime) { this.endTime = endTime; }
    public Double getDurationDays() { return durationDays; }
    public void setDurationDays(Double durationDays) { this.durationDays = durationDays; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Boolean getIsLeaveCampus() { return isLeaveCampus; }
    public void setIsLeaveCampus(Boolean isLeaveCampus) { this.isLeaveCampus = isLeaveCampus; }
    public String getDestinationProvince() { return destinationProvince; }
    public void setDestinationProvince(String destinationProvince) { this.destinationProvince = destinationProvince; }
    public String getDestinationCity() { return destinationCity; }
    public void setDestinationCity(String destinationCity) { this.destinationCity = destinationCity; }
    public String getDestinationDistrict() { return destinationDistrict; }
    public void setDestinationDistrict(String destinationDistrict) { this.destinationDistrict = destinationDistrict; }
    public String getDestinationDetail() { return destinationDetail; }
    public void setDestinationDetail(String destinationDetail) { this.destinationDetail = destinationDetail; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }
    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public java.sql.Timestamp getCreateTime() { return createTime; }
    public void setCreateTime(java.sql.Timestamp createTime) { this.createTime = createTime; }
    public java.sql.Timestamp getUpdateTime() { return updateTime; }
    public void setUpdateTime(java.sql.Timestamp updateTime) { this.updateTime = updateTime; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
}
