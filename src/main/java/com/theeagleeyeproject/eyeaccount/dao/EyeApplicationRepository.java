package com.theeagleeyeproject.eyeaccount.dao;

import com.theeagleeyeproject.eyeaccount.entity.EyeApplicationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link EyeApplicationRepository} repository used to access the registered application data from the database.
 *
 * @author johnmartinez
 */
@Repository
public interface EyeApplicationRepository extends MongoRepository<EyeApplicationEntity, String> {

    /**
     * Used to find the registered applications by application name.
     *
     * @param applicationName name of the application
     * @return an entity of type {@link EyeApplicationEntity}
     */
    EyeApplicationEntity findByApplicationName(String applicationName);
}
