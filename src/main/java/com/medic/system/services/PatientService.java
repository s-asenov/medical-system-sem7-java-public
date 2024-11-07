package com.medic.system.services;

import com.medic.system.dtos.patient.EditPatientRequestDto;
import com.medic.system.dtos.patient.PatientRequestDto;
import com.medic.system.entities.Doctor;
import com.medic.system.entities.Patient;
import com.medic.system.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;


@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PasswordEncoder passwordEncoder;

    public Page<Patient> findAll(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    public Patient findById(Long id) {
        return patientRepository.findById(id).orElseThrow();
    }

    public Patient save(PatientRequestDto patientRequestDto, BindingResult bindingResult) {
        if (patientRequestDto == null) {
            bindingResult.rejectValue("generalPractitionerId", "error.patient", "Грешка при създаване на пациент");
            return null;
        }

        Doctor doctor;

        try {
            doctor = doctorService.isDoctorAndGp(patientRequestDto.getGeneralPractitionerId());
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("generalPractitionerId", "error.patient", "Грешка при създаване на пациент");
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
        } catch (Exception e) {
            bindingResult.rejectValue("username", "error.patient", "Пациентът не е намерен");
            return null;
        }

        Doctor doctor;

        try {
            doctor = doctorService.isDoctorAndGp(editPatientRequestDto.getGeneralPractitionerId());
            patient.setGeneralPractitioner(doctor);
        } catch (IllegalArgumentException e) {
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
}
