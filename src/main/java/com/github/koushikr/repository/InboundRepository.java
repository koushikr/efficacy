package com.github.koushikr.repository;

import com.github.koushikr.models.InboundEntity;

/**
 * Created by koushikr on 19/05/16.
 */
public interface InboundRepository {

    InboundEntity findByMessageId(String messageId) throws Exception;

    void persist(InboundEntity message) throws Exception;

    void update(InboundEntity message) throws Exception;

}
