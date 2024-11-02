package com.medic.system.services;

import com.medic.system.entities.Doctor;
import com.medic.system.entities.User;
import com.medic.system.repositories.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.ArrayList;
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

    public boolean isCurrentUserGp() {
        return UserServiceImpl.getCurrentUser().isDoctor() && ((Doctor) UserServiceImpl.getCurrentUser()).isGeneralPractitioner();
    }

    public List<Doctor> getListOfGps() {
        List<Doctor> gps = new ArrayList<>();
        User currentUser = UserServiceImpl.getCurrentUser();

        if (currentUser.isDoctor()) {
            if (((Doctor) currentUser).isGeneralPractitioner()) {
                gps.add((Doctor) UserServiceImpl.getCurrentUser());
            }
        } else {
            gps = this.findAllGeneralPractitioners();
        }

        return gps;
    }

    public Doctor isDoctorAndGp(Long id)
    {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Няма такъв доктор"));

        if (!doctor.isGeneralPractitioner()) {
            throw new IllegalArgumentException("Докторът не е общопрактикуващ");
        }

        return doctor;
    }
}
