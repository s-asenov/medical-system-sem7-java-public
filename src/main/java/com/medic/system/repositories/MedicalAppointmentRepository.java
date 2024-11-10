package com.medic.system.repositories;

import com.medic.system.entities.MedicalAppointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalAppointmentRepository extends JpaRepository<MedicalAppointment, Long> {
    boolean existsByIdAndDoctorId(Long id, Long doctorId);

    Page<MedicalAppointment> findAllByDoctorId(Long id, Pageable pageable);
    List<MedicalAppointment> findAllByDoctorId(Long id);
    Page<MedicalAppointment> findAllByPatientId(Long id, Pageable pageable);
    List<MedicalAppointment> findAllByPatientId(Long id);
}