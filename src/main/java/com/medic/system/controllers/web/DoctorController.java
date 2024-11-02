package com.medic.system.controllers.web;

import com.medic.system.dtos.DoctorRequestDto;
import com.medic.system.enums.Role;
import com.medic.system.services.DoctorService;
import com.medic.system.services.UserServiceImpl;
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

    private final UserServiceImpl userServiceImpl;
    private final DoctorService doctorService;

    @GetMapping
    public String index(Model model, Pageable pageable) {
        model.addAttribute("doctors", doctorService.findAll(pageable));
        return "doctors/index";
    }

//    @GetMapping("/create/patient")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String createPatient(Model model) {
//        List<Doctor> gps = doctorService.findAllGeneralPractitioners();
//        model.addAttribute("generalPractitioners", gps);
//        model.addAttribute("patient", new PatientRequestDto());
//        return "users/create_patient";
//    }
//
//    @PostMapping("/create/patient")
//    @PreAuthorize("@doctorService.isCurrentUserGp(authentication.principal.id)")
//    public String storePatient(@Valid @ModelAttribute("patient") PatientRequestDto patient, BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            List<Doctor> gps = doctorService.findAllGeneralPractitioners();
//            model.addAttribute("generalPractitioners", gps);
//
//            return "users/create_patient";
//        }
//        // Save patient user logic here
//        return "redirect:/users";
//    }

    @GetMapping("/create/doctor")
    @PreAuthorize("hasRole('ADMIN')")
    public String createDoctor(Model model) {
        DoctorRequestDto doctor = new DoctorRequestDto();
        doctor.setRole(Role.ROLE_DOCTOR);
        model.addAttribute("doctor", doctor);
        return "users/create_doctor";
    }

    @PostMapping("/create/doctor")
    @PreAuthorize("hasRole('ADMIN')")
    public String storeDoctor(@Valid @ModelAttribute("doctor") DoctorRequestDto doctor, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "users/create_doctor";
        }

        return "redirect:/doctors";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("user", userServiceImpl.findById(id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Потребителят не съществува");
        }

        return "users/show";
    }
}
