package com.github.koushikr.models;


import com.github.koushikr.enums.Status;
import io.dropwizard.sharding.sharding.LookupKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by koushikr on 18/05/16.
 */
@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "inbound_messages")
public class InboundEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @Id
    private long id;

    @Column(name = "message_id", nullable = false, unique = true)
    @LookupKey
    protected String messageId;

    @Column(name = "transaction_id", nullable = false)
    protected String transactionId;

    @Column(name = "request_body")
    @Type(type = "text")
    protected String requestBody;

    @Column(name = "created_at")
    protected Date createdAt;

    @Column(name = "processed_at")
    protected Date processedAt;

    @Column(name = "updated_at")
    protected Date updatedAt;

    @Column(name = "processed")
    protected Status processed = Status.PROCESSING;

    @Column(name = "retry_count")
    protected Integer retryCount = 0;

    @Column(name = "response_status")
    protected Integer responseStatus;

    @Type(type = "text")
    @Column(name = "response_headers")
    protected String responseHeaders;

    @Type(type = "text")
    @Column(name = "response_body")
    protected String responseBody;

    @Column(name = "duplicate_count")
    protected Integer duplicateRequestCount = 0;

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

    public void incrementDuplicateRequestCount() {
        this.duplicateRequestCount += 1;
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

}
