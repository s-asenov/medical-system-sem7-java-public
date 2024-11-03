package com.medic.system.dtos.user;

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
@Unique(entityClass = User.class, fieldName = "username", message = "Потребителското име вече съществува")
public class BaseUserRequestDto {
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

    public BaseUserRequestDto() {
    }

    public BaseUserRequestDto(Role role) {
        setRole(role);
    }
}