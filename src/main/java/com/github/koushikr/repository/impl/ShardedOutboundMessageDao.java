package com.github.koushikr.repository.impl;

import io.dropwizard.Configuration;
import io.dropwizard.sharding.DBShardingBundle;

/**
 * Created by koushikr on 19/05/16.
 */
public class ShardedOutboundMessageDao extends OutboundMessageDao {


    public ShardedOutboundMessageDao(DBShardingBundle<? extends Configuration> shardingBundle) {
        super(shardingBundle);
    }
}
