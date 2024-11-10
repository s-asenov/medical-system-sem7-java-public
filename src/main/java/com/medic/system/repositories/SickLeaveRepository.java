package com.medic.system.repositories;

import com.medic.system.entities.SickLeave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SickLeaveRepository extends JpaRepository<SickLeave, Long> {
    Page<SickLeave> findAllByMedicalAppointment_PatientId(Long patientId, Pageable pageable);
    Page<SickLeave> findAllByMedicalAppointment_DoctorId(Long doctorId, Pageable pageable);
}