package com.medic.system.validators;

import com.medic.system.annotations.FieldMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;
    private String message;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            // access the fields
            Field firstField = getField(value.getClass(), firstFieldName);
            Field secondField = getField(value.getClass(), secondFieldName);

            firstField.setAccessible(true);
            secondField.setAccessible(true);

            // get values
            Object firstObj = firstField.get(value);
            Object secondObj = secondField.get(value);

            // check if equal
            boolean valid = (firstObj == null && secondObj == null) ||
                    (firstObj != null && firstObj.equals(secondObj));

            if (!valid) {
                context.disableDefaultConstraintViolation();
                // attach the error to the second field
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(secondFieldName)
                        .addConstraintViolation();
            }

            return valid;
        } catch (Exception e) {
            // catch any exception
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