package com.medic.system.dtos.doctor;

import com.medic.system.annotations.Unique;
import com.medic.system.dtos.user.EditBaseUserRequestDto;
import com.medic.system.entities.Doctor;
import com.medic.system.entities.Patient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditDoctorRequestDto extends EditBaseUserRequestDto {
    private Boolean isGeneralPractitioner;

    public EditDoctorRequestDto() {
        super();
    }

    public EditDoctorRequestDto(Doctor doctor) {
        super(doctor);
        setIsGeneralPractitioner(doctor.isGeneralPractitioner());
    }
}
