package com.demidovn.fruitbounty.game.model.miniquests.validator;

import com.demidovn.fruitbounty.game.model.quest.NonTargetAnswers;
import com.demidovn.fruitbounty.game.model.quest.OutOfStatements;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Data
public class ValidParams {
    public final int rightAnswersNum;
    public final NonTargetAnswers nonTargetAnswers;
    public final OutOfStatements answerOutOfStatements;
    public int minAnswerInfers = 0;
    public int maxAnswerInfers = Integer.MAX_VALUE;
    public final Map<Integer, Integer> globalRightAnswers;

    public ValidParams(int rightAnswersNum,
                       NonTargetAnswers nonTargetAnswers, OutOfStatements answerOutOfStatements,
                       Map<Integer, Integer> globalRightAnswers) {
        this.rightAnswersNum = rightAnswersNum;
        this.nonTargetAnswers = nonTargetAnswers;
        this.answerOutOfStatements = answerOutOfStatements;
        this.globalRightAnswers = globalRightAnswers;
    }

}
