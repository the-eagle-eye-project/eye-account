package com.theeagleeyeproject.eyeaccount.model;

import lombok.Getter;

@Getter
public enum AccountType {

    CLASSIC(100, 1);

    /**
     * Determines the throttler for the account. How many logs can the consumer push to Eagle Eye in a daily basis.
     */
    private final int logThrottler;

    /**
     * Determines the life of a log record in the database.
     */
    private final int logTtlMonth;

    AccountType(int logThrottler, int logTtlMonth) {
        this.logThrottler = logThrottler;
        this.logTtlMonth = logTtlMonth;
    }
}
