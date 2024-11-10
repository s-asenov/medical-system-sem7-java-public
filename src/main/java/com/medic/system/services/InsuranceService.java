package com.medic.system.services;

import com.medic.system.dtos.insurance.EditInsuranceRequestDto;
import com.medic.system.dtos.insurance.InsuranceRequestDto;
import com.medic.system.entities.*;
import com.medic.system.repositories.InsuranceRepository;
import com.medic.system.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class InsuranceService {
    private final PatientRepository patientRepository;
    private final InsuranceRepository insuranceRepository;

    public List<Insurance> findAll() {
        return insuranceRepository.findAll();
    }

    public Page<Insurance> findAll(Pageable pageable) {
        return insuranceRepository.findAll(pageable);
    }

    public Insurance create(InsuranceRequestDto insuranceDto, BindingResult bindingResult)
    {
        if (insuranceDto == null) {
            bindingResult.rejectValue("date", "error.insurance", "Грешка при създаване на осигуровка");
            return null;
        }

        Patient patient;
        try {
            patient = patientRepository.findById(insuranceDto.getPatientId()).orElseThrow();
        } catch (Exception e) {
            bindingResult.rejectValue("patientId", "error.insurance", "Пациентът не съществува");
            return null;
        }

        boolean existing = insuranceRepository.existsByPatientIdAndInsuranceDate(patient.getId(), insuranceDto.getInsuranceDate());

        if (existing) {
            bindingResult.rejectValue("insuranceMonth", "error.insurance", "Пациентът вече има платена осигуровка за този месец");
            return null;
        }

        Insurance insurance = new Insurance();
        insurance.setPatient(patient);
        insurance.setInsuranceDate(LocalDate.of(insuranceDto.getInsuranceYear(), insuranceDto.getInsuranceMonth(), 1));
        insurance.setDateOfPayment(insuranceDto.getDateOfPayment());
        insurance.setSum(insuranceDto.getSum());

        try {
            return insuranceRepository.save(insurance);
        } catch (Exception e) {
            bindingResult.rejectValue("date", "error.insurance", "Грешка при създаване на осигуровка");
            return null;
        }
    }

    public Insurance update(Long id, EditInsuranceRequestDto editInsuranceDto, BindingResult bindingResult) {
        if (editInsuranceDto == null) {
            bindingResult.rejectValue("date", "error.insurance", "Грешка при редактиране на осигуровка");
            return null;
        }

        Insurance insurance;
        try {
            insurance = findById(id);
        } catch (Exception e) {
            bindingResult.rejectValue("date", "error.insurance", "Специалността не съществува");
            return null;
        }

        Patient patient;
        try {
            patient = patientRepository.findById(editInsuranceDto.getPatientId()).orElseThrow();
        } catch (Exception e) {
            bindingResult.rejectValue("patientId", "error.insurance", "Пациентът не съществува");
            return null;
        }

        boolean existing = insuranceRepository.existsByPatientIdAndInsuranceDateAndIdNot(patient.getId(), editInsuranceDto.getInsuranceDate(), id);

        if (existing) {
            bindingResult.rejectValue("insuranceMonth", "error.insurance", "Пациентът вече има платена осигуровка за този месец");
            return null;
        }

        insurance.setPatient(patient);
        insurance.setInsuranceDate(editInsuranceDto.getInsuranceDate());
        insurance.setDateOfPayment(editInsuranceDto.getDateOfPayment());
        insurance.setSum(editInsuranceDto.getSum());

        try {
            return insuranceRepository.save(insurance);
        } catch (Exception e) {
            bindingResult.rejectValue("date", "error.insurance", "Грешка при редактиране на осигуровка");
            return null;
        }
    }

    public Insurance findById(Long id) {
        return insuranceRepository.findById(id).orElseThrow();
    }

    public void deleteById(Long id) {
        insuranceRepository.deleteById(id);
    }

    public Page<Insurance> findAllBasedOnRole(Pageable pageable) {
        User user = UserServiceImpl.getCurrentUser();

        if (user.isAdmin()) {
            return findAll(pageable);
        }

        if (user.isDoctor()) {
            return insuranceRepository.findAllByPatient_GeneralPractitionerId(user.getId(), pageable);
        }

        return insuranceRepository.findAllByPatientId(user.getId(), pageable);
    }

    public boolean isPatientInsurance(Long insuranceId, Long patientId) {
        return insuranceRepository.existsByIdAndPatientId(insuranceId, patientId);
    }
}
