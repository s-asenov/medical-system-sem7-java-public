package com.medic.system.repositories;

import com.medic.system.dtos.doctor.DoctorWithAppointmentCount;
import com.medic.system.entities.Diagnose;
import com.medic.system.entities.Doctor;
import com.medic.system.entities.MedicalAppointment;
import com.medic.system.entities.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MedicalAppointmentRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MedicalAppointmentRepository medicalAppointmentRepository;

    private Doctor doctor1;
    private Doctor doctor2;
    private Patient patient;
    private Diagnose diagnose;

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
        diagnose.setDescription("Вирусно заболяване");
        testEntityManager.persistAndFlush(diagnose);
    }

    @Test
    void existsByIdAndDoctorIdTest() {
        MedicalAppointment appointment = new MedicalAppointment();
        appointment.setDate(LocalDate.now().plusDays(1));
        appointment.setDoctor(doctor1);
        appointment.setPatient(patient);
        appointment.setDiagnose(diagnose);
        testEntityManager.persistAndFlush(appointment);

        boolean exists = medicalAppointmentRepository.existsByIdAndDoctorId(appointment.getId(), doctor1.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void countDoctorAppointmentsTest() {
        MedicalAppointment appointment1 = new MedicalAppointment();
        appointment1.setDate(LocalDate.now().plusDays(1));
        appointment1.setDoctor(doctor1);
        appointment1.setPatient(patient);
        appointment1.setDiagnose(diagnose);
        testEntityManager.persistAndFlush(appointment1);

        MedicalAppointment appointment2 = new MedicalAppointment();
        appointment2.setDate(LocalDate.now().plusDays(2));
        appointment2.setDoctor(doctor1);
        appointment2.setPatient(patient);
        appointment2.setDiagnose(diagnose);
        testEntityManager.persistAndFlush(appointment2);

        MedicalAppointment appointment3 = new MedicalAppointment();
        appointment3.setDate(LocalDate.now().plusDays(3));
        appointment3.setDoctor(doctor2);
        appointment3.setPatient(patient);
        appointment3.setDiagnose(diagnose);
        testEntityManager.persistAndFlush(appointment3);

        List<DoctorWithAppointmentCount> result = medicalAppointmentRepository.countDoctorAppointments();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getDoctor().getId()).isEqualTo(doctor1.getId());
        assertThat(result.get(0).getAppointmentCount()).isEqualTo(2);
        assertThat(result.get(1).getDoctor().getId()).isEqualTo(doctor2.getId());
        assertThat(result.get(1).getAppointmentCount()).isEqualTo(1);
    }
}