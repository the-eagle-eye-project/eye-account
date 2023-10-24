package com.theeagleeyeproject.eyeaccount.controller;


import com.theeagleeyeproject.eyeaccount.model.CreateAccountServiceRequest;
import com.theeagleeyeproject.eyeaccount.model.CreateAccountServiceResponse;
import com.theeagleeyeproject.eyeaccount.service.CreateAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eye/v1/account")
@RequiredArgsConstructor
public class CreateAccountController {

    private final CreateAccountService createAccountService;


    @PostMapping
    public ResponseEntity<CreateAccountServiceResponse> createResource(@RequestHeader HttpHeaders headers, @Valid @RequestBody CreateAccountServiceRequest request) {
        return createResponseEntity(createAccountService.create(request));
    }


    private <O> ResponseEntity<O> createResponseEntity(O response) {
        return response != null ? ResponseEntity.ok(response) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
