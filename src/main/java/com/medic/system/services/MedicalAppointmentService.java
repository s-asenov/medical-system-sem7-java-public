package com.medic.system.services;

import com.medic.system.dtos.medical_appointment.EditMedicalAppointmentRequestDto;
import com.medic.system.dtos.medical_appointment.MedicalAppointmentRequestDto;
import com.medic.system.entities.*;
import com.medic.system.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MedicalAppointmentService {
    private final MedicalAppointmentRepository medicalAppointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DiagnoseRepository diagnoseRepository;
    private final DrugRepository drugRepository;

    public MedicalAppointment create(MedicalAppointmentRequestDto appointmentDto, BindingResult bindingResult)
    {
        if (appointmentDto == null) {
            bindingResult.rejectValue("date", "error.medical_appointment", "Грешка при създаване на специалност");
            return null;
        }

        Doctor doctor;
        try {
            doctor = doctorRepository.findById(appointmentDto.getDoctorId()).orElseThrow();
        } catch (Exception e) {
            bindingResult.rejectValue("doctorId", "error.medical_appointment", "Докторът не съществува");
            return null;
        }

        Patient patient;
        try {
            patient = patientRepository.findById(appointmentDto.getPatientId()).orElseThrow();
        } catch (Exception e) {
            bindingResult.rejectValue("patientId", "error.medical_appointment", "Пациентът не съществува");
            return null;
        }

        Diagnose diagnose;
        try {
            diagnose = diagnoseRepository.findById(appointmentDto.getDiagnoseId()).orElseThrow();
        } catch (Exception e) {
            bindingResult.rejectValue("diagnoseId", "error.medical_appointment", "Диагнозата не съществува");
            return null;
        }

        MedicalAppointment appointment = new MedicalAppointment();
        appointment.setDate(appointmentDto.getDate());
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDiagnose(diagnose);

        // add drugs, if error return null and put error in bindingResult
        try {
            for (Long drugId : appointmentDto.getDrugs()) {
                Drug drug = drugRepository.findById(drugId)
                        .orElseThrow(() -> new NoSuchElementException("Лекарството не е намерена"));
                appointment.addDrug(drug);
            }
        } catch (NoSuchElementException e) {
            bindingResult.rejectValue("specialities", "error.medical_appointment", "Грешка при добавяне на лекарства");
            return null;
        }

        try {
            return medicalAppointmentRepository.save(appointment);
        } catch (Exception e) {
            bindingResult.rejectValue("date", "error.medical_appointment", "Грешка при създаване на специалност");
            return null;
        }
    }

    public MedicalAppointment update(Long id, EditMedicalAppointmentRequestDto editAppointmentDto, BindingResult bindingResult) {
        if (editAppointmentDto == null) {
            bindingResult.rejectValue("date", "error.medical_appointment", "Грешка при редактиране на специалност");
            return null;
        }

        MedicalAppointment appointment;
        try {
            appointment = findById(id);
        } catch (Exception e) {
            bindingResult.rejectValue("date", "error.medical_appointment", "Специалността не съществува");
            return null;
        }

        Doctor doctor;
        try {
            doctor = doctorRepository.findById(editAppointmentDto.getDoctorId()).orElseThrow();
        } catch (Exception e) {
            bindingResult.rejectValue("doctorId", "error.medical_appointment", "Докторът не съществува");
            return null;
        }

        Patient patient;
        try {
            patient = patientRepository.findById(editAppointmentDto.getPatientId()).orElseThrow();
        } catch (Exception e) {
            bindingResult.rejectValue("patientId", "error.medical_appointment", "Пациентът не съществува");
            return null;
        }

        Diagnose diagnose;
        try {
            diagnose = diagnoseRepository.findById(editAppointmentDto.getDiagnoseId()).orElseThrow();
        } catch (Exception e) {
            bindingResult.rejectValue("diagnoseId", "error.medical_appointment", "Диагнозата не съществува");
            return null;
        }

        // add drugs, if error return null and put error in bindingResult
        try {
            appointment.clearDrugs();
            for (Long drugId : editAppointmentDto.getDrugs()) {
                Drug drug = drugRepository.findById(drugId)
                        .orElseThrow(() -> new NoSuchElementException("Лекарството не е намерена"));
                appointment.addDrug(drug);
            }
        } catch (NoSuchElementException e) {
            bindingResult.rejectValue("specialities", "error.medical_appointment", "Грешка при добавяне на лекарства");
            return null;
        }

        appointment.setDate(editAppointmentDto.getDate());
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDiagnose(diagnose);

        try {
            return medicalAppointmentRepository.save(appointment);
        } catch (Exception e) {
            bindingResult.rejectValue("date", "error.medical_appointment", "Грешка при редактиране на специалност");
            return null;
        }
    }

    public MedicalAppointment findById(Long id) {
        return medicalAppointmentRepository.findById(id).orElseThrow();
    }

    public void deleteById(Long id) {
        medicalAppointmentRepository.deleteById(id);
    }

    public boolean isDoctorAppointment(Long appointmentId, Long doctorId) {
        return medicalAppointmentRepository.existsByIdAndDoctorId(appointmentId, doctorId);
    }

    public Page<MedicalAppointment> findAllBasedOnRole(Pageable pageable) {
        User user = UserServiceImpl.getCurrentUser();

        if (user.isAdmin()) {
            return medicalAppointmentRepository.findAll(pageable);
        }

        if (user.isDoctor()) {
            return medicalAppointmentRepository.findAllByDoctorId(user.getId(), pageable);
        }

        return medicalAppointmentRepository.findAllByPatientId(user.getId(), pageable);
    }

    public List<MedicalAppointment> findAllBasedOnRole() {
        User user = UserServiceImpl.getCurrentUser();

        if (user.isAdmin()) {
            return medicalAppointmentRepository.findAll();
        }

        if (user.isDoctor()) {
            return medicalAppointmentRepository.findAllByDoctorId(user.getId());
        }

        return medicalAppointmentRepository.findAllByPatientId(user.getId());
    }
}
