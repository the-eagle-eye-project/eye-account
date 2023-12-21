package com.theeagleeyeproject.eyeaccount.service;

import com.theeagleeyeproject.eaglewings.exception.BirdException;
import com.theeagleeyeproject.eaglewings.exception.ExceptionCategory;
import com.theeagleeyeproject.eyeaccount.config.WebSecurityConfig;
import com.theeagleeyeproject.eyeaccount.dao.EyeAccountRepository;
import com.theeagleeyeproject.eyeaccount.dao.EyeApplicationRepository;
import com.theeagleeyeproject.eyeaccount.entity.EyeAccountEntity;
import com.theeagleeyeproject.eyeaccount.entity.EyeApplicationEntity;
import com.theeagleeyeproject.eyeaccount.model.CreateApplicationServiceRequest;
import com.theeagleeyeproject.eyeaccount.model.CreateApplicationServiceResponse;
import com.theeagleeyeproject.eyeaccount.service.helper.EyeApplicationMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * {@link CreateAccountService} used to orchestrate consumer's request throughout the creation of a new application.
 *
 * @author johnmartinez
 */
@Service
@RequiredArgsConstructor
public class CreateApplicationService {

    private final EyeApplicationRepository eyeApplicationRepository;

    private final EyeAccountRepository eyeAccountRepository;

    public CreateApplicationServiceResponse create(CreateApplicationServiceRequest request) {

        EyeApplicationEntity existingEyeApplicationEntity = eyeApplicationRepository.findByApplicationName(request.getApplicationName());
        CreateApplicationServiceResponse createApplicationServiceResponse;

        if (existingEyeApplicationEntity == null) {
            EyeApplicationMapper eyeApplicationMapper = Mappers.getMapper(EyeApplicationMapper.class);
            EyeApplicationEntity eyeApplicationEntity = eyeApplicationMapper.createApplicationServiceRequestToEyeApplicationEntity(request);
            EyeApplicationEntity savedEyeApplicationEntity = null;


            // Save the Application to the account, so that it's related.
            UUID accountId = WebSecurityConfig.getPrincipal();
            if (accountId != null) {
                EyeAccountEntity accountEntity = eyeAccountRepository.findByAccountId(accountId);
                List<String> accountApplications = accountEntity.getApplications();
                String newApplications = String.valueOf(eyeApplicationEntity.getApplicationId());

                // Verify if there are available applications related to the accounts. If there are non, then create a
                // new collection under the account.
                if (accountApplications == null) {
                    accountEntity.setApplications(Collections.singletonList(newApplications));
                } else {
                    accountApplications.add(newApplications);
                    accountEntity.setApplications(accountApplications);
                }
                savedEyeApplicationEntity = eyeApplicationRepository.save(eyeApplicationEntity);
                eyeAccountRepository.save(accountEntity); // TODO: fix this update, which is throwing an duplicate key exception.
            }

            createApplicationServiceResponse = eyeApplicationMapper.eyeApplicationEntityToCreateApplicationServiceResponse(savedEyeApplicationEntity);

        } else {
            throw new BirdException(ExceptionCategory.CONFLICT, "There is a conflict with the application name.");
        }

        return createApplicationServiceResponse;
    }
}
