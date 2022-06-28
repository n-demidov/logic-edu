package com.demidovn.fruitbounty.game.services.game.generating;

import com.demidovn.fruitbounty.game.GameOptions;
import com.demidovn.fruitbounty.game.exceptions.GenerateQuestSentenceTriesLimitException;
import com.demidovn.fruitbounty.game.exceptions.GenerateQuestTriesLimitException;
import com.demidovn.fruitbounty.game.model.miniquests.AnsweredMiniquestGeneratorResult;
import com.demidovn.fruitbounty.game.model.miniquests.MiniquestInfo;
import com.demidovn.fruitbounty.game.model.missions.MissionDescriptor;
import com.demidovn.fruitbounty.game.model.missions.MissionType;
import com.demidovn.fruitbounty.game.model.tablequests.TableQuestInfo2;
import com.demidovn.fruitbounty.game.model.tablequests.TableQuestResult;
import com.demidovn.fruitbounty.game.services.Randomizer;
import com.demidovn.fruitbounty.game.services.game.format.StringFormatter;
import com.demidovn.fruitbounty.game.services.game.missions.MissionsService;
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
public class MissionGameCreator {

  @Autowired
  private MissionsService missionsService;

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
    Game game = new Game(QuestType.MISSION, GameOptions.GameRating.MISSION, gameDescription.getPlayers(), GameOptions.TimeGameMs.DEFAULT);
    final List<List<String>> names = new ArrayList<>();

    int mission = gameDescription.getMission();

    MissionDescriptor missionsDesc = missionsService.getMissionsDescriptor(mission);
//    MissionDescriptor missionsDesc = missionsService.getMissionsDescriptor(7);
    String intro = missionsDesc.getIntro();

    final String questAnswer;
    final List<String> answerOptions;
    if (missionsDesc.getType() == MissionType.EDU) {
      intro += "<br><br><p><button class=\"btn btn-primary\" type=\"button\" data-toggle=\"collapse\" data-target=\"#collapseExample\" aria-expanded=\"false\" aria-controls=\"collapseExample\">Показать решение</button></p><div class=\"collapse\" id=\"collapseExample\"><div id=\"solution-text\" class=\"card card-body\">{solution-text}</div></div>";
      intro = intro.replace("{solution-text}", missionsDesc.getSolution());

      intro = intro.replace("{subjects}", "<b>" + stringFormatter.joinList(missionsDesc.getSubjects()) + "</b>");
      intro = intro.replace("{objects}", "<b>" + stringFormatter.joinList(missionsDesc.getObjects()) + "</b>");

      answerOptions = generatorMethods.getAnswerOptions(missionsDesc.getAskAbout(), missionsDesc.getSubjects(), missionsDesc.getObjects());
      questAnswer = missionsDesc.getRightAnswer();
      names.add(missionsDesc.getSubjects());
      names.add(missionsDesc.getObjects());
    } else if (missionsDesc.getType() == MissionType.STANDARD) {
      List<String> subjects = new ArrayList<>(missionsDesc.getSubjects());
      List<String> objects = new ArrayList<>(missionsDesc.getObjects());

      rand.shuffle(subjects);
      rand.shuffle(objects);

      Set<Integer> subjectsRangeIdxs = IntStream.range(0, subjects.size())
                .boxed()
                .collect(Collectors.toSet());
      Set<Integer> objectsRangeIdxs = IntStream.range(0, objects.size())
                .boxed()
                .collect(Collectors.toSet());

      AnsweredMiniquestGeneratorResult miniquestResult = miniQuestGenerator.generateStatementsWithAnswer(new MiniquestInfo(
              missionsDesc.isTable(),
              missionsDesc.getVerbPositive(), missionsDesc.getVerbNegative(), subjects, objects, subjectsRangeIdxs, objectsRangeIdxs,
              missionsDesc.getRepeatsLimit(), missionsDesc.isContradictionsEnabled(), missionsDesc.getAnswerOutOfStatements(),
              missionsDesc.getRightAnswersNum(), missionsDesc.getSentencesNum(), missionsDesc.getStatementsInSentence(),
              missionsDesc.getVerityAllocation(), missionsDesc.getNonTargetAnswers(),
              missionsDesc.getMinAnswerInfers(), missionsDesc.getMaxAnswerInfers(), Collections.emptyMap()
      ));

      intro = intro.replace("{subjects}", "<b>" + stringFormatter.joinList(subjects) + "</b>");
      intro = intro.replace("{objects}", "<b>" + stringFormatter.joinList(objects) + "</b>");
      intro = intro.replace("{statements}", stringFormatter.replaceStatements(miniquestResult.statements, commonSources.generateMenNames(miniquestResult.statements.size())));
      intro = intro.replace("{answer_subject}", "<b>" + miniquestResult.answerSubject + "</b>");
      intro = intro.replace("{answer_object}", "<b>" + miniquestResult.answerObject + "</b>");

      answerOptions = generatorMethods.getAnswerOptions(missionsDesc.getAskAbout(), subjects, objects);
      questAnswer = generatorMethods.getQuestAnswer(missionsDesc.getAskAbout(), miniquestResult.answerSubject, miniquestResult.answerObject);

      game.getGeneratedMiniquests().add(miniquestResult);
      miniquestResult.miniquestNumber = game.getGeneratedMiniquests().size();
      names.add(subjects);
      names.add(objects);
    } else if (missionsDesc.getType() == MissionType.TABLE_2) {
      List<String> subjects = new ArrayList<>(missionsDesc.getSubjects());
      List<List<String>> objectsMany = new ArrayList<>(missionsDesc.getObjectsMany());

      rand.shuffle(subjects);
      objectsMany.forEach(rand::shuffle);

      TableQuestResult tableQuestResult = tableQuestGenerator2.generateStatements(
              new TableQuestInfo2(subjects, missionsDesc.getVerbMany(), objectsMany, missionsDesc.getDemonstrativePronoun()));
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
      throw new UnsupportedOperationException("Unknown game type: " + missionsDesc.getType());
    }

    game.setAnswerOptions(answerOptions);
    game.setRightAnswer(questAnswer);

    game.getPages().add(intro);
    game.setMissionNumber(missionsDesc.getMissionNumber());

    game.setShowTables(missionsDesc.isTable() || missionsDesc.getType() == MissionType.TABLE_2);
    game.setTables(createUiTables(names));

    return game;
  }

  private List<List<List<String>>> createUiTables(List<List<String>> names) {
    return listCombinator.getCombinations(names, 2);
  }

}
