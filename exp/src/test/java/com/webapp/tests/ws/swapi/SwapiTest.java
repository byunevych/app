package com.webapp.tests.ws.swapi;


import com.webapp.api.services.FilmsApi;
import com.webapp.api.services.PeopleApi;
import com.webapp.api.services.StarshipsApi;
import com.webapp.app.pojo.characters.People;
import com.webapp.app.pojo.films.Film;
import com.webapp.app.pojo.starship.Starship;
import com.webapp.app.pojo.starship.StarshipsList;
import com.webapp.common.utils.StrUtil;
import com.webapp.tests.common.WSTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Epic("Regression WS Tests")
@Feature("swapi")
@Link(name = "Jira", url = "Swapi operations")
public class SwapiTest extends WSTest {
    private static final String FILM_TITLE = "A New Hope";
    private static final String PERSON_NAME = "Biggs Darklighter";
    private static final String STARSHIP_CLASS = "Starfighter";
    private static final String PILOT_NAME = "Luke Skywalker";
    private FilmsApi filmsApi;
    private PeopleApi peopleApi;
    private StarshipsApi starshipsApi;

    @BeforeAll
    void beforeAll() {
        filmsApi = app.getFilmsApi();
        peopleApi = app.getPeopleApi();
        starshipsApi = app.getStarshipsApi();
    }

    @Test
    void checkSwapiApiTest() {
        //	Find film with a title ”A New Hope”
        Film film = filmsApi.getFilmList().getFilms().stream().filter(t -> t.getTitle().equalsIgnoreCase(FILM_TITLE))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("Film '%s' was not found", FILM_TITLE)));
        List<String> charactersIndexes = film.getCharacters().stream().map(StrUtil::getDigits).toList();

        //Using previous response (1) find person with name “Biggs Darklighter” among the characters that were part of that film.
        List<People> peopleList = new ArrayList<>();
        for (String p : charactersIndexes) {
            peopleList.add(peopleApi.getCharacter(p));
        }
        People person = peopleList.stream().filter(t -> t.getName().equalsIgnoreCase(PERSON_NAME))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("Person '%s' was not found", PERSON_NAME)));

        //Using previous response (2) find which starship he/she was flying on.
        List<String> starshipsIndexes = person.getStarships().stream().map(StrUtil::getDigits).toList();
        List<Starship> ships = new ArrayList<>();
        for (String s : starshipsIndexes) {
            ships.add(starshipsApi.getStarship(s));
        }

        //“Luke Skywalker” is among pilots that were also flying this kind of starship
        StarshipsList starshipsList = starshipsApi.getStarships();
        Starship starfighterShip = starshipsList.getStarships().stream()
                .filter(starship -> starship.getStarship_class().equalsIgnoreCase(STARSHIP_CLASS))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("StarshipsList does not have '%s' starship class", STARSHIP_CLASS)));

        List<String> pilotsIndexes = starfighterShip.getPilots().stream().map(StrUtil::getDigits).toList();
        List<People> pilotsObjctList = new ArrayList<>();
        for (String p : pilotsIndexes) {
            pilotsObjctList.add(peopleApi.getCharacter(p));
        }

        SoftAssertions.assertSoftly(s -> {
            //Starship class is “Starfighter” check for Biggs Darklighter pilot check
            s.assertThat(ships.stream().map(Starship::getStarship_class).collect(Collectors.toList()))
                    .as("Incorrect starship class")
                    .containsExactlyInAnyOrder(STARSHIP_CLASS);
            //“Luke Skywalker” check
            s.assertThat(pilotsObjctList.stream().map(People::getName).collect(Collectors.toList()))
                    .as(" Pilot %s should fly on Starfighter starship class", PILOT_NAME)
                    .contains(PILOT_NAME);
        });
    }
}