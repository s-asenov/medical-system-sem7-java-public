package com.medic.system.config;

import com.medic.system.entities.*;
import com.medic.system.enums.Role;
import com.medic.system.repositories.DiagnoseRepository;
import com.medic.system.repositories.DrugRepository;
import com.medic.system.repositories.SpecialityRepository;
import com.medic.system.repositories.UserRepository;
import groovy.lang.Tuple;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DatabaseLoader {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SpecialityRepository specialityRepository;
    private final DiagnoseRepository diagnoseRepository;
    private final DrugRepository drugRepository;

    @PostConstruct
    public void init() {
        loadUsers();
        loadSpecialities();
        loadDiagnoses();
        loadDrugs();
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

        // create 2 dummy gp
        List<Doctor> doctors = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            Doctor doctor = (Doctor) userRepository.findByUsername("doctor" + i);
            if (doctor == null) {
                doctor = new Doctor();
                doctor.setFirstName("doctor" + i);
                doctor.setLastName("doctor" + i);
                doctor.setUsername("doctor" + i);
                doctor.setPassword(passwordEncoder.encode("password"));
                doctor.setRole(Role.ROLE_DOCTOR);
                doctor.setIsGeneralPractitioner(true);
                userRepository.save(doctor);
            }
            doctors.add(doctor);
        }

        for (int i = 1; i <= 5; i++) {
            Patient patient = (Patient) userRepository.findByUsername("patient" + i);
            if (patient == null) {
                patient = new Patient();
                patient.setFirstName("patient" + i);
                patient.setLastName("patient" + i);
                patient.setUsername("patient" + i);
                patient.setPassword(passwordEncoder.encode("password"));
                patient.setRole(Role.ROLE_PATIENT);
                patient.setGeneralPractitioner(doctors.get(i % 2));
                patient.setEgn("1234567890" + i);
                userRepository.save(patient);
            }
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
            if (!specialityRepository.existsByName(specialityName)) {
                Speciality speciality = new Speciality();
                speciality.setName(specialityName);
                specialityRepository.save(speciality);
            }
        });
    }

    private void loadDiagnoses() {
        Set<Tuple<String>> diagnoses = Set.of(
                new Tuple("Грип", "Вирусно заболяване, причинено от грипен вирус"),
                new Tuple("Ангина", "Заразно заболяване на гърлото, причинено от бактерии"),
                new Tuple("Бронхит", "Заразно заболяване на бронхите, причинено от бактерии"),
                new Tuple("Пневмония", "Заразно заболяване на белите дробове, причинено от бактерии"),
                new Tuple("Астма", "Хронично заболяване на дихателните пътища, причинено от алергии"),
                new Tuple("Диабет", "Хронично заболяване на организма, причинено от недостатъчно количество инсулин"),
                new Tuple("Хипертония", "Хронично заболяване на кръвното налягане, причинено от стрес"),
                new Tuple("Артрит", "Хронично заболяване на ставите, причинено от възпаление"),
                new Tuple("Алергия", "Заразно заболяване на кожата, причинено от алергии"),
                new Tuple("Рак", "Хронично заболяване на организма, причинено от ракови клетки"),
                new Tuple<>("Вирус", "Заразно заболяване на организма, причинено от вирус")
        );

        diagnoses.forEach(diagnose -> {
            if (!diagnoseRepository.existsByName(diagnose.get(0))) {
                Diagnose newDiagnose = new Diagnose();
                newDiagnose.setName(diagnose.get(0));
                newDiagnose.setDescription(diagnose.get(1));
                diagnoseRepository.save(newDiagnose);
            }
        });
    }

    private void loadDrugs()
    {
        Set<Tuple<Object>> drugs = Set.of(
                new Tuple("Аспирин", "Лекарство за болка и възпаление", 1.0),
                new Tuple("Парацетамол", "Лекарство за болка и възпаление", 1.0),
                new Tuple("Ибупрофен", "Лекарство за болка и възпаление", 1.0),
                new Tuple("Амоксицилин", "Лекарство за бактериални инфекции", 1.0),
                new Tuple("Азитромицин", "Лекарство за бактериални инфекции", 1.0),
                new Tuple("Кларитромицин", "Лекарство за бактериални инфекции", 1.0),
                new Tuple("Левофлоксацин", "Лекарство за бактериални инфекции", 1.0),
                new Tuple("Метронидазол", "Лекарство за бактериални инфекции", 1.0),
                new Tuple("Флуконазол", "Лекарство за гъбични инфекции", 1.0),
                new Tuple("Кетоконазол", "Лекарство за гъбични инфекции", 1.0),
                new Tuple("Тербинафин", "Лекарство за гъбични инфекции", 1.0),
                new Tuple("Ацетилцистеин", "Лекарство за бронхит", 1.0),
                new Tuple("Амброксол", "Лекарство за бронхит", 1.0),
                new Tuple("Бромгексин", "Лекарство за бронхит", 1.0)
        );

        drugs.forEach(drug -> {
            if (!drugRepository.existsByName((String) drug.get(0))) {
                Drug newDrug = new Drug();
                newDrug.setName((String) drug.get(0));
                newDrug.setDescription((String) drug.get(1));
                newDrug.setPrice((Double) drug.get(2));
                drugRepository.save(newDrug);
            }
        });
    }
}

