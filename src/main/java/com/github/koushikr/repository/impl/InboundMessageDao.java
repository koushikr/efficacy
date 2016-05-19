package com.github.koushikr.repository.impl;

import com.github.koushikr.models.InboundEntity;
import com.github.koushikr.repository.InboundRepository;
import io.dropwizard.Configuration;
import io.dropwizard.sharding.DBShardingBundle;
import io.dropwizard.sharding.dao.LookupDao;
import io.dropwizard.sharding.dao.RelationalDao;

import java.sql.Date;

/**
 * Created by koushikr on 19/05/16.
 */
public class InboundMessageDao implements InboundRepository {

    private final RelationalDao<InboundEntity> relationalDao;
    private final LookupDao<InboundEntity> lookupDao;

    /**
     * Creates a new Dao with the given lookUp and relational values
     *
     * @param shardingBundle
     */
    public InboundMessageDao(DBShardingBundle<? extends Configuration> shardingBundle) {
        this.relationalDao = DBShardingBundle.createRelatedObjectDao(shardingBundle, InboundEntity.class);
        this.lookupDao = DBShardingBundle.createParentObjectDao(shardingBundle, InboundEntity.class);
    }

    @Override
    public InboundEntity findByMessageId(String messageId) throws Exception {
        return lookupDao.get(messageId).get();
    }

    @Override
    public void persist(InboundEntity message) throws Exception {
        message.setUpdatedAt(new Date(System.currentTimeMillis()));
        relationalDao.save(message.getMessageId(), message);
    }

    @Override
    public void update(InboundEntity message) throws Exception {
        InboundEntity inboundEntity = findByMessageId(message.getMessageId());
        message.loadFromMessage(inboundEntity);
        relationalDao.save(message.getMessageId(), message);
    }
}
