package com.medic.system.repositories;

import com.medic.system.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Query("SELECT p FROM Patient p WHERE p.firstName LIKE CONCAT('%', :name, '%') " +
            "OR p.lastName LIKE CONCAT('%', :name, '%') " +
            "OR p.username LIKE CONCAT('%', :name, '%')")
    Page<Patient> searchByNameAcrossFields(String name, Pageable pageable);

//    Page<Patient> findAllByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndUsernameContainingIgnoreCase(
//            String firstName, String lastName, String username, Pageable pageable);
}