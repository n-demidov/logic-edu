package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.numanswers;

import com.demidovn.fruitbounty.game.model.miniquests.validator.AnswerParams;
import com.demidovn.fruitbounty.game.model.miniquests.MiniquestCondition;
import com.demidovn.fruitbounty.game.model.miniquests.validator.ValidParams;
import com.demidovn.fruitbounty.game.model.quest.NonTargetAnswers;
import com.demidovn.fruitbounty.game.model.quest.OutOfStatements;
import com.demidovn.fruitbounty.game.services.Randomizer;
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators.NumAnswersValidator;
import com.demidovn.fruitbounty.game.services.list.SubListPermutter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class NumAnswersExecutor {
    private static final int MAX_TOTAL_PERMUTATIONS = 3000;

    private final NumAnswersValidator miniQuestValidator = new NumAnswersValidator();
    private final SubListPermutter subListPermutter = new SubListPermutter();
    private final Randomizer randomizer = new Randomizer();

    public List<Boolean> apply(List<Integer> objectsIdxs, List<Boolean> inversesInfo, List<Integer> objectsRangeIdxs,
                               int rightVariantIdx, int rightAnswersNum, int trueStatementsNum, OutOfStatements outOfStatements) {
        int size = objectsIdxs.size();
        List<List<Integer>> subjIdxs = new ArrayList<>(size);
        List<List<Integer>> objIdxs = new ArrayList<>(size);
        List<List<Boolean>> inverses = new ArrayList<>(size);
        List<List<Boolean>> truePhrases = new ArrayList<>(size);

        int counter = 0;
        for (int i = 0; i < size; i++) {
            List<Integer> sentence = new ArrayList<>(1);
            sentence.add(0);
            subjIdxs.add(sentence);

            sentence = new ArrayList<>(1);
            sentence.add(objectsIdxs.get(i));
            objIdxs.add(sentence);

            List<Boolean> sentenceB = new ArrayList<>(1);
            sentenceB.add(inversesInfo.get(i));
            inverses.add(sentenceB);

            sentenceB = new ArrayList<>(1);
            sentenceB.add(counter < trueStatementsNum);
            truePhrases.add(sentenceB);

            counter++;
        }

        return apply(subjIdxs, objIdxs, inverses, truePhrases,
                Collections.singletonList(0), objectsRangeIdxs,
                0, rightVariantIdx,
                new ValidParams(rightAnswersNum, NonTargetAnswers.SKIP_VALIDATION, outOfStatements, Collections.emptyMap()),
                true);
    }

    /**
     * For table quests
     */
    public List<Boolean> apply(List<List<Integer>> subjectsIdxs, List<List<Integer>> objectsIdxs,
                               List<List<Boolean>> inverses,
                               List<Integer> subjectsRangeIdxs, List<Integer> objectsRangeIdxs,
                               int targetSubjectIdx, int rightVarIdx,
                               ValidParams validParams) {
        List<List<Boolean>> truePhrases = new ArrayList<>(subjectsIdxs.size());
        for (int i = 0; i < subjectsIdxs.size(); i++) {
            truePhrases.add(new ArrayList<>());
            List<Integer> sentenceObjectIdxs = subjectsIdxs.get(i);
            for (int j = 0; j < sentenceObjectIdxs.size(); j++) {
                truePhrases.get(i).add(true);
            }
        }

        return apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjectsRangeIdxs, objectsRangeIdxs,
                targetSubjectIdx, rightVarIdx, validParams, true);
    }

    /**
     * For table quests
     */
    public List<Boolean> apply(List<List<Integer>> subjectsIdxs, List<List<Integer>> objectsIdxs,
                               List<List<Boolean>> inverses,
                               List<Integer> subjectsRangeIdxs, List<Integer> objectsRangeIdxs,
                               int targetSubjectIdx, int rightVarIdx,
                               ValidParams validParams, boolean permute) {
        List<List<Boolean>> truePhrases = new ArrayList<>(subjectsIdxs.size());
        for (int i = 0; i < subjectsIdxs.size(); i++) {
            truePhrases.add(new ArrayList<>());
            List<Integer> sentenceObjectIdxs = subjectsIdxs.get(i);
            for (int j = 0; j < sentenceObjectIdxs.size(); j++) {
                truePhrases.get(i).add(true);
            }
        }

        return apply(subjectsIdxs, objectsIdxs, inverses, truePhrases, subjectsRangeIdxs, objectsRangeIdxs,
                targetSubjectIdx, rightVarIdx, validParams, permute);
    }

    public List<Boolean> apply(List<List<Integer>> subjectsIdxs, List<List<Integer>> objectsIdxs,
                               List<List<Boolean>> inverses, List<List<Boolean>> truePhrases,
                               List<Integer> subjectsRangeIdxs, List<Integer> objectsRangeIdxs,
                               int targetSubjectIdx, int rightVarIdx, int rightAnswersNum,
                               int minAnswerInfers, int maxAnswerInfers,
                               NonTargetAnswers nonTargetAnswers, OutOfStatements answerOutOfStatements,
                               Map<Integer, Integer> globalRightAnswers) {
        return apply(subjectsIdxs, objectsIdxs, inverses, truePhrases,
                subjectsRangeIdxs, objectsRangeIdxs,
                targetSubjectIdx, rightVarIdx,
                new ValidParams(rightAnswersNum, nonTargetAnswers, answerOutOfStatements, minAnswerInfers, maxAnswerInfers, globalRightAnswers), true);
    }

    /**
     * Valid the list in all variety of combinations
     */
    public List<Boolean> apply(List<List<Integer>> subjectsIdxs, List<List<Integer>> objectsIdxs,
                               List<List<Boolean>> inverses, List<List<Boolean>> truePhrases,
                               List<Integer> subjectsRangeIdxs, List<Integer> objectsRangeIdxs,
                               int targetSubjectIdx, int rightVarIdx, ValidParams validParams, boolean permute) {
        // Not need more
//        validateTestArgs(subjectsIdxs, objectsIdxs, inverses, truePhrases);

        List<List<TempMiniquestCondition>> conditions = new ArrayList<>(subjectsIdxs.size());
        for (int i = 0; i < subjectsIdxs.size(); i++) {
            conditions.add(new ArrayList<>());
            List<Integer> sentenceObjectIdxs = subjectsIdxs.get(i);
            for (int j = 0; j < sentenceObjectIdxs.size(); j++) {
                TempMiniquestCondition miniQuestCondition = new TempMiniquestCondition(
                        subjectsIdxs.get(i).get(j),
                        objectsIdxs.get(i).get(j),
                        inverses.get(i).get(j));
                conditions.get(i).add(miniQuestCondition);
            }
        }

        List<List<List<TempMiniquestCondition>>> allPermutations;
        if (permute) {
            allPermutations = cutPermutations(new ArrayList<>(subListPermutter.permuteAll(conditions)), truePhrases);
        } else {
            List<List<List<TempMiniquestCondition>>> tempL0 = new ArrayList<>(1);
            List<List<TempMiniquestCondition>> tempL1 = new ArrayList<>(1);

            List<TempMiniquestCondition> tempMiniquestConditions = conditions.get(0);
            tempL1.add(tempMiniquestConditions);
            tempL0.add(tempL1);
            allPermutations = tempL0;
        }

        List<Boolean> actualResults = new ArrayList<>(allPermutations.size());

        for (List<List<TempMiniquestCondition>> permutation : allPermutations) {
            boolean actualResult = execute(
                    permutation, truePhrases,
                    new HashSet<>(subjectsRangeIdxs), new HashSet<>(objectsRangeIdxs),
                    validParams,
                    new AnswerParams(targetSubjectIdx, rightVarIdx));
            actualResults.add(actualResult);
        }

        return actualResults;
    }

    private <T> List<T> cutPermutations(List<T> reducingList, List<List<Boolean>> truePhrases) {
        Set<List<List<Boolean>>> truePhrasesPermutations = subListPermutter.permuteAll(truePhrases);
        int totalSize = reducingList.size() * truePhrasesPermutations.size();

        if (totalSize < MAX_TOTAL_PERMUTATIONS) {
            return reducingList;
        }

        List<T> result = new ArrayList<>();
        result.add(reducingList.get(0));
        reducingList = reducingList.subList(1, reducingList.size());

        int remainPermutationReserve = MAX_TOTAL_PERMUTATIONS / truePhrasesPermutations.size() - 1;
        if (remainPermutationReserve >= 1) {
            int maxStartRandomIdx = reducingList.size() - remainPermutationReserve;
            if (maxStartRandomIdx < 0) {
                maxStartRandomIdx = 0;
            }

            int startIdx = randomizer.generateFromRange(1, maxStartRandomIdx);
            int endIdx = startIdx + remainPermutationReserve;
            result.addAll(reducingList.subList(startIdx, endIdx));
        }

        return result;
    }

    private boolean execute(List<List<TempMiniquestCondition>> descrMiniquestConditions, List<List<Boolean>> truePhrases,
                            Set<Integer> subjectsRangeIdxs, Set<Integer> objectsRangeIdxs,
                            ValidParams validParams, AnswerParams answerParams) {
        List<List<MiniquestCondition>> conditions = new ArrayList<>(descrMiniquestConditions.size());

        int counter = 0;
        for (int i = 0; i < descrMiniquestConditions.size(); i++) {
            List<TempMiniquestCondition> sentenceList = descrMiniquestConditions.get(i);
            conditions.add(new ArrayList<>());
            for (int j = 0; j < sentenceList.size(); j++) {
                TempMiniquestCondition tempMiniquestCondition = sentenceList.get(j);
                MiniquestCondition miniQuestCondition = new MiniquestCondition(
                        counter++,
                        tempMiniquestCondition.subjectIdx,
                        tempMiniquestCondition.objectIdx,
                        tempMiniquestCondition.inverse);
                conditions.get(i).add(miniQuestCondition);
            }
        }

        return miniQuestValidator.isValid(conditions, truePhrases,
                new HashSet<>(subjectsRangeIdxs), new HashSet<>(objectsRangeIdxs),
                validParams,
                answerParams);
    }

//    private void validateTestArgs(List<List<Integer>> subjectsIdxs, List<List<Integer>> objectsIdxs,
//                                  List<List<Boolean>> inverses, List<List<Boolean>> truePhrases) {
//        if (subjectsIdxs.size() != objectsIdxs.size() || subjectsIdxs.size() != inverses.size()) {
//            throw new RuntimeException("subjectsIdxs.size() != objectsIdxs.size() || subjectsIdxs.size() != inverses.size() || subjectsIdxs.size() != truePhrases.size()");
//        }
//
//        int size = subjectsIdxs.size();
//        for (int i = 0; i < size; i++) {
//            if (subjectsIdxs.get(i).size() != objectsIdxs.get(i).size() || subjectsIdxs.get(i).size() != inverses.get(i).size()) {
//                throw new RuntimeException("subjectsIdxs.get(i).size() != objectsIdxs.get(i).size() || subjectsIdxs.get(i).size() != inverses.get(i).size() || subjectsIdxs.get(i).size() != truePhrases.get(i).size()");
//            }
//        }
//    }

    private static class TempMiniquestCondition {
        public final int subjectIdx;
        public final int objectIdx;
        public boolean inverse;

        public TempMiniquestCondition(int subjectIdx, int objectIdx, boolean inverse) {
            this.subjectIdx = subjectIdx;
            this.objectIdx = objectIdx;
            this.inverse = inverse;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TempMiniquestCondition that = (TempMiniquestCondition) o;
            return subjectIdx == that.subjectIdx && objectIdx == that.objectIdx && inverse == that.inverse;
        }

        @Override
        public int hashCode() {
            return Objects.hash(subjectIdx, objectIdx, inverse);
        }
    }

}
