package com.demidovn.fruitbounty.game.model.tablequests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TableQuestInfo2 {

    private final List<String> subjects;
    private final List<String> verbs;
    private final List<List<String>> objectsMany;
    private final String thirdPersonSex;

}
