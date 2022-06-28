package com.demidovn.fruitbounty.game.model.tablequests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TableQuestInfo {

    private final List<String> subjects;
    private final String verb;
    private final List<String> objects;

}
