package com.demidovn.fruitbounty.game.model.miniquests;

import com.demidovn.fruitbounty.game.model.miniquests.validator.ValidResult;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.Map;

@Data
@AllArgsConstructor
public class ValidMiniquestResult {
    public static final ValidMiniquestResult INVALID_QUEST_RESULT = new ValidMiniquestResult(Collections.emptyMap(), Collections.emptyMap());

    public final Map<Integer, ValidResult> allAnswers;
    public final Map<Integer, ValidResult> validAnswers;

}
