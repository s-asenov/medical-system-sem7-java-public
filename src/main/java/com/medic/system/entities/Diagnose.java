package com.medic.system.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "diagnoses")
@Getter
@Setter
public class Diagnose extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "medical_exam_id")
    private MedicalExam medicalExam;
}
