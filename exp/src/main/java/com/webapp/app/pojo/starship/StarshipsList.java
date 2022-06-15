package com.webapp.app.pojo.starship;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.webapp.app.pojo.films.Film;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class StarshipsList {
    private long count;
    private String next;
    private String previous;
    @JsonProperty("results")
    private List<Starship> starships;

}