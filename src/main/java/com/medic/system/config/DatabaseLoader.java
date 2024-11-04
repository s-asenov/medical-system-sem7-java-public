package com.medic.system.config;

import com.medic.system.entities.Doctor;
import com.medic.system.entities.Patient;
import com.medic.system.entities.Speciality;
import com.medic.system.entities.User;
import com.medic.system.enums.Role;
import com.medic.system.repositories.SpecialityRepository;
import com.medic.system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DatabaseLoader {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SpecialityRepository specialityRepository;

    @Autowired
    public DatabaseLoader(UserRepository userRepository, PasswordEncoder passwordEncoder, SpecialityRepository specialityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.specialityRepository = specialityRepository;
        loadUsers();
        loadSpecialities();
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

        Doctor doctor = (Doctor) userRepository.findByUsername("doctor");
        if (doctor == null) {
            doctor = new Doctor();
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
            user.setGeneralPractitioner(doctor);
            userRepository.save(user);
        }
    }

    private void loadSpecialities()
    {
        Set<String> specialityNames = Set.of(
                "Кардиолог",
                "Дерматолог",
                "Ортопед",
                "Оториноларинголог",
                "Педиатър",
                "Психиатър",
                "Хирург",
                "Офталмолог",
                "Уролог",
                "Гинеколог",
                "Невролог",
                "Ендокринолог",
                "Гастроентеролог",
                "Пулмолог",
                "Ревматолог",
                "Имунолог",
                "Алерголог",
                "Онколог",
                "Радиолог"
        );

        specialityNames.forEach(specialityName -> {
            if (specialityRepository.findByName(specialityName) == null) {
                Speciality speciality = new Speciality();
                speciality.setName(specialityName);
                specialityRepository.save(speciality);
            }
        });
    }
}

