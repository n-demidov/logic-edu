package com.demidovn.fruitbounty.game.model.miniquests;

import com.demidovn.fruitbounty.game.model.quest.NonTargetAnswers;
import com.demidovn.fruitbounty.game.model.quest.OutOfStatements;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
public class MiniquestInfo {
    public final boolean table;

    // Standard
    public final String verbPositive;
    public final String verbNegative;
    public final List<String> subjects;
    public final List<String> objects;
    public final Set<Integer> subjectsRangeIdxs;
    public final Set<Integer> objectsRangeIdxs;

    public final int repeatsLimit;
    public final boolean contradictionsEnabled;
    public final OutOfStatements answerOutOfStatements;

    public final int rightAnswersNum;

    public final int sentencesNum;
    public final int statementsInSentence;

    public final List<List<Boolean>> verityAllocation;
    public final NonTargetAnswers nonTargetAnswers;

    public final int minAnswerInfers;
    public final int maxAnswerInfers;
    public final Map<Integer, Integer> globalRightAnswers;

}
