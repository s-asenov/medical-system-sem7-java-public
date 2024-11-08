package com.medic.system.controllers.web;

import com.medic.system.dtos.medical_appointment.EditMedicalAppointmentRequestDto;
import com.medic.system.dtos.medical_appointment.MedicalAppointmentRequestDto;
import com.medic.system.entities.MedicalAppointment;
import com.medic.system.services.DiagnoseService;
import com.medic.system.services.DoctorService;
import com.medic.system.services.MedicalAppointmentService;
import com.medic.system.services.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/medical_appointments")
@Controller
@RequiredArgsConstructor
public class MedicalAppointmentController {

    private final MedicalAppointmentService medicalAppointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final DiagnoseService diagnoseService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String index(Model model, Pageable pageable) {
        model.addAttribute("appointments", medicalAppointmentService.findAll(pageable));
        return "medical_appointments/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(Model model) {
        MedicalAppointmentRequestDto appointment = new MedicalAppointmentRequestDto();

        model.addAttribute("appointment", appointment);
        model.addAttribute("doctors", doctorService.findAll());
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("diagnoses", diagnoseService.findAll());

        return "medical_appointments/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String store(@Valid @ModelAttribute("appointment") MedicalAppointmentRequestDto appointment, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("doctors", doctorService.findAll());
            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("diagnoses", diagnoseService.findAll());

            return "medical_appointments/create";
        }

        medicalAppointmentService.create(appointment, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("doctors", doctorService.findAll());
            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("diagnoses", diagnoseService.findAll());

            return "medical_appointments/create";
        }

        return "redirect:/medical_appointments";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String edit(@PathVariable Long id, Model model) {
        try {
            MedicalAppointment appointment = medicalAppointmentService.findById(id);

            model.addAttribute("appointment", new EditMedicalAppointmentRequestDto(appointment));
            model.addAttribute("doctors", doctorService.findAll());
            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("diagnoses", diagnoseService.findAll());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Посещението не съществува");
        }

        return "medical_appointments/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("appointment") EditMedicalAppointmentRequestDto appointment, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("doctors", doctorService.findAll());
            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("diagnoses", diagnoseService.findAll());

            return "medical_appointments/edit";
        }

        medicalAppointmentService.update(id, appointment, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("doctors", doctorService.findAll());
            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("diagnoses", diagnoseService.findAll());

            return "medical_appointments/edit";
        }

        return "redirect:/medical_appointments";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        try {
            medicalAppointmentService.deleteById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Посещението не съществува");
        }

        return "redirect:/medical_appointments";
    }
}
