package com.medic.system.controllers.web;

import com.medic.system.dtos.BaseUserRequestDto;
import com.medic.system.dtos.DoctorRequestDto;
import com.medic.system.dtos.EditBaseUserRequestDto;
import com.medic.system.dtos.PatientRequestDto;
import com.medic.system.entities.Doctor;
import com.medic.system.entities.User;
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

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/users")
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final DoctorService doctorService;

    @GetMapping
    public String index(Model model, Pageable pageable) {
        model.addAttribute("users", userServiceImpl.findAll(pageable));
        return "users/index";
    }

    @GetMapping("/create/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String createAdmin(Model model) {
        model.addAttribute("admin", new BaseUserRequestDto());
        return "users/create_admin";
    }

    @PostMapping("/create/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String storeAdmin(@Valid @ModelAttribute("admin") BaseUserRequestDto admin, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "users/create_admin";
        }
        // Save admin user logic here
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userServiceImpl.isCurrentUser(#id, authentication.name)")
    public String edit(@PathVariable Long id, Model model) {
        User user;

        try {
            user = userServiceImpl.findById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Потребителят не съществува");
        }

        if (user.getRole() == Role.ROLE_DOCTOR) {
            return "redirect:/doctors/edit/" + id;
        }

        if (user.getRole() == Role.ROLE_PATIENT) {
            return "redirect:/patients/edit/" + id;
        }

        model.addAttribute("user", new EditBaseUserRequestDto(user));

        return "users/edit";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        try
        {
            userServiceImpl.deleteById(id);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Потребителят не съществува");
        }

        return "redirect:/users";
    }
}
