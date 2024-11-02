package com.medic.system.entities;

import com.medic.system.dtos.doctor.DoctorRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "doctors")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class Doctor extends User {
    private boolean isGeneralPractitioner;

    public Doctor() {
    }

    public Doctor(DoctorRequestDto doctorRequestDto) {
        super(doctorRequestDto);
        isGeneralPractitioner = doctorRequestDto.getIsGeneralPractitioner();
    }

//    @CollectionTable(name = "doctor_specialties", joinColumns = @JoinColumn(name = "doctor_id"))
//    @Column(name = "specialty")
//    @ElementCollection
//    private List<String> specialties;
}
