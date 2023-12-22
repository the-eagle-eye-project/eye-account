package com.theeagleeyeproject.eyeaccount.service.helper;

import com.theeagleeyeproject.eyeaccount.entity.EyeApplicationEntity;
import com.theeagleeyeproject.eyeaccount.entity.JobConfigurationEntity;
import com.theeagleeyeproject.eyeaccount.model.CreateApplicationServiceRequest;
import com.theeagleeyeproject.eyeaccount.model.CreateApplicationServiceResponse;
import com.theeagleeyeproject.eyeaccount.model.JobConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {@link EyeApplicationMapper} used to map attributes from service request to entity and entity to service response.
 *
 * @author johnmartinez
 */
@Mapper
public interface EyeApplicationMapper {


    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(source = "jobConfiguration", target = "jobConfigurationsEntity", qualifiedByName = "toJobConfigurationEntityList")
    EyeApplicationEntity createApplicationServiceRequestToEyeApplicationEntity(CreateApplicationServiceRequest request);

    @Mapping(target = "message", expression = "java(\"Please save the integration id, since it has to be used" +
            "to send logs to the database.\")")
    CreateApplicationServiceResponse eyeApplicationEntityToCreateApplicationServiceResponse(EyeApplicationEntity entity);


    @Mapping(target = "jobId", expression = "java(java.util.UUID.randomUUID().toString())")
    JobConfigurationEntity jobConfigurationToJobConfigurationEntity(JobConfiguration jobConfiguration);

    @Named("toJobConfigurationEntityList")
    default List<JobConfigurationEntity> toJobConfigurationEntityList(List<JobConfiguration> jobConfigurations) {
        return jobConfigurations.stream()
                .filter(job -> Optional.ofNullable(job).isPresent())
                .map(this::jobConfigurationToJobConfigurationEntity)
                .collect(Collectors.toList());
    }

}
