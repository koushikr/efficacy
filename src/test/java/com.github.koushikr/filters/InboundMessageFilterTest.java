package com.github.koushikr.filters;

import com.github.koushikr.core.MessageManager;
import com.github.koushikr.core.MessageReceiver;
import com.github.koushikr.enums.Exceptions;
import com.github.koushikr.enums.Status;
import com.github.koushikr.exceptions.EfficacyException;
import com.github.koushikr.exceptions.MessageProcessedException;
import com.github.koushikr.models.InboundEntity;
import com.github.koushikr.utils.InboundUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * Created by koushikr on 19/05/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({InboundUtils.class, MessageReceiver.class})
public class InboundMessageFilterTest {

    private InboundMessageFilter idempotencyFilter;

    @Mock
    private MessageReceiver receiver;
    @Mock private ContainerRequestContext requestContext;
    @Mock private ContainerResponseContext responseContext;
    @Mock private InboundEntity message;
    @Mock private InboundEntity inboundMessage;
    private String requestBody = "{\"workflow_instance_id\":\"12356\"}";

    @Before
    public void setup() throws Exception{
        MockitoAnnotations.initMocks(this);
        idempotencyFilter = new InboundMessageFilter(receiver);

        mockStatic(InboundMessageFilter.class);
        when(receiver.preHandle(message)).thenReturn(inboundMessage);
    }

    @Test
    public void testRequestFilterShouldReturnExistingResponseIfMessageAlreadyProcessed() throws IOException {
        String value = "{\"workflow_instance_id\":\"12356\"}";

        when(inboundMessage.getProcessed()).thenReturn(Status.PROCESSED);
        when(inboundMessage.getResponseStatus()).thenReturn(201);
        when(inboundMessage.getResponseBody()).thenReturn(value);

        try {
            idempotencyFilter.filter(requestContext);
        } catch (MessageProcessedException e)  {
            Response response = e.getResponse();
            assertEquals(response.getStatus(), 201);
            assertEquals(response.getEntity().toString(), value);
        }
    }


    @Test
    public void testResponseFilterShouldNotCallPostHandleIfMessageIsAlreadyProcessed() throws Exception {
        when(inboundMessage.getProcessed()).thenReturn(Status.PROCESSED);

        MessageManager.setCurrentMessage(inboundMessage);

        idempotencyFilter.filter(requestContext, responseContext);

        verify(receiver, times(0)).postHandle(message);
    }

}
