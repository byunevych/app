package com.webapp.api.services;

import com.webapp.api.services.films.FilmsService;
import com.webapp.app.pojo.films.FilmList;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class FilmsApi {
    private FilmsService filmsService = ServiceManager.getFilmsService();

    public FilmList getFilmList() {
        return filmsService
                .get()
                .extract()
                .as(FilmList.class);
    }
}