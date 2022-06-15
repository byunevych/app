package com.webapp.api.services.films;

import com.webapp.api.services.AbstractService;

public class FilmsService extends AbstractService {
    @Override
    protected String getEndpoint() {
        return "films";
    }
}