package com.medic.system.controllers.web;

import com.medic.system.dtos.patient.EditPatientRequestDto;
import com.medic.system.dtos.patient.PatientRequestDto;
import com.medic.system.entities.Patient;
import com.medic.system.services.DoctorService;
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

@RequestMapping("/patients")
@RequiredArgsConstructor
@Controller
public class PatientController {

    private final PatientService patientService;
    private final DoctorService doctorService;

    @GetMapping
    public String index(Model model, Pageable pageable) {
        model.addAttribute("patients", patientService.findAll(pageable));
        return "patients/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('DOCTOR') and @doctorService.isCurrentUserGp())")
    public String createPatient(Model model) {

        model.addAttribute("generalPractitioners", doctorService.getListOfGps());
        model.addAttribute("patient", new PatientRequestDto());

        return "patients/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('DOCTOR') and @userServiceImpl.getCurrentUser().isGeneralPractitioner())")
    public String storePatient(@Valid @ModelAttribute("patient") PatientRequestDto patient, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("generalPractitioners", doctorService.getListOfGps());
            return "patients/create";
        }

        patientService.save(patient, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("generalPractitioners", doctorService.getListOfGps());
            return "patients/create";
        }

        return "redirect:/patients";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN') or " +
            "@userServiceImpl.isCurrentUser(#id, authentication.name) or " +
            "(hasRole('DOCTOR') and @userServiceImpl.getCurrentUser().isGeneralPractitioner())")
    public String edit(@PathVariable Long id, Model model) {
        try {
            Patient patient = patientService.findById(id);
            model.addAttribute("patient", new EditPatientRequestDto(patient));
            model.addAttribute("generalPractitioners", doctorService.getListOfGps());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пациентът не съществува");
        }

        return "patients/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN') or " +
            "@userServiceImpl.isCurrentUser(#id, authentication.name) or " +
            "(hasRole('DOCTOR') and @userServiceImpl.getCurrentUser().isGeneralPractitioner())")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("patient") EditPatientRequestDto patient, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("generalPractitioners", doctorService.getListOfGps());
            return "patients/edit";
        }

        patientService.update(id, patient, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("generalPractitioners", doctorService.getListOfGps());
            return "patients/edit";
        }

        return "redirect:/patients";
    }
}
