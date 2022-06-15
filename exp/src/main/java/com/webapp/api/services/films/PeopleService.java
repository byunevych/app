package com.webapp.api.services.films;

import com.webapp.api.services.AbstractService;

public class PeopleService extends AbstractService {
    @Override
    protected String getEndpoint() {
        return "people";
    }
}