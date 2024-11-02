package com.medic.system.dtos;

import com.medic.system.annotations.FieldMatch;
import com.medic.system.annotations.Unique;
import com.medic.system.entities.User;
import com.medic.system.enums.Role;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FieldMatch(first = "password", second = "confirmPassword", message = "Паролите не съвпадат")
public class BaseUserRequestDto {

    @NotBlank(message = "Потребителското име е задължително")
    @Unique(entityClass = User.class, fieldName = "username", message = "Username already exists")
    public String username;

    @NotBlank(message = "Паролата е задължителна")
    public String password;

    @NotBlank(message = "Потвърждението на паролата е задължително")
    public String confirmPassword;

    @NotNull(message = "Ролята е задължителна")
    public Role role;

    @NotBlank(message = "Името е задължително")
    public String firstName;

    @NotBlank(message = "Фамилията е задължителна")
    public String lastName;

    @AssertTrue(message = "Паролите не съвпадат")
    public boolean isPasswordMatching() {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }
}