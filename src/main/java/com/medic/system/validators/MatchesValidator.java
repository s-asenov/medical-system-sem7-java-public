package com.medic.system.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import com.medic.system.annotations.Matches;

public class MatchesValidator implements ConstraintValidator<Matches, Object> {

    private String fieldToMatch;

    @Override
    public void initialize(Matches constraintAnnotation) {
        this.fieldToMatch = constraintAnnotation.fieldToMatch();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // Get value of annotated field
        Object currentFieldValue = value;

        try {
            // Get the value of the field to match using reflection
            Object object = context.unwrap(BeanWrapperImpl.class).getWrappedInstance();
            Object fieldToMatchValue = new BeanWrapperImpl(object).getPropertyValue(fieldToMatch);

            // Check if both fields match
            boolean isValid = currentFieldValue != null && currentFieldValue.equals(fieldToMatchValue);

            return isValid;
        } catch (Exception e) {
            return false;
        }
    }
}

