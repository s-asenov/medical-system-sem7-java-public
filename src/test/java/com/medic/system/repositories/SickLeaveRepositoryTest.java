package com.medic.system.repositories;

import com.medic.system.entities.MedicalAppointment;
import com.medic.system.entities.Doctor;
import com.medic.system.entities.Patient;
import com.medic.system.entities.Diagnose;
import com.medic.system.entities.SickLeave;
import com.medic.system.dtos.sick_leave.DoctorWithSickLeaveCount;
import com.medic.system.dtos.sick_leave.MonthWithSickLeaveCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SickLeaveRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private SickLeaveRepository sickLeaveRepository;

    private Doctor doctor1;
    private Doctor doctor2;
    private Patient patient;
    private Diagnose diagnose;
    private MedicalAppointment appointment;
    private MedicalAppointment appointment2;

    @BeforeEach
    void setUp() {
        doctor1 = new Doctor();
        doctor1.setUsername("doctor1");
        doctor1.setFirstName("Петър");
        doctor1.setLastName("Петров");
        doctor1.setPassword("password");
        testEntityManager.persistAndFlush(doctor1);

        doctor2 = new Doctor();
        doctor2.setUsername("doctor2");
        doctor2.setFirstName("Мария");
        doctor2.setLastName("Маринова");
        doctor2.setPassword("password");
        testEntityManager.persistAndFlush(doctor2);

        patient = new Patient();
        patient.setUsername("patient1");
        patient.setFirstName("Иван");
        patient.setLastName("Иванов");
        patient.setPassword("password");
        patient.setGeneralPractitioner(doctor1);
        patient.setEgn("1234567890");
        testEntityManager.persistAndFlush(patient);

        diagnose = new Diagnose();
        diagnose.setName("Грип");
        diagnose.setDescription("Описание");
        testEntityManager.persistAndFlush(diagnose);

        appointment = new MedicalAppointment();
        appointment.setDate(LocalDate.now().plusDays(1));
        appointment.setDoctor(doctor1);
        appointment.setPatient(patient);
        appointment.setDiagnose(diagnose);
        testEntityManager.persistAndFlush(appointment);

        appointment2 = new MedicalAppointment();
        appointment2.setDate(LocalDate.now().plusDays(2));
        appointment2.setDoctor(doctor2);
        appointment2.setPatient(patient);
        appointment2.setDiagnose(diagnose);
        testEntityManager.persistAndFlush(appointment2);
    }

    @Test
    void findAllByMedicalAppointment_PatientIdTest() {
        SickLeave sickLeave = new SickLeave();
        sickLeave.setMedicalAppointment(appointment);
        sickLeave.setStartDate(LocalDate.now().plusDays(1));
        sickLeave.setDays(7);
        testEntityManager.persistAndFlush(sickLeave);

        Pageable pageable = PageRequest.of(0, 10);
        Page<SickLeave> result = sickLeaveRepository.findAllByMedicalAppointment_PatientId(patient.getId(), pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findAllByMedicalAppointment_DoctorIdTest() {
        SickLeave sickLeave = new SickLeave();
        sickLeave.setMedicalAppointment(appointment);
        sickLeave.setStartDate(LocalDate.now().plusDays(1));
        sickLeave.setDays(7);
        testEntityManager.persistAndFlush(sickLeave);

        Pageable pageable = PageRequest.of(0, 10);
        Page<SickLeave> result = sickLeaveRepository.findAllByMedicalAppointment_DoctorId(doctor1.getId(), pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void doctorWithMostSickLeavesTest() {
        SickLeave sickLeave1 = new SickLeave();
        sickLeave1.setMedicalAppointment(appointment);
        sickLeave1.setStartDate(LocalDate.now().plusDays(1));
        sickLeave1.setDays(7);
        testEntityManager.persistAndFlush(sickLeave1);

        SickLeave sickLeave2 = new SickLeave();
        sickLeave2.setMedicalAppointment(appointment2);
        sickLeave2.setStartDate(LocalDate.now().plusDays(2));
        sickLeave2.setDays(3);
        testEntityManager.persistAndFlush(sickLeave2);

        List<DoctorWithSickLeaveCount> result = sickLeaveRepository.doctorWithMostSickLeaves();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getDoctor().getId()).isEqualTo(doctor1.getId());
    }

    @Test
    void monthWithMostSickLeavesTest() {
        SickLeave sickLeave1 = new SickLeave();
        sickLeave1.setMedicalAppointment(appointment);
        sickLeave1.setStartDate(LocalDate.now().plusDays(1));
        sickLeave1.setDays(7);
        testEntityManager.persistAndFlush(sickLeave1);

        SickLeave sickLeave2 = new SickLeave();
        sickLeave2.setMedicalAppointment(appointment2);
        sickLeave2.setStartDate(LocalDate.now().plusDays(15));
        sickLeave2.setDays(3);
        testEntityManager.persistAndFlush(sickLeave2);

        List<MonthWithSickLeaveCount> result = sickLeaveRepository.monthWithMostSickLeaves(2024);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getMonth()).isEqualTo(LocalDate.now().plusDays(1).getMonthValue());
    }
}