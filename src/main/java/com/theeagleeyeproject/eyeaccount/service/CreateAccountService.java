package com.theeagleeyeproject.eyeaccount.service;

import com.theeagleeyeproject.eyeaccount.EyeAccountRepository;
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

    public CreateAccountServiceResponse create(CreateAccountServiceRequest request) {

        if (request != null) {
            // Query the collection to verify that the account been registered doesn't already exist.
            EyeAccountEntity existingEyeAccountEntity = eyeAccountRepository.findByEmailAddress(request.getEmailAddress());
            if (existingEyeAccountEntity == null) {
                EyeAccountMapper eyeAccountMapper = Mappers.getMapper(EyeAccountMapper.class);
                EyeAccountEntity eyeAccountEntity = eyeAccountMapper.createAccountServiceRequestToEyeAccountEntity(request);

                // Save the record into the database.
                eyeAccountRepository.save(eyeAccountEntity);

                // TODO: call a client to send a verification email to the consumer, so that the account can be activated.
            } else {
                // TODO: create a proper ApplicationException class, that returns a proper response to the user.
                System.out.println("Account already exists");
            }


        }
        return null;
    }
}
