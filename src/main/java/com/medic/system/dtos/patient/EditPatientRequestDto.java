package com.medic.system.dtos.patient;

import com.medic.system.annotations.Unique;
import com.medic.system.dtos.user.BaseUserRequestDto;
import com.medic.system.dtos.user.EditBaseUserRequestDto;
import com.medic.system.entities.Patient;
import com.medic.system.entities.User;
import com.medic.system.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Unique(entityClass = Patient.class, fieldName = "egn", message = "ЕГН-то вече съществува")
public class EditPatientRequestDto extends EditBaseUserRequestDto {
    @NotBlank(message = "ЕГН-то е задължително")
    private String egn;

    @NotNull(message = "Личният лекар е задължителен")
    private Long generalPractitionerId;

    public EditPatientRequestDto() {
        super();
    }

    public EditPatientRequestDto(Patient patient) {
        super(patient);
        setEgn(patient.getEgn());
        if (patient.getGeneralPractitioner() != null) {
            setGeneralPractitionerId(patient.getGeneralPractitioner().getId());
        }
    }
}
