package com.medic.system.dtos.doctor;

import com.medic.system.dtos.user.EditBaseUserRequestDto;
import com.medic.system.entities.Doctor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EditDoctorRequestDto extends EditBaseUserRequestDto {
    private Boolean isGeneralPractitioner;
    private List<Long> specialities;

    public EditDoctorRequestDto() {
        super();
    }

    public EditDoctorRequestDto(Doctor doctor) {
        super(doctor);
        specialities = doctor.getSpecialities()
                .stream()
                .map(speciality -> speciality.getId())
                .toList();
        setIsGeneralPractitioner(doctor.getIsGeneralPractitioner());
    }
}
