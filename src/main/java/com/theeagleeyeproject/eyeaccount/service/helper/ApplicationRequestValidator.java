package com.theeagleeyeproject.eyeaccount.service.helper;

import com.theeagleeyeproject.eaglewings.exception.BirdException;
import com.theeagleeyeproject.eaglewings.exception.ExceptionCategory;
import com.theeagleeyeproject.eaglewings.validator.BaseServiceValidator;
import com.theeagleeyeproject.eyeaccount.config.WebSecurityConfig;
import com.theeagleeyeproject.eyeaccount.dao.EyeAccountRepository;
import com.theeagleeyeproject.eyeaccount.dao.EyeApplicationRepository;
import com.theeagleeyeproject.eyeaccount.entity.EyeAccountEntity;
import com.theeagleeyeproject.eyeaccount.entity.EyeApplicationEntity;
import com.theeagleeyeproject.eyeaccount.model.CreateApplicationServiceRequest;
import com.theeagleeyeproject.eyeaccount.model.JobConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {@link ApplicationRequestValidator} is used to validate the incoming user request to create new applications into the system.
 * <p>
 * TODO: Should this validator be a Annotation that is added at the request object? Should the annotation be generic?
 *          Passing the validator class, the call to validate doesn't need to happen at the service, but at the request.
 *
 * @author johnmartinez
 */
@Component
@RequiredArgsConstructor
public class ApplicationRequestValidator implements BaseServiceValidator<CreateApplicationServiceRequest> {

    /**
     * Used to retrieve the account's data from the database.
     */
    private final EyeAccountRepository accountRepository;

    /**
     * Used to access the applications stored in the database.
     */
    private final EyeApplicationRepository applicationRepository;

    /**
     * Used to validate the user's request to create a new application in the system. An application is onboarded and
     * associated to an account.
     * <p>
     * The applications that are been used as pre-processed, should also be associated to the account. If a request with
     * application with a new application it's been onboarded, contains applications that are not associated with the
     * account related to the requester, then the validation would return an error message to the consumer.
     *
     * @param request object of type {@link CreateApplicationServiceRequest}
     */
    public void validate(CreateApplicationServiceRequest request) {

        // Validates if the name in the requested application to onboard exists as part of the applications associated to
        // the account making the request.
        validateApplicationName(request.getApplicationName());

        // Validates the integrity of all the Jobs register under the request application.
        validateJobConfiguration(request.getJobConfiguration());
    }

    /**
     * Used to validate the account's name. This validation ensures that the name is not repeated within the applications
     * that are associated to the account making the request to the API.
     *
     * @param applicationName name of the application that will be validated.
     */
    private void validateApplicationName(String applicationName) {
        // Retrieve the account id, so that the app can validate the accounts data, with the application it's been onboarded
        // to the system.
        Optional<EyeAccountEntity> accountEntity = accountRepository.findById(WebSecurityConfig.getPrincipal());
        if (accountEntity.isPresent()) {

            // Finds if there is any application in the database that has the same name as the one requested.
            List<EyeApplicationEntity> applicationEntities = applicationRepository.findByApplicationName(applicationName);

            if (!applicationEntities.isEmpty()) {
                // Extract all the IDs of the applications that matched with the application name.
                List<String> existingApplicationIds = applicationEntities.stream()
                        .map(EyeApplicationEntity::getId)
                        .toList();

                // Extract all the application ids related to the account that performed the request to create a new App.
                List<String> accountApplicationIds = accountEntity.get().getApplications();

                // Compare both lists of application ids, if one matches, then return an exception to the requester.
                if (!accountApplicationIds.isEmpty() && accountApplicationIds.stream().anyMatch(existingApplicationIds::contains)) {
                    throw new BirdException(ExceptionCategory.CONFLICT,
                            "There is an application with the same name associated with account.");

                }
            }
        }
    }

    /**
     * Validates the integrity of the job configuration object. It compares the jobs to make sure there are no duplicates.
     * </p>
     * The following attributes will be validated, with the explained logic.
     * - It to validate that the pre-processed job ids are actually present in the application request.
     * - Validates that there are no duplicated job in the configuration
     *
     * @param jobConfigurations configuration object that contains the details of a specific job inside an application.
     */
    private void validateJobConfiguration(List<JobConfiguration> jobConfigurations) {

        // TODO: NEEDS TO BE TESTED
        //      Needs to be moved to a BaseAccountValidator abstract class, so that it can be re-used when adding or editing jobs.

        // Verifies that the name of each job are not duplicate within the same application.
        boolean doesHaveRepeatedJobNames = jobConfigurations.stream()
                .collect(Collectors.groupingBy(JobConfiguration::getJobName))
                .values()
                .stream()
                .anyMatch(numberOfNames -> numberOfNames.size() > 1);

        if (doesHaveRepeatedJobNames) {
            throw new BirdException(ExceptionCategory.VALIDATION_ERROR,
                    "One or more Job names are the same. All jobs inside an application should contain a unique name.");
        }

        // Validate the pre-processed job ID is valid and exists within the Application's jobs.
        List<String> jobNames = jobConfigurations.stream()
                .map(JobConfiguration::getJobName)
                .toList();

        // TODO: finish this logic
        for (JobConfiguration jobConfiguration : jobConfigurations) {
            List<String> preProcessedJobs = jobConfiguration.getPreProcessedJobsName();
            boolean allJobsFound = false;
            for (String preProcessedJob : preProcessedJobs) {
                for (String jobName : jobNames) {
                    if (preProcessedJob.equals(jobName)) {
                        break;
                    }
                }
            }

        }

        // Do the comparison
        if (!jobConfigurations.stream().allMatch(jobNames::contains)) {
            throw new BirdException(ExceptionCategory.VALIDATION_ERROR, "One or more job/s in the pre processed list doesn't have a parent.");
        }

    }
}
