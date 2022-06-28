package com.demidovn.fruitbounty.game.services.game.generating.miniquests;

import com.demidovn.fruitbounty.game.GameOptions;
import com.demidovn.fruitbounty.game.exceptions.GenerateQuestSentenceTriesLimitException;
import com.demidovn.fruitbounty.game.exceptions.GenerateQuestTriesLimitException;
import com.demidovn.fruitbounty.game.model.miniquests.MiniquestEngineResult;
import com.demidovn.fruitbounty.game.model.miniquests.MiniquestCondition;
import com.demidovn.fruitbounty.game.model.miniquests.MiniquestGeneratorInfo;
import com.demidovn.fruitbounty.game.model.miniquests.ValidMiniquestResult;
import com.demidovn.fruitbounty.game.model.miniquests.validator.ValidParams;
import com.demidovn.fruitbounty.game.model.quest.OutOfStatements;
import com.demidovn.fruitbounty.game.services.Randomizer;
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.GlobalAnswersValidator;
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.NoContradictionsValidator;
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.NoRepeatsValidator;
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.NumAnswersValidator;
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.CorrectSentenceValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
public class MiniquestEngineGenerator {

    private static final Randomizer rand = new Randomizer();
    private static final NumAnswersValidator numAnswersValidator = new NumAnswersValidator();
    private static final NoRepeatsValidator noRepeatsValidator = new NoRepeatsValidator();
    private static final NoContradictionsValidator noContradictionsValidator = new NoContradictionsValidator();
    private static final MiniquestParamsValidator miniquestParamsValidator = new MiniquestParamsValidator();
    private static final CorrectSentenceValidator correctSentenceValidator = new CorrectSentenceValidator();
    private static final GlobalAnswersValidator globalAnswersValidator = new GlobalAnswersValidator();
    private static final SentencePermuter sentencePermuter = new SentencePermuter();

    public MiniquestEngineResult generateStatements(MiniquestGeneratorInfo info)
            throws GenerateQuestTriesLimitException, GenerateQuestSentenceTriesLimitException {
        validateArgs(info);

        rand.shuffleIncludeSublists(info.verityAllocation);

        List<List<MiniquestCondition>> statements;
        ValidMiniquestResult validResult;
        int triesCount = 1;
        do {
            statements = generateRandomStatements(info);
            validResult = isValid(statements, info);
            triesCount++;
            checkGenerateQuestTriesLimit(triesCount, info);
        } while (validResult.validAnswers.isEmpty());

        return new MiniquestEngineResult(statements, validResult.allAnswers, validResult.validAnswers);
    }

    private List<List<MiniquestCondition>> generateRandomStatements(MiniquestGeneratorInfo info) throws GenerateQuestSentenceTriesLimitException {
        List<List<MiniquestCondition>> result = new ArrayList<>(info.sentencesNum);

        int sentencesNum = info.sentencesNum;
        int statementsInSentenceNum = info.statementsInSentence;
        if (info.table) {
            statementsInSentenceNum = 1;
            int maxStatements = info.subjectsRangeIdxs.size() * info.allVariantIdxs.size() / 2 + 1;
            sentencesNum = rand.generateFromRange(1, maxStatements);

            info.verityAllocation = generateVerityAllocation(sentencesNum);
        }

        int statementId = 0;
        for (int iSentence = 0; iSentence < sentencesNum; iSentence++) {
            List<MiniquestCondition> statementsInSentence = new ArrayList<>(info.statementsInSentence);

            for (int iStatement = 0; iStatement < statementsInSentenceNum; iStatement++) {
                MiniquestCondition miniQuestCondition;
                boolean truth = info.verityAllocation.get(iSentence).get(iStatement);
                int triesCount = 1;

                do {
                    miniQuestCondition = generateRandomStatement(info, statementId);
                    triesCount++;
                    checkGenerateQuestSentenceTriesLimit(triesCount, info);
                } while (!correctSentenceValidator.validNewCondition(miniQuestCondition, statementsInSentence) ||
                         !globalAnswersValidator.validNewCondition(miniQuestCondition, truth, info.globalRightAnswers));

                statementsInSentence.add(miniQuestCondition);
                statementId++;
            }

            sentencePermuter.permuteIfNeed(statementsInSentence);

            result.add(statementsInSentence);
        }

        return result;
    }

    private List<List<Boolean>> generateVerityAllocation(int statementsInSentenceNum) {
        List<List<Boolean>> truePhrases = new ArrayList<>(statementsInSentenceNum);
        for (int i = 0; i < statementsInSentenceNum; i++) {
            List<Boolean> tempList = new ArrayList<>();
            tempList.add(true);
            truePhrases.add(tempList);
        }
        return truePhrases;
    }

    private MiniquestCondition generateRandomStatement(MiniquestGeneratorInfo info, int statementId) {
        int randomSubjectIdx = rand.getRandomElement(info.subjectsRangeIdxs);
        int randomObjectIdx = rand.getRandomElement(info.allVariantIdxs);

        return new MiniquestCondition(
                statementId,
                randomSubjectIdx,
                randomObjectIdx,
                rand.generateFromRange(0, 1) == 0);
    }

    private ValidMiniquestResult isValid(List<List<MiniquestCondition>> statements,
                                         MiniquestGeneratorInfo info) {
        if (info.repeatsLimit != GameOptions.INFINITY_REPEATS_LIMIT &&
                !noRepeatsValidator.valid(statements, info.repeatsLimit)) {
            return ValidMiniquestResult.INVALID_QUEST_RESULT;
        }

        if (!noContradictionsValidator.valid(statements, info.contradictionsEnabled)) {
            return ValidMiniquestResult.INVALID_QUEST_RESULT;
        }

        return numAnswersValidator.validate(
                statements,
                info.verityAllocation,
                new HashSet<>(info.subjectsRangeIdxs),
                new HashSet<>(info.allVariantIdxs),
                new ValidParams(info.rightAnswersNum, info.nonTargetAnswers, info.answerOutOfStatements,
                        info.minAnswerInfers, info.maxAnswerInfers, info.globalRightAnswers));
    }

    private void validateArgs(MiniquestGeneratorInfo info) {
        miniquestParamsValidator.validateArgs(info.subjectsRangeIdxs, info.allVariantIdxs,
                info.rightAnswersNum, info.minAnswerInfers, info.maxAnswerInfers,
                info.answerOutOfStatements, info.verityAllocation, info.sentencesNum,
                info.nonTargetAnswers, info.globalRightAnswers, info);

        if (!info.table) {
            for (int i = 0; i < info.sentencesNum; i++) {
                if (info.verityAllocation.get(i).size() != info.statementsInSentence) {
                    log.error("info.verityAllocation.get(i).size() != info.statementsInSentence");
                    throw new IllegalArgumentException("info.verityAllocation.get(i).size() != info.statementsInSentence");
                }
            }

            int allStatementsSum = info.sentencesNum * info.statementsInSentence;
            if (info.answerOutOfStatements == OutOfStatements.STRICTLY_OUT_OF_STATEMENTS
                    && allStatementsSum < info.allVariantIdxs.size() - info.rightAnswersNum) {
                log.error("Too low statementsNum for STRICTLY_OUT_OF_STATEMENTS, info={}", info);
                throw new IllegalArgumentException("Too low statementsNum for STRICTLY_OUT_OF_STATEMENTS");
            }
        }
    }

    private void checkGenerateQuestTriesLimit(int triesCount, MiniquestGeneratorInfo info) throws GenerateQuestTriesLimitException {
        if (triesCount > GameOptions.GENERATE_QUESTS_TRIES_LIMIT) {
            log.error("Error GenerateQuestTriesLimitException, info={}", info);
            throw new GenerateQuestTriesLimitException();
        }
    }

    private void checkGenerateQuestSentenceTriesLimit(int triesCount, MiniquestGeneratorInfo info) throws GenerateQuestSentenceTriesLimitException {
        if (triesCount > GameOptions.GENERATE_QUESTS_SENTENCES_TRIES_LIMIT) {
            log.error("Error GenerateQuestSentenceTriesLimitException, info={}", info);
            throw new GenerateQuestSentenceTriesLimitException();
        }
    }

}
