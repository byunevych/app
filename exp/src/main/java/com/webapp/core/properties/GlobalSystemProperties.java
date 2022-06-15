package com.webapp.core.properties;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
public interface GlobalSystemProperties extends Config {

    @DefaultValue("${size}")
    String runSize();
}