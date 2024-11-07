package com.medic.system.repositories;

import com.medic.system.entities.Diagnose;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnoseRepository extends JpaRepository<Diagnose, Long> {
    Diagnose findByName(String name);
}