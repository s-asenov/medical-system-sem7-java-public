package com.medic.system.repositories;

import com.medic.system.dtos.diagnose.DiagnoseMedicalAppointmentCount;
import com.medic.system.entities.Diagnose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiagnoseRepository extends JpaRepository<Diagnose, Long> {
    boolean existsByName(String name);

    List<Diagnose> findAllByOrderByName();

    @Query("SELECT new com.medic.system.dtos.diagnose.DiagnoseMedicalAppointmentCount(d, COUNT(ma)) " +
            "FROM Diagnose d JOIN d.medicalAppointments ma " +
            "GROUP BY d ORDER BY COUNT(ma) DESC")
    List<DiagnoseMedicalAppointmentCount> getDiagnosesAndMedicalAppointmentsCount();
}