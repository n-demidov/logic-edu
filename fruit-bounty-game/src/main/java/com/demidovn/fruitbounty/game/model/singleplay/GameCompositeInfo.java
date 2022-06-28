package com.demidovn.fruitbounty.game.model.singleplay;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameCompositeInfo {
//    List<String> allSubjectsNames;
//    List<ObjectsInfo> allObjectsInfos;

    List<ObjectsInfo> allInfos;
    List<List<Integer>> answersIdxs;
    String objectToSubjectPrefix;

    public GameCompositeInfo(List<String> allSubjectsNames, List<ObjectsInfo> allObjectsInfos,
                             List<Integer> answersSubjectsIdxs, List<List<Integer>> answersObjectsIdxs,
                             String objectToSubjectPrefix) {
//        this.allSubjectsNames = allSubjectsNames;
//        this.allObjectsInfos = allObjectsInfos;

        this.allInfos = createAllInfos(allSubjectsNames, allObjectsInfos);
        this.answersIdxs = createAnswers(answersSubjectsIdxs, answersObjectsIdxs);
        this.objectToSubjectPrefix = objectToSubjectPrefix;
    }

    private List<List<Integer>> createAnswers(List<Integer> answersSubjectsIdxs, List<List<Integer>> answersObjectsIdxs) {
        List<List<Integer>> result = new ArrayList<>(answersSubjectsIdxs.size() + answersObjectsIdxs.size());

        result.add(answersSubjectsIdxs);
        result.addAll(answersObjectsIdxs);

        return result;
    }

    private List<ObjectsInfo> createAllInfos(List<String> allSubjectsNames, List<ObjectsInfo> allObjectsInfos) {
        List<ObjectsInfo> result = new ArrayList<>();
        ObjectsInfo tmp = new ObjectsInfo(
                allSubjectsNames, null, null, null, null);
        result.add(tmp);
        result.addAll(allObjectsInfos);
        return result;
    }

}
