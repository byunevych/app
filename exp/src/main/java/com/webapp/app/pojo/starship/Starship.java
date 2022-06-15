package com.webapp.app.pojo.starship;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class Starship {
    private String name;
    private String starship_class;
    private List<String> pilots;
}