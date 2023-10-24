package com.theeagleeyeproject.eyeaccount.model;

import com.theeagleeyeproject.eyeaccount.service.helper.ValidCountryName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAccountServiceRequest {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @Email
    private String emailAddress;

    @ValidCountryName
    private String countryOfRegistration;


}
