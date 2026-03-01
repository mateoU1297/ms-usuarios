package com.pragma.users.domain.model;

import java.time.LocalDateTime;

public class UserRole {
    private Long userId;
    private Long roleId;
    private LocalDateTime assignedAt;

    public UserRole() {}

    public UserRole(Long userId, Long roleId,  LocalDateTime assignedAt) {
        this.userId = userId;
        this.roleId = roleId;
        this.assignedAt = assignedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }
}
