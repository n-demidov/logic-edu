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
public class MiniquestGeneratorInfo {
    public final boolean table;

    public final Set<Integer> allVariantIdxs;
    public final int repeatsLimit;
    public final boolean contradictionsEnabled;
    public final OutOfStatements answerOutOfStatements;
    public final int rightAnswersNum;

    // Standard (nStatementsInSentence)
    public final Set<Integer> subjectsRangeIdxs;

    public final int sentencesNum;
    public final int statementsInSentence;

    public List<List<Boolean>> verityAllocation;
    public final NonTargetAnswers nonTargetAnswers;

    public final int minAnswerInfers;
    public final int maxAnswerInfers;

    public final Map<Integer, Integer> globalRightAnswers;

}
