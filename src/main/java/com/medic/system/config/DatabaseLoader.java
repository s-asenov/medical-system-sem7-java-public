package com.medic.system.config;

import com.medic.system.entities.Doctor;
import com.medic.system.entities.Patient;
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
        if (userRepository.findByUsername("admin") == null) {
            User user = new User();
            user.setFirstName("admin");
            user.setLastName("admin");
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole(Role.ROLE_ADMIN);
            userRepository.save(user);
        }


        if (userRepository.findByUsername("doctor") == null) {
            Doctor doctor = new Doctor();
            doctor.setFirstName("doctor");
            doctor.setLastName("doctor");
            doctor.setUsername("doctor");
            doctor.setPassword(passwordEncoder.encode("password"));
            doctor.setRole(Role.ROLE_DOCTOR);
            doctor.setGeneralPractitioner(true);
            userRepository.save(doctor);
        }

        if (userRepository.findByUsername("patient") == null) {
            Patient user = new Patient();
            user.setFirstName("patient");
            user.setLastName("patient");
            user.setUsername("patient");
            user.setEgn("1234567890");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole(Role.ROLE_PATIENT);
            userRepository.save(user);
        }
    }
}

