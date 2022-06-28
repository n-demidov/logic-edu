package com.demidovn.fruitbounty.game.services.game.generating.miniquests;

import com.demidovn.fruitbounty.game.exceptions.NotImplementedException;
import com.demidovn.fruitbounty.game.model.miniquests.MiniquestGeneratorInfo;
import com.demidovn.fruitbounty.game.model.quest.NonTargetAnswers;
import com.demidovn.fruitbounty.game.model.quest.OutOfStatements;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class MiniquestParamsValidator {

    public void validateArgs(Set<Integer> subjectsRangeIdxs, Set<Integer> objectsRangeIdxs,
                             int rightAnswersNum, int minAnswerInfers, int maxAnswerInfers,
                             OutOfStatements answerOutOfStatements, List<List<Boolean>> verityAllocation,
                             int sentencesNum, NonTargetAnswers nonTargetAnswers, Map<Integer, Integer> globalRightAnswers, MiniquestGeneratorInfo info) {
        if (!(answerOutOfStatements == OutOfStatements.SKIP_VALIDATION
                || answerOutOfStatements == OutOfStatements.STRICTLY_IN_STATEMENTS
                || answerOutOfStatements == OutOfStatements.STRICTLY_OUT_OF_STATEMENTS)) {
            log.error("Unknown answerOutOfStatements={}, info={}", answerOutOfStatements, info);
            throw new IllegalArgumentException("Unknown answerOutOfStatements=" + answerOutOfStatements);
        }

        if (!(nonTargetAnswers == NonTargetAnswers.SKIP_VALIDATION
                || nonTargetAnswers == NonTargetAnswers.ONLY_ONE_ANSWER_FOR_MENTIONED_IN_STATEMENTS
                || nonTargetAnswers == NonTargetAnswers.ONLY_ONE_ANSWER_FOR_ALL)) {
            log.error("Unknown nonTargetAnswers={}, info={}", nonTargetAnswers, info);
            throw new IllegalArgumentException("Unknown nonTargetAnswers=" + nonTargetAnswers);
        }

        if (subjectsRangeIdxs.size() > objectsRangeIdxs.size()) {
            log.error("subjectsRangeIdxs.size() > objectsRangeIdxs.size(), subjectsRangeIdxs={}, objectsRangeIdxs={}, info={}", subjectsRangeIdxs, objectsRangeIdxs, info);
            throw new NotImplementedException();
        }

        if (rightAnswersNum < 0) {
            log.error("rightAnswersNum < 0, info={}", info);
            throw new IllegalArgumentException("rightAnswersNum < 0");
        }

        if (rightAnswersNum > objectsRangeIdxs.size()) {
            log.error("rightAnswersNum > objectsRangeIdxs.size(), rightAnswersNum={}, objectsRangeIdxs={}, info={}", rightAnswersNum, objectsRangeIdxs, info);
            throw new IllegalArgumentException("rightAnswersNum > objectsRangeIdxs.size()");
        }

        if (minAnswerInfers < 0 || minAnswerInfers > maxAnswerInfers) {
            log.error("Illegal minAnswerInfers or maxAnswerInfers, minAnswerInfers={}, maxAnswerInfers={}, info={}", minAnswerInfers, maxAnswerInfers, info);
            throw new IllegalArgumentException("Illegal minAnswerInfers or maxAnswerInfers");
        }

        if (verityAllocation.size() != sentencesNum) {
            log.error("verityAllocation.size() != sentencesNum, info={}", info);
            throw new IllegalArgumentException("verityAllocation.size() != sentencesNum");
        }

        if (!subjectsRangeIdxs.containsAll(globalRightAnswers.keySet())) {
            log.error("globalRightAnswers are out of subjectsRange, globalRightAnswers={}, subjectsRangeIdxs={}", globalRightAnswers, subjectsRangeIdxs);
            throw new IllegalArgumentException("globalRightAnswers are out of subjectsRange");
        }

        if (!objectsRangeIdxs.containsAll(globalRightAnswers.values())) {
            log.error("globalRightAnswers are out of objectsRangeIdxs, globalRightAnswers={}, objectsRangeIdxs={}", globalRightAnswers, objectsRangeIdxs);
            throw new IllegalArgumentException("globalRightAnswers are out of objectsRangeIdxs");
        }
    }

}
