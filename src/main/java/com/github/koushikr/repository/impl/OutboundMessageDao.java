package com.github.koushikr.repository.impl;

import com.github.koushikr.models.InboundEntity;
import com.github.koushikr.models.OutboundEntity;
import com.github.koushikr.repository.OutboundRepository;
import com.google.inject.Singleton;
import io.dropwizard.Configuration;
import io.dropwizard.sharding.DBShardingBundle;
import io.dropwizard.sharding.dao.LookupDao;
import io.dropwizard.sharding.dao.RelationalDao;
import lombok.Getter;

import javax.el.MethodNotFoundException;
import java.sql.Date;

/**
 * Created by koushikr on 19/05/16.
 */
@Singleton @Getter
public class OutboundMessageDao implements OutboundRepository {

    private final RelationalDao<OutboundEntity> relationalDao;
    private final LookupDao<OutboundEntity> lookupDao;

    private static final String OUTBOUND_MESSAGES = "outbound_messages";

    public OutboundMessageDao(DBShardingBundle<? extends Configuration> shardingBundle) {
        this.relationalDao = DBShardingBundle.createRelatedObjectDao(shardingBundle, OutboundEntity.class);
        this.lookupDao = DBShardingBundle.createParentObjectDao(shardingBundle, OutboundEntity.class);
    }

    @Override
    public OutboundEntity findByMessageId(String messageId) throws Exception {
        return lookupDao.get(messageId).get();
    }

    @Override
    public void persist(OutboundEntity message) throws Exception {
        message.setUpdatedAt(new Date(System.currentTimeMillis()));
        relationalDao.save(message.getMessageId(), message);
    }

    @Override
    public void update(OutboundEntity message) throws Exception {
        OutboundEntity inboundEntity = findByMessageId(message.getMessageId());
        message.loadFromMessage(inboundEntity);
        relationalDao.save(message.getMessageId(), message);
    }

    @Override
    public OutboundEntity findByMessageId(String messageId, String tableName) throws Exception {
        throw new MethodNotFoundException("Table name shard read not available on outbound_messages.");
    }

}
