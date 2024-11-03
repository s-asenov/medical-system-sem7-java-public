package com.medic.system.controllers.web;

import com.medic.system.dtos.user.EditBaseUserRequestDto;
import com.medic.system.entities.Doctor;
import com.medic.system.entities.User;
import com.medic.system.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/users")
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping
    public String index(Model model, Pageable pageable) {
        User user = UserServiceImpl.getCurrentUser();

        boolean isGeneralPractitioner = false;
        if (user.isDoctor()) {
            isGeneralPractitioner = ((Doctor) user).isGeneralPractitioner();
        }

        model.addAttribute("users", userServiceImpl.findAll(pageable));
        model.addAttribute("isGeneralPractitioner", isGeneralPractitioner);

        return "users/index";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userServiceImpl.isCurrentUser(#id, authentication.name)")
    public String edit(@PathVariable Long id) {
        User user;

        try {
            user = userServiceImpl.findById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Потребителят не съществува");
        }

        if (user.isDoctor()) {
            return "redirect:/doctors/edit/" + id;
        }

        if (user.isPatient()) {
            return "redirect:/patients/edit/" + id;
        }

        if (user.isAdmin()) {
            return "redirect:/admins/edit/" + id;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Потребителят не съществува");
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
