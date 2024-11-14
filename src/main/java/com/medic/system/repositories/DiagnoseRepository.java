package com.medic.system.repositories;

import com.medic.system.entities.Diagnose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnoseRepository extends JpaRepository<Diagnose, Long> {
    boolean existsByName(String name);

    List<Diagnose> findAllByOrderByName();
}