package com.demidovn.fruitbounty.game.model.miniquests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MiniquestGeneratorResult {

    // For logging
    public int miniquestNumber;
    public final Map<String, List<String>> allAnswers;
    public final Map<String, List<String>> validAnswers;

    // Base fields
    public final List<String> statements;
    public final MiniquestEngineResult miniquestEngineResult;

    public MiniquestGeneratorResult(Map<String, List<String>> allAnswers, Map<String, List<String>> validAnswers, List<String> statements, MiniquestEngineResult miniquestEngineResult) {
        this.allAnswers = allAnswers;
        this.validAnswers = validAnswers;
        this.statements = statements;
        this.miniquestEngineResult = miniquestEngineResult;
    }
}
