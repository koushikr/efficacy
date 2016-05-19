package com.github.koushikr.core;

import com.github.koushikr.enums.Exceptions;
import com.github.koushikr.exceptions.DuplicateInboundMessageException;
import com.github.koushikr.exceptions.EfficacyException;
import com.github.koushikr.models.CallEntity.Status;
import com.github.koushikr.models.InboundEntity;
import com.github.koushikr.repository.InboundRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Created by koushikr on 19/05/16.
 */
@Slf4j
public enum MessageReceiver {
    INSTANCE;

    private InboundRepository inboundMessageRepository;

    public void init(InboundRepository repository) {
        this.inboundMessageRepository = repository;
    }

    /**
     * @param message
     */
    public InboundEntity preHandle(InboundEntity message) throws Exception {
        log.debug("Pre-handling message");
        InboundEntity inboundMessage;

        inboundMessage = getInboundMessage(message);
        if (inboundMessage == null) {
            try {
                inboundMessage = createInboundMessage(message);
            } catch (RuntimeException ex) {
                log.error("Error! Message (" + message.getMessageId() + " is already present. You can't write the same messageId twice.");
                throw new DuplicateInboundMessageException(Exceptions.DUPLICATE_MESSAGE.name());
            }
        } else {
            processExistingMessage(inboundMessage);
        }

        return inboundMessage;
    }

    private void processExistingMessage(InboundEntity inboundMessage) throws Exception {
        switch (inboundMessage.getProcessed()) {
            case PROCESSING:
                break;
            case FAILED:
                break;
            case PROCESSED:
                log.info("Message already processed in the INBOUND_MESSAGES table: " + inboundMessage.getMessageId());
                inboundMessage.incrementDuplicateRequestCount();
                updateMessage(inboundMessage);
                break;
            default:
                log.error("Message has a wrong unimplemented processed state: " + inboundMessage.getMessageId());
                throw new EfficacyException(Exceptions.UNIMPLEMENTED_PROCESSED_STATE.name());
        }
    }

    /**
     * @param message
     */
    public void postHandle(InboundEntity message) throws Exception {
        log.debug("PostHandling message");

        final InboundEntity inboundMessage = getInboundMessage(message);
        try {
            inboundMessage.setResponseStatus(message.getResponseStatus());
            inboundMessage.setResponseBody(message.getResponseBody());
            inboundMessage.setResponseHeaders(message.getResponseHeaders());
        } finally {
            updateMessage(inboundMessage);
        }
    }

    private InboundEntity getInboundMessage(InboundEntity message) throws Exception {
        return inboundMessageRepository.findByMessageId(message.getMessageId());
    }

    protected InboundEntity createInboundMessage(InboundEntity message) throws Exception {
        //new request
        final InboundEntity ibMessage = new InboundEntity(message);
        inboundMessageRepository.persist(ibMessage);
        return ibMessage;
    }

    /**
     * called by the filter upon completion of API call
     * independent of the application
     *
     * @param message
     */
    private void updateMessage(InboundEntity message) throws Exception {
        final int status = message.getResponseStatus();

        if (status >= 200 && status < 300) {
            message.setProcessed(Status.PROCESSED);
            message.setProcessedAt(new Date(System.currentTimeMillis()));
        } else {
            message.setProcessed(Status.FAILED);
        }

        inboundMessageRepository.update(message);
    }
}

