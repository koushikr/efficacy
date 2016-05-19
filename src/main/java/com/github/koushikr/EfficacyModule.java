package com.github.koushikr;

import com.github.koushikr.core.MessageReceiver;
import com.github.koushikr.filters.InboundMessageFilter;
import com.github.koushikr.providers.MessageReceiverProvider;
import com.github.koushikr.repository.InboundRepository;
import com.github.koushikr.repository.OutboundRepository;
import com.github.koushikr.repository.impl.InboundMessageDao;
import com.github.koushikr.repository.impl.OutboundMessageDao;
import com.github.koushikr.repository.impl.ShardedOutboundMessageDao;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.dropwizard.Configuration;
import io.dropwizard.sharding.DBShardingBundle;
import lombok.AllArgsConstructor;

/**
 * Created by koushikr on 19/05/16.
 */
@AllArgsConstructor
public class EfficacyModule extends AbstractModule{

    private EfficacyConfiguration efficacyConfiguration;
    private DBShardingBundle<? extends Configuration> shardingBundle;

    @Override
    protected void configure() {
        bind(MessageReceiver.class).toProvider(MessageReceiverProvider.class).in(Scopes.SINGLETON);
        bind(InboundMessageFilter.class).in(Scopes.SINGLETON);
    }

    @Provides @Named("save_request_body")
    public boolean provideSaveRequestBodyConfiguration(){
        return efficacyConfiguration.isSaveRequestBody();
    }

    @Provides @Singleton
    public InboundRepository provideInboundRepository(){
        return new InboundMessageDao(shardingBundle);
    }

    @Provides @Singleton
    public OutboundRepository provideOutboundRepository(){
        return new ShardedOutboundMessageDao(shardingBundle);
    }

}
