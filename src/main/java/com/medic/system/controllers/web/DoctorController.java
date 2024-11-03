package com.medic.system.controllers.web;

import com.medic.system.dtos.doctor.DoctorRequestDto;
import com.medic.system.dtos.doctor.EditDoctorRequestDto;
import com.medic.system.dtos.patient.EditPatientRequestDto;
import com.medic.system.entities.Doctor;
import com.medic.system.services.DoctorService;
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

@RequestMapping("/doctors")
@RequiredArgsConstructor
@Controller
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR')")
    public String index(Model model, Pageable pageable) {
        model.addAttribute("doctors", doctorService.findAll(pageable));
        return "doctors/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(Model model) {
        DoctorRequestDto doctor = new DoctorRequestDto();
        model.addAttribute("doctor", doctor);

        return "doctors/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String store(@Valid @ModelAttribute("doctor") DoctorRequestDto doctor, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "doctors/create";
        }

        doctorService.create(doctor, bindingResult);

        if (bindingResult.hasErrors()) {
            return "doctors/create";
        }

        return "redirect:/doctors";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userServiceImpl.isCurrentUser(#id, authentication.name)")
    public String edit(@PathVariable Long id, Model model) {
        try {
            Doctor doctor = doctorService.findById(id);
            model.addAttribute("doctor", new EditDoctorRequestDto(doctor));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Докторът не съществува");
        }

        return "doctors/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userServiceImpl.isCurrentUser(#id, authentication.name)")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("doctor") EditDoctorRequestDto doctor, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "doctors/edit";
        }

        doctorService.update(id, doctor, bindingResult);

        if (bindingResult.hasErrors()) {
            return "doctors/edit";
        }

        return "redirect:/doctors";
    }
}
