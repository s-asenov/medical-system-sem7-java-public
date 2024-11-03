package com.medic.system.services;

import com.medic.system.dtos.user.BaseUserRequestDto;
import com.medic.system.dtos.user.EditBaseUserRequestDto;
import com.medic.system.entities.User;
import com.medic.system.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAllAdminUsers(pageable);
    }

    public User create(BaseUserRequestDto baseUserRequestDto, BindingResult bindingResult)
    {
        if (baseUserRequestDto == null) {
            bindingResult.rejectValue("username", "error.user", "Грешка при създаване на администратор");
            return null;
        }

        baseUserRequestDto.setPassword(passwordEncoder.encode(baseUserRequestDto.getPassword()));

        User doctor = new User(baseUserRequestDto);

        try {
            return userRepository.save(doctor);
        } catch (DataIntegrityViolationException e) {
            // set to generalPractitionerId because it is last one
            bindingResult.rejectValue("isGeneralPractitioner", "error.user",  "Грешка при създаване на администратор");
            return null;
        }
    }

    public User update(Long id, EditBaseUserRequestDto editBaseUserRequestDto, BindingResult bindingResult) {
        User user;

        try {
            user = findById(id);
        } catch (Exception e) {
            bindingResult.rejectValue("username", "error.user", "Администраторът не съществува");
            return null;
        }

        user.setFirstName(editBaseUserRequestDto.getFirstName());
        user.setLastName(editBaseUserRequestDto.getLastName());
        user.setUsername(editBaseUserRequestDto.getUsername());

        if (editBaseUserRequestDto.getPassword() != null && !editBaseUserRequestDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(editBaseUserRequestDto.getPassword()));
        }

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            // set to generalPractitionerId because it is last one
            bindingResult.rejectValue("username", "error.user",  "Грешка при редактиране на администратор");
            return null;
        }
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }
}
