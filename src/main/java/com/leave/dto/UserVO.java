package com.leave.dto;

public class UserVO {
    private Long id;
    private String username;
    private String realName;
    private String role;
    private String phone;
    private String email;
    private String department;
    private String className;
    private Long advisorId;
    private Long counselorId;

    // Static factory for security - avoids exposing password
    public static UserVO fromUser(com.leave.entity.User u) {
        UserVO vo = new UserVO();
        vo.id = u.getId();
        vo.username = u.getUsername();
        vo.realName = u.getRealName();
        vo.role = u.getRole();
        vo.phone = u.getPhone();
        vo.email = u.getEmail();
        vo.department = u.getDepartment();
        vo.className = u.getClassName();
        vo.advisorId = u.getAdvisorId();
        vo.counselorId = u.getCounselorId();
        return vo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
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
}
