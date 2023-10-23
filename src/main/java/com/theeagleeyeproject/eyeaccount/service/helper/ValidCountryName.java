package com.theeagleeyeproject.eyeaccount.service.helper;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CountryNameValidator.class)
@ReportAsSingleViolation
public @interface ValidCountryName {

    String message() default "Invalid country name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
