package com.github.koushikr;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class EfficacyBundle <T extends Configuration> implements ConfiguredBundle<T>, EfficacyConfigurationService<T> {

    @Override
    public void run(T configuration, Environment environment) throws Exception {

    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public EfficacyConfiguration getEfficacyConfiguration(T configuration) {
        return null;
    }
}