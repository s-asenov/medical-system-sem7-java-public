package com.medic.system.services;

import com.medic.system.dtos.doctor.DoctorRequestDto;
import com.medic.system.dtos.doctor.EditDoctorRequestDto;
import com.medic.system.entities.Doctor;
import com.medic.system.entities.Speciality;
import com.medic.system.entities.User;
import com.medic.system.repositories.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final SpecialityService specialityService;

    public List<Doctor> findAllGeneralPractitioners() {
        return doctorRepository.findAllByIsGeneralPractitioner(true);
    }

    public Page<Doctor> findAll(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    public boolean isCurrentUserGp() {
        return UserServiceImpl.getCurrentUser().isDoctor() && ((Doctor) UserServiceImpl.getCurrentUser()).getIsGeneralPractitioner();
    }

    public List<Doctor> getListOfGps() {
        List<Doctor> gps = new ArrayList<>();
        User currentUser = UserServiceImpl.getCurrentUser();

        if (currentUser.isDoctor()) {
            if (((Doctor) currentUser).getIsGeneralPractitioner()) {
                gps.add((Doctor) UserServiceImpl.getCurrentUser());
            }
        } else {
            gps = findAllGeneralPractitioners();
        }

        return gps;
    }

    public Doctor isDoctorAndGp(Long id)
    {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Няма такъв доктор"));

        if (!doctor.getIsGeneralPractitioner()) {
            throw new IllegalArgumentException("Докторът не е общопрактикуващ");
        }

        return doctor;
    }

    public Doctor create(DoctorRequestDto doctorRequestDto, BindingResult bindingResult)
    {
        if (doctorRequestDto == null) {
            bindingResult.rejectValue("username", "error.doctor", "Грешка при създаване на доктор");
            return null;
        }

        doctorRequestDto.setPassword(passwordEncoder.encode(doctorRequestDto.getPassword()));

        Doctor doctor = new Doctor(doctorRequestDto);

        // add specialities, if error return null and put error in bindingResult
        try {
            for (Long specialityId : doctorRequestDto.getSpecialities()) {
                Speciality speciality = specialityService.findById(specialityId);
                doctor.addSpeciality(speciality);
            }
        } catch (Exception e) {
            bindingResult.rejectValue("specialities", "error.doctor", "Грешка при добавяне на специалности");
            return null;
        }

        try {
            return doctorRepository.save(doctor);
        } catch (DataIntegrityViolationException e) {
            // set to specialities because it is last one
            bindingResult.rejectValue("specialities", "error.doctor",  "Грешка при създаване на доктор");
            return null;
        }
    }

    public Doctor update(Long id, EditDoctorRequestDto editDoctorRequestDto, BindingResult bindingResult) {
        Doctor doctor;

        try {
            doctor = findById(id);
        } catch (Exception e) {
            bindingResult.rejectValue("username", "error.doctor", "Докторът не е намерен");
            return null;
        }

        doctor.setIsGeneralPractitioner(editDoctorRequestDto.getIsGeneralPractitioner());
        doctor.setFirstName(editDoctorRequestDto.getFirstName());
        doctor.setLastName(editDoctorRequestDto.getLastName());
        doctor.setUsername(editDoctorRequestDto.getUsername());

        // add specialities, if error return null and put error in bindingResult
        try {
            doctor.clearSpecialities();
            for (Long specialityId : editDoctorRequestDto.getSpecialities()) {
                Speciality speciality = specialityService.findById(specialityId);
                doctor.addSpeciality(speciality);
            }
        } catch (Exception e) {
            bindingResult.rejectValue("specialities", "error.doctor", "Грешка при добавяне на специалности");
            return null;
        }

        if (editDoctorRequestDto.getPassword() != null && !editDoctorRequestDto.getPassword().isEmpty()) {
            doctor.setPassword(passwordEncoder.encode(editDoctorRequestDto.getPassword()));
        }

        try {
            Doctor doc = doctorRepository.save(doctor);

            // if doc is current user update principal with new data
            if (doc.getId().equals(UserServiceImpl.getCurrentUser().getId())) {
                UserServiceImpl.setCurrentUser(doc);
            }

            return doc;
        } catch (DataIntegrityViolationException e) {
            // set to generalPractitionerId because it is last one
            bindingResult.rejectValue("isGeneralPractitioner", "error.doctor",  "Грешка при редактиране на доктор");
            return null;
        }
    }

    public Doctor findById(Long id) {
        return doctorRepository.findById(id).orElseThrow();
    }
}
