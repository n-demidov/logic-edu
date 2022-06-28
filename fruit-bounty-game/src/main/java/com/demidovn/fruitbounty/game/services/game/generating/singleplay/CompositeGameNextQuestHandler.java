package com.demidovn.fruitbounty.game.services.game.generating.singleplay;

import com.demidovn.fruitbounty.game.GameOptions;
import com.demidovn.fruitbounty.game.exceptions.BaseGenerateQuestTriesLimitException;
import com.demidovn.fruitbounty.game.model.miniquests.MiniquestGeneratorResult;
import com.demidovn.fruitbounty.game.model.miniquests.MiniquestInfo;
import com.demidovn.fruitbounty.game.model.quest.OutOfStatements;
import com.demidovn.fruitbounty.game.model.singleplay.CompositeGame;
import com.demidovn.fruitbounty.game.model.singleplay.GameCompositeInfo;
import com.demidovn.fruitbounty.game.model.singleplay.ObjectsInfo;
import com.demidovn.fruitbounty.game.services.Randomizer;
import com.demidovn.fruitbounty.game.services.game.format.StringFormatter;
import com.demidovn.fruitbounty.game.services.game.generating.CommonSources;
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.MiniquestGenerator;
import com.demidovn.fruitbounty.gameapi.model.Game;
import com.demidovn.fruitbounty.gameapi.model.backend.QuestType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CompositeGameNextQuestHandler {
    private static final int SUBJECTS_IDX = 0;

    @Autowired
    private MiniquestGenerator miniQuestGenerator;

    @Autowired
    private StringFormatter stringFormatter;

    @Autowired
    private CommonSources commonSources;

    private static final Randomizer rand = new Randomizer();

    public void process(Game gameRaw) {
        CompositeGame game = (CompositeGame) gameRaw;
        QuestType questType = game.getQuestType();
        GameCompositeInfo gameInfo = game.getGameCompositeInfo();

        int sentencesNum = getSentencesNum(questType);
        int statementsInSentence = getStatementsInSentence(questType);
        List<List<Boolean>> verityAllocation = createVerityAllocation(sentencesNum, statementsInSentence, questType);

        int firstRandomIdx = getFirstRandomIdx(gameInfo.getAnswersIdxs());
        int secondRandomIdx = getSecondRandomIdx(firstRandomIdx, gameInfo.getAnswersIdxs());
        Map<Integer, Integer> globalRightAnswers = getGlobalRightAnswers(firstRandomIdx, secondRandomIdx, gameInfo.getAnswersIdxs());

        List<String> subjects = createSubjects(firstRandomIdx, gameInfo.getAllInfos(), gameInfo.getObjectToSubjectPrefix());
        ObjectsInfo objectsInfo = gameInfo.getAllInfos().get(secondRandomIdx);
        List<String> objects = objectsInfo.getObjects();

        String verbPositive = objectsInfo.getVerbPositive();
        String verbNegative = objectsInfo.getVerbNegative();

        final MiniquestGeneratorResult miniquestGeneratorResult;
        MiniquestInfo miniquestInfo = new MiniquestInfo(
                false,
                verbPositive, verbNegative, subjects, objects,
                new HashSet<>(gameInfo.getAnswersIdxs().get(firstRandomIdx)), new HashSet<>(gameInfo.getAnswersIdxs().get(secondRandomIdx)),
                GameOptions.SubquestGeneral.repeatsLimit, true, OutOfStatements.SKIP_VALIDATION,
                GameOptions.SubquestGeneral.rightAnswersNum, sentencesNum, statementsInSentence,
                verityAllocation, GameOptions.SubquestGeneral.nonTargetAnswers,
                GameOptions.SubquestGeneral.minAnswerInfers, GameOptions.SubquestGeneral.maxAnswerInfers, globalRightAnswers
        );
        try {
            miniquestGeneratorResult = miniQuestGenerator.generateStatements(miniquestInfo);
        } catch (BaseGenerateQuestTriesLimitException e) {
            log.error("Error BaseGenerateQuestTriesLimitException on creating subquest, game={}, gameInfo={}, miniquestInfo={}", game, gameInfo, miniquestInfo, e);
            return;
        }

        String page = preparePage(miniquestGeneratorResult, verityAllocation, game.getGeneratedMiniquests().size() + 1, questType);

        game.getPages().add(page);
        game.getGeneratedMiniquests().add(miniquestGeneratorResult);
        miniquestGeneratorResult.miniquestNumber = game.getGeneratedMiniquests().size();

        log.debug("Game's miniquest created: {}", game);
    }

    private int getSentencesNum(QuestType questType) {
        if (questType == QuestType.COMPOSITE_1) {
            return rand.generateFromRange(GameOptions.Subquest1.minSentencesNum, GameOptions.Subquest1.maxSentencesNum);
        } else if (questType == QuestType.COMPOSITE_2) {
            return rand.generateFromRange(GameOptions.Subquest2.minSentencesNum, GameOptions.Subquest2.maxSentencesNum);
        } else {
            throw new UnsupportedOperationException("Unknown questType: " + questType);
        }
    }

    private int getStatementsInSentence(QuestType questType) {
        if (questType == QuestType.COMPOSITE_1) {
            return rand.generateFromRange(GameOptions.Subquest1.minStatementsInSentence, GameOptions.Subquest1.maxStatementsInSentence);
        } else if (questType == QuestType.COMPOSITE_2) {
            return rand.generateFromRange(GameOptions.Subquest2.minStatementsInSentence, GameOptions.Subquest2.maxStatementsInSentence);
        } else {
            throw new UnsupportedOperationException("Unknown questType: " + questType);
        }
    }

    private int getFirstRandomIdx(List<List<Integer>> answersIdxs) {
        if (answersIdxs.size() == 2) {
            return 0;
        } else if (answersIdxs.size() < 2) {
            throw new IllegalArgumentException("answersIdxs.size() < 2");
        } else {
            return rand.generateFromRange(0, answersIdxs.size() - 1);
        }
    }

    private int getSecondRandomIdx(int firstRandomIdx, List<List<Integer>> answersIdxs) {
        // valid
        if (answersIdxs.size() == 1) {
            log.error("answersIdxs contains only 1 element, answersIdxs={}", answersIdxs);
            throw new IllegalArgumentException("answersIdxs contains only 1 element");
        }

        int MIN_OBJECTS_IDX = 1;
        int idx;
        do {
            idx = rand.generateFromRange(MIN_OBJECTS_IDX, answersIdxs.size() - 1);
        } while (idx == firstRandomIdx);
        return idx;
    }

    private Map<Integer, Integer> getGlobalRightAnswers(int firstRandomIdx, int secondRandomIdx, List<List<Integer>> answersIdxs) {
        int size = answersIdxs.get(firstRandomIdx).size();
        Map<Integer, Integer> result = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            result.put(
                    answersIdxs.get(firstRandomIdx).get(i),
                    answersIdxs.get(secondRandomIdx).get(i));
        }
        return result;
    }

    private List<String> createSubjects(int firstRandomIdx, List<ObjectsInfo> allInfos, String objectToSubjectPrefix) {
        ObjectsInfo info = allInfos.get(firstRandomIdx);
        List<String> sourceObjects = info.getObjects();
        if (firstRandomIdx == SUBJECTS_IDX) {
            return sourceObjects;
        }

        int size = sourceObjects.size();
        List<String> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(objectToSubjectPrefix + " " + info.getVerbPositive() + " " + sourceObjects.get(i));
        }

        return result;
    }

    private String preparePage(MiniquestGeneratorResult miniquestGeneratorResult,
                               List<List<Boolean>> verityAllocation, int miniquestNumber, QuestType questType) {
        StringBuilder sb = new StringBuilder();  // todo: out to source file
        sb.append("Опрос #")
                .append(miniquestNumber);

        if (questType == QuestType.COMPOSITE_1) {
            sb.append("<br>Среди суждений ниже только одно истинное:<br>");
        } else {
            sb.append("<br>Среди суждений ниже:<br>")
                    .append(verityAllocation.toString())
                    .append("<br><br>");
        }

        sb.append(stringFormatter.replaceStatements(
                miniquestGeneratorResult.statements, commonSources.generateEngSurnames(miniquestGeneratorResult.statements.size())));
        return sb.toString();
    }

    private String getStringVerityAllocation(List<List<Boolean>> verityAllocation) {
        int trueNum = 0;
        int falseNum = 0;
        for (int i = 0; i < verityAllocation.size(); i++) {
            for (int j = 0; j < verityAllocation.get(i).size(); j++) {
                if (verityAllocation.get(i).get(j)) {
                    trueNum++;
                } else {
                    falseNum++;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(trueNum);
        sb.append(" истинных и ");
        sb.append(falseNum);
        sb.append(" ложных:");
        return sb.toString();
    }

    private List<List<Boolean>> createVerityAllocation(int minSentencesNum, int statementsInSentence, QuestType questType) {
        List<List<Boolean>> result = new ArrayList<>(minSentencesNum);
        for (int row = 0; row < minSentencesNum; row++) {
            List<Boolean> tempList = new ArrayList<>(statementsInSentence);
            for (int w = 0; w < statementsInSentence; w++) {
                if (questType == QuestType.COMPOSITE_1) {
                    tempList.add(row == 0 && w == 0);
                } else {
                    tempList.add(rand.isPercentFired(50));
                }
            }
            result.add(tempList);
        }
        return result;
    }

}
