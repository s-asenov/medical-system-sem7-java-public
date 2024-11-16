package com.medic.system.controllers.web;

import com.medic.system.dtos.sick_leave.EditSickLeaveRequestDto;
import com.medic.system.dtos.sick_leave.SickLeaveRequestDto;
import com.medic.system.entities.SickLeave;
import com.medic.system.services.*;
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

@RequestMapping("/sick_leaves")
@Controller
@RequiredArgsConstructor
public class SickLeaveController {

    private final MedicalAppointmentService medicalAppointmentService;
    private final SickLeaveService sickLeaveService;

    @GetMapping
    public String index(Model model, Pageable pageable) {
        model.addAttribute("sickLeaves", sickLeaveService.findAllBasedOnRole(pageable));
        model.addAttribute("doctorsWithMostSickLeaves", sickLeaveService.doctorWithMostSickLeaves());
        model.addAttribute("monthsWithMostSickLeaves", sickLeaveService.monthWithMostSickLeaves(null));

        return "sick_leaves/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public String create(Model model, @RequestParam(required = false) Long appointmentId) {
        SickLeaveRequestDto sickLeave = new SickLeaveRequestDto();

        if (appointmentId != null)
        {
            sickLeave.setMedicalAppointmentId(appointmentId);
        }

        model.addAttribute("sickLeave", sickLeave);
        model.addAttribute("medicalAppointments", medicalAppointmentService.findAllBasedOnRole());

        return "sick_leaves/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public String store(@Valid @ModelAttribute("sickLeave") SickLeaveRequestDto sickLeave, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("medicalAppointments", medicalAppointmentService.findAllBasedOnRole());
            return "sick_leaves/create";
        }

        sickLeaveService.create(sickLeave, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("medicalAppointments", medicalAppointmentService.findAllBasedOnRole());
            return "sick_leaves/create";
        }

        return "redirect:/sick_leaves";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('DOCTOR') and @sickLeaveService.isSickLeaveAppointmentFromDoctor(#id, principal.id))")
    public String edit(@PathVariable Long id, Model model) {
        try {
            SickLeave sickLeave = sickLeaveService.findById(id);

            model.addAttribute("sickLeave", new EditSickLeaveRequestDto(sickLeave));
            model.addAttribute("medicalAppointments", medicalAppointmentService.findAllBasedOnRole());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Болничният лист не съществува");
        }

        return "sick_leaves/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('DOCTOR') and @sickLeaveService.isSickLeaveAppointmentFromDoctor(#id, principal.id))")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("sickLeave") EditSickLeaveRequestDto sickLeave, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("medicalAppointments", medicalAppointmentService.findAllBasedOnRole());
            return "sick_leaves/edit";
        }

        sickLeaveService.update(id, sickLeave, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("medicalAppointments", medicalAppointmentService.findAllBasedOnRole());
            return "sick_leaves/edit";
        }

        return "redirect:/sick_leaves";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        try {
            sickLeaveService.deleteById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Болничният лист не съществува");
        }

        return "redirect:/sick_leaves";
    }
}
