package com.github.koushikr.repository;

import com.github.koushikr.models.OutboundEntity;

/**
 * Created by koushikr on 19/05/16.
 */
public interface OutboundRepository {

    void persist(OutboundEntity message) throws Exception;

    OutboundEntity findByMessageId(String messageId) throws Exception;

    void update(OutboundEntity message) throws Exception;

    OutboundEntity findByMessageId(String messageId, String tableName) throws Exception;

}
