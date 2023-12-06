package com.theeagleeyeproject.eyeaccount.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateApplicationServiceRequest {

    @NotNull
    private String applicationName;

    @NotNull
    private ApplicationType applicationType;

    @Valid
    private List<JobConfiguration> jobConfiguration;
}
