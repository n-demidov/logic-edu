package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators;

import com.demidovn.fruitbounty.game.model.miniquests.MiniquestCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoContradictionsValidator {
    private static final String SEPARATOR_SYMBOL = "@";

    public boolean valid(List<List<MiniquestCondition>> statements, boolean contradictionsEnabled) {
        if (contradictionsEnabled) {
            return true;
        }

        Map<String, Boolean> valuesBySubjectIdx = new HashMap<>();
        for (int i = 0; i < statements.size(); i++) {
            List<MiniquestCondition> sentence = statements.get(i);
            for (int j = 0; j < sentence.size(); j++) {
                MiniquestCondition statement = sentence.get(j);
                String key = statement.subjectIdx + SEPARATOR_SYMBOL + statement.objectIdx;
                Boolean value = valuesBySubjectIdx.get(key);
                if (value != null && value != statement.inverse) {
                    return false;
                } else {
                    valuesBySubjectIdx.put(key, statement.inverse);
                }
            }
        }

        return true;
    }

}
