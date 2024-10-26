package com.medic.system.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "patients")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class Patient extends User {

    @Column(unique = true, nullable = false)
    private String egn;

    @ManyToOne
    @JoinColumn(name = "general_practitioner_id")
    private Doctor generalPractitioner;
}
