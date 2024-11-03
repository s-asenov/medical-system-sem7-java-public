package com.medic.system.entities;

import com.medic.system.dtos.patient.PatientRequestDto;
import com.medic.system.dtos.user.BaseUserRequestDto;
import com.medic.system.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity implements UserDetails {
    @Column(unique = true)
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {
        super();
    }

    public User(BaseUserRequestDto baseUserRequestDto) {
        super();
        setFirstName(baseUserRequestDto.getFirstName());
        setLastName(baseUserRequestDto.getLastName());
        setUsername(baseUserRequestDto.getUsername());
        setPassword(baseUserRequestDto.getPassword());
        setRole(baseUserRequestDto.getRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(() -> role.name());
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public boolean isDoctor() {
        return role == Role.ROLE_DOCTOR;
    }

    public boolean isPatient() {
        return role == Role.ROLE_PATIENT;
    }

    public boolean isAdmin() {
        return role == Role.ROLE_ADMIN;
    }
}
