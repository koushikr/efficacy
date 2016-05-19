package com.github.koushikr.core;

import com.github.koushikr.models.InboundEntity;

/**
 * Created by koushikr on 19/05/16.
 */
public class MessageManager {

    private static final ThreadLocal<InboundEntity> currentMessage = new ThreadLocal<InboundEntity>();

    public static void endMessageProcessing() {
        currentMessage.remove();
    }

    public static InboundEntity getCurrentMessage() {
        return currentMessage.get();
    }

    public static void setCurrentMessage(InboundEntity message) {
        currentMessage.set(message);
    }

}
