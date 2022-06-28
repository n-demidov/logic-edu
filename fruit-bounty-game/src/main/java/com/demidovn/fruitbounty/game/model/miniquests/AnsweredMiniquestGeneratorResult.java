package com.demidovn.fruitbounty.game.model.miniquests;

import java.util.List;
import java.util.Map;

public class AnsweredMiniquestGeneratorResult extends MiniquestGeneratorResult {
    public final String answerSubject;
    public final String answerObject;

    public AnsweredMiniquestGeneratorResult(Map<String, List<String>> allAnswers, Map<String, List<String>> validAnswers,
                                            String answerSubject, String answerObject,
                                            List<String> statements, MiniquestEngineResult miniquestEngineResult) {
        super(allAnswers, validAnswers, statements, miniquestEngineResult);
        this.answerSubject = answerSubject;
        this.answerObject = answerObject;
    }

    @Override
    public String toString() {
        return "AnsweredMiniquestGeneratorResult{" +
                "answerSubject='" + answerSubject + '\'' +
                ", answerObject='" + answerObject + '\'' +
                "} " + super.toString();
    }
}
