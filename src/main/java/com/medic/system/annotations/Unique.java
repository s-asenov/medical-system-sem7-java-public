package com.medic.system.annotations;

import com.medic.system.validators.UniqueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {
    String message() default "Field value must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    // The entity class to check
    Class<?> entityClass();

    // The field name to check
    String fieldName();
}