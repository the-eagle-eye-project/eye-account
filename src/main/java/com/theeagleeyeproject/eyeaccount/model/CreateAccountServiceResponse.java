package com.theeagleeyeproject.eyeaccount.model;

import lombok.Data;

@Data
public class CreateAccountServiceResponse {

    private String id;

    private String jwt;

    private String message;
}
