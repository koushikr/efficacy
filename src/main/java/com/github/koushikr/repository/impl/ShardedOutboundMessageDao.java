package com.github.koushikr.repository.impl;

import com.github.koushikr.models.OutboundEntity;
import io.dropwizard.Configuration;
import io.dropwizard.sharding.DBShardingBundle;

/**
 * Created by koushikr on 19/05/16.
 */
//TODO::Implement a shard factory
public class ShardedOutboundMessageDao extends OutboundMessageDao {

    public ShardedOutboundMessageDao(DBShardingBundle<? extends Configuration> shardingBundle) {
        super(shardingBundle);
    }

    @Override
    public OutboundEntity findByMessageId(String messageId, String tableName) throws Exception {
        return null;
    }

}
