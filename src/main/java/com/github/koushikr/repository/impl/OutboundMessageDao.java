package com.github.koushikr.repository.impl;

import com.github.koushikr.models.OutboundEntity;
import com.github.koushikr.repository.OutboundRepository;
import io.dropwizard.Configuration;
import io.dropwizard.sharding.DBShardingBundle;

/**
 * Created by koushikr on 19/05/16.
 */
public class OutboundMessageDao implements OutboundRepository {

    public OutboundMessageDao(DBShardingBundle<? extends Configuration> shardingBundle) {

    }

    @Override
    public void persist(OutboundEntity message) {

    }

    @Override
    public OutboundEntity findByMessageId(String messageId, String tableName) {
        return null;
    }

    @Override
    public void update(OutboundEntity message) {

    }
}
