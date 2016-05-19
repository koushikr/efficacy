package com.github.koushikr.enums;

/**
 * Created by koushikr on 19/05/16.
 */
public enum ErrorCodes {

    LOCKED("Message Locked");

    private String errorMessage;

    ErrorCodes(String message) {
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
