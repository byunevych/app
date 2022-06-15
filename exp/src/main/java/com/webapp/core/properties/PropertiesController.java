package com.webapp.core.properties;

import org.aeonbits.owner.ConfigFactory;

import java.util.Optional;

public class PropertiesController {
    private static AppTimeoutConfig appTimeoutConfig;
    private static GlobalSystemProperties systemProperties;
    private static AllureConfig allureConfig;

    public static AppTimeoutConfig appTimeoutConfig() {
        return Optional.ofNullable(appTimeoutConfig).orElseGet(() -> appTimeoutConfig = ConfigFactory.create(AppTimeoutConfig.class));
    }

    public static GlobalSystemProperties systemProperties() {
        return Optional.ofNullable(systemProperties).orElseGet(() -> systemProperties
                = ConfigFactory.create(GlobalSystemProperties.class, System.getProperties()));
    }

    public static AllureConfig allureConfig() {
        return Optional.ofNullable(allureConfig).orElseGet(() -> allureConfig = ConfigFactory.create(AllureConfig.class));
    }
}
