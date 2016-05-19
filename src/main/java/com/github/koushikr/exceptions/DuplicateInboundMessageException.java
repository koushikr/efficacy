package com.github.koushikr.exceptions;

import com.github.koushikr.enums.Exceptions;

/**
 * Created by koushikr on 19/05/16.
 */
public class DuplicateInboundMessageException extends EfficacyException {

    public DuplicateInboundMessageException(String message) {
        super(message);
    }

}
