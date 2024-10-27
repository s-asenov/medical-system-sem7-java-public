package com.medic.system.controllers.web;

import com.medic.system.dtos.BaseUserRequestDto;
import com.medic.system.dtos.DoctorRequestDto;
import com.medic.system.dtos.PatientRequestDto;
import com.medic.system.entities.Doctor;
import com.medic.system.enums.Role;
import com.medic.system.services.DoctorService;
import com.medic.system.services.PatientService;
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

import java.util.List;

@RequestMapping("/patients")
@RequiredArgsConstructor
@Controller
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public String index(Model model, Pageable pageable) {
        model.addAttribute("patients", patientService.findAll(pageable));
        return "patients/index";
    }

//    @GetMapping("/edit/{id}")
//    @PreAuthorize("hasRole('ADMIN') or @userServiceImpl.isCurrentUser(#id, authentication.name)")
//    public String edit(@PathVariable Long id, Model model) {
//        return "users/edit";
//    }
}
