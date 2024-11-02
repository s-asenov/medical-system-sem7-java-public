package com.medic.system.validators;

import com.medic.system.annotations.Unique;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;

@RequiredArgsConstructor
public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    @PersistenceContext
    private final EntityManager entityManager;

    private Class<?> entityClass;
    private String fieldName;

    @Override
    public void initialize(Unique constraintAnnotation) {
        entityClass = constraintAnnotation.entityClass();
        fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Field idField;
        Object idForm;
        // in case of form not having id field, set it to 0
        try {
            idField = getField(value.getClass(), "id");

            idField.setAccessible(true);
            idForm = idField.get(value);
        } catch (Exception e) {
            idForm = 0;
        }

        try {
            Field field = getField(value.getClass(), fieldName);
            field.setAccessible(true);

            // check if the entity with the same value exists for given fieldname
            String query = String.format("SELECT e FROM %s e WHERE e.%s = :value", entityClass.getSimpleName(), fieldName);
            Object entity;

            try {
                entity = entityManager.createQuery(query, entityClass)
                        .setParameter("value", field.get(value))
                        .getSingleResult();
            } catch (NoResultException e) {
                // no result means the value is unique
                return true;
            }

            if (entity != null)
            {
                // check if the entity has same id from ifForm
                Field entityIdField = getField(entity.getClass(), "id");
                entityIdField.setAccessible(true);
                Object entityId = entityIdField.get(entity);


                // if the entity id is different from the id from the form, reject the form
                if (!entityId.equals(idForm)) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("Запис със същата стойност вече съществува")
                            .addPropertyNode(fieldName)
                            .addConstraintViolation();
                    return false;
                }
            }

            return true;
        } catch (Exception e){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Грешка при валидацията")
                    .addPropertyNode(fieldName)
                    .addConstraintViolation();
            return false;
        }
    }

    public Field getField(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }

        return null;
    }
}