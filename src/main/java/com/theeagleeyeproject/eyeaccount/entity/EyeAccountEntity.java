package com.theeagleeyeproject.eyeaccount.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Getter
@Setter
@Document(collation = "eye_account")
public class EyeAccountEntity {

    @Id
    @Field(name = "account_id")
    private String accountId;

    @Field(name = "first_name")
    private String firstName;

}
