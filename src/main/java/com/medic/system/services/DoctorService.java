package com.medic.system.services;

import com.medic.system.entities.Doctor;
import com.medic.system.repositories.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public List<Doctor> findAllGeneralPractitioners() {
        return doctorRepository.findAllByIsGeneralPractitioner(true);
    }

    public Page<Doctor> findAll(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    public boolean isCurrentUserGp(Long id) {
        return doctorRepository.findById(id)
                .map(user -> user.isGeneralPractitioner())
                .orElse(false);
    }
}
