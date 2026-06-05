package com.leave.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ApplicationCreateRequest {

    @NotBlank(message = "请假类型不能为空")
    private String leaveType;

    @NotBlank(message = "开始时间不能为空")
    private String startTime;

    @NotBlank(message = "结束时间不能为空")
    private String endTime;

    @NotBlank(message = "请假原因不能为空")
    private String reason;

    @NotNull(message = "是否离校不能为空")
    private Boolean isLeaveCampus;

    private String destinationProvince;
    private String destinationCity;
    private String destinationDistrict;
    private String destinationDetail;

    @NotBlank(message = "本人联系电话不能为空")
    private String contactPhone;

    @NotBlank(message = "紧急联系人姓名不能为空")
    private String emergencyContactName;

    @NotBlank(message = "紧急联系人电话不能为空")
    private String emergencyContactPhone;

    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
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
}
