package com.theeagleeyeproject.eyeaccount.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateApplicationServiceRequest {

    @NotNull
    private String applicationName;

    @NotNull
    private ApplicationType applicationType;

    @Valid
    @NotNull
    @Size(min = 1, message = "Job Configuration is null")
    private List<JobConfiguration> jobConfiguration;
}
