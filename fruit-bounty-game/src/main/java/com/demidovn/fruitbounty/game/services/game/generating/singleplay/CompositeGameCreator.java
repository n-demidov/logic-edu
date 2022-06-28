package com.demidovn.fruitbounty.game.services.game.generating.singleplay;

import com.demidovn.fruitbounty.game.GameOptions;
import com.demidovn.fruitbounty.game.model.singleplay.CompositeDescriptor;
import com.demidovn.fruitbounty.game.model.singleplay.CompositeGame;
import com.demidovn.fruitbounty.game.model.singleplay.CompositeSources;
import com.demidovn.fruitbounty.game.model.singleplay.ObjectsInfo;
import com.demidovn.fruitbounty.game.services.Randomizer;
import com.demidovn.fruitbounty.game.services.game.converters.NamesConverters;
import com.demidovn.fruitbounty.game.services.game.format.StringFormatter;
import com.demidovn.fruitbounty.game.services.game.generating.CompositeGameConfig;
import com.demidovn.fruitbounty.game.services.list.ListCombinator;
import com.demidovn.fruitbounty.game.services.list.ListCopier;
import com.demidovn.fruitbounty.gameapi.model.Game;
import com.demidovn.fruitbounty.game.model.singleplay.GameCompositeInfo;
import com.demidovn.fruitbounty.gameapi.model.backend.GameDescription;
import com.demidovn.fruitbounty.gameapi.model.backend.QuestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CompositeGameCreator {
    private static final Randomizer rand = new Randomizer();

    @Autowired
    private CompositeGameConfig compositeGameConfig;

    @Autowired
    private StringFormatter stringFormatter;

    @Autowired
    private NamesConverters namesConverters;

    private static final ListCopier listCopier = new ListCopier();
    private static final ListCombinator listCombinator = new ListCombinator();

    public Game createNewGame(GameDescription gameDescription) {
        QuestType questType = gameDescription.getQuestType();
        CompositeSources compositeSources = compositeGameConfig.getCompositeSources();
        CompositeDescriptor descriptor = compositeGameConfig.getCompositeDescriptor();

        List<String> allSubjectsNames = generateRandomSubjects(compositeSources, questType);
        int subjectsNum = allSubjectsNames.size();
        List<ObjectsInfo> allObjectsInfos = generateRandomInfos(subjectsNum, compositeSources, questType);

        List<Integer> allSubjectsIdxs = generateSubjectsIdxs(allSubjectsNames);
        List<List<Integer>> allObjectsIdxs = generateObjectsIdxs(allObjectsInfos);
        List<List<Integer>> answersObjectsIdxs = generateObjectsAnswersIdxs(allObjectsIdxs, subjectsNum);

        //
        int answerIdx = rand.generateFromRange(0, allSubjectsIdxs.size() - 1);
        String answerSubject = allSubjectsNames.get(answerIdx);
        String answerObject = getObjectAnswer(answerIdx, answersObjectsIdxs, allObjectsInfos);
        String objectsData = generateObjectsData(allObjectsInfos);

        String intro = descriptor.getIntro();
        intro = intro.replace("{subjects}", "<b>" + stringFormatter.joinList(allSubjectsNames) + "</b>");
        intro = intro.replace("{answer_object}", "<b>" + answerObject + "</b>");
        intro = intro.replace("{objects_data}", objectsData);

        //
        GameCompositeInfo gameCompositeInfo = new GameCompositeInfo(
                allSubjectsNames, allObjectsInfos, allSubjectsIdxs, answersObjectsIdxs, compositeSources.getObjectToSubjectPrefix());
        CompositeGame game = new CompositeGame(questType, gameDescription.getPlayers(), gameCompositeInfo, GameOptions.TimeGameMs.COMPOSITE);

        game.setAnswerOptions(allSubjectsNames);
        game.setRightAnswer(answerSubject);
        game.getPages().add(intro);

        // For log
        game.setGlobalAnswersIdxs(gameCompositeInfo.getAnswersIdxs());
        game.setGlobalAnswers(convert2Strings(gameCompositeInfo.getAnswersIdxs(), allSubjectsNames, allObjectsInfos));

        game.setShowTables(true);
        game.setTables(createUiTables(allSubjectsNames, allObjectsInfos));

        return game;
    }

    private List<String> generateRandomSubjects(CompositeSources compositeSources, QuestType questType) {
        final int minSubjects;
        final int maxSubjects;
        if (questType == QuestType.COMPOSITE_1) {
            minSubjects = GameOptions.Globalquest_1.MIN_SUBJECTS;
            maxSubjects = GameOptions.Globalquest_1.MAX_SUBJECTS;
        } else if (questType == QuestType.COMPOSITE_2) {
            minSubjects = GameOptions.Globalquest_2.MIN_SUBJECTS;
            maxSubjects = GameOptions.Globalquest_2.MAX_SUBJECTS;
        } else {
            throw new UnsupportedOperationException("Unknown questType: " + questType);
        }

        List<String> allSubjects = new ArrayList<>(compositeSources.getSubjects());
        rand.shuffle(allSubjects);
        int subjectsNum = rand.generateFromRange(minSubjects, maxSubjects);
        return allSubjects.subList(0, subjectsNum);
    }

    private List<ObjectsInfo> generateRandomInfos(int subjectsNum, CompositeSources compositeSources, QuestType questType) {
        final int minObjectsGroups;
        final int maxObjectsGroups;
        final int maxObjects;

        if (questType == QuestType.COMPOSITE_1) {
            minObjectsGroups = GameOptions.Globalquest_1.MIN_OBJECTS_GROUPS;
            maxObjectsGroups = GameOptions.Globalquest_1.MAX_OBJECTS_GROUPS;
            maxObjects = GameOptions.Globalquest_1.MAX_OBJECTS;
        } else if (questType == QuestType.COMPOSITE_2) {
            minObjectsGroups = GameOptions.Globalquest_2.MIN_OBJECTS_GROUPS;
            maxObjectsGroups = GameOptions.Globalquest_2.MAX_OBJECTS_GROUPS;
            maxObjects = GameOptions.Globalquest_2.MAX_OBJECTS;
        } else {
            throw new UnsupportedOperationException("Unknown questType: " + questType);
        }

        int size = compositeSources.getObjectsInfos().size();
        List<ObjectsInfo> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ObjectsInfo descrObjectsInfo = compositeSources.getObjectsInfos().get(i);

            List<String> objectsInSentence = new ArrayList<>(descrObjectsInfo.getObjects());
            rand.shuffle(objectsInSentence);
            int objectsNum = rand.generateFromRange(subjectsNum, maxObjects);
            objectsInSentence = objectsInSentence.subList(0, objectsNum);

            ObjectsInfo objectsInfo = new ObjectsInfo(objectsInSentence, descrObjectsInfo.getIntro(),
                    null, descrObjectsInfo.getVerbPositive(), descrObjectsInfo.getVerbNegative());
            result.add(objectsInfo);
        }
        rand.shuffle(result);
        int descriptorObjectsNum = rand.generateFromRange(minObjectsGroups, maxObjectsGroups);
        result = result.subList(0, descriptorObjectsNum);

        return result;
    }

    private List<Integer> generateSubjectsIdxs(List<String> subjectsNames) {
        return IntStream.range(0, subjectsNames.size())
                .boxed()
                .collect(Collectors.toList());
    }

    private List<List<Integer>> generateObjectsIdxs(List<ObjectsInfo> allObjectsInfos) {
        int size = allObjectsInfos.size();
        List<List<Integer>> result = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            List<Integer> range = IntStream.range(0, allObjectsInfos.get(i).getObjects().size())
                    .boxed()
                    .collect(Collectors.toList());
            result.add(range);
        }
        return result;
    }

    private List<List<Integer>> generateObjectsAnswersIdxs(List<List<Integer>> objectsIdxs, int subjectsNum) {
        List<List<Integer>> result = listCopier.deepCopy(objectsIdxs);
        for (int i = 0; i < result.size(); i++) {
            List<Integer> sublist = result.get(i);
            rand.shuffle(sublist);
            result.set(i, sublist.subList(0, subjectsNum));
        }
        return result;
    }

    private String getObjectAnswer(int answerIdx, List<List<Integer>> answersObjectsIdxs, List<ObjectsInfo> allObjectsInfos) {
        int randomGroupIdx = rand.generateFromRange(0, answersObjectsIdxs.size() - 1);
        return getObjectName(randomGroupIdx, answerIdx, answersObjectsIdxs, allObjectsInfos);
    }

    private String getObjectName(int groupIdx, int throughIdx, List<List<Integer>> answersObjectsIdxs, List<ObjectsInfo> allObjectsInfos) {
        Integer objectAnswerIdx = answersObjectsIdxs.get(groupIdx).get(throughIdx);
        return allObjectsInfos.get(groupIdx).getObjects().get(objectAnswerIdx);
    }

    private String generateObjectsData(List<ObjectsInfo> allObjectsInfos) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < allObjectsInfos.size(); i++) {
            ObjectsInfo objectsInfo = allObjectsInfos.get(i);
            sb.append(objectsInfo.getIntro())
                    .append("<b>")
                    .append(stringFormatter.joinList(objectsInfo.getObjects()))
                    .append("</b>.<br><br>");
        }

        return sb.toString();
    }

    public String convert2Strings(List<List<Integer>> answersIdxs,
                                  List<String> allSubjectsNames,
                                  List<ObjectsInfo> allObjectsInfos) {
        StringBuilder sb = new StringBuilder();
        List<List<String>> tempResult = new ArrayList<>(answersIdxs.size());
        for (int i = 0; i < answersIdxs.size(); i++) {
            List<Integer> list = answersIdxs.get(i);
            List<String> tempList = new ArrayList<>(list.size());

            for (int j = 0; j < list.size(); j++) {
                int idx = list.get(j);
                if (i == 0) {
                    tempList.add(namesConverters.getString(idx, allSubjectsNames));
                } else {
                    ObjectsInfo objectsInfo = allObjectsInfos.get(i - 1);
                    tempList.add(namesConverters.getString(idx, objectsInfo.getObjects()));
                }
            }
            tempResult.add(tempList);
        }

        for (int i = 0; i < tempResult.get(0).size(); i++) {
            for (int jj = 0; jj < tempResult.size(); jj++) {
                sb.append(tempResult.get(jj).get(i));
                if (jj + 1 < tempResult.size()) {
                    sb.append("=");
                } else {
                    sb.append(". ");
                }
            }
        }
        return sb.toString();
    }

    private List<List<List<String>>> createUiTables(List<String> allSubjectsNames, List<ObjectsInfo> allObjectsInfos) {
        List<List<String>> names = new ArrayList<>();
        names.add(allSubjectsNames);
        for (ObjectsInfo allObjectsInfo : allObjectsInfos) {
            names.add(allObjectsInfo.getObjects());
        }

        return listCombinator.getCombinations(names, 2);
    }

}
