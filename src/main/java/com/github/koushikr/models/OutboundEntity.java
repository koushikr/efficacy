package com.github.koushikr.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.dropwizard.sharding.sharding.LookupKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by koushikr on 18/05/16.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "outbound_messages")
public class OutboundEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    @Id
    private long id;

    @Column(name = "message_id", nullable = false)
    @LookupKey
    protected String messageId;

    @Type(type = "text")
    @Column(name = "request_headerss")
    protected String requestHeaders;

    @Type(type = "text")
    @Column(name = "request_body")
    protected String requestBody;

    @Column(name = "response_status")
    protected Integer responseStatus;

    @Type(type = "text")
    @Column(name = "response_headers")
    protected String responseHeaders;

    @Type(type = "text")
    @Column(name = "response_body")
    protected String responseBody;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss z", timezone = "IST")
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss z", timezone = "IST")
    private Date updatedAt;

    public void loadFromMessage(OutboundEntity message){
        setMessageId(message.getMessageId());
        setResponseStatus(message.getResponseStatus());
        setResponseBody(message.getResponseBody());
        setResponseHeaders(message.getResponseHeaders());
        setRequestBody(message.getRequestBody());
    }

}
