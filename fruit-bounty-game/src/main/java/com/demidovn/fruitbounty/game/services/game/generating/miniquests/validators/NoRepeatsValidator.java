package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators;

import com.demidovn.fruitbounty.game.model.miniquests.MiniquestCondition;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NoRepeatsValidator {
    private static final String SEPARATOR_SYMBOL = "@";

    public boolean valid(List<List<MiniquestCondition>> statements, int repeatsLimit) {
        Map<String, Integer> inverseBySubjectIdx = new HashMap<>();

        for (int i = 0; i < statements.size(); i++) {
            List<MiniquestCondition> sentence = statements.get(i);
            for (int j = 0; j < sentence.size(); j++) {
                MiniquestCondition statement = sentence.get(j);
                String key = statement.subjectIdx + SEPARATOR_SYMBOL + statement.objectIdx + statement.inverse;
                inverseBySubjectIdx.compute(key,
                        (k, v) -> {
                            if (v == null) {
                                v = 0;
                            }
                            v++;
                            return v;
                        });
            }
        }

        int sum = 0;
        Iterator<Integer> it = inverseBySubjectIdx.values().iterator();
        while (it.hasNext()) {
            Integer value = it.next();
            if (value > 1) {
                sum += value;
            }
        }

        return sum <= repeatsLimit;
    }

}
