package com.github.koushikr.core;

/**
 * Created by koushikr on 19/05/16.
 */
public class TransactionManager {

    private static final ThreadLocal<String> transactionContext = new ThreadLocal<String>();

    public static void startTransaction(String transactionId) {
        transactionContext.set(transactionId);
    }

    public static String getTransaction() {
        return transactionContext.get();
    }

    public static void endTransaction() {
        transactionContext.remove();
    }

}
