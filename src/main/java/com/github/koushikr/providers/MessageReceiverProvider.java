package com.github.koushikr.providers;

import com.github.koushikr.core.MessageReceiver;
import com.github.koushikr.repository.InboundRepository;
import com.google.inject.Provider;

import javax.inject.Inject;

/**
 * Created by koushikr on 19/05/16.
 */
public class MessageReceiverProvider implements Provider<MessageReceiver> {
    private final InboundRepository inboundMessageRepository;

    @Inject
    public MessageReceiverProvider(InboundRepository inboundRepository){
        this.inboundMessageRepository = inboundRepository;
    }

    @Override
    public MessageReceiver get() {
        MessageReceiver receiver = MessageReceiver.INSTANCE;
        receiver.init(inboundMessageRepository);
        return receiver;
    }
}


