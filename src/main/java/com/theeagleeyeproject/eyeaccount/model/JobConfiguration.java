package com.theeagleeyeproject.eyeaccount.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * {@link JobConfiguration} contains the metadata of a job that will be monitored.
 *
 * @author John Robert Martinez
 */
@Data
public class JobConfiguration {

    /**
     * Name of the job. It shouldn't be repeated under the same application.
     */
    @NotNull
    private String jobId;

    /**
     * What kind of job is.
     */
    @NotNull
    private JobType jobType;

    /**
     * Creates a monitoring dependency between different jobs within the same application.
     * </p>
     * If the Application's jobs should be monitored in a specific order, then each should
     * have the dependencies respective to the order.
     */
    private List<String> preProcessedJobIds;

    /**
     * Transactions per second, that is expected by the job, otherwise it would create an alert.
     */
    private int tps;

    /**
     * Alert information used to communicate messages/alerts.
     */
    @Valid
    private AlertingInformation alertingInformation;


}
