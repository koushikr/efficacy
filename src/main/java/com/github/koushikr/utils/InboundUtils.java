package com.github.koushikr.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.koushikr.core.TransactionManager;
import com.github.koushikr.models.InboundEntity;
import com.google.common.base.Joiner;
import org.glassfish.jersey.message.internal.ReaderWriter;
import org.glassfish.jersey.server.ContainerException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.github.koushikr.core.TransactionManager.startTransaction;
import static com.github.koushikr.enums.EfficacyConstants.*;

/**
 * Created by koushikr on 19/05/16.
 */
public class InboundUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static boolean isLoggingEnabled(ContainerRequestContext request) {
        String loggingEnabled = request.getHeaderString(LOGGING_ENABLED.getHeaderName());
        return null == loggingEnabled || (null != request.getHeaderString(MESSAGE_ID.getHeaderName()) && Boolean.valueOf(loggingEnabled));
    }

    public static void setupTransactionTracing(ContainerRequestContext request) {
        String transactionId = request.getHeaderString(TRANSACTION_ID.getHeaderName());
        if (transactionId == null)
            transactionId = "TXN-" + UUID.randomUUID().toString();

        startTransaction(transactionId);
    }

    public static void endTransaction() {
        TransactionManager.endTransaction();
    }

    public static InboundEntity createInboundMessage(ContainerRequestContext request, Boolean saveRequestBody, InboundEntity inboundMessage) {
        InboundEntity message = new InboundEntity();
        message.setMessageId(request.getHeaderString(MESSAGE_ID.getHeaderName()));
        message.setTransactionId(request.getHeaderString(TRANSACTION_ID.getHeaderName()));

        if (saveRequestBody)
            message.setRequestBody(Objects.isNull(inboundMessage) ? readFromRequest(request) : inboundMessage.getRequestBody());

        return message;
    }

    private static String readFromRequest(ContainerRequestContext request) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = request.getEntityStream();

        byte[] requestEntity = new byte[0];

        try {
            if (in.available() > 0) {
                ReaderWriter.writeTo(in, out);
                requestEntity = out.toByteArray();
                request.setEntityStream(new ByteArrayInputStream(requestEntity));  //Write back so that it gets consumed later
            }

            return new String(requestEntity);
        } catch (IOException ex) {
            throw new ContainerException(ex);
        }
    }

    private static String readFromResponse(ContainerResponseContext response) {
        Object entity = response.getEntity();
        String result = null;

        try {
            result = mapper.writeValueAsString(entity);   //Deserialize anyway
        } catch (JsonProcessingException e) {
            result = entity.toString();
        }

        return result;
    }

    public static InboundEntity createFromPreviousResponse(ContainerRequestContext request, InboundEntity inboundMessage, Boolean saveRequestBody) {
        InboundEntity message = createInboundMessage(request, saveRequestBody, inboundMessage);
        message.setResponseStatus(inboundMessage.getResponseStatus());
        message.setResponseBody(inboundMessage.getResponseBody());
        message.setResponseHeaders(inboundMessage.getResponseHeaders());

        return message;
    }

    public static InboundEntity createInboundMessage(ContainerRequestContext request, ContainerResponseContext response, Boolean saveRequestBody, InboundEntity inboundMessage) throws JsonProcessingException {
        InboundEntity message = createInboundMessage(request, saveRequestBody, inboundMessage);
        message.setResponseStatus(response.getStatus());
        message.setResponseBody(readFromResponse(response));

        Map<String, String> responseHeaders = new HashMap<String, String>();

        for (Map.Entry<String, List<Object>> entry : response.getHeaders().entrySet()) {
            responseHeaders.put(entry.getKey(), Joiner.on(",").join(entry.getValue()));
        }

        //Waste of serialization and de-serialization.
        message.setResponseHeaders(mapper.writeValueAsString(responseHeaders));
        return message;
    }

}
