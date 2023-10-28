package com.theeagleeyeproject.eyeaccount.service.helper;

import com.theeagleeyeproject.eyeaccount.entity.EyeAccountEntity;
import com.theeagleeyeproject.eyeaccount.model.CreateAccountServiceRequest;
import com.theeagleeyeproject.eyeaccount.model.CreateAccountServiceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EyeAccountMapper {

    @Mapping(target = "accountId", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "accountType", constant = "CLASSIC")
    EyeAccountEntity createAccountServiceRequestToEyeAccountEntity(CreateAccountServiceRequest createAccountServiceRequest);

    @Mapping(target = "message", expression = "java(\"Please save the integration id, since it has to be used" +
            "to send logs to the database.\")")
    CreateAccountServiceResponse eyeAccountEntityToCreateAccountServiceResponse(EyeAccountEntity eyeAccountEntity);
}
