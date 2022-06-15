package com.webapp.api.services;

import com.webapp.api.services.films.PeopleService;
import com.webapp.app.pojo.characters.People;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class PeopleApi {
    private PeopleService peopleService = ServiceManager.getCharactersService();

    public People getCharacter(String id) {
        return peopleService
                .get(id)
                .extract()
                .as(People.class);
    }
}