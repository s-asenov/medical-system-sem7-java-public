package com.medic.system.dtos.diagnose;

import com.medic.system.annotations.Unique;
import com.medic.system.entities.Diagnose;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Unique(entityClass = Diagnose.class, fieldName = "name", message = "Диагнозата вече съществува")
public class DiagnoseRequestDto {
    @NotBlank(message = "Името е задължително")
    private String name;

    @NotBlank(message = "Описанието е задължително")
    @Length(max = 255, message = "Описанието на диагнозата трябва да бъде по-малко от 255 символа")
    private String description;
}