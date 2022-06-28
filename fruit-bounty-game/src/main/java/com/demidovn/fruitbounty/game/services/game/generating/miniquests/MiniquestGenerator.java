package com.demidovn.fruitbounty.game.services.game.generating.miniquests;

import com.demidovn.fruitbounty.game.exceptions.GenerateQuestSentenceTriesLimitException;
import com.demidovn.fruitbounty.game.exceptions.GenerateQuestTriesLimitException;
import com.demidovn.fruitbounty.game.model.miniquests.AnsweredMiniquestGeneratorResult;
import com.demidovn.fruitbounty.game.model.miniquests.MiniquestCondition;
import com.demidovn.fruitbounty.game.model.miniquests.MiniquestGeneratorInfo;
import com.demidovn.fruitbounty.game.model.miniquests.MiniquestInfo;
import com.demidovn.fruitbounty.game.model.miniquests.MiniquestEngineResult;
import com.demidovn.fruitbounty.game.model.miniquests.MiniquestGeneratorResult;
import com.demidovn.fruitbounty.game.model.miniquests.validator.ValidResult;
import com.demidovn.fruitbounty.game.services.Randomizer;
import com.demidovn.fruitbounty.game.services.game.converters.NamesConverters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MiniquestGenerator {

    @Autowired
    private MiniquestEngineGenerator miniQuestEngineGenerator;

    @Autowired
    private NamesConverters namesConverters;

    private static final Randomizer rand = new Randomizer();

    public AnsweredMiniquestGeneratorResult generateStatementsWithAnswer(MiniquestInfo miniquestInfo)
            throws GenerateQuestSentenceTriesLimitException, GenerateQuestTriesLimitException {
        MiniquestGeneratorResult miniquestResult = generateStatements(miniquestInfo);

        Map.Entry<Integer, ValidResult> randomValidAnswer =
                rand.getRandomElement(new ArrayList<>(miniquestResult.miniquestEngineResult.validAnswers.entrySet()));
        int randomAnswerSubject = randomValidAnswer.getKey();
        int randomAnswerObject = rand.getRandomElement(new ArrayList<>(randomValidAnswer.getValue().objects));

        String answerSubject = miniquestInfo.getSubjects().get(randomAnswerSubject);
        String answerObject = miniquestInfo.getObjects().get(randomAnswerObject);

        return new AnsweredMiniquestGeneratorResult(
                miniquestResult.allAnswers, miniquestResult.validAnswers, answerSubject, answerObject,
                miniquestResult.statements, miniquestResult.miniquestEngineResult);
    }

    public MiniquestGeneratorResult generateStatements(MiniquestInfo miniquestInfo)
            throws GenerateQuestSentenceTriesLimitException, GenerateQuestTriesLimitException {
        List<String> statements = new ArrayList<>();

        MiniquestEngineResult miniquestEngineResult = miniQuestEngineGenerator.generateStatements(
                new MiniquestGeneratorInfo(miniquestInfo.table, miniquestInfo.objectsRangeIdxs,
                        miniquestInfo.getRepeatsLimit(), miniquestInfo.isContradictionsEnabled(),
                        miniquestInfo.getAnswerOutOfStatements(), miniquestInfo.getRightAnswersNum(),
                        miniquestInfo.subjectsRangeIdxs,
                        miniquestInfo.sentencesNum, miniquestInfo.statementsInSentence,
                        miniquestInfo.verityAllocation, miniquestInfo.nonTargetAnswers,
                        miniquestInfo.minAnswerInfers, miniquestInfo.maxAnswerInfers, miniquestInfo.globalRightAnswers
                ));

        StringBuilder sb = new StringBuilder();

        for (List<MiniquestCondition> sentence : miniquestEngineResult.statements) {
            sb.setLength(0);

            for (MiniquestCondition miniQuestCondition : sentence) {
                sb.append("<b>");
                sb.append(namesConverters.getString(miniQuestCondition.subjectIdx, miniquestInfo.getSubjects()));
                sb.append("</b> ");

                if (miniQuestCondition.inverse) {
                    sb.append(miniquestInfo.getVerbNegative());
                } else {
                    sb.append(miniquestInfo.getVerbPositive());
                }

                sb.append(" <b>");
                sb.append(namesConverters.getString(miniQuestCondition.objectIdx, miniquestInfo.getObjects()));
                sb.append("</b>. ");
            }

            sb.deleteCharAt(sb.length() - 1);
            statements.add(sb.toString());
        }

        Map<String, List<String>> allAnswers
                = namesConverters.convert2Strings(miniquestEngineResult.allAnswers, miniquestInfo.getSubjects(), miniquestInfo.getObjects());
        Map<String, List<String>> validAnswers
                = namesConverters.convert2Strings(miniquestEngineResult.validAnswers, miniquestInfo.getSubjects(), miniquestInfo.getObjects());

        return new MiniquestGeneratorResult(allAnswers, validAnswers, statements, miniquestEngineResult);
    }

}
