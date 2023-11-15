package com.theeagleeyeproject.eyeaccount.dao;

import com.theeagleeyeproject.eyeaccount.entity.EyeAccountEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link EyeAccountRepository} is used to query the account collection.
 *
 * @author John Robert Martinez Ponce
 */
@Repository
public interface EyeAccountRepository extends MongoRepository<EyeAccountEntity, String> {

    /**
     * Used to find an account by email address stored in the database.
     *
     * @param emailAddress to lookup
     * @return an object of type {@link EyeAccountEntity}
     */
    EyeAccountEntity findByEmailAddress(String emailAddress);
}
