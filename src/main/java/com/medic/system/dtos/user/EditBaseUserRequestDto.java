package com.medic.system.dtos.user;

import com.medic.system.annotations.FieldMatch;
import com.medic.system.annotations.Unique;
import com.medic.system.entities.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Unique(entityClass = User.class, fieldName = "username", message = "Потребителското име вече съществува")
@FieldMatch(first = "password", second = "confirmPassword", message = "Паролите не съвпадат")
public class EditBaseUserRequestDto {
    private Long id;

    @NotBlank(message = "Потребителското име е задължително")
    private String username;

    private String password;

    private String confirmPassword;

    @NotBlank(message = "Името е задължително")
    private String firstName;

    @NotBlank(message = "Фамилията е задължителна")
    private String lastName;

    public EditBaseUserRequestDto() {}

    public EditBaseUserRequestDto(User user)
    {
        setUsername(user.getUsername());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setId(user.getId());
    }
}