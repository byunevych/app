package com.webapp.api.services;

import com.webapp.api.services.films.StarshipsService;
import com.webapp.app.pojo.starship.Starship;
import com.webapp.app.pojo.starship.StarshipsList;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class StarshipsApi {
    private StarshipsService starshipsService = ServiceManager.getStarshipsService();

    public Starship getStarship(String id) {
        return starshipsService
                .get(id)
                .extract()
                .as(Starship.class);
    }

    public StarshipsList getStarships() {
        return starshipsService
                .get()
                .extract()
                .as(StarshipsList.class);
    }
}