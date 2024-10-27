package com.medic.system.repositories;

import aj.org.objectweb.asm.commons.Remapper;
import com.medic.system.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findAllByIsGeneralPractitioner(boolean isGeneralPractitioner);
}