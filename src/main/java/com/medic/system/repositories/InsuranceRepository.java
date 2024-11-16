package com.medic.system.repositories;

import com.medic.system.entities.Insurance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    boolean existsByPatientIdAndInsuranceDateAndIdNot(Long patientId, LocalDate insuranceDate, Long Id);
    boolean existsByPatientIdAndInsuranceDate(Long patientId, LocalDate insuranceDate);

    Page<Insurance> findAllByPatientId(Long patientId, Pageable pageable);
    Page<Insurance> findAllByPatient_GeneralPractitionerId(Long generalPractitionerId, Pageable pageable);

    boolean existsByIdAndPatientId(Long id, Long patientId);

    @Query("SELECT i FROM Insurance i WHERE i.patient.id = :patientId AND i.insuranceDate BETWEEN :startDate AND :endDate")
    List<Insurance> findInsurancesInDateRange(Long patientId, LocalDate startDate, LocalDate endDate);

    List<Insurance> findAllByPatientIdAndInsuranceDateBetween(Long patientId, LocalDate startDate, LocalDate endDate);
}