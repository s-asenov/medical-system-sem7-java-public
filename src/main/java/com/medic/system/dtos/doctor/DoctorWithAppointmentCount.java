package com.medic.system.dtos.doctor;

import com.medic.system.entities.Doctor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorWithAppointmentCount {
    private Doctor doctor;
    private Long appointmentCount;

    public DoctorWithAppointmentCount(Doctor doctor, Long appointmentCount) {
        this.doctor = doctor;
        this.appointmentCount = appointmentCount;
    }
}
