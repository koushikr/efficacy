package com.github.koushikr.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by koushikr on 19/05/16.
 */
public class MessageLockedException extends WebApplicationException {

    public MessageLockedException(Response response) {
        super(response);
    }

}
