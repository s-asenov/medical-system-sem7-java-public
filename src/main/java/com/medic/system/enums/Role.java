package com.medic.system.enums;

// Enum for the roles of the users, add ROLE_ prefix to the roles as it is required by Spring Security
public enum Role {
    ROLE_ADMIN,
    ROLE_DOCTOR,
    ROLE_PATIENT
}
