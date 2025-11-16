package com.codifytech.hospitalManagement.common;

import lombok.Getter;

@Getter
public enum RoleType {
    USER("USER"),
    DOCTOR("DOCTOR"),
    ADMIN("ADMIN");

    private final String label;
    RoleType(String roleDoctor) {
        label = roleDoctor;
    }

}
