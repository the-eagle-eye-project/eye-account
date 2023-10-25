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
            EyeAccountMapper eyeAccountMapper = Mappers.getMapper(EyeAccountMapper.class);
            EyeAccountEntity eyeAccountEntity = eyeAccountMapper.createAccountServiceRequestToEyeAccountEntity(request);
            eyeAccountRepository.save(eyeAccountEntity);
        }

        return null;
    }
}
