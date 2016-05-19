package com.github.koushikr.exceptions;

import com.github.koushikr.enums.Exceptions;

/**
 * Created by koushikr on 19/05/16.
 */
public class EfficacyException extends RuntimeException {

    private Exceptions exception;

    public EfficacyException(String message) {
        super(message);
    }

    public Exceptions getException() {
        return this.exception;
    }
}