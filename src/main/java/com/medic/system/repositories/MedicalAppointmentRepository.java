package com.medic.system.repositories;

import com.medic.system.entities.MedicalAppointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface MedicalAppointmentRepository extends JpaRepository<MedicalAppointment, Long>, JpaSpecificationExecutor<MedicalAppointment> {
    boolean existsByIdAndDoctorId(Long id, Long doctorId);
}