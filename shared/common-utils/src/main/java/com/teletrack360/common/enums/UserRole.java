package com.teletrack360.common.enums;

/**
 * User roles for RBAC
 */
public enum UserRole {
    ADMIN("Full access to all endpoints"),
    OPERATOR("Create/update incidents, view reports"),
    SUPPORT("View incidents, add comments");
    
    private final String description;
    
    UserRole(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
