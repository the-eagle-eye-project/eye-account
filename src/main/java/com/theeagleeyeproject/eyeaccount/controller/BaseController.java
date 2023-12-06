package com.theeagleeyeproject.eyeaccount.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    protected <O> ResponseEntity<O> createResponseEntity(O response) {
        return response != null ? ResponseEntity.ok(response) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
