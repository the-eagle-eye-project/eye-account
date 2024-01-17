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
import java.util.Optional;

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

        CreateApplicationServiceResponse createApplicationServiceResponse;

        if (eyeApplicationRepository.findByApplicationName(request.getApplicationName()) == null) {
            EyeApplicationMapper eyeApplicationMapper = Mappers.getMapper(EyeApplicationMapper.class);

            EyeApplicationEntity savedEyeApplicationEntity = null;


            // Save the Application to the account, so that it's related.
            String accountId = WebSecurityConfig.getPrincipal();
            if (accountId != null) {
                Optional<EyeAccountEntity> accountEntity = eyeAccountRepository.findById(accountId);
                // Verify if there are available applications related to the accounts. If there are non, then create a
                // new collection under the account.
                if (accountEntity.isPresent()) {
                    EyeAccountEntity eyeAccountEntity = accountEntity.get();
                    List<String> applications = eyeAccountEntity.getApplications();
                    EyeApplicationEntity eyeApplicationEntity = eyeApplicationMapper.createApplicationServiceRequestToEyeApplicationEntity(request);
                    if (applications == null) {
                        List<String> newApplications = Collections.singletonList(eyeApplicationEntity.getId());
                        eyeAccountEntity.setApplications(newApplications);
                    } else {
                        eyeAccountEntity.getApplications().add(eyeApplicationEntity.getId());
                    }
                    savedEyeApplicationEntity = eyeApplicationRepository.save(eyeApplicationEntity);
                    eyeAccountRepository.save(eyeAccountEntity);
                }
            }

            createApplicationServiceResponse = eyeApplicationMapper.eyeApplicationEntityToCreateApplicationServiceResponse(savedEyeApplicationEntity);

        } else {
            throw new BirdException(ExceptionCategory.CONFLICT, "There is a conflict with the application name.");
        }

        return createApplicationServiceResponse;
    }
}
