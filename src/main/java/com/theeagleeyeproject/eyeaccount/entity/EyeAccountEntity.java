package com.theeagleeyeproject.eyeaccount.entity;

import com.theeagleeyeproject.eyeaccount.model.AccountType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "eye_account")
public class EyeAccountEntity {

    @Id
    private String id;

    @Field(name = "first_name")
    private String firstName;

    @Field(name = "last_name")
    private String lastName;

    @Field(name = "email_address")
    private String emailAddress;

    @Field(name = "country_of_registration")
    private String countryOfRegistration;

    @Enumerated(EnumType.STRING)
    @Field(name = "account_type")
    private AccountType accountType;

    @Field(name = "company_name")
    private String companyName;

    @Field(name = "applications")
    private List<String> applications;

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

    @Version
    @Field(name = "version")
    private Long version;

}
