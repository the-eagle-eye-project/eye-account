package com.theeagleeyeproject.eyeaccount.service;

import com.theeagleeyeproject.eaglewings.exception.BirdException;
import com.theeagleeyeproject.eaglewings.exception.ExceptionCategory;
import com.theeagleeyeproject.eyeaccount.dao.EyeApplicationRepository;
import com.theeagleeyeproject.eyeaccount.entity.EyeApplicationEntity;
import com.theeagleeyeproject.eyeaccount.model.CreateApplicationServiceRequest;
import com.theeagleeyeproject.eyeaccount.model.CreateApplicationServiceResponse;
import com.theeagleeyeproject.eyeaccount.service.helper.EyeApplicationMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

/**
 * {@link CreateAccountService} used to orchestrate consumer's request throughout the creation of a new application.
 *
 * @author johnmartinez
 */
@Service
@RequiredArgsConstructor
public class CreateApplicationService {

    private final EyeApplicationRepository eyeApplicationRepository;

    public CreateApplicationServiceResponse create(CreateApplicationServiceRequest request) {

        EyeApplicationEntity existingEyeApplicationEntity = eyeApplicationRepository.findByApplicationName(request.getApplicationName());
        CreateApplicationServiceResponse createApplicationServiceResponse;

        if (existingEyeApplicationEntity == null) {
            EyeApplicationMapper eyeApplicationMapper = Mappers.getMapper(EyeApplicationMapper.class);
            EyeApplicationEntity eyeApplicationEntity = eyeApplicationMapper.createApplicationServiceRequestToEyeApplicationEntity(request);
            var savedEyeApplicationEntity = eyeApplicationRepository.save(eyeApplicationEntity);


            // TODO: Save the Application to the account, so that it's related.

            
            createApplicationServiceResponse = eyeApplicationMapper.eyeApplicationEntityToCreateApplicationServiceResponse(savedEyeApplicationEntity);

        } else {
            throw new BirdException(ExceptionCategory.CONFLICT, "There is a conflict with the application name.");
        }

        return createApplicationServiceResponse;
    }
}
