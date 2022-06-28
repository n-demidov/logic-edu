package com.demidovn.fruitbounty.game.services.game.generating.miniquests.validators;

import com.demidovn.fruitbounty.game.model.miniquests.MiniquestCondition;
import com.demidovn.fruitbounty.game.model.miniquests.ValidMiniquestResult;
import com.demidovn.fruitbounty.game.model.miniquests.validator.AnswerParams;
import com.demidovn.fruitbounty.game.model.miniquests.validator.ValidParams;
import com.demidovn.fruitbounty.game.model.miniquests.validator.ValidResult;
import com.demidovn.fruitbounty.game.model.quest.NonTargetAnswers;
import com.demidovn.fruitbounty.game.model.quest.OutOfStatements;
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.MiniquestParamsValidator;
import com.demidovn.fruitbounty.game.services.list.SubListPermutter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class NumAnswersValidator {
    private static final Map<Integer, ValidResult> INVALID_ANSWER_IDX = Collections.emptyMap();

    private static final MiniquestParamsValidator miniquestParamsValidator = new MiniquestParamsValidator();
    private static final SubListPermutter subListPermutter = new SubListPermutter();

    public boolean isValid(List<List<MiniquestCondition>> statements, List<List<Boolean>> truePhrases,
                           Set<Integer> subjectsRangeIdxs, Set<Integer> objectsRangeIdxs,
                           ValidParams validParams, AnswerParams answerParams) {
        validateAnswerArgs(answerParams, subjectsRangeIdxs, objectsRangeIdxs);

        ValidMiniquestResult result = execute(statements, truePhrases, subjectsRangeIdxs, objectsRangeIdxs, validParams);

        ValidResult validResult = result.validAnswers.get(answerParams.targetSubjectIdx);
        return validResult != null && validResult.objects.contains(answerParams.rightVariantIdx);
    }

    public ValidMiniquestResult validate(List<List<MiniquestCondition>> statements, List<List<Boolean>> truePhrases,
                                         Set<Integer> subjectsRangeIdxs, Set<Integer> objectsRangeIdxs, ValidParams validParams) {
        return execute(statements, truePhrases, subjectsRangeIdxs, objectsRangeIdxs, validParams);
    }

    private ValidMiniquestResult execute(List<List<MiniquestCondition>> statements, List<List<Boolean>> truePhrases,
                                         Set<Integer> subjectsRangeIdxs, Set<Integer> objectsRangeIdxs, ValidParams validParams) {
        validateArgs(statements, truePhrases, subjectsRangeIdxs, objectsRangeIdxs, validParams);

        /* TF, TF => TF, TF
                     TF, FT
                     FT, TF
                     FT, FT */
        Set<List<List<Boolean>>> truePhrasesPermutations = subListPermutter.permuteAll(truePhrases);
        List<List<Integer>> allPotentiallyTrueStatements = getAllPotentiallyTrueStatementsIds(statements, truePhrasesPermutations);

        Map<Integer, ValidResult> allAnswers = new HashMap<>(subjectsRangeIdxs.size());
        boolean containsAllRightAnswers = false;

        for (int i = 0; i < allPotentiallyTrueStatements.size(); i++) {
            List<Integer> potentiallyTrueStatements = allPotentiallyTrueStatements.get(i);
            List<MiniquestCondition> checkingStatements = getCheckingStatements(potentiallyTrueStatements, statements);
            IntermediateResult intermediateResult = countRightAnswers(checkingStatements, statements, subjectsRangeIdxs, objectsRangeIdxs, validParams);

            if (intermediateResult.state == IntermediateResult.State.VALID) {
                addAnswers(intermediateResult, allAnswers);
                containsAllRightAnswers = containsAllRightAnswers || intermediateResult.containsAllRightAnswers;
            }
        }

        if (!containsAllRightAnswers) {
            return ValidMiniquestResult.INVALID_QUEST_RESULT;
        }

        Map<Integer, ValidResult> validAnswers = new HashMap<>(subjectsRangeIdxs.size());

        Iterator<Map.Entry<Integer, ValidResult>> it3 = allAnswers.entrySet().iterator();
        while (it3.hasNext()) {
            Map.Entry<Integer, ValidResult> next = it3.next();
            int subject = next.getKey();
            ValidResult validResult = next.getValue();

            if (validResult.objects.size() != validParams.rightAnswersNum  // nRightAnswers
                    || validResult.sharedMinAnswerInfers < validParams.minAnswerInfers  // nInfersNum
                    || validResult.sharedMinAnswerInfers > validParams.maxAnswerInfers  // nInfersNum
                    || !areNonTargetSubjectsValid(subject, allAnswers, subjectsRangeIdxs, validParams, statements)
                    || !objectsRangeIdxs.containsAll(validResult.objects)) {
                continue;
            }

            ValidResult filteredValidResult = filterByGlobalRightAnswers(validParams.globalRightAnswers, subject, validResult);
            filteredValidResult = filterByAnswerOutOfStatementsMode(validParams, subject, filteredValidResult, statements);
            if (filteredValidResult.objects.isEmpty()) {
                continue;
            }

            validAnswers.put(subject, filteredValidResult);
        }

        return new ValidMiniquestResult(allAnswers, validAnswers);
    }

    private IntermediateResult countRightAnswers(List<MiniquestCondition> checkingStatements,
                                                 List<List<MiniquestCondition>> allStatements,
                                                 Set<Integer> subjectsRangeIdxs,
                                                 Set<Integer> objectsRangeIdxs,
                                                 ValidParams validParams) {
        TempData tempData = new TempData(subjectsRangeIdxs, objectsRangeIdxs);

        for (List<MiniquestCondition> statements : allStatements) {
            for (MiniquestCondition statement : statements) {
                if (isCheckingStatements(statement.id, checkingStatements)) {
                    if (statement.inverse) {
                        addNegativeStatement(statement.subjectIdx, statement.objectIdx, tempData);
                    } else {
                        if (!addStatement(tempData.positiveObjectBySubject, tempData.positiveSubjectByObject, statement)) {
                            return IntermediateResult.INVALID_RESULT;
                        }
                    }
                } else {
                    if (statement.inverse) {
                        if (!addStatement(tempData.positiveObjectBySubject, tempData.positiveSubjectByObject, statement)) {
                            return IntermediateResult.INVALID_RESULT;
                        }
                    } else {
                        addNegativeStatement(statement.subjectIdx, statement.objectIdx, tempData);
                    }
                }
            }
        }

        inferData(tempData, subjectsRangeIdxs, objectsRangeIdxs);

        // Validation
        if (areThereContradictions(tempData)) {
            return IntermediateResult.INVALID_RESULT;
        }

        Map<Integer, Set<Integer>> outOfRangeDataBySubject = getOutOfRangeAnswers(subjectsRangeIdxs, objectsRangeIdxs, tempData);
        Map<Integer, ValidResult> result = prepareAnswers(subjectsRangeIdxs, objectsRangeIdxs, tempData, outOfRangeDataBySubject);

        if (result.size() != subjectsRangeIdxs.size()) {
            return IntermediateResult.INVALID_RESULT;
        }
        if (hasEmptySubjects(result)) {
            return IntermediateResult.INVALID_RESULT;
        }

        boolean containsAllRightAnswers = areContainsAllRightAnswers(result, validParams.globalRightAnswers);

        return new IntermediateResult(IntermediateResult.State.VALID, result, containsAllRightAnswers);
    }

    private boolean isCheckingStatements(int id, List<MiniquestCondition> checkingStatements) {
        for (int i = 0; i < checkingStatements.size(); i++) {
            if (checkingStatements.get(i).id == id) {
                return true;
            }
        }
        return false;
    }

    private boolean addStatement(Map<Integer, Integer> positiveObjectBySubject,
                                 Map<Integer, Integer> positiveSubjectByObject,
                                 MiniquestCondition statement) {
        if (putAndError(positiveObjectBySubject, statement.subjectIdx, statement.objectIdx)) {
            return false;
        }

        if (putAndError(positiveSubjectByObject, statement.objectIdx, statement.subjectIdx)) {
            return false;
        }
        return true;
    }

    private void addNegativeStatement(int subjectIdx, int objectIdx, TempData tempData) {
        tempData.negativeObjectsBySubject.get(subjectIdx).add(objectIdx);
        tempData.negativeSubjectsByObject.get(objectIdx).add(subjectIdx);
    }

    private boolean putAndError(Map<Integer, Integer> map, Integer key, Integer newValue) {
        Integer prevValue = map.put(key, newValue);
        if (prevValue != null && !newValue.equals(prevValue)) {
            return true;
        }
        return false;
    }

    private void inferData(TempData tempData,
                           Set<Integer> subjectsRangeIdxs,
                           Set<Integer> allVariantIdxs) {
        boolean updated;
        do {
            updateIfAnswersInfered(tempData, subjectsRangeIdxs);

            inferDataFromLastGap(tempData, subjectsRangeIdxs, allVariantIdxs);
            updated = inferDataFromNegatives(tempData, subjectsRangeIdxs, allVariantIdxs);
            updated = updateNegatives(tempData) || updated;

            incrementAnswersInfers(tempData, subjectsRangeIdxs);
        } while (updated);
    }

    private boolean inferDataFromNegatives(TempData tempData,
                                           Set<Integer> subjectsRangeIdxs,
                                           Set<Integer> allVariantIdxs) {
        boolean updated = false;

        if (subjectsRangeIdxs.size() <= allVariantIdxs.size()) {
            for (Integer subjectId : subjectsRangeIdxs) {
                Integer positiveObjectBySubjectId = tempData.positiveObjectBySubject.get(subjectId);
                if (positiveObjectBySubjectId != null) {
                    continue;
                }

                Set<Integer> negativeObjectIds = tempData.negativeObjectsBySubject.get(subjectId);
                if (negativeObjectIds.isEmpty()) {
                    continue;
                }

                if (negativeObjectIds.size() == allVariantIdxs.size() - 1) {
                    Set<Integer> inferentialObjectIds = new HashSet<>(allVariantIdxs);
                    boolean b = inferentialObjectIds.removeAll(negativeObjectIds);
                    if (!b) {
                        log.error("Can not infer ObjectId, negativeObjectIds={}, allVariantIdxs={}", negativeObjectIds, allVariantIdxs);
                        throw new IllegalStateException("Can not infer ObjectId");
                    }

                    int inferentialObjectId = inferentialObjectIds.stream().findFirst().get();
                    updated = updateNewData(tempData, subjectId, inferentialObjectId);
                }
            }
        }

        if (subjectsRangeIdxs.size() >= allVariantIdxs.size()) {
            for (Integer objectId : allVariantIdxs) {
                Integer positiveSubjectByObjectId = tempData.positiveSubjectByObject.get(objectId);
                if (positiveSubjectByObjectId != null) {
                    continue;
                }

                Set<Integer> negativeSubjectsIds = tempData.negativeSubjectsByObject.get(objectId);
                if (negativeSubjectsIds.isEmpty()) {
                    continue;
                }

                if (negativeSubjectsIds.size() == subjectsRangeIdxs.size() - 1) {
                    Set<Integer> inferentialIds = new HashSet<>(subjectsRangeIdxs);
                    boolean b = inferentialIds.removeAll(negativeSubjectsIds);
                    if (!b) {
                        log.error("Can not infer SubjectId, negativeSubjectsIds={}, subjectsRangeIdxs={}", negativeSubjectsIds, subjectsRangeIdxs);
                        throw new IllegalStateException("Can not infer SubjectId");
                    }

                    int inferentialSubjectId = inferentialIds.stream().findFirst().get();
                    updated = updateNewData(tempData, inferentialSubjectId, objectId);
                }
            }
        }

        return updated;
    }

    private boolean updateNewData(TempData tempData, int subjectId, int objectId) {
        boolean updated = false;

        if (tempData.positiveObjectBySubject.containsKey(subjectId)
                || tempData.negativeObjectsBySubject.get(subjectId).contains(objectId)) {
            return updated;
        }

        tempData.positiveObjectBySubject.put(subjectId, objectId);
        updated = true;

        if (!tempData.positiveSubjectByObject.containsKey(objectId)) {
            tempData.positiveSubjectByObject.put(objectId, subjectId);
        }

        return updated;
    }

    private boolean updateNegatives(TempData tempData) {
        boolean updated = false;

        for (Map.Entry<Integer, Integer> positiveEntry : tempData.positiveObjectBySubject.entrySet()) {
            int positiveSubjectId = positiveEntry.getKey();
            int positiveObjectId = positiveEntry.getValue();

            for (Map.Entry<Integer, Set<Integer>> negativeEntry : tempData.negativeObjectsBySubject.entrySet()) {
                int negativeSubjectId = negativeEntry.getKey();
                if (negativeSubjectId == positiveSubjectId) {
                    continue;
                }

                Set<Integer> values = negativeEntry.getValue();
                if (values.add(positiveObjectId)) {
                    updated = true;
                }

                tempData.negativeSubjectsByObject.get(positiveObjectId).add(negativeSubjectId);
            }
        }

        for (Map.Entry<Integer, Integer> positiveEntry : tempData.positiveSubjectByObject.entrySet()) {
            int positiveObjectId = positiveEntry.getKey();
            int positiveSubjectId = positiveEntry.getValue();

            for (Map.Entry<Integer, Set<Integer>> negativeEntry : tempData.negativeSubjectsByObject.entrySet()) {
                int negativeObjectId = negativeEntry.getKey();
                if (negativeObjectId == positiveObjectId) {
                    continue;
                }

                Set<Integer> values = negativeEntry.getValue();
                if (values.add(positiveSubjectId)) {
                    updated = true;
                }

                tempData.negativeObjectsBySubject.get(positiveSubjectId).add(negativeObjectId);
            }
        }

        return updated;
    }

    private void inferDataFromLastGap(TempData tempData,
                                      Set<Integer> subjectsRangeIdxs,
                                      Set<Integer> objectsRangeIdxs) {
        if (subjectsRangeIdxs.size() - tempData.positiveObjectBySubject.size() != 1
                || objectsRangeIdxs.size() - tempData.positiveSubjectByObject.size() != 1) {
            return;
        }

        int sub = -1;
        int obj = -1;

        Iterator<Integer> it = subjectsRangeIdxs.iterator();
        while (it.hasNext()) {
            int rangeIdx = it.next();
            if (!tempData.positiveObjectBySubject.containsKey(rangeIdx)) {
                sub = rangeIdx;
                break;
            }
        }

        it = objectsRangeIdxs.iterator();
        while (it.hasNext()) {
            int rangeIdx = it.next();
            if (!tempData.positiveSubjectByObject.containsKey(rangeIdx)) {
                obj = rangeIdx;
                break;
            }
        }

        if (sub == -1 || obj == -1) {
            throw new IllegalStateException("sub == -1 || obj == -1");
        }

        Set<Integer> negativeObjects = tempData.negativeObjectsBySubject.get(sub);
        if (negativeObjects != null && negativeObjects.contains(obj)) {
            return;
        }

        tempData.positiveObjectBySubject.put(sub, obj);
    }

    private void updateIfAnswersInfered(TempData tempData, Set<Integer> subjectsRangeIdxs) {
        Iterator<Integer> it = subjectsRangeIdxs.iterator();
        while (it.hasNext()) {
            int subject = it.next();
            if (!tempData.answerInferedBySubject.get(subject)) {
                if (tempData.positiveObjectBySubject.containsKey(subject)) {
                    tempData.answerInferedBySubject.put(subject, true);
                }
            }
        }
    }

    private void incrementAnswersInfers(TempData tempData, Set<Integer> subjectsRangeIdxs) {
        Iterator<Integer> it = subjectsRangeIdxs.iterator();
        while (it.hasNext()) {
            int subject = it.next();
            if (!tempData.answerInferedBySubject.get(subject)) {
                int answerInfers = tempData.answerInfersCountBySubject.get(subject);
                answerInfers++;
                tempData.answerInfersCountBySubject.put(subject, answerInfers);
            }
        }
    }

    private boolean areThereContradictions(TempData tempData) {
        Iterator<Map.Entry<Integer, Integer>> it = tempData.positiveObjectBySubject.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> next = it.next();
            int subjectId = next.getKey();
            int objectId = next.getValue();

            Integer positiveSubjectByObjectValue = tempData.positiveSubjectByObject.get(objectId);
            if (positiveSubjectByObjectValue != null && positiveSubjectByObjectValue != subjectId) {
                return true;
            }
        }

        Iterator<Map.Entry<Integer, Set<Integer>>> it2 = tempData.negativeObjectsBySubject.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry<Integer, Set<Integer>> next = it2.next();
            int subjectId = next.getKey();
            Set<Integer> negativeObjectIds = next.getValue();

            Integer positiveObjectBySubjectValue = tempData.positiveObjectBySubject.get(subjectId);
            if (negativeObjectIds.contains(positiveObjectBySubjectValue)) {
                return true;
            }
        }

        it = tempData.positiveSubjectByObject.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> next = it.next();
            int objectId = next.getKey();
            int subjectId = next.getValue();

            Integer positiveObjectBySubjectValue = tempData.positiveObjectBySubject.get(subjectId);
            if (positiveObjectBySubjectValue != null && positiveObjectBySubjectValue != objectId) {
                return true;
            }
        }

        it2 = tempData.negativeSubjectsByObject.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry<Integer, Set<Integer>> next = it2.next();
            int objectId = next.getKey();
            Set<Integer> negativeSubjectIds = next.getValue();

            Integer positiveSubjectByObjectValue = tempData.positiveSubjectByObject.get(objectId);
            if (negativeSubjectIds.contains(positiveSubjectByObjectValue)) {
                return true;
            }
        }

        return false;
    }

    private Map<Integer, Set<Integer>> getOutOfRangeAnswers(Set<Integer> subjectsRangeIdxs, Set<Integer> objectsRangeIdxs, TempData tempData) {
        Map<Integer, Set<Integer>> outOfRangeDataBySubject = new HashMap<>(subjectsRangeIdxs.size());

        Iterator<Integer> it = subjectsRangeIdxs.iterator();
        while (it.hasNext()) {
            int subject = it.next();
            if (tempData.positiveObjectBySubject.get(subject) == null) {
                Set<Integer> notIncludedObjectsIdxs = new HashSet<>(objectsRangeIdxs);
                notIncludedObjectsIdxs.removeAll(tempData.negativeObjectsBySubject.get(subject));

                outOfRangeDataBySubject.put(subject, notIncludedObjectsIdxs);
            }
        }
        return outOfRangeDataBySubject;
    }

    private Map<Integer, ValidResult> prepareAnswers(Set<Integer> subjectsRangeIdxs, Set<Integer> objectsRangeIdxs,
                                                     TempData tempData, Map<Integer, Set<Integer>> outOfRangeDataBySubject) {
        Map<Integer, ValidResult> result = new HashMap<>(subjectsRangeIdxs.size());

        Iterator<Integer> it = subjectsRangeIdxs.iterator();
        while (it.hasNext()) {
            int subject = it.next();

            Set<Integer> resultObjects = new HashSet<>(objectsRangeIdxs.size());

            Integer positiveObject = tempData.positiveObjectBySubject.get(subject);
            if (positiveObject != null) {
                resultObjects.add(positiveObject);
            } else {
                resultObjects = outOfRangeDataBySubject.get(subject);
            }
            int answerInfers = tempData.answerInfersCountBySubject.get(subject);

            result.put(subject, new ValidResult(resultObjects, answerInfers));
        }
        return result;
    }

    private boolean hasEmptySubjects(Map<Integer, ValidResult> result) {
        Iterator<ValidResult> it = result.values().iterator();
        while (it.hasNext()) {
            ValidResult next = it.next();
            if (next.getObjects().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean areContainsAllRightAnswers(Map<Integer, ValidResult> result,
                                               Map<Integer, Integer> rightAnswers) {
        Iterator<Map.Entry<Integer, Integer>> it = rightAnswers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> rightAnswer = it.next();
            int rightSubject = rightAnswer.getKey();
            int rightObject = rightAnswer.getValue();

            boolean contains = result.get(rightSubject).objects.contains(rightObject);
            if (!contains) {
                return false;
            }
        }
        return true;
    }

    private void validateArgs(List<List<MiniquestCondition>> statements, List<List<Boolean>> truePhrases,
                              Set<Integer> subjectsRangeIdxs, Set<Integer> objectsRangeIdxs, ValidParams validParams) {
        miniquestParamsValidator.validateArgs(subjectsRangeIdxs, objectsRangeIdxs,
                validParams.rightAnswersNum, validParams.minAnswerInfers, validParams.maxAnswerInfers,
                validParams.answerOutOfStatements, truePhrases, statements.size(), validParams.nonTargetAnswers, validParams.globalRightAnswers, null);

        for (int i = 0; i < statements.size(); i++) {
            if (statements.get(i).size() != truePhrases.get(i).size()) {
                log.error("statements.get(i).size() != truePhrases.get(i).size(), statements={}", statements);
                throw new IllegalArgumentException("statements.get(i).size() != truePhrases.get(i).size()");
            }
        }

        for (int i = 0; i < statements.size(); i++) {
            List<MiniquestCondition> sentenceConditions = statements.get(i);
            for (int j = 0; j < sentenceConditions.size(); j++) {
                MiniquestCondition miniQuestCondition = sentenceConditions.get(j);

                if (!subjectsRangeIdxs.contains(miniQuestCondition.subjectIdx)) {
                    log.error("statement.subjectIdx is out of 'subjectsRangeIdxs', statements={}, subjectsRangeIdxs={}", statements, subjectsRangeIdxs);
                    throw new IllegalArgumentException("statement.subjectIdx is out of 'subjectsRangeIdxs'");
                }
                if (!objectsRangeIdxs.contains(miniQuestCondition.objectIdx)) {
                    log.error("statement.objectIdx is out of 'objectsRangeIdxs', statements={}, objectsRangeIdxs={}", statements, objectsRangeIdxs);
                    throw new IllegalArgumentException("statement.objectIdx is out of 'objectsRangeIdxs'");
                }
            }
        }
    }

    private void validateAnswerArgs(AnswerParams answerParams, Set<Integer> subjectsRangeIdxs, Set<Integer> objectsRangeIdxs) {
        if (!subjectsRangeIdxs.contains(answerParams.targetSubjectIdx)) {
            log.error("targetSubjectIdx should be contains in subjectsRangeIdxs, answerParams={}, subjectsRangeIdxs={}, objectsRangeIdxs={}", answerParams, subjectsRangeIdxs, objectsRangeIdxs);
            throw new IllegalArgumentException("targetSubjectIdx should be contains in subjectsRangeIdxs: " + subjectsRangeIdxs);
        }

        if (!objectsRangeIdxs.contains(answerParams.rightVariantIdx)) {
            log.error("rightVariantIdx should be contains in objectsRangeIdxs, answerParams={}, subjectsRangeIdxs={}, objectsRangeIdxs={}", answerParams, subjectsRangeIdxs, objectsRangeIdxs);
            throw new IllegalArgumentException("rightVariantIdx should be contains in objectsRangeIdxs: " + objectsRangeIdxs);
        }
    }

    private List<MiniquestCondition> getCheckingStatements(List<Integer> potentiallyTrueStatements, List<List<MiniquestCondition>> statements) {
        List<MiniquestCondition> result = new ArrayList<>();

        for (int p = 0; p < potentiallyTrueStatements.size(); p++) {
            Integer id = potentiallyTrueStatements.get(p);
            for (int i = 0; i < statements.size(); i++) {
                List<MiniquestCondition> miniquestConditions = statements.get(i);
                for (int jj = 0; jj < miniquestConditions.size(); jj++) {
                    if (miniquestConditions.get(jj).id == id) {
                        result.add(miniquestConditions.get(jj));
                    }
                }
            }
        }
        return result;
    }

    private List<List<Integer>> getAllPotentiallyTrueStatementsIds(List<List<MiniquestCondition>> statements, Set<List<List<Boolean>>> truePhrasesPermutations) {
        List<List<Integer>> allPotentiallyTrueStatements = new ArrayList<>();

        for (List<List<Boolean>> truePhrasesPermutation : truePhrasesPermutations) {
            List<Integer> potentiallyTrueStatements = new ArrayList<>();

            for (int jj = 0; jj < truePhrasesPermutation.size(); jj++) {
                List<Boolean> sentenceTruePhrases = truePhrasesPermutation.get(jj);
                for (int kk = 0; kk < sentenceTruePhrases.size(); kk++) {
                    boolean isTrueCondition = sentenceTruePhrases.get(kk);
                    if (isTrueCondition) {
                        MiniquestCondition miniQuestCondition = statements.get(jj).get(kk);
                        potentiallyTrueStatements.add(miniQuestCondition.id);
                    }
                }
            }
            allPotentiallyTrueStatements.add(potentiallyTrueStatements);
        }
        return allPotentiallyTrueStatements;
    }

    private boolean areNonTargetSubjectsValid(int checkingSubject, Map<Integer, ValidResult> allAnswers,
                                              Set<Integer> subjectsRangeIdxs, ValidParams validParams,
                                              List<List<MiniquestCondition>> statements) {
        if (validParams.nonTargetAnswers == NonTargetAnswers.SKIP_VALIDATION) {
            return true;
        }

        Iterator<Integer> it = subjectsRangeIdxs.iterator();
        while (it.hasNext()) {
            int subject = it.next();
            if (subject == checkingSubject) {
                continue;
            }

            if (validParams.nonTargetAnswers == NonTargetAnswers.ONLY_ONE_ANSWER_FOR_MENTIONED_IN_STATEMENTS &&
                    !containsSubjectInStatements(subject, statements)) {
                continue;
            }

            ValidResult checkingSubjValidResult = allAnswers.get(subject);
            if (checkingSubjValidResult.objects.size() != 1) {
                return false;
            }
        }
        return true;
    }

    private ValidResult filterByGlobalRightAnswers(Map<Integer, Integer> globalRightAnswers, int checkingSubject, ValidResult validResult) {
        Integer globalObjectRightAnswer = globalRightAnswers.get(checkingSubject);
        if (globalObjectRightAnswer == null) {
            return validResult;
        }

        Set<Integer> checkingObjects = validResult.objects;
        Set<Integer> resultObjects = new HashSet<>(checkingObjects.size());

        Iterator<Integer> it = checkingObjects.iterator();
        while ((it.hasNext())) {
            int checkingObject = it.next();
            if (globalObjectRightAnswer == checkingObject) {
                resultObjects.add(checkingObject);
            }
        }

        return new ValidResult(resultObjects, validResult.sharedMinAnswerInfers);
    }

    private ValidResult filterByAnswerOutOfStatementsMode(ValidParams validParams, int checkingSubject, ValidResult validResult, List<List<MiniquestCondition>> statements) {
        if (validParams.answerOutOfStatements == OutOfStatements.SKIP_VALIDATION) {
            return validResult;
        }

        Set<Integer> resultObjects = new HashSet<>(validResult.objects.size());
        Iterator<Integer> it = validResult.objects.iterator();
        while ((it.hasNext())) {
            int checkingObject = it.next();

            boolean containsCheckingStatement = containsCheckingStatement(statements, checkingSubject, checkingObject);

            if (validParams.answerOutOfStatements == OutOfStatements.STRICTLY_IN_STATEMENTS) {
                if (containsCheckingStatement) {
                    resultObjects.add(checkingObject);
                }
            } else if (validParams.answerOutOfStatements == OutOfStatements.STRICTLY_OUT_OF_STATEMENTS) {
                if (!containsCheckingStatement) {
                    resultObjects.add(checkingObject);
                }
            }
        }

        return new ValidResult(resultObjects, validResult.sharedMinAnswerInfers);
    }

    private boolean containsCheckingStatement(List<List<MiniquestCondition>> statements, int checkingSubject, int checkingObject) {
        for (int sentenceIdx = 0; sentenceIdx < statements.size(); sentenceIdx++) {
            List<MiniquestCondition> sentence = statements.get(sentenceIdx);
            for (int stIdx = 0; stIdx < sentence.size(); stIdx++) {
                MiniquestCondition statement = sentence.get(stIdx);
                if (statement.subjectIdx == checkingSubject && statement.objectIdx == checkingObject) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean containsSubjectInStatements(int subject, List<List<MiniquestCondition>> statements) {
        for (int sentenceIdx = 0; sentenceIdx < statements.size(); sentenceIdx++) {
            List<MiniquestCondition> sentence = statements.get(sentenceIdx);
            for (int stIdx = 0; stIdx < sentence.size(); stIdx++) {
                MiniquestCondition statement = sentence.get(stIdx);
                if (statement.subjectIdx == subject) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addAnswers(IntermediateResult intermediateResult, Map<Integer, ValidResult> allAnswers) {
        Iterator<Map.Entry<Integer, ValidResult>> it2 = intermediateResult.answersBySubject.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry<Integer, ValidResult> next = it2.next();
            Integer subject = next.getKey();
            ValidResult validResult = next.getValue();

            if (allAnswers.get(subject) == null) {
                allAnswers.put(subject, validResult);
            } else {
                allAnswers.get(subject).objects.addAll(validResult.objects);
                // May be make more smart merging (for each object).
                allAnswers.get(subject).sharedMinAnswerInfers =
                        Math.min(allAnswers.get(subject).sharedMinAnswerInfers, validResult.sharedMinAnswerInfers);
            }
        }
    }

    private static class TempData {
        final Map<Integer, Integer> positiveObjectBySubject;
        final Map<Integer, Set<Integer>> negativeObjectsBySubject;
        final Map<Integer, Integer> positiveSubjectByObject;
        final Map<Integer, Set<Integer>> negativeSubjectsByObject;
        final Map<Integer, Boolean> answerInferedBySubject;
        final Map<Integer, Integer> answerInfersCountBySubject;

        public TempData(Set<Integer> subjectsRangeIdxs, Set<Integer> objectsRangeIdxs) {
            this.positiveObjectBySubject = new HashMap<>(subjectsRangeIdxs.size());
            this.negativeObjectsBySubject = new HashMap<>(subjectsRangeIdxs.size());
            this.positiveSubjectByObject = new HashMap<>(objectsRangeIdxs.size());
            this.negativeSubjectsByObject = new HashMap<>(objectsRangeIdxs.size());
            this.answerInferedBySubject = new HashMap<>(subjectsRangeIdxs.size());
            this.answerInfersCountBySubject = new HashMap<>(subjectsRangeIdxs.size());

            for (Integer subjectIdx : subjectsRangeIdxs) {
                negativeObjectsBySubject.put(subjectIdx, new HashSet<>(objectsRangeIdxs.size()));
                answerInferedBySubject.put(subjectIdx, false);
                answerInfersCountBySubject.put(subjectIdx, 0);
            }
            for (Integer objectIdx : objectsRangeIdxs) {
                negativeSubjectsByObject.put(objectIdx, new HashSet<>(subjectsRangeIdxs.size()));
            }
        }
    }

    private static class IntermediateResult {
        static IntermediateResult INVALID_RESULT = new IntermediateResult(State.INVALID, INVALID_ANSWER_IDX, false);

        final State state;
        final Map<Integer, ValidResult> answersBySubject;
        final boolean containsAllRightAnswers;

        public IntermediateResult(State state, Map<Integer, ValidResult> answersBySubject, boolean containsAllRightAnswers) {
            this.state = state;
            this.answersBySubject = answersBySubject;
            this.containsAllRightAnswers = containsAllRightAnswers;
        }

        private enum State {
            VALID, INVALID
        }
    }

}
