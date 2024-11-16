package com.medic.system.dtos.diagnose;

import com.medic.system.entities.Diagnose;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiagnoseMedicalAppointmentCount {
    private Diagnose diagnose;
    private long appointmentCount;

    public DiagnoseMedicalAppointmentCount(Diagnose diagnose, long appointmentCount) {
        this.diagnose = diagnose;
        this.appointmentCount = appointmentCount;
    }
}
