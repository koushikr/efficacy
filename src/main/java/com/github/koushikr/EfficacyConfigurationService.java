package com.github.koushikr;

import io.dropwizard.Configuration;

/**
 * Created by koushikr on 19/05/16.
 */
public interface EfficacyConfigurationService<T extends Configuration> {

    EfficacyConfiguration getEfficacyConfiguration(T configuration);

}
