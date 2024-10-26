package com.medic.system.controllers.web;

import com.medic.system.dtos.BaseUserRequestDto;
import com.medic.system.dtos.DoctorRequestDto;
import com.medic.system.dtos.PatientRequestDto;
import com.medic.system.enums.Role;
import com.medic.system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/users")
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/index";
    }

    @GetMapping("/create/doctor")
    @PreAuthorize("hasRole('ADMIN')")
    public String createDoctor(Model model) {
        model.addAttribute("doctor", new DoctorRequestDto());
        return "users/create_doctor";
    }

    @GetMapping("/create/patient")
    @PreAuthorize("hasRole('ADMIN')")
    public String createPatient(Model model) {
        model.addAttribute("patient", new PatientRequestDto());
        return "users/create_patient";
    }

    @GetMapping("/create/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String createAdmin(Model model) {
        model.addAttribute("admin", new BaseUserRequestDto());
        return "users/create_admin";
    }

    @PostMapping
    public String store() {
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.isCurrentUser(#id, authentication.name)")
    public String edit(@PathVariable Long id, Model model) {
        return "users/edit";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@PathVariable Long id) {
        return "redirect:/users";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        return "users/show";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        return "redirect:/users";
    }
}
