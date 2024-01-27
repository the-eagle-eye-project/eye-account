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

        // Validates that the pre-process jobs on each Job, do have a Job related and is not orphan.
        validatePreProcessedJobs(jobConfigurations);
    }

    /**
     * Validates the pre-processed jobs that are listed as part of the Jobs configured under one application.
     *
     * @param jobConfigurations a list of all the Jobs Configurations an account has.
     */
    private void validatePreProcessedJobs(List<JobConfiguration> jobConfigurations) {
        // Validate the pre-processed job ID is valid and exists within the Application's jobs.
        List<String> jobNames = jobConfigurations.stream()
                .map(JobConfiguration::getJobName)
                .toList();

        for (JobConfiguration jobConfiguration : jobConfigurations) {
            List<String> preProcessedJobs = jobConfiguration.getPreProcessedJobsName();

            // If Pre-processed jobs are found under the Job configuration, will then proceed with the  logic.
            if (preProcessedJobs != null) {
                for (String preProcessedJob : preProcessedJobs) {

                    // Temp variable that holds the validation status
                    boolean preProcessFound = false;
                    // Iterates through all the Jobs configured under the same application.
                    for (String jobName : jobNames) {

                        // Compares the Pre-processed job name with all the Jobs configured under the application.
                        // If a Pre-processed Job is found in the configured job list, then it will set the flag to true,
                        // however, if it's not found, then it will  leave the flag as FALSE, then an exception will be thrown.
                        if (preProcessedJob.equals(jobName)) {
                            preProcessFound = true;
                            break;
                        }
                    }
                    // Throws an exception when a Pre-Processed job is found, but it's orphan from the configured jobs. Therefore, no Jobs are found under the same name
                    if (!preProcessFound) {
                        throw new BirdException(ExceptionCategory.VALIDATION_ERROR, "One or more job/s in the pre processed list doesn't have a parent.");
                    }
                }
            }
        }
    }
}
