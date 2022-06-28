package com.demidovn.fruitbounty.game.model.tablequests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TableQuestResult {

    private final List<String> statements;
    private final String rightAnswerObject;
    private final String rightAnswerSubject;

}
