package com.medic.system.services;

import com.medic.system.entities.Doctor;
import com.medic.system.entities.Patient;
import com.medic.system.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    public Page<Patient> findAll(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }
}
