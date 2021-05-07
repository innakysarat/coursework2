package com.example.coursework.security;

public enum UserPermission {
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    COURSE_READ("course:read"),
    COURSE_WRITE("course:write"),
    COURSE_CHECK("course:check"),
    ORGANIZATION_WRITE("organization:write"),
    ORGANIZATION_READ("organization:read"),
    REVIEW_ADD("review:add"),
    REVIEW_EDIT("review:edit")
    ;
    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}