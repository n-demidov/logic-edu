package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators;

import com.demidovn.fruitbounty.game.model.miniquests.MiniquestCondition;

import java.util.List;

public class CorrectSentenceValidator {

    public boolean validNewCondition(MiniquestCondition checkingMiniquestCondition, List<MiniquestCondition> sentence) {
        for (int iStatement = 0; iStatement < sentence.size(); iStatement++) {
            MiniquestCondition generatedMiniquestCondition = sentence.get(iStatement);
            // Full repeats and self-contradiction
            if (checkingMiniquestCondition.subjectIdx == generatedMiniquestCondition.subjectIdx &&
                    checkingMiniquestCondition.objectIdx == generatedMiniquestCondition.objectIdx) {
                return false;
            }

            // "Swede drank whiskey, Swede drank rum." (self-contradiction)
            if (checkingMiniquestCondition.subjectIdx == generatedMiniquestCondition.subjectIdx &&
                    !checkingMiniquestCondition.inverse && !generatedMiniquestCondition.inverse) {
                return false;
            }

            // "Swede drink whiskey. Chinese drink whiskey." (self-contradiction)
            if (checkingMiniquestCondition.objectIdx == generatedMiniquestCondition.objectIdx &&
                    !checkingMiniquestCondition.inverse && !generatedMiniquestCondition.inverse) {
                return false;
            }
        }

        return true;
    }

}
