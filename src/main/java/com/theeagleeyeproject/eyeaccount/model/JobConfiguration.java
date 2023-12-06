package com.theeagleeyeproject.eyeaccount.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * {@link JobConfiguration} used to add more jobs to
 */
@Data
public class JobConfiguration {

    @NotNull
    private String jobName;

    @NotNull
    private JobType jobType;

    private List<String> preProcessedJobs;

    private int tps;

    @Valid
    private AlertingInformation alertingInformation;


}
