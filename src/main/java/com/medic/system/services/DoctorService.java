package com.medic.system.services;

import com.medic.system.dtos.doctor.DoctorRequestDto;
import com.medic.system.dtos.doctor.EditDoctorRequestDto;
import com.medic.system.entities.Doctor;
import com.medic.system.entities.Speciality;
import com.medic.system.repositories.DoctorRepository;
import com.medic.system.repositories.SpecialityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final SpecialityRepository specialityRepository;

    public List<Doctor> findAllGeneralPractitioners() {
        return doctorRepository.findAllByIsGeneralPractitioner(true);
    }

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    public Page<Doctor> findAll(Pageable pageable) {
        return doctorRepository.findAll(pageable);
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
                Speciality speciality = specialityRepository.findById(specialityId)
                        .orElseThrow(() -> new NoSuchElementException("Специалността не е намерена"));
                doctor.addSpeciality(speciality);
            }
        } catch (NoSuchElementException e) {
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
                Speciality speciality = specialityRepository.findById(specialityId)
                        .orElseThrow(() -> new NoSuchElementException("Специалността не е намерена"));;
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
