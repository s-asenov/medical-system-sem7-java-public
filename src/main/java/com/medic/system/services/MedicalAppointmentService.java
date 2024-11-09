package com.medic.system.services;

import com.medic.system.dtos.medical_appointment.EditMedicalAppointmentRequestDto;
import com.medic.system.dtos.medical_appointment.MedicalAppointmentRequestDto;
import com.medic.system.entities.*;
import com.medic.system.repositories.MedicalAppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalAppointmentService {
    private final MedicalAppointmentRepository medicalAppointmentRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final DiagnoseService diagnoseService;

    public List<MedicalAppointment> findAll() {
        return medicalAppointmentRepository.findAll();
    }

    public Page<MedicalAppointment> findAll(Pageable pageable) {
        return medicalAppointmentRepository.findAll(pageable);
    }

    public MedicalAppointment create(MedicalAppointmentRequestDto appointmentDto, BindingResult bindingResult)
    {
        if (appointmentDto == null) {
            bindingResult.rejectValue("date", "error.medical_appointment", "Грешка при създаване на специалност");
            return null;
        }

        Doctor doctor;
        try {
            doctor = doctorService.findById(appointmentDto.getDoctorId());
        } catch (Exception e) {
            bindingResult.rejectValue("doctorId", "error.medical_appointment", "Докторът не съществува");
            return null;
        }

        Patient patient;
        try {
            patient = patientService.findById(appointmentDto.getPatientId());
        } catch (Exception e) {
            bindingResult.rejectValue("patientId", "error.medical_appointment", "Пациентът не съществува");
            return null;
        }

        Diagnose diagnose;
        try {
            diagnose = diagnoseService.findById(appointmentDto.getDiagnoseId());
        } catch (Exception e) {
            bindingResult.rejectValue("diagnoseId", "error.medical_appointment", "Диагнозата не съществува");
            return null;
        }

        MedicalAppointment appointment = new MedicalAppointment();
        appointment.setDate(appointmentDto.getDate());
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDiagnose(diagnose);

        try {
            return medicalAppointmentRepository.save(appointment);
        } catch (Exception e) {
            bindingResult.rejectValue("name", "error.medical_appointment", "Грешка при създаване на специалност");
            return null;
        }
    }

    public MedicalAppointment update(Long id, EditMedicalAppointmentRequestDto editAppointmentDto, BindingResult bindingResult) {
        if (editAppointmentDto == null) {
            bindingResult.rejectValue("name", "error.medical_appointment", "Грешка при редактиране на специалност");
            return null;
        }

        MedicalAppointment appointment;
        try {
            appointment = findById(id);
        } catch (Exception e) {
            bindingResult.rejectValue("name", "error.medical_appointment", "Специалността не съществува");
            return null;
        }

        Doctor doctor;
        try {
            doctor = doctorService.findById(editAppointmentDto.getDoctorId());
        } catch (Exception e) {
            bindingResult.rejectValue("doctorId", "error.medical_appointment", "Докторът не съществува");
            return null;
        }

        Patient patient;
        try {
            patient = patientService.findById(editAppointmentDto.getPatientId());
        } catch (Exception e) {
            bindingResult.rejectValue("patientId", "error.medical_appointment", "Пациентът не съществува");
            return null;
        }

        Diagnose diagnose;
        try {
            diagnose = diagnoseService.findById(editAppointmentDto.getDiagnoseId());
        } catch (Exception e) {
            bindingResult.rejectValue("diagnoseId", "error.medical_appointment", "Диагнозата не съществува");
            return null;
        }

        appointment.setDate(editAppointmentDto.getDate());
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDiagnose(diagnose);

        try {
            return medicalAppointmentRepository.save(appointment);
        } catch (Exception e) {
            bindingResult.rejectValue("name", "error.medical_appointment", "Грешка при редактиране на специалност");
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
        return medicalAppointmentRepository.findByIdAndDoctorId(appointmentId, doctorId).orElse(null) != null;
    }

    public Page<MedicalAppointment> findAllBasedOnRole(Pageable pageable) {
        User user = UserServiceImpl.getCurrentUser();

        if (user.isAdmin()) {
            return findAll(pageable);
        }

        if (user.isDoctor()) {
            return medicalAppointmentRepository.findAllByDoctorId(user.getId(), pageable);
        }

        return medicalAppointmentRepository.findAllByPatientId(user.getId(), pageable);
    }
}
