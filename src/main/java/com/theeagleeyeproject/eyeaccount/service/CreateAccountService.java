package com.theeagleeyeproject.eyeaccount.service;

import com.theeagleeyeproject.eaglewings.exception.BirdException;
import com.theeagleeyeproject.eaglewings.exception.ExceptionCategory;
import com.theeagleeyeproject.eaglewings.security.Role;
import com.theeagleeyeproject.eaglewings.utility.JwtUtil;
import com.theeagleeyeproject.eyeaccount.dao.EyeAccountRepository;
import com.theeagleeyeproject.eyeaccount.entity.EyeAccountEntity;
import com.theeagleeyeproject.eyeaccount.model.CreateAccountServiceRequest;
import com.theeagleeyeproject.eyeaccount.model.CreateAccountServiceResponse;
import com.theeagleeyeproject.eyeaccount.service.helper.EyeAccountMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAccountService {

    private final EyeAccountRepository eyeAccountRepository;

    private final JwtUtil jwtUtil;

    public CreateAccountServiceResponse create(CreateAccountServiceRequest request) {

        CreateAccountServiceResponse serviceResponse = null;

        if (request != null) {
            // Query the collection to verify that the account been registered doesn't already exist.
            EyeAccountEntity existingEyeAccountEntity = eyeAccountRepository.findByEmailAddress(request.getEmailAddress());
            if (existingEyeAccountEntity == null) {
                EyeAccountMapper eyeAccountMapper = Mappers.getMapper(EyeAccountMapper.class);
                EyeAccountEntity eyeAccountEntity = eyeAccountMapper.createAccountServiceRequestToEyeAccountEntity(request);

                // Save the record into the database.
                EyeAccountEntity savedAccount = eyeAccountRepository.save(eyeAccountEntity);

                String jwt = jwtUtil.generateToken(savedAccount.getId(), Role.USER);
                serviceResponse = eyeAccountMapper.eyeAccountEntityToCreateAccountServiceResponse(savedAccount);
                serviceResponse.setJwt(jwt);


                // TODO: call a client to send a verification email to the consumer, so that the account can be activated.
            } else {
                throw new BirdException(ExceptionCategory.CONFLICT, "The account already exists.");
            }

        }
        return serviceResponse;
    }
}
