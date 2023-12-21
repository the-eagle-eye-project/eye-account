package com.theeagleeyeproject.eyeaccount.dao;

import com.theeagleeyeproject.eyeaccount.entity.EyeAccountEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * {@link EyeAccountRepository} is used to query the account collection.
 *
 * @author John Robert Martinez Ponce
 */
@Repository
public interface EyeAccountRepository extends MongoRepository<EyeAccountEntity, UUID> {

    /**
     * Used to find an account by email address stored in the database.
     *
     * @param emailAddress to lookup
     * @return an object of type {@link EyeAccountEntity}
     */
    EyeAccountEntity findByEmailAddress(String emailAddress);

    /**
     * Used to lookup account's stored information.
     *
     * @param accountId UUID generated when the account was initially created
     * @return an object of type {@link EyeAccountEntity}
     */
    EyeAccountEntity findByAccountId(UUID accountId);
}
