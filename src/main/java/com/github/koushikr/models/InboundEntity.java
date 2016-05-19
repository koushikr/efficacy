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

}
