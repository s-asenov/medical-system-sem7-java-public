package com.medic.system.dtos;

import com.medic.system.annotations.Matches;
import com.medic.system.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseUserRequestDto {

    @NotBlank(message = "Username is mandatory")
    public String username;

    @NotBlank(message = "Password is mandatory")
    public String password;

    @NotBlank(message = "Confirm password is mandatory")
    @Matches(fieldToMatch = "password", message = "Passwords do not match")
    public String confirmPassword;

    @NotNull(message = "Role is mandatory")
    public Role role;

    @NotBlank(message = "First name is mandatory")
    public String firstName;

    @NotBlank(message = "Last name is mandatory")
    public String lastName;
}