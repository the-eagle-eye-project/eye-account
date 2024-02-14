package com.theeagleeyeproject.eyeaccount.controller;

import com.theeagleeyeproject.eyeaccount.model.CreateApplicationServiceRequest;
import com.theeagleeyeproject.eyeaccount.model.CreateApplicationServiceResponse;
import com.theeagleeyeproject.eyeaccount.service.CreateApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(CreateApplicationController.APPLICATION_RESOURCE_URL)
@RequiredArgsConstructor
public class CreateApplicationController extends BaseController {

    public static final String APPLICATION_RESOURCE_URL = "/eye/v1/applications";

    private final CreateApplicationService createApplicationService;

    @PostMapping
    public ResponseEntity<CreateApplicationServiceResponse> createResource(@RequestHeader HttpHeaders headers, @Valid @RequestBody CreateApplicationServiceRequest request) {
        return createResponseEntity(createApplicationService.create(request));
    }
}

