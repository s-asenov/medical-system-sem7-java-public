package com.medic.system.repositories;

import com.medic.system.entities.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
    boolean existsByName(String name);
}