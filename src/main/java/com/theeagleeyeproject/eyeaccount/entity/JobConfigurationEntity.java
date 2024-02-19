package com.theeagleeyeproject.eyeaccount.entity;

import com.theeagleeyeproject.eyeaccount.model.AlertingInformation;
import com.theeagleeyeproject.eyeaccount.model.JobType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

/**
 * {@link JobConfigurationEntity} entity used to save/update application data to the database.
 *
 * @author John Robert Martinez Ponce
 */
@Setter
@Getter
public class JobConfigurationEntity {

    /**
     * Identifier of the job been configured
     */
    @Field(name = "job_id")
    private String jobId;

    /**
     * Name of the job. The name is based on the functionality of the job
     */
    @Field(name = "job_name")
    private String jobName;

    /**
     * Type of job
     */
    @Enumerated(EnumType.STRING)
    @Field(name = "job_type")
    private JobType jobType;

    /**
     * All the job Identifiers that needs to run before the current job can execute
     */
    @Field(name = "pre_processed_job_ids")
    private List<String> preProcessedJobIds;

    /**
     * Expected transactions per second of the job
     */
    @Field(name = "tps")
    private int tps;

    /**
     * Job's alerting information in case of an alert is needed
     */
    @Field(name = "alerting_information")
    private AlertingInformation alertingInformation;
}
