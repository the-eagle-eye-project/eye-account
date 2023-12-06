package com.theeagleeyeproject.eyeaccount.entity;

import com.theeagleeyeproject.eyeaccount.model.ApplicationType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.List;

@Getter
@Setter
@Document("eye_app")
public class EyeApplicationEntity {

    @Id
    @Field(name = "application_id")
    private String applicationId;

    @Field(name = "application_name")
    private String applicationName;

    @Enumerated(EnumType.STRING)
    @Field(name = "application_type")
    private ApplicationType applicationType;

    @Field(name = "job_configuration")
    private List<JobConfigurationEntity> jobConfigurationsEntity;
}
