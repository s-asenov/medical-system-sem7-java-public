package com.medic.system.repositories;

import com.medic.system.entities.Drug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugRepository extends JpaRepository<Drug, Long> {
    boolean existsByName(String name);;
}