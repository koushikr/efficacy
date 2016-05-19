package com.github.koushikr.enums;

/**
 * Created by koushikr on 19/05/16.
 */
public enum EfficacyConstants {

    MESSAGE_ID("X-REQUEST-ID"),

    TRANSACTION_ID("X-TRANSACTION-ID"),

    LOGGING_ENABLED("X-LOGGING-ENABLED");

    private String headerName;

    EfficacyConstants(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return headerName;
    }

}
