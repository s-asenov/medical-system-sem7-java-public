package com.medic.system.entities;

import com.medic.system.dtos.doctor.DoctorRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class Doctor extends User implements Serializable {
    @Column(nullable = false)
    private Boolean isGeneralPractitioner = false;

    @ManyToMany
    @JoinTable(
            name = "doctor_specialities",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "speciality_id")
    )
    @OrderBy("name ASC")
    private List<Speciality> specialities = new ArrayList<>();

    @OneToMany(mappedBy = "doctor")
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<MedicalAppointment> medicalAppointments = new ArrayList<>();

    @OneToMany(mappedBy = "generalPractitioner")
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Patient> patients = new ArrayList<>();

    // Formula for counting patients associated with this doctor
    @Formula("(SELECT COUNT(*) FROM patients p WHERE p.general_practitioner_id = user_id)")
    private Long patientsCount;

    // Formula for counting medical appointments associated with this doctor
    @Formula("(SELECT COUNT(*) FROM medical_appointments m WHERE m.doctor_id = user_id)")
    private Long medicalAppointmentsCount;

    public Doctor() {
    }

    public Doctor(DoctorRequestDto doctorRequestDto) {
        super(doctorRequestDto);
        isGeneralPractitioner = doctorRequestDto.getIsGeneralPractitioner();
    }

    public void addSpeciality(Speciality speciality) {
        specialities.add(speciality);
    }

    public void clearSpecialities() {
        specialities.clear();
    }
}
