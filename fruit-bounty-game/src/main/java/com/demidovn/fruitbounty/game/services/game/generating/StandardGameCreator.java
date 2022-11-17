package com.demidovn.fruitbounty.game.services.game.generating;

import com.demidovn.fruitbounty.game.GameOptions;
import com.demidovn.fruitbounty.game.exceptions.GenerateQuestSentenceTriesLimitException;
import com.demidovn.fruitbounty.game.exceptions.GenerateQuestTriesLimitException;
import com.demidovn.fruitbounty.game.model.descriptors.StandardDescriptor;
import com.demidovn.fruitbounty.game.model.miniquests.AnsweredMiniquestGeneratorResult;
import com.demidovn.fruitbounty.game.model.miniquests.MiniquestInfo;
import com.demidovn.fruitbounty.game.model.descriptors.DescriptorType;
import com.demidovn.fruitbounty.game.model.tablequests.TableQuestInfo2;
import com.demidovn.fruitbounty.game.model.tablequests.TableQuestResult;
import com.demidovn.fruitbounty.game.services.Randomizer;
import com.demidovn.fruitbounty.game.services.game.format.StringFormatter;
import com.demidovn.fruitbounty.game.services.game.parsing.DescriptorService;
import com.demidovn.fruitbounty.game.services.game.generating.miniquests.MiniquestGenerator;
import com.demidovn.fruitbounty.game.services.game.generating.tablequests.TableQuestGenerator2;
import com.demidovn.fruitbounty.game.services.list.ListCombinator;
import com.demidovn.fruitbounty.gameapi.model.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.demidovn.fruitbounty.gameapi.model.backend.GameDescription;
import com.demidovn.fruitbounty.gameapi.model.backend.QuestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StandardGameCreator {

  @Autowired
  private DescriptorService descriptorService;

  @Autowired
  private StringFormatter stringFormatter;

  @Autowired
  private GeneratorMethods generatorMethods;

  @Autowired
  private MiniquestGenerator miniQuestGenerator;

  @Autowired
  private TableQuestGenerator2 tableQuestGenerator2;

  @Autowired
  private CommonSources commonSources;

  private static final Randomizer rand = new Randomizer();
  private static final ListCombinator listCombinator = new ListCombinator();

  public Game createNewGame(GameDescription gameDescription) throws GenerateQuestSentenceTriesLimitException, GenerateQuestTriesLimitException {
    Game game;
    StandardDescriptor descriptor;
    if (gameDescription.getQuestType() == QuestType.MISSION) {
      int mission = gameDescription.getMission();
      descriptor = descriptorService.getMissionsDescriptor(mission);
      game = new Game(QuestType.MISSION, GameOptions.GameRating.MISSION, gameDescription.getPlayers(),
              GameOptions.TimeGameMs.DEFAULT);
      game.setMissionNumber(mission);
    } else if (gameDescription.getQuestType() == QuestType.BRIEF_1) {
      descriptor = descriptorService.getBrief1Descriptor();
      game = new Game(QuestType.BRIEF_1, GameOptions.GameRating.BRIEF_1, gameDescription.getPlayers(),
              GameOptions.TimeGameMs.DEFAULT);
    } else if (gameDescription.getQuestType() == QuestType.BRIEF_2) {
      descriptor = descriptorService.getBrief2Descriptor();
      game = new Game(QuestType.BRIEF_2, GameOptions.GameRating.BRIEF_2, gameDescription.getPlayers(),
              GameOptions.TimeGameMs.DEFAULT);
    } else if (gameDescription.getQuestType() == QuestType.BRIEF_3) {
      descriptor = descriptorService.getBrief3Descriptor();
      game = new Game(QuestType.BRIEF_3, GameOptions.GameRating.BRIEF_3, gameDescription.getPlayers(),
              GameOptions.TimeGameMs.DEFAULT);
    } else if (gameDescription.getQuestType() == QuestType.BRIEF_100) {
      descriptor = descriptorService.getBrief100Descriptor();
      game = new Game(QuestType.BRIEF_100, GameOptions.GameRating.BRIEF_100, gameDescription.getPlayers(),
              GameOptions.TimeGameMs.DEFAULT);
    } else {
      throw new IllegalStateException("Unknown gameType=" + gameDescription);
    }

    final List<List<String>> names = new ArrayList<>();
    String intro = descriptor.getIntro();

    final String questAnswer;
    final List<String> answerOptions;
    if (descriptor.getType() == DescriptorType.MISSION_EDU) {
      List<String> subjects = new ArrayList<>(descriptor.getSubjects());
      List<String> objects = new ArrayList<>(descriptor.getObjects());

      rand.shuffle(subjects);
      rand.shuffle(objects);

      intro = addSolution(descriptor, intro);
      intro = intro.replace("{subjects}", "<b>" + stringFormatter.joinList(subjects) + "</b>");
      intro = intro.replace("{objects}", "<b>" + stringFormatter.joinList(objects) + "</b>");

      answerOptions = generatorMethods.getAnswerOptions(descriptor.getAskAbout(), subjects, objects);
      questAnswer = descriptor.getRightAnswer();
      names.add(subjects);
      names.add(objects);
    } else if (descriptor.getType() == DescriptorType.MISSION_STANDARD
            || descriptor.getType() == DescriptorType.STANDARD) {
      List<String> subjects = new ArrayList<>(descriptor.getSubjects());
      List<String> objects = new ArrayList<>(descriptor.getObjects());

      rand.shuffle(subjects);
      rand.shuffle(objects);

      int subjectsNum = subjects.size();
      int objectsNum = evaluateObjectsNum(descriptor.getMinObjectsNum(), descriptor.getMaxObjectsNum(), objects.size());
      if (descriptor.getMinSubjectsAndObjectsNum() != null) {
        subjectsNum = rand.generateFromRange(descriptor.getMinSubjectsAndObjectsNum(), descriptor.getMaxSubjectsAndObjectsNum());
        objectsNum = subjectsNum;
      }

      subjects = subjects.subList(0, subjectsNum);
      objects = objects.subList(0, objectsNum);

      Set<Integer> subjectsRangeIdxs = IntStream.range(0, subjects.size())
                .boxed()
                .collect(Collectors.toSet());
      Set<Integer> objectsRangeIdxs = IntStream.range(0, objects.size())
                .boxed()
                .collect(Collectors.toSet());

      int sentencesNum = evaluateSentencesNum(descriptor);
      boolean contradictionsEnabled = evaluateContradictionEnabled(descriptor.getContradictionsEnabled());
      List<List<Boolean>> verityAllocation = getVerityAllocation(descriptor, sentencesNum);

      AnsweredMiniquestGeneratorResult miniquestResult = miniQuestGenerator.generateStatementsWithAnswer(new MiniquestInfo(
              descriptor.isTable(),
              descriptor.getVerbPositive(), descriptor.getVerbNegative(), subjects, objects, subjectsRangeIdxs, objectsRangeIdxs,
              descriptor.getRepeatsLimit(), contradictionsEnabled, descriptor.getAnswerOutOfStatements(),
              descriptor.getRightAnswersNum(), sentencesNum, descriptor.getStatementsInSentence(),
              verityAllocation, descriptor.getNonTargetAnswers(),
              descriptor.getMinAnswerInfers(), descriptor.getMaxAnswerInfers(), Collections.emptyMap()
      ));

      intro = addSolution(descriptor, intro);
      intro = intro.replace("{subjects}", "<b>" + stringFormatter.joinList(subjects, descriptor.getLastSubjectConnector()) + "</b>");
      intro = intro.replace("{objects}", "<b>" + stringFormatter.joinList(objects, descriptor.getLastObjectConnector()) + "</b>");
      intro = intro.replace("{statements}", stringFormatter.replaceStatements(miniquestResult.statements, commonSources.generateMenNames(miniquestResult.statements.size())));
      intro = intro.replace("{answer_subject}", "<b>" + miniquestResult.answerSubject + "</b>");
      intro = intro.replace("{answer_object}", "<b>" + miniquestResult.answerObject + "</b>");

      answerOptions = generatorMethods.getAnswerOptions(descriptor.getAskAbout(), subjects, objects);
      questAnswer = generatorMethods.getQuestAnswer(descriptor.getAskAbout(), miniquestResult.answerSubject, miniquestResult.answerObject);

      game.getGeneratedMiniquests().add(miniquestResult);
      miniquestResult.miniquestNumber = game.getGeneratedMiniquests().size();

      names.add(subjects);
      names.add(objects);
    } else if (descriptor.getType() == DescriptorType.MISSION_TABLE_2) {
      List<String> subjects = new ArrayList<>(descriptor.getSubjects());
      List<List<String>> objectsMany = new ArrayList<>(descriptor.getObjectsMany());

      rand.shuffle(subjects);
      objectsMany.forEach(rand::shuffle);

      TableQuestResult tableQuestResult = tableQuestGenerator2.generateStatements(
              new TableQuestInfo2(subjects, descriptor.getVerbMany(), objectsMany, descriptor.getDemonstrativePronoun()));
      questAnswer = tableQuestResult.getRightAnswerSubject();

      intro = intro.replace("{subjects}", "<b>" + stringFormatter.joinList(subjects) + "</b>");
      intro = intro.replace("{objects-0}", "<b>" + stringFormatter.joinList(objectsMany.get(0)) + "</b>");
      intro = intro.replace("{objects-1}", "<b>" + stringFormatter.joinList(objectsMany.get(1)) + "</b>");
      intro = intro.replace("{statements}", stringFormatter.replaceStatements(tableQuestResult.getStatements(), commonSources.generateMenNames(tableQuestResult.getStatements().size())));
      intro = intro.replace("{answer_object}", "<b>" + tableQuestResult.getRightAnswerObject() + "</b>");
      answerOptions = subjects;

      game.getGeneratedMiniquests().add(tableQuestResult);
      names.add(subjects);
      names.add(objectsMany.get(0));
      names.add(objectsMany.get(1));
    } else {
      throw new UnsupportedOperationException("Unknown game type: " + descriptor.getType());
    }

    game.setAnswerOptions(answerOptions);
    game.setRightAnswer(questAnswer);

    game.getPages().add(intro);

    game.setShowTables(descriptor.isTable() || descriptor.getType() == DescriptorType.MISSION_TABLE_2);
    game.setTables(createUiTables(names));

    return game;
  }

  private String addSolution(StandardDescriptor descriptor, String intro) {
    if (descriptor.getSolution() == null) {
      return intro;
    }

    intro += "<br><br><p><button id='hintBtn' onmouseup='hintBtn()' class=\"btn btn-primary\" type=\"button\" data-toggle=\"collapse\" data-target='#collapseExample' data-container='body' aria-expanded=\"false\" aria-controls=\"collapseExample\">Порассуждать</button></p><div class=\"collapse\" id=\"collapseExample\"><div id=\"solution-text\" class='card card-body background-colored'>{solution-text}</div></div>";
    intro = intro.replace("{solution-text}", descriptor.getSolution());
    return intro;
  }

  private int evaluateObjectsNum(Integer minObjectsNum, Integer maxObjectsNum, int objectsSize) {
    if (minObjectsNum == null && maxObjectsNum == null) {
      return objectsSize;
    }

    return rand.generateFromRange(minObjectsNum, maxObjectsNum);
  }

  private boolean evaluateContradictionEnabled(Object contradictionsEnabled) {
    if (contradictionsEnabled instanceof Boolean) {
      return (boolean) contradictionsEnabled;
    }

    return rand.isPercentFired((int) contradictionsEnabled);
  }

  private int evaluateSentencesNum(StandardDescriptor descriptor) {
    if (descriptor.isTable()) {
      // todo: It's may extract where generates sentencesNum and verityAllocation for table quests
      return 0;
    }

    if (descriptor.getSentencesNum() != null) {
      return descriptor.getSentencesNum();
    }

    return rand.generateFromRange(descriptor.getSentencesNumMin(), descriptor.getSentencesNumMax());
  }

  private List<List<Boolean>> getVerityAllocation(StandardDescriptor descriptor, int sentencesNum) {
    if (descriptor.getVerityAllocation() != null) {
      return descriptor.getVerityAllocation();
    }

    return evaluateVerityAllocation(descriptor, sentencesNum);
  }

  private List<List<Boolean>> evaluateVerityAllocation(StandardDescriptor descriptor, int sentencesNum) {
    List<List<Boolean>> truePhrases = new ArrayList<>(sentencesNum);
    int counter = 0;
    for (int i = 0; i < sentencesNum; i++) {
      List<Boolean> sentenceTruePhrases = new ArrayList<>(1);
      sentenceTruePhrases.add(counter < descriptor.getTrueStatementsNum());
      truePhrases.add(sentenceTruePhrases);

      counter++;
    }

    return truePhrases;
  }

  private List<List<List<String>>> createUiTables(List<List<String>> names) {
    return listCombinator.getCombinations(names, 2);
  }

}
