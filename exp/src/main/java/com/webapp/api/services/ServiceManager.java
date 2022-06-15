package com.webapp.api.services;

import com.webapp.api.services.films.FilmsService;
import com.webapp.api.services.films.PeopleService;
import com.webapp.api.services.films.StarshipsService;

import java.util.Optional;

public class ServiceManager {
    private static FilmsService filmsService;
    private static PeopleService peopleService;
    private static StarshipsService starshipsService;

    public static FilmsService getFilmsService() {
        return Optional.ofNullable(filmsService)
                .orElseGet(() -> filmsService = new FilmsService());
    }

    public static PeopleService getCharactersService() {
        return Optional.ofNullable(peopleService)
                .orElseGet(() -> peopleService = new PeopleService());
    }

    public static StarshipsService getStarshipsService() {
        return Optional.ofNullable(starshipsService)
                .orElseGet(() -> starshipsService = new StarshipsService());
    }
}
