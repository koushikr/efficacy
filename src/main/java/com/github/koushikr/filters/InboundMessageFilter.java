package com.github.koushikr.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.koushikr.annotations.Inbound;
import com.github.koushikr.core.MessageManager;
import com.github.koushikr.core.MessageReceiver;
import com.github.koushikr.enums.ErrorCodes;
import com.github.koushikr.enums.Exceptions;
import com.github.koushikr.enums.Status;
import com.github.koushikr.exceptions.EfficacyException;
import com.github.koushikr.exceptions.MessageLockedException;
import com.github.koushikr.exceptions.MessageProcessedException;
import com.github.koushikr.models.InboundEntity;
import com.github.koushikr.utils.InboundUtils;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by koushikr on 19/05/16.
 */
@Singleton @Slf4j @Inbound
public class InboundMessageFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final ObjectMapper mapper = new ObjectMapper();
    private MessageReceiver receiver;
    private Boolean saveRequestBody;

    @Inject
    public InboundMessageFilter(MessageReceiver messageReceiver, @Named("saveRequestBody") Boolean saveRequestBody) {
        this.receiver = messageReceiver;
        this.saveRequestBody = saveRequestBody;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (InboundUtils.isLoggingEnabled(requestContext)) {
            InboundUtils.setupTransactionTracing(requestContext);

            try {
                InboundEntity message = InboundUtils.createInboundMessage(requestContext, saveRequestBody, null);
                log.info("Received message with id {} ", message.getMessageId());

                InboundEntity inboundMessage = receiver.preHandle(message);
                MessageManager.setCurrentMessage(inboundMessage);

                if (inboundMessage.getProcessed() == Status.PROCESSED)
                    handleProcessedMessage(inboundMessage);
            } catch (Exception e) {
                if (e instanceof EfficacyException) {
                    EfficacyException exception = (EfficacyException) e;
                    if (exception.getException() == Exceptions.DUPLICATE_MESSAGE) {
                        log.error("Unable to process message. REASON: {}", Exceptions.DUPLICATE_MESSAGE.name());

                        Map<String, String> output = new HashMap<String, String>();
                        output.put("CODE", ErrorCodes.LOCKED.name());
                        output.put("MESSAGE", ErrorCodes.LOCKED.getErrorMessage());

                        Response response = Response.status(Response.Status.CONFLICT).header("Content-Type", "application/json").entity(output).build();
                        throw new MessageLockedException(response); //For aborting request
                    }
                }
                throw new WebApplicationException(e.getMessage());
            }
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if (InboundUtils.isLoggingEnabled(requestContext)) {
            InboundEntity inboundMessage = MessageManager.getCurrentMessage();
            InboundEntity message = null;

            try {
                if (inboundMessage != null && inboundMessage.getProcessed() == Status.PROCESSED) {
                    log.info("Message with id {} already processed. Returning previous response", inboundMessage.getMessageId());

                    //Message already processed, do not set from app, but set data from DB
                    message = InboundUtils.createFromPreviousResponse(requestContext, inboundMessage, saveRequestBody);
                } else {
                    message = InboundUtils.createInboundMessage(requestContext, responseContext, saveRequestBody, inboundMessage);
                    receiver.postHandle(message);
                }
            } catch (JsonProcessingException e) {
                log.error("Unable to parse response headers to JSON {}", e.getMessage());
                throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                        entity("Unable to parse response headers to JSON " + e.getMessage()).build());
            } catch (Exception e) {
                log.error("There is an error trying to process your request");
                throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                        entity("Unable to parse response headers to JSON " + e.getMessage()).build());
            } finally {
                if (message != null)
                    log.info("Finished processing message with id {}. Response Code {}", message.getMessageId(), message.getResponseStatus());

                MessageManager.endMessageProcessing();
                InboundUtils.endTransaction();
            }
        }
    }

    private void handleProcessedMessage(InboundEntity message) {
        Map<String, String> responseHeaders = new HashMap<String, String>();

        if (message.getResponseHeaders() != null) {
            try {
                responseHeaders = mapper.readValue(message.getResponseHeaders(), new TypeReference<Map<String, String>>() {
                });
            } catch (IOException e) {
                log.error("Unable to parse response headers with value {}.", message.getResponseHeaders());
            }
        }

        Response.ResponseBuilder builder = Response.status(Response.Status.fromStatusCode(message.getResponseStatus())).entity(message.getResponseBody());
        responseHeaders.entrySet().stream().map(entry -> builder.header(entry.getKey(), entry.getValue()));

        //If message already processed, return
        throw new MessageProcessedException(builder.build());
    }

}