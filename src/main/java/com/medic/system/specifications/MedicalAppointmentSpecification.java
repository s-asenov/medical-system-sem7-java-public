package com.medic.system.specifications;

import com.medic.system.entities.MedicalAppointment;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class MedicalAppointmentSpecification {
    public static Specification<MedicalAppointment> hasDiagnoseId(Long diagnoseId) {
        return (Root<MedicalAppointment> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (diagnoseId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("diagnose").get("id"), diagnoseId);
        };
    }

    public static Specification<MedicalAppointment> hasDoctorId(Long doctorId) {
        return (Root<MedicalAppointment> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (doctorId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("doctor").get("id"), doctorId);
        };
    }

    public static Specification<MedicalAppointment> hasPatientId(Long patientId) {
        return (Root<MedicalAppointment> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (patientId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("patient").get("id"), patientId);
        };
    }
}