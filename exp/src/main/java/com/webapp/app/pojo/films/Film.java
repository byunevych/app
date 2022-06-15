package com.webapp.app.pojo.films;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class Film {
    private String title;
    private String next;
    private List<String> characters;
    private List<String> starships;

}