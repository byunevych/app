package com.webapp.app;

import com.webapp.api.services.FilmsApi;
import com.webapp.api.services.PeopleApi;
import com.webapp.api.services.StarshipsApi;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationManager {
    private static ApplicationManager instance;
    private FilmsApi filmsApi;
    private PeopleApi peopleApi;
    private StarshipsApi starshipsApi;

    public synchronized static ApplicationManager getInstance() {
        return Optional.ofNullable(instance).orElseGet(() -> instance = new ApplicationManager());
    }

    public FilmsApi getFilmsApi() {
        return Optional.ofNullable(filmsApi).orElseGet(() -> filmsApi = new FilmsApi());
    }

    public PeopleApi getPeopleApi() {
        return Optional.ofNullable(peopleApi).orElseGet(() -> peopleApi = new PeopleApi());
    }

    public StarshipsApi getStarshipsApi() {
        return Optional.ofNullable(starshipsApi).orElseGet(() -> starshipsApi = new StarshipsApi());
    }
}