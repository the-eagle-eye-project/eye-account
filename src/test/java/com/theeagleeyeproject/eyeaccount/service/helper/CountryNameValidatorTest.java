package com.theeagleeyeproject.eyeaccount.service.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CountryNameValidatorTest {

    private CountryNameValidator countryNameValidator;

    @BeforeEach
    void init() {
        countryNameValidator = new CountryNameValidator();
    }

    @Test
    void isValid() {
        Assertions.assertTrue(countryNameValidator.isValid("United States", null));
    }

    @Test
    void isValid_givenPlanetName_expectedFalse() {
        Assertions.assertFalse(countryNameValidator.isValid("Mars", null));
    }

    @Test
    void isValid_givenState_expectedFalse() {
        Assertions.assertFalse(countryNameValidator.isValid("Florida", null));
    }

    @Test
    void isValid_givenNull_expectedFalse() {
        Assertions.assertFalse(countryNameValidator.isValid(null, null));
    }
}