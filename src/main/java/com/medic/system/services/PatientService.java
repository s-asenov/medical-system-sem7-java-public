package com.medic.system.services;

import com.medic.system.dtos.PatientRequestDto;
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

    public Patient save(PatientRequestDto patientRequestDto, BindingResult bindingResult) {
        if (patientRequestDto == null) {
            bindingResult.reject("patientRequestDto", "PatientRequestDto cannot be null");
            return null;
        }

        Doctor doctor;

        try {
            doctor = doctorService.isDoctorAndGp(patientRequestDto.getGeneralPractitionerId());
        } catch (IllegalArgumentException e) {
            bindingResult.reject("generalPractitionerId", e.getMessage());
            return null;
        }


        patientRequestDto.setPassword(passwordEncoder.encode(patientRequestDto.getPassword()));
        Patient patient = new Patient(patientRequestDto, doctor);

        try {
            return patientRepository.save(patient);
        } catch (DataIntegrityViolationException e) {
            // set to generalPractitionerId because it is last one
            bindingResult.reject("generalPractitionerId", "Database error: " + e.getMostSpecificCause().getMessage());
            return null;
        }
    }
}
