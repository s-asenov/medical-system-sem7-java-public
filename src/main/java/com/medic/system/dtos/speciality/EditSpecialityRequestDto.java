package com.medic.system.dtos.speciality;

import com.medic.system.annotations.Unique;
import com.medic.system.entities.Speciality;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Unique(entityClass = Speciality.class, fieldName = "name", message = "Специалността вече съществува")
public class EditSpecialityRequestDto {
    private Long id;

    @NotBlank(message = "Името е задължително")
    private String name;

    public EditSpecialityRequestDto() {
    }

    public EditSpecialityRequestDto(Speciality speciality) {
        id = speciality.getId();
        name = speciality.getName();
    }
}