package com.theeagleeyeproject.eyeaccount.model;

import com.theeagleeyeproject.eyeaccount.service.helper.ValidCountryName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * {@link CreateAccountServiceRequest} used to handle the consumer request to create an account.
 *
 * @author johnmartinez
 */
@Data
public class CreateAccountServiceRequest {

    /**
     * Consumer's first name
     */
    @NotNull
    private String firstName;

    /**
     * Consumer's last name
     */
    @NotNull
    private String lastName;

    /**
     * Consumer's email address
     */
    @NotNull
    @Email
    private String emailAddress;

    /**
     * Consumer's country of origin
     */
    @ValidCountryName
    private String countryOfRegistration;

    /**
     * Name of the company that will be used to send
     */
    private String companyName;

}
