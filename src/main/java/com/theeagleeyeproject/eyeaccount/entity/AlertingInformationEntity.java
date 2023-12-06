package com.theeagleeyeproject.eyeaccount.entity;

import com.theeagleeyeproject.eyeaccount.model.AlertChannel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class AlertingInformationEntity {

    @Field(name = "is_active_alert")
    private boolean activeAlert;

    @Enumerated(EnumType.STRING)
    @Field(name = "alert_channel")
    private AlertChannel alertChannel;

    @Field(name = "alert_send_info")
    private String alertSendInformation;
}
