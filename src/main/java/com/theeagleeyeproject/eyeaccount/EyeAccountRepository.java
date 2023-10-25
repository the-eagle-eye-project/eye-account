package com.theeagleeyeproject.eyeaccount;

import com.theeagleeyeproject.eyeaccount.entity.EyeAccountEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EyeAccountRepository extends MongoRepository<EyeAccountEntity, String> {
}
