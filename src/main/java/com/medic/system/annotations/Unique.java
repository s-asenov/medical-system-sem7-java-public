package com.medic.system.annotations;

import com.medic.system.validators.UniqueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
public @interface Unique {
    String message() default "Field value must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    // The entity class to check
    Class<?> entityClass();

    // The field name to check
    String fieldName();
}