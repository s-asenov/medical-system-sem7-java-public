package com.medic.system.dtos;

import com.medic.system.annotations.Matches;
import com.medic.system.entities.User;
import com.medic.system.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditBaseUserRequestDto {

    @NotBlank(message = "Username is mandatory")
    @Matches(fieldToMatch = "^[a-zA-Z0-9]*$", message = "Username must contain only letters and numbers")
    public String username;

    public String password;

    @Matches(fieldToMatch = "password", message = "Passwords do not match")
    public String confirmPassword;;

    @NotBlank(message = "First name is mandatory")
    public String firstName;

    @NotBlank(message = "Last name is mandatory")
    public String lastName;

    public EditBaseUserRequestDto() {}

    public EditBaseUserRequestDto(User user)
    {
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }
}