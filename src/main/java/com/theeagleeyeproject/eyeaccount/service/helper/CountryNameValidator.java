package com.theeagleeyeproject.eyeaccount.service.helper;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Locale;

/**
 * {@link CountryNameValidator} validator used to validate that the input country name is a valid name and it is an
 * actual country. This is used to avoid processing of junk data.
 *
 * @author John Robert Martinez Ponce
 */
public class CountryNameValidator implements ConstraintValidator<ValidCountryName, String> {
    @Override
    public void initialize(ValidCountryName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String countryName, ConstraintValidatorContext context) {
        if (countryName == null) {
            return false;
        }

        // Check if the provided country name is a valid ISO country code
        Locale[] availableLocales = Locale.getAvailableLocales();
        for (Locale locale : availableLocales) {
            if (countryName.equalsIgnoreCase(locale.getDisplayCountry())) {
                return true;
            }
        }

        return false;
    }
}
