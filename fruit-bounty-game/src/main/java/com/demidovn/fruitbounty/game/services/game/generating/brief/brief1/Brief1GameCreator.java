package com.demidovn.fruitbounty.game.services.game.generating.brief.brief1;

import com.demidovn.fruitbounty.game.GameOptions;
import com.demidovn.fruitbounty.game.exceptions.GenerateQuestSentenceTriesLimitException;
import com.demidovn.fruitbounty.game.exceptions.GenerateQuestTriesLimitException;
import com.demidovn.fruitbounty.game.model.miniquests.AnsweredMiniquestGeneratorResult;
import com.demidovn.fruitbounty.game.model.miniquests.MiniquestInfo;
import com.demidovn.fruitbounty.game.model.quest.AskAbout;
import com.demidovn.fruitbounty.game.model.quest.NonTargetAnswers;
import com.demidovn.fruitbounty.game.model.quest.OutOfStatements;
import com.demidovn.fruitbounty.game.model.singleplay.Composite1Descriptor;
import com.demidovn.fruitbounty.game.services.Randomizer;
import com.demidovn.fruitbounty.game.services.game.format.StringFormatter;
import com.demidovn.fruitbounty.game.services.game.generating.CommonSources;
import com.demidovn.fruitbounty.game.services.game.generating.GeneratorMethods;
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.MiniquestGenerator;
import com.demidovn.fruitbounty.game.services.game.singleplay.Rand1DescriptorService;
import com.demidovn.fruitbounty.gameapi.model.Game;
import com.demidovn.fruitbounty.gameapi.model.backend.GameDescription;
import com.demidovn.fruitbounty.gameapi.model.backend.QuestType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Random #1 game: simple miniquests.
 */
@Slf4j
@Component
public class Brief1GameCreator {
    private static final int TRUE_STATEMENTS_NUM = 1;

    @Autowired
    private Rand1DescriptorService descriptorService;

    @Autowired
    private MiniquestGenerator miniQuestGenerator;

    @Autowired
    private StringFormatter stringFormatter;

    @Autowired
    private GeneratorMethods generatorMethods;

    @Autowired
    private CommonSources commonSources;

    private static final Randomizer rand = new Randomizer();

    public Game createNewGame(GameDescription gameDescription) throws GenerateQuestSentenceTriesLimitException, GenerateQuestTriesLimitException {
        Game game = new Game(QuestType.BRIEF_1, GameOptions.GameRating.BRIEF_1, gameDescription.getPlayers(), GameOptions.TimeGameMs.DEFAULT);

        Composite1Descriptor compositeDescriptor = descriptorService.getDescriptor();
        String intro = compositeDescriptor.getIntro();

        List<String> subjects = new ArrayList<>(compositeDescriptor.getSubjects());
        List<String> objects = new ArrayList<>(compositeDescriptor.getObjects());

        rand.shuffle(subjects);
        rand.shuffle(objects);

        int objectsNum = rand.generateFromRange(GameOptions.Brief1.MIN_OBJECTS, GameOptions.Brief1.MAX_OBJECTS);
        objects = objects.subList(0, objectsNum);

        Set<Integer> subjectsRangeIdxs = IntStream.range(0, subjects.size())
                .boxed()
                .collect(Collectors.toSet());
        Set<Integer> objectsRangeIdxs = IntStream.range(0, objects.size())
                .boxed()
                .collect(Collectors.toSet());

        int sentencesNum = rand.generateFromRange(compositeDescriptor.getSentencesNumMin(), compositeDescriptor.getSentencesNumMax());
        boolean contradictionsEnabled = rand.isPercentFired(50);
        List<List<Boolean>> verityAllocation = generateVerityAllocation(sentencesNum, TRUE_STATEMENTS_NUM);

        AnsweredMiniquestGeneratorResult miniquestResult = miniQuestGenerator.generateStatementsWithAnswer(new MiniquestInfo(
                false,
                compositeDescriptor.getVerbPositive(), compositeDescriptor.getVerbNegative(), subjects, objects,
                subjectsRangeIdxs, objectsRangeIdxs,
                compositeDescriptor.getRepeatsLimit(), contradictionsEnabled,
                OutOfStatements.SKIP_VALIDATION, 1, sentencesNum, 1,
                verityAllocation, NonTargetAnswers.SKIP_VALIDATION,
                0, Integer.MAX_VALUE, Collections.emptyMap()
        ));

        intro = intro.replace("{subjects}", "<b>" + stringFormatter.joinList(subjects) + "</b>");
        intro = intro.replace("{objects}", "<b>" + stringFormatter.joinList(objects) + "</b>");
        intro = intro.replace("{statements}", stringFormatter.replaceStatements(miniquestResult.statements, commonSources.generateMenNames(miniquestResult.statements.size())));
        intro = intro.replace("{answer_subject}", "<b>" + miniquestResult.answerSubject + "</b>");
        intro = intro.replace("{answer_object}", "<b>" + miniquestResult.answerObject + "</b>");

        String questAnswer = generatorMethods.getQuestAnswer(AskAbout.OBJECT, miniquestResult.answerSubject, miniquestResult.answerObject);
        List<String> answerOptions = generatorMethods.getAnswerOptions(AskAbout.OBJECT, subjects, objects);

        game.getGeneratedMiniquests().add(miniquestResult);
        miniquestResult.miniquestNumber = game.getGeneratedMiniquests().size();

        game.setAnswerOptions(answerOptions);
        game.setRightAnswer(questAnswer);

        game.getPages().add(intro);

        return game;
    }

    private List<List<Boolean>> generateVerityAllocation(int sentencesNum, int trueStatementsNum) {
        List<List<Boolean>> result = new ArrayList<>(sentencesNum);
        int counter = 0;
        for (int i = 0; i < sentencesNum; i++) {
            List<Boolean> tempList = new ArrayList<>(1);
            tempList.add(counter < trueStatementsNum);
            result.add(tempList);

            counter++;
        }
        return result;
    }

}
