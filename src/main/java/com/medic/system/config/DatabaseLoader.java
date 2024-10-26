package com.medic.system.config;

import com.medic.system.entities.User;
import com.medic.system.enums.Role;
import com.medic.system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DatabaseLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        loadUsers();
    }

    private void loadUsers() {
        if (userRepository.findByUsername("admin") != null) {
            return;
        }

        User user = new User();
        user.setFirstName("admin");
        user.setLastName("admin");
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(Role.SUPER_ADMIN);
        userRepository.save(user);
    }
}

