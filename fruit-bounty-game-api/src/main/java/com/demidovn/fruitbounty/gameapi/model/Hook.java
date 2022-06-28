package com.demidovn.fruitbounty.gameapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Hook {

    private final String name;

    @JsonIgnore
    private String miniquestIntro;

    private final int price;

    public Hook(String name, String miniquestIntro) {
        this(name, miniquestIntro, 0);
    }

}
