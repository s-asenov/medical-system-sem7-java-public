package com.medic.system.controllers.web;

import com.medic.system.services.UserService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/create")
    public String create() {
        return "users/create";
    }

    @PostMapping
    public String store() {
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        return "users/edit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id) {
        return "redirect:/users";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        return "users/show";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        return "redirect:/users";
    }
}
