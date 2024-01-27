package com.theeagleeyeproject.eyeaccount.service.helper;

import com.theeagleeyeproject.eaglewings.exception.BirdException;
import com.theeagleeyeproject.eyeaccount.dao.EyeAccountRepository;
import com.theeagleeyeproject.eyeaccount.dao.EyeApplicationRepository;
import com.theeagleeyeproject.eyeaccount.entity.EyeAccountEntity;
import com.theeagleeyeproject.eyeaccount.entity.EyeApplicationEntity;
import com.theeagleeyeproject.eyeaccount.model.CreateApplicationServiceRequest;
import com.theeagleeyeproject.eyeaccount.model.JobConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationRequestValidatorTest {

    @Mock
    private EyeAccountRepository accountRepository;

    @Mock
    private EyeApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationRequestValidator validator;

    @Test
    void validate_doesASuccessfulComparison_Of_RequestApplicationName() {

        // Mocks the request object.
        String sampleApplicationName = "AppSample";
        CreateApplicationServiceRequest applicationServiceRequest = new CreateApplicationServiceRequest();
        applicationServiceRequest.setApplicationName(sampleApplicationName);

        // Mocks the Account stored in the database.
        EyeAccountEntity accountEntity = new EyeAccountEntity();
        accountEntity.setApplications(List.of("appId1", "appId2", "appId3"));
        when(accountRepository.findById(any())).thenReturn(Optional.of(accountEntity));

        // Mocks the retrieve of some Application stored in the database that matched the name in the request.
        EyeApplicationEntity applicationEntityId1 = new EyeApplicationEntity();
        applicationEntityId1.setId("appId5");
        EyeApplicationEntity applicationEntityId4 = new EyeApplicationEntity();
        applicationEntityId4.setId("appId4");
        when(applicationRepository.findByApplicationName(anyString())).thenReturn(List.of(applicationEntityId1, applicationEntityId4));

        // Mocks the Job Configurations
        JobConfiguration jobConfiguration1 = new JobConfiguration();
        jobConfiguration1.setJobName("Job_1");

        JobConfiguration jobConfiguration2 = new JobConfiguration();
        jobConfiguration2.setJobName("Job_2");
        jobConfiguration2.setPreProcessedJobsName(List.of("Job_1"));

        JobConfiguration jobConfiguration3 = new JobConfiguration();
        jobConfiguration3.setJobName("Job_3");
        jobConfiguration3.setJobName("Job_3");
        jobConfiguration3.setPreProcessedJobsName(List.of("Job_1", "Job_2"));

        List<JobConfiguration> jobConfigurations = new ArrayList<>(List.of(jobConfiguration1, jobConfiguration2, jobConfiguration3));
        applicationServiceRequest.setJobConfiguration(jobConfigurations);

        Assertions.assertDoesNotThrow(() -> validator.validate(applicationServiceRequest),
                "The request validator is failing at the compare of the existing applications and requested application name.");
    }

    @Test
    void validate_doesAFailed_Comparison_Of_RequestApplicationName() {

        // Mocks the request object.
        String sampleApplicationName = "AppSample";
        CreateApplicationServiceRequest applicationServiceRequest = new CreateApplicationServiceRequest();
        applicationServiceRequest.setApplicationName(sampleApplicationName);

        // Mocks the Account stored in the database.
        EyeAccountEntity accountEntity = new EyeAccountEntity();
        accountEntity.setApplications(List.of("appId1", "appId2", "appId3"));
        when(accountRepository.findById(any())).thenReturn(Optional.of(accountEntity));

        // Mocks the retrieve of some Application stored in the database that matched the name in the request.
        EyeApplicationEntity applicationEntityId1 = new EyeApplicationEntity();
        applicationEntityId1.setId("appId1");
        EyeApplicationEntity applicationEntityId4 = new EyeApplicationEntity();
        applicationEntityId4.setId("appId4");
        when(applicationRepository.findByApplicationName(anyString())).thenReturn(List.of(applicationEntityId1, applicationEntityId4));


        Assertions.assertThrows(BirdException.class, () -> validator.validate(applicationServiceRequest),
                "The request validator is failing at the compare of the existing applications and requested application name.");
    }

    @Test
    void validate_doesFail_whenOrphanJob_isFound() {

        // Mocks the request object.
        String sampleApplicationName = "AppSample";
        CreateApplicationServiceRequest applicationServiceRequest = new CreateApplicationServiceRequest();
        applicationServiceRequest.setApplicationName(sampleApplicationName);

        // Mocks the Account stored in the database.
        EyeAccountEntity accountEntity = new EyeAccountEntity();
        accountEntity.setApplications(List.of("appId1", "appId2", "appId3"));
        when(accountRepository.findById(any())).thenReturn(Optional.of(accountEntity));

        // Mocks the retrieve of some Application stored in the database that matched the name in the request.
        EyeApplicationEntity applicationEntityId1 = new EyeApplicationEntity();
        applicationEntityId1.setId("appId5");
        EyeApplicationEntity applicationEntityId4 = new EyeApplicationEntity();
        applicationEntityId4.setId("appId4");
        when(applicationRepository.findByApplicationName(anyString())).thenReturn(List.of(applicationEntityId1, applicationEntityId4));

        // Mocks the Job Configurations
        JobConfiguration jobConfiguration1 = new JobConfiguration();
        jobConfiguration1.setJobName("Job_1");

        JobConfiguration jobConfiguration2 = new JobConfiguration();
        jobConfiguration2.setJobName("Job_2");
        jobConfiguration2.setPreProcessedJobsName(List.of("Job_1"));

        JobConfiguration jobConfiguration3 = new JobConfiguration();
        jobConfiguration3.setJobName("Job_3");
        jobConfiguration3.setPreProcessedJobsName(List.of("Job_1", "Job_4"));

        List<JobConfiguration> jobConfigurations = new ArrayList<>(List.of(jobConfiguration1, jobConfiguration2, jobConfiguration3));
        applicationServiceRequest.setJobConfiguration(jobConfigurations);

        Assertions.assertThrows(BirdException.class, () -> validator.validate(applicationServiceRequest),
                "The request validator is failing at the compare of the existing applications and requested application name.");

    }
}