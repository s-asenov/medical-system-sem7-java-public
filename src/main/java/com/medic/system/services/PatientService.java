package com.medic.system.services;

import com.medic.system.dtos.patient.EditPatientRequestDto;
import com.medic.system.dtos.patient.PatientRequestDto;
import com.medic.system.dtos.patient.PatientSearchDto;
import com.medic.system.entities.Doctor;
import com.medic.system.entities.Insurance;
import com.medic.system.entities.Patient;
import com.medic.system.repositories.DoctorRepository;
import com.medic.system.repositories.InsuranceRepository;
import com.medic.system.repositories.PatientRepository;
import com.medic.system.specifications.PatientSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final InsuranceRepository insuranceRepository;

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Page<Patient> findAll(Pageable pageable, PatientSearchDto searchForm) {
        Specification<Patient> specification = Specification.where(PatientSpecification.hasNameLike(searchForm.getName()))
                .and(PatientSpecification.hasDoctorId(searchForm.getGeneralPractitionerId()));

        Page<Patient> patientsPage = patientRepository.findAll(specification, pageable);

        return patientsPage.map(patient -> {
            patient.setHasPaidInsuranceLast6Months(hasPaidInsuranceLast6Months(patient.getId()));
            return patient;
        });
    }

    public Patient findById(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Пациентът не е намерен"));
    }

    public Patient save(PatientRequestDto patientRequestDto, BindingResult bindingResult) {
        if (patientRequestDto == null) {
            bindingResult.rejectValue("generalPractitionerId", "error.patient", "Грешка при създаване на пациент");
            return null;
        }

        Doctor doctor;

        try {
            doctor = findGeneralPractitioner(patientRequestDto.getGeneralPractitionerId());
        } catch (NoSuchElementException e) {
            bindingResult.rejectValue("generalPractitionerId", "error.patient", e.getMessage());
            return null;
        }

        patientRequestDto.setPassword(passwordEncoder.encode(patientRequestDto.getPassword()));
        Patient patient = new Patient(patientRequestDto, doctor);

        try {
            return patientRepository.save(patient);
        } catch (DataIntegrityViolationException e) {
            // set to generalPractitionerId because it is last one
            bindingResult.rejectValue("generalPractitionerId", "error.patient",  "Грешка при създаване на пациент");
            return null;
        }
    }

    public Patient update(Long id, EditPatientRequestDto editPatientRequestDto, BindingResult bindingResult) {
        if (editPatientRequestDto == null) {
            bindingResult.rejectValue("generalPractitionerId", "error.patient", "Грешка при редактиране на пациент");
            return null;
        }

        Patient patient;
        try {
            patient = findById(id);
        } catch (NoSuchElementException e) {
            bindingResult.rejectValue("username", "error.patient", e.getMessage());
            return null;
        }

        Doctor doctor;

        try {
            doctor = findGeneralPractitioner(editPatientRequestDto.getGeneralPractitionerId());
        } catch (NoSuchElementException e) {
            bindingResult.rejectValue("generalPractitionerId", "error.patient", e.getMessage());
            return null;
        }

        patient.setFirstName(editPatientRequestDto.getFirstName());
        patient.setLastName(editPatientRequestDto.getLastName());
        patient.setEgn(editPatientRequestDto.getEgn());
        patient.setUsername(editPatientRequestDto.getUsername());

        // if password not empty encode and set it
        if (editPatientRequestDto.getPassword() != null && !editPatientRequestDto.getPassword().isEmpty()) {
            patient.setPassword(passwordEncoder.encode(editPatientRequestDto.getPassword()));
        }

        try {
            return patientRepository.save(patient);
        } catch (DataIntegrityViolationException e) {
            // set to generalPractitionerId because it is last one
            bindingResult.rejectValue("generalPractitionerId", "error.patient",  "Грешка при редактиране на пациент");
            return null;
        }
    }

    private boolean hasPaidInsuranceLast6Months(Long patientId) {
        LocalDate now = LocalDate.now();
        LocalDate sixMonthsAgo = now.minus(6, ChronoUnit.MONTHS).withDayOfMonth(1);

        List<Insurance> insurances = insuranceRepository.findAllByPatientIdAndInsuranceDateBetween(patientId, sixMonthsAgo, now);

        List<LocalDate> requiredMonths = IntStream.rangeClosed(1, 6)
                .mapToObj(i -> sixMonthsAgo.plusMonths(i))
                .collect(Collectors.toList());

        List<LocalDate> paidMonths = insurances.stream()
                .map(Insurance::getInsuranceDate)
                .collect(Collectors.toList());

        return requiredMonths.stream().allMatch(paidMonths::contains);
    }

    private Doctor findGeneralPractitioner(Long generalPractitionerId) {
        Doctor doctor = doctorRepository.findById(generalPractitionerId)
                .orElseThrow(() -> new NoSuchElementException("Докторът не е намерен"));

        if (doctor.getIsGeneralPractitioner() != true) {
            throw new NoSuchElementException("Докторът не е общопрактикуващ лекар");
        }

        return doctor;
    }
}
