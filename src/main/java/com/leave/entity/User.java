package com.leave.entity;

public class User {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String role;
    private String phone;
    private String email;
    private String department;
    private String className;
    private Long advisorId;
    private Long counselorId;
    private Integer enabled;
    private java.sql.Timestamp createTime;
    private java.sql.Timestamp updateTime;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public Long getAdvisorId() { return advisorId; }
    public void setAdvisorId(Long advisorId) { this.advisorId = advisorId; }
    public Long getCounselorId() { return counselorId; }
    public void setCounselorId(Long counselorId) { this.counselorId = counselorId; }
    public Integer getEnabled() { return enabled; }
    public void setEnabled(Integer enabled) { this.enabled = enabled; }
    public java.sql.Timestamp getCreateTime() { return createTime; }
    public void setCreateTime(java.sql.Timestamp createTime) { this.createTime = createTime; }
    public java.sql.Timestamp getUpdateTime() { return updateTime; }
    public void setUpdateTime(java.sql.Timestamp updateTime) { this.updateTime = updateTime; }
}
