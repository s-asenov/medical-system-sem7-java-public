package com.medic.system.dtos;

import com.medic.system.entities.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditBaseUserRequestDto {

    @NotBlank(message = "Потребителското име е задължително")
    private String username;

    private String password;

    private String confirmPassword;

    @NotBlank(message = "Името е задължително")
    private String firstName;

    @NotBlank(message = "Фамилията е задължителна")
    private String lastName;

    public EditBaseUserRequestDto(User user)
    {
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }
}