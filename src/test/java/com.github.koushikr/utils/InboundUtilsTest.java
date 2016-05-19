package com.github.koushikr.utils;

import com.github.koushikr.core.TransactionManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * Created by koushikr on 19/05/16.
 */
public class InboundUtilsTest {

    @Mock
    private ContainerRequestContext request;
    @Mock private ContainerResponseContext response;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIsRestbusRequestShouldReturnTrueForRestbusRequests()    {
        when(request.getHeaderString("X-REQUEST-ID")).thenReturn("1");
        assertTrue(InboundUtils.isLoggingEnabled(request));
    }

    @Test
    public void testIsRestbusRequestShouldReturnFalseForNormalRequests()    {
        when(request.getHeaderString("X-REQUEST-ID")).thenReturn(null);
        assertFalse(InboundUtils.isLoggingEnabled(request));
    }

    @Test
    public void testSetupTransactionTracingShouldUseExistingTransaction()   {
        when(request.getHeaderString("X-TRANSACTION-ID")).thenReturn("TXN-12345");
        InboundUtils.setupTransactionTracing(request);
        assertEquals("TXN-12345", TransactionManager.getTransaction());
    }

    @Test
    public void testSetupTransactionTracingShouldCreateNewTransactionIfNoTransactionInContext() {
        when(request.getHeaderString("X-TRANSACTION-ID")).thenReturn(null);
        InboundUtils.setupTransactionTracing(request);
        assertNotNull(TransactionManager.getTransaction());
    }

    @Test
    public void testEndTransactionShouldDeleteTransaction() {
        when(request.getHeaderString("X-TRANSACTION-ID")).thenReturn("TXN-12345");
        InboundUtils.setupTransactionTracing(request);
        InboundUtils.endTransaction();
        assertNull(TransactionManager.getTransaction());
    }

}
