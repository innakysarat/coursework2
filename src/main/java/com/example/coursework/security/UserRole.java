package com.example.coursework.security;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.example.coursework.security.UserPermission.*;

public enum UserRole {
    STUDENT(Sets.newHashSet(COURSE_READ, STUDENT_READ, REVIEW_ADD, REVIEW_EDIT)),
    ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, COURSE_CHECK, STUDENT_READ, STUDENT_WRITE, ORGANIZATION_WRITE, ORGANIZATION_READ, REVIEW_EDIT)),
    LEADER(Sets.newHashSet(COURSE_READ, COURSE_WRITE, ORGANIZATION_WRITE));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
