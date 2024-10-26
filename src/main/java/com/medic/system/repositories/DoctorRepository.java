package com.medic.system.repositories;

import com.medic.system.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findAllByIsGeneralPractitioner(boolean isGeneralPractitioner);
}