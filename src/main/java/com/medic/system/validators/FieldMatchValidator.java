package com.medic.system.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.medic.system.annotations.FieldMatch;

import java.lang.reflect.Field;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;
    private String message;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            // access the fields
            Field firstField = value.getClass().getField(firstFieldName);
            Field secondField = value.getClass().getField(secondFieldName);

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
}