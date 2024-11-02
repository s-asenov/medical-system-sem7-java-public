package com.medic.system.dtos;

import com.medic.system.entities.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditBaseUserRequestDto {

    @NotBlank(message = "Потребителското име е задължително")
    public String username;

    public String password;

    public String confirmPassword;

    @NotBlank(message = "Името е задължително")
    public String firstName;

    @NotBlank(message = "Фамилията е задължителна")
    public String lastName;

    public EditBaseUserRequestDto() {}

    public EditBaseUserRequestDto(User user)
    {
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }
}