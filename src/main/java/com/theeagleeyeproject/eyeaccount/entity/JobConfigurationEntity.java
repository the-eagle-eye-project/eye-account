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
 * {@link JobConfigurationEntity} used to add more jobs to
 */
@Setter
@Getter
public class JobConfigurationEntity {

    @Field(name = "job_id")
    private String jobId;

    @Field(name = "job_name")
    private String jobName;

    @Enumerated(EnumType.STRING)
    @Field(name = "job_type")
    private JobType jobType;

    @Field(name = "pre_process_jobs")
    private List<String> preProcessJobs;

    @Field(name = "tps")
    private int tps;

    @Field(name = "alerting_information")
    private AlertingInformation alertingInformation;
}
