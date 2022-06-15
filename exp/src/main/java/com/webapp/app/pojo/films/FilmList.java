package com.webapp.app.pojo.films;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FilmList {
    private long count;
    private String next;
    private String previous;
    @JsonProperty("results")
    private List<Film> films;

}