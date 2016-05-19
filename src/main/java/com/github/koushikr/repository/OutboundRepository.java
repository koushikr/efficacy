package com.github.koushikr.repository;

import com.github.koushikr.models.OutboundEntity;

/**
 * Created by koushikr on 19/05/16.
 */
public interface OutboundRepository {

    void persist(OutboundEntity message);

    OutboundEntity findByMessageId(String messageId, String tableName);

    void update(OutboundEntity message);

}
