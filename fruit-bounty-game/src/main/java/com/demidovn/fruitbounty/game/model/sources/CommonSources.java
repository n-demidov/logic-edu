package com.demidovn.fruitbounty.game.model.sources;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class CommonSources {

    private List<String> agreementWords;
    private String and;
    private String right;

    private List<String> menNames;
    private List<String> engSurnames;

}
