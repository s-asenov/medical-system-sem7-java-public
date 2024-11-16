package com.medic.system.repositories;

import com.medic.system.dtos.sick_leave.DoctorWithSickLeaveCount;
import com.medic.system.dtos.sick_leave.MonthWithSickLeaveCount;
import com.medic.system.entities.SickLeave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SickLeaveRepository extends JpaRepository<SickLeave, Long> {
    Page<SickLeave> findAllByMedicalAppointment_PatientId(Long patientId, Pageable pageable);
    Page<SickLeave> findAllByMedicalAppointment_DoctorId(Long doctorId, Pageable pageable);

    @Query("SELECT new com.medic.system.dtos.sick_leave.DoctorWithSickLeaveCount(sl.medicalAppointment.doctor, COUNT(sl)) " +
            "FROM SickLeave sl " +
            "GROUP BY sl.medicalAppointment.doctor " +
            "HAVING COUNT(sl) = (SELECT MAX(sickLeaveCount) FROM (SELECT COUNT(sl2) AS sickLeaveCount " +
            "FROM SickLeave sl2 " +
            "GROUP BY sl2.medicalAppointment.doctor) AS subquery)")
    List<DoctorWithSickLeaveCount> doctorWithMostSickLeaves();

    @Query("SELECT new com.medic.system.dtos.sick_leave.MonthWithSickLeaveCount(MONTH(sl.startDate), COUNT(sl)) " +
                  "FROM SickLeave sl " +
                  "WHERE YEAR(sl.startDate) = :year " +
                  "GROUP BY MONTH(sl.startDate) " +
                  "HAVING COUNT(sl) = (SELECT MAX(sickLeaveCount) FROM (SELECT COUNT(sl2) AS sickLeaveCount " +
                  "FROM SickLeave sl2 " +
                  "WHERE YEAR(sl2.startDate) = :year " +
                  "GROUP BY MONTH(sl2.startDate)) AS subquery)")
    List<MonthWithSickLeaveCount> monthWithMostSickLeaves(Integer year);
}