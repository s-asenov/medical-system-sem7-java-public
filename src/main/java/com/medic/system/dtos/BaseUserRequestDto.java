package com.medic.system.dtos;

import com.medic.system.annotations.FieldMatch;
import com.medic.system.annotations.Unique;
import com.medic.system.entities.User;
import com.medic.system.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FieldMatch(first = "password", second = "confirmPassword", message = "Паролите не съвпадат")
@Unique(entityClass = User.class, fieldName = "username", message = "Username already exists")
public class BaseUserRequestDto {
    public Long id;

    @NotBlank(message = "Потребителското име е задължително")
    private String username;

    @NotBlank(message = "Паролата е задължителна")
    private String password;

    @NotBlank(message = "Потвърждението на паролата е задължително")
    private String confirmPassword;

    @NotNull(message = "Ролята е задължителна")
    private Role role;

    @NotBlank(message = "Името е задължително")
    private String firstName;

    @NotBlank(message = "Фамилията е задължителна")
    private String lastName;
}