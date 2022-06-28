package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators;

import com.demidovn.fruitbounty.game.model.miniquests.MiniquestCondition;

import java.util.Map;

public class GlobalAnswersValidator {

    public boolean validNewCondition(MiniquestCondition checkingStatement, boolean truth, Map<Integer, Integer> globalRightAnswers) {
        int subjectIdx = checkingStatement.subjectIdx;

        Integer globalAnswerObject = globalRightAnswers.get(subjectIdx);
        if (globalAnswerObject == null) {
            return true;
        }

        /*
          - if verity=T and inversed=F -- values should be equals;
                            inversed=T -- values should be different;
          - if verity=F and inversed=F -- values should be different;
                            inversed=T -- values should be equals;
          * values - are statement.object and globalAnswer.object
         */

        if (truth) {
            if (!checkingStatement.inverse) {
                return checkingStatement.objectIdx == globalAnswerObject;
            } else {
                return checkingStatement.objectIdx != globalAnswerObject;
            }
        } else {
            if (!checkingStatement.inverse) {
                return checkingStatement.objectIdx != globalAnswerObject;
            } else {
                return checkingStatement.objectIdx == globalAnswerObject;
            }
        }
    }

}
