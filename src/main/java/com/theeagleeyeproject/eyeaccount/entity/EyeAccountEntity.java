package com.theeagleeyeproject.eyeaccount.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "eye_account")
public class EyeAccountEntity {

    @Id
    @Field(name = "account_id")
    private String accountId;

    @Field(name = "first_name")
    private String firstName;

    @Field(name = "last_name")
    private String lastName;

    @Field(name = "email_address")
    private String emailAddress;

    @Field(name = "country_of_registration")
    private String countryOfRegistration;

    @CreatedDate
    @Field(name = "record_created_timestamp")
    private LocalDateTime recordCreatedTimestamp;

    @LastModifiedDate
    @Field(name = "record_updated_timestamp")
    private LocalDateTime recordUpdatedTimestamp;

    @CreatedBy
    @Field(name = "record_created_by")
    private String recordCreatedBy;

    @LastModifiedBy
    @Field(name = "record_updated_by")
    private String recordUpdatedBy;

}
