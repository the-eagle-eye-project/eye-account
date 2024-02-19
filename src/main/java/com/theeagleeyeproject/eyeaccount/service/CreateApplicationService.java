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
import com.theeagleeyeproject.eyeaccount.service.helper.ApplicationRequestValidator;
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

    /**
     * Application repository, used to access and perform CRUD ops into the application collection.
     */
    private final EyeApplicationRepository eyeApplicationRepository;

    /**
     * Account repository, used to access and read the existing accounts stored in the accounts collection.
     */
    private final EyeAccountRepository eyeAccountRepository;

    /**
     * Request application validator
     */
    private final ApplicationRequestValidator applicationRequestValidator;

    /**
     * Used to handle the user's request to register or create a new application into the system.
     *
     * @param request user's request of type {@link CreateApplicationServiceRequest}
     * @return an object of type {@link CreateApplicationServiceResponse}
     */
    public CreateApplicationServiceResponse create(CreateApplicationServiceRequest request) {

        // Validate the application request.
        applicationRequestValidator.validate(request);

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
            } else {
                // If the account is not available, then throw an exception.
                throw new BirdException(ExceptionCategory.VALIDATION_ERROR, "Account information or data not found.");
            }
        }

        return eyeApplicationMapper.eyeApplicationEntityToCreateApplicationServiceResponse(savedEyeApplicationEntity);
    }
}
