package com.webapp.core.properties;

import org.aeonbits.owner.Config;

@Config.Sources({"file:src/main/resources/properties/allure.properties"})
public interface AllureConfig extends Config {

    @Key("allure.results.directory")
    String resultsDirectory();
}