package com.medic.system.dtos.sick_leave;

import com.medic.system.entities.Doctor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorWithSickLeaveCount {
    private Doctor doctor;
    private Long sickLeaveCount;

    public DoctorWithSickLeaveCount(Doctor doctor, Long sickLeaveCount) {
        this.doctor = doctor;
        this.sickLeaveCount = sickLeaveCount;
    }
}
