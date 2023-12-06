package com.theeagleeyeproject.eyeaccount.model;

import lombok.Getter;

/**
 * {@link ApplicationType} shows what type of application is been registered.
 *
 * @author johnmartinez
 */
@Getter
public enum ApplicationType {

    REALTIME,
    BATCH,
    STREAM
}
