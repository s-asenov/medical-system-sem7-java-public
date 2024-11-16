package com.medic.system.services;

import com.medic.system.entities.User;
import com.medic.system.enums.Role;
import com.medic.system.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
}
