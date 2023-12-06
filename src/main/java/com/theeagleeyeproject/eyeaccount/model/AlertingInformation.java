package com.theeagleeyeproject.eyeaccount.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AlertingInformation {

    @NotNull
    private boolean activeAlert;

    @NotNull
    private AlertChannel alertChannel;

    @NotNull
    private String alertSendInformation;
}
