package com.medic.system.controllers.web;

import com.medic.system.dtos.user.BaseUserRequestDto;
import com.medic.system.dtos.user.EditBaseUserRequestDto;
import com.medic.system.entities.User;
import com.medic.system.enums.Role;
import com.medic.system.services.AdminService;
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

@RequestMapping("/admins")
@RequiredArgsConstructor
@Controller
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String index(Model model, Pageable pageable) {
        model.addAttribute("admins", adminService.findAll(pageable));
        return "admins/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(Model model) {
        BaseUserRequestDto admin = new BaseUserRequestDto(Role.ROLE_ADMIN);
        model.addAttribute("admin", admin);

        return "admins/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String store(@Valid @ModelAttribute("admin") BaseUserRequestDto admin, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admins/create";
        }

        adminService.create(admin, bindingResult);

        if (bindingResult.hasErrors()) {
            return "admins/create";
        }

        return "redirect:/admins";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String edit(@PathVariable Long id, Model model) {
        try {
            User user = adminService.findById(id);
            model.addAttribute("admin", new EditBaseUserRequestDto(user));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Админът не съществува");
        }

        return "admins/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("admin") EditBaseUserRequestDto admin, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admins/edit";
        }

        adminService.update(id, admin, bindingResult);

        if (bindingResult.hasErrors()) {
            return "admins/edit";
        }

        return "redirect:/admins";
    }
}
