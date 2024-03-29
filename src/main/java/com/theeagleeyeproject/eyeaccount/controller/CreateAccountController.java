package com.theeagleeyeproject.eyeaccount.controller;


import com.theeagleeyeproject.eyeaccount.model.CreateAccountServiceRequest;
import com.theeagleeyeproject.eyeaccount.model.CreateAccountServiceResponse;
import com.theeagleeyeproject.eyeaccount.service.CreateAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CreateAccountController.ACCOUNT_RESOURCE_URL)
@RequiredArgsConstructor
public class CreateAccountController extends BaseController {

    private final CreateAccountService createAccountService;

    public static final String ACCOUNT_RESOURCE_URL = "/eye/v1/accounts";

    @PostMapping
    public ResponseEntity<CreateAccountServiceResponse> createResource(@RequestHeader HttpHeaders headers, @Valid @RequestBody CreateAccountServiceRequest request) {
        return createResponseEntity(createAccountService.create(request));
    }
}
