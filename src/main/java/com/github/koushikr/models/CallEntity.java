package com.github.koushikr.models;

import io.dropwizard.sharding.sharding.LookupKey;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by koushikr on 18/05/16.
 */
@MappedSuperclass
@Setter
@Getter
public abstract class CallEntity {

    @Column(name = "message_id", nullable = false)
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

    public enum Status {
        FAILED(0), PROCESSED(1), PROCESSING(null);   //As they are in DB

        private static final Map<Integer, Status> lookup = new HashMap<Integer, Status>();    //Reverse map from code to ENUM

        static {
            for (Status s : EnumSet.allOf(Status.class))
                lookup.put(s.getCode(), s);
        }

        private Integer code = null;

        private Status(Integer code) {
            this.code = code;
        }

        public static Status get(Integer code) {
            return lookup.get(code);
        }

        public Integer getCode() {
            return code;
        }
    }

}
