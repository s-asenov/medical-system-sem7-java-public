package com.medic.system.services;

import com.medic.system.dtos.doctor.EditDoctorRequestDto;
import com.medic.system.dtos.user.BaseUserRequestDto;
import com.medic.system.dtos.user.EditBaseUserRequestDto;
import com.medic.system.entities.Doctor;
import com.medic.system.entities.User;
import com.medic.system.enums.Role;
import com.medic.system.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public Page<User> findAllBasedOnRole(Pageable pageable) {
        User user = getCurrentUser();
        if (user.isAdmin()) {
            return userRepository.findAll(pageable);
        }

        if (user.isDoctor()) {
            return userRepository.findAllByRoleIsIn(pageable, Role.ROLE_DOCTOR, Role.ROLE_PATIENT);
        }

        return userRepository.findAllByRoleIsIn(pageable, Role.ROLE_PATIENT);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static void setCurrentUser(User user) {
        // set user as current user to update the context for new data such as changed username or names
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the principal is an instance of your User class
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User currentUser = (User) authentication.getPrincipal();

            currentUser.setUsername(user.getUsername());
            currentUser.setFirstName(user.getFirstName());
            currentUser.setLastName(user.getLastName());
        }
    }

    public Page<User> findAllAdminUsers(Pageable pageable) {
        return userRepository.findAllAdminUsers(pageable);
    }

    public User findAdminById(Long id) {
        return userRepository.findAdminById(id);
    }

    public User create(BaseUserRequestDto baseUserRequestDto, BindingResult bindingResult) {
        if (baseUserRequestDto == null) {
            bindingResult.rejectValue("username", "error.user", "Грешка при създаване на потребител");
            return null;
        }

        baseUserRequestDto.setPassword(baseUserRequestDto.getPassword());

        User user = new User(baseUserRequestDto);

        try {
            return userRepository.save(user);
        } catch (Exception e) {
            bindingResult.rejectValue("username", "error.user", "Грешка при създаване на потребител");
            return null;
        }
    }

    public User update(Long id, EditBaseUserRequestDto editBaseUserRequestDto, BindingResult bindingResult) {
        if (editBaseUserRequestDto == null) {
            bindingResult.rejectValue("username", "error.user", "Грешка при редакция на потребител");
            return null;
        }

        User user;
        try {
            user = findById(id);
        } catch (Exception e) {
            bindingResult.rejectValue("username", "error.user", "Потребителят не е намерен");
            return null;
        }

        if (editBaseUserRequestDto.getPassword() != null && !editBaseUserRequestDto.getPassword().isEmpty()) {
            user.setPassword(editBaseUserRequestDto.getPassword());
        }

        user.setFirstName(editBaseUserRequestDto.getFirstName());
        user.setLastName(editBaseUserRequestDto.getLastName());
        user.setUsername(editBaseUserRequestDto.getUsername());

        try {
            return userRepository.save(user);
        } catch (Exception e) {
            bindingResult.rejectValue("username", "error.user", "Грешка при редакция на потребител");
            return null;
        }
    }
}
