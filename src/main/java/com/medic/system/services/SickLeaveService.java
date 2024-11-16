package com.medic.system.services;

import com.medic.system.dtos.sick_leave.DoctorWithSickLeaveCount;
import com.medic.system.dtos.sick_leave.EditSickLeaveRequestDto;
import com.medic.system.dtos.sick_leave.MonthWithSickLeaveCount;
import com.medic.system.dtos.sick_leave.SickLeaveRequestDto;
import com.medic.system.entities.*;
import com.medic.system.repositories.MedicalAppointmentRepository;
import com.medic.system.repositories.SickLeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SickLeaveService {
    private final MedicalAppointmentRepository medicalAppointmentRepository;
    private final SickLeaveRepository sickLeaveRepository;

    public Page<SickLeave> findAllBasedOnRole(Pageable pageable) {
        User user = UserServiceImpl.getCurrentUser();

        if (user.isAdmin()) {
            return sickLeaveRepository.findAll(pageable);
        }

        if (user.isDoctor()) {
            return sickLeaveRepository.findAllByMedicalAppointment_DoctorId(user.getId(), pageable);
        }

        return sickLeaveRepository.findAllByMedicalAppointment_PatientId(user.getId(), pageable);
    }

    public SickLeave create(SickLeaveRequestDto sickLeaveDto, BindingResult bindingResult)
    {
        if (sickLeaveDto == null) {
            bindingResult.rejectValue("days", "error.sick_leave", "Грешка при създаване на болничен лист");
            return null;
        }

        MedicalAppointment appointment;
        try {
            appointment = medicalAppointmentRepository.findById(sickLeaveDto.getMedicalAppointmentId()).orElseThrow();
        } catch (NoSuchElementException e) {
            bindingResult.rejectValue("medicalAppointmentId", "error.sick_leave", "Прегледът не съществува");
            return null;
        }

        User user = UserServiceImpl.getCurrentUser();

        if (user.isDoctor() && !appointment.getDoctor().getId().equals(user.getId())) {
            bindingResult.rejectValue("medicalAppointmentId", "error.sick_leave", "Прегледът не е направен от лекаря");
            return null;
        }

        if (sickLeaveDto.getStartDate().isBefore(appointment.getDate())) {
            bindingResult.rejectValue("startDate", "error.sick_leave", "Дата на болничен лист не може да бъде преди датата на прегледа");
            return null;
        }

        if (appointment.getSickLeave() != null) {
            bindingResult.rejectValue("medicalAppointmentId", "error.sick_leave", "Прегледът вече има болничен лист");
            return null;
        }

        SickLeave sickLeave = new SickLeave();
        sickLeave.setMedicalAppointment(appointment);
        sickLeave.setStartDate(sickLeaveDto.getStartDate());
        sickLeave.setDays(sickLeaveDto.getDays());

        try {
            return sickLeaveRepository.save(sickLeave);
        } catch (Exception e) {
            bindingResult.rejectValue("days", "error.sick_leave", "Грешка при създаване на болничен лист");
            return null;
        }
    }

    public SickLeave update(Long id, EditSickLeaveRequestDto editSickLeaveDto, BindingResult bindingResult) {
        if (editSickLeaveDto == null) {
            bindingResult.rejectValue("days", "error.sick_leave", "Грешка при редактиране на болничен лист");
            return null;
        }

        SickLeave sickLeave;
        try {
            sickLeave = findById(id);
        } catch (NoSuchElementException e) {
            bindingResult.rejectValue("days", "error.sick_leave", "Болничният лист не съществува");
            return null;
        }

        MedicalAppointment appointment;
        try {
            appointment = medicalAppointmentRepository.findById(editSickLeaveDto.getMedicalAppointmentId()).orElseThrow();
        } catch (NoSuchElementException e) {
            bindingResult.rejectValue("medicalAppointmentId", "error.sick_leave", "Прегледът не съществува");
            return null;
        }

        User user = UserServiceImpl.getCurrentUser();

        if (user.isDoctor() && !appointment.getDoctor().getId().equals(user.getId())) {
            bindingResult.rejectValue("medicalAppointmentId", "error.sick_leave", "Прегледът не е направен от лекаря");
            return null;
        }

        if (appointment.getSickLeave() != null && !appointment.getSickLeave().getId().equals(id)) {
            bindingResult.rejectValue("medicalAppointmentId", "error.sick_leave", "Прегледът вече има болничен лист");
            return null;
        }

        sickLeave.setMedicalAppointment(appointment);
        sickLeave.setStartDate(editSickLeaveDto.getStartDate());
        sickLeave.setDays(editSickLeaveDto.getDays());

        try {
            return sickLeaveRepository.save(sickLeave);
        } catch (Exception e) {
            bindingResult.rejectValue("name", "error.sick_leave", "Грешка при редактиране на болничен лист");
            return null;
        }
    }

    public SickLeave findById(Long id) {
        return sickLeaveRepository.findById(id).orElseThrow();
    }

    public void deleteById(Long id) {
        sickLeaveRepository.deleteById(id);
    }

    public boolean isSickLeaveAppointmentFromDoctor(Long sickLeaveId, Long doctorId) {
        SickLeave sickLeave;
        try {
            sickLeave = findById(sickLeaveId);
        } catch (NoSuchElementException e) {
            return false;
        }

        return sickLeave.getMedicalAppointment().getDoctor().getId().equals(doctorId);
    }

    public List<DoctorWithSickLeaveCount> doctorWithMostSickLeaves() {
        return sickLeaveRepository.doctorWithMostSickLeaves();
    }

    public List<MonthWithSickLeaveCount> monthWithMostSickLeaves(Integer year) {
        if (year == null) {
            year = (LocalDate.now()).getYear();
        }

        return sickLeaveRepository.monthWithMostSickLeaves(year);
    }
}
