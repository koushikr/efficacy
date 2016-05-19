package com.github.koushikr.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by koushikr on 18/05/16.
 */
@Setter
@Getter
@Entity
@Table(name = "inbound_messages")
public class InboundEntity extends CallEntity {

    public InboundEntity() {
        super();
    }

    public InboundEntity(InboundEntity message) {
        this.messageId = message.getMessageId();
        this.createdAt = message.getCreatedAt();
        this.updatedAt = message.getUpdatedAt();
        this.transactionId = message.getTransactionId();
        this.responseBody = message.getResponseBody();
        this.responseHeaders = message.getResponseHeaders();
        this.requestBody = message.getRequestBody();
        this.responseStatus = message.getResponseStatus();
    }

    /**
     *
     */
    public void incrementDuplicateRequestCount() {
        this.setDuplicateRequestCount(this.getDuplicateRequestCount() + 1);
    }

    public void loadFromMessage(InboundEntity message) {
        setMessageId(message.getMessageId());
        setRetryCount(message.getRetryCount());
        setResponseStatus(message.getResponseStatus());
        setResponseBody(message.getResponseBody());
        setResponseHeaders(message.getResponseHeaders());
        setProcessed(message.getProcessed());
        setProcessedAt(message.getProcessedAt());
        setDuplicateRequestCount(message.getDuplicateRequestCount());
        setRequestBody(message.getRequestBody());

        if (message.getCreatedAt() != null)
            setCreatedAt(message.getCreatedAt());

        if (message.getUpdatedAt() != null)
            setUpdatedAt(message.getUpdatedAt());
    }

}
