package com.theeagleeyeproject.eyeaccount.filter;

/**
 * Used to validate the role that it's been assigned in the JWT against the App's Security roles.
 *
 * @author John Robert Martinez Ponce
 */
public enum Role {

    USER,
    SYS_ADMIN,
    DEFAULT
}
