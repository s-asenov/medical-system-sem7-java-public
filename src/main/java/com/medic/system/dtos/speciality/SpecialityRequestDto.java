package com.medic.system.dtos.speciality;

import com.medic.system.annotations.Unique;
import com.medic.system.entities.Speciality;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Unique(entityClass = Speciality.class, fieldName = "name", message = "Специалността вече съществува")
public class SpecialityRequestDto {
    @NotBlank(message = "Името е задължително")
    private String name;
}