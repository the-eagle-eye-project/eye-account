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
@RequestMapping("/eye/v1/account")
@RequiredArgsConstructor
public class CreateAccountController extends BaseController {

    private final CreateAccountService createAccountService;
    
    @PostMapping
    public ResponseEntity<CreateAccountServiceResponse> createResource(@RequestHeader HttpHeaders headers, @Valid @RequestBody CreateAccountServiceRequest request) {
        return createResponseEntity(createAccountService.create(request));
    }
}
