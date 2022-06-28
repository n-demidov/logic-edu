package com.demidovn.fruitbounty.game.model.miniquests;

import com.demidovn.fruitbounty.game.model.miniquests.validator.ValidResult;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class MiniquestEngineResult {

    public final List<List<MiniquestCondition>> statements;
    public final Map<Integer, ValidResult> allAnswers;  // For logging
    public final Map<Integer, ValidResult> validAnswers;

}
