package com.demidovn.fruitbounty.game.services;

import com.demidovn.fruitbounty.game.GameOptions;
import com.demidovn.fruitbounty.game.exceptions.BaseGenerateQuestTriesLimitException;
import com.demidovn.fruitbounty.game.exceptions.GenerateMissionTriesLimitException;
import com.demidovn.fruitbounty.game.services.game.generating.singleplay.CompositeGameCreator;
import com.demidovn.fruitbounty.gameapi.model.backend.GameDescription;
import com.demidovn.fruitbounty.game.services.game.generating.StandardGameCreator;
import com.demidovn.fruitbounty.gameapi.model.Game;
import com.demidovn.fruitbounty.gameapi.model.GameAction;
import com.demidovn.fruitbounty.gameapi.model.backend.QuestType;
import com.demidovn.fruitbounty.gameapi.services.GameFacade;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultGameFacade implements GameFacade {
  private static final int CHANCE_TO_JUDI_QUEST_IN_TABLE_3 = 20;

  @Autowired
  private StandardGameCreator standardGameCreator;

  @Autowired
  private CompositeGameCreator compositeGameCreator;

  private final List<Game> games = new CopyOnWriteArrayList<>();
  private static final Randomizer rand = new Randomizer();

  @Override
  public Game startGame(GameDescription gameDescription) {
    Optional<Game> game;

    if (gameDescription.getQuestType() == QuestType.TABLE_3) {
      int random = rand.generateFromRange(1, 100);
      if (CHANCE_TO_JUDI_QUEST_IN_TABLE_3 >= random) {
        gameDescription.setQuestType(QuestType.TABLE_3_2);
      }
    }

    int triesCount = 1;
    do {
      game = createGame(gameDescription);
      triesCount++;
      checkGenerateQuestTriesLimit(triesCount, gameDescription);
    } while (!game.isPresent());

    games.add(game.get());

    log.debug("Game created: {}", game.get());
    return game.get();
  }

  @Override
  public void processGameAction(GameAction gameAction) {
    gameAction.getGame().getGameActions().add(gameAction);
  }

  @Override
  public List<Game> getAllGames() {
    return games;
  }

  public void gamesFinished(List<Game> finishedGames) {
    if (!finishedGames.isEmpty()) {
      games.removeAll(finishedGames);

      log.trace("{} games were removed, current games={}", finishedGames.size(), games.size());
    }
  }

  private Optional<Game> createGame(GameDescription gameDescription) {
    try {
      if (gameDescription.getQuestType() == QuestType.MISSION
              || gameDescription.getQuestType() == QuestType.BRIEF_1
              || gameDescription.getQuestType() == QuestType.BRIEF_2
              || gameDescription.getQuestType() == QuestType.BRIEF_3
              || gameDescription.getQuestType() == QuestType.TABLE_1
              || gameDescription.getQuestType() == QuestType.TABLE_2
              || gameDescription.getQuestType() == QuestType.TABLE_3
              || gameDescription.getQuestType() == QuestType.TABLE_3_2
              || gameDescription.getQuestType() == QuestType.BRIEF_100) {
        return Optional.of(standardGameCreator.createNewGame(gameDescription));
      } else if (gameDescription.getQuestType() == QuestType.COMPOSITE_1 ||
                 gameDescription.getQuestType() == QuestType.COMPOSITE_2) {
        return Optional.of(compositeGameCreator.createNewGame(gameDescription));
      } else {
        throw new IllegalArgumentException("Unknown gameType: " + gameDescription.getQuestType());
      }
    } catch (BaseGenerateQuestTriesLimitException e) {
      log.error("Error BaseGenerateQuestTriesLimitException on creating game, gameDescription={}", gameDescription, e);
      return Optional.empty();
    }
  }

  private void checkGenerateQuestTriesLimit(int triesCount, GameDescription info) {
    if (triesCount > GameOptions.GENERATE_MISSIONS_TRIES_LIMIT) {
      log.error("Error GenerateMissionTriesLimitException, info={}", info);
      throw new RuntimeException(new GenerateMissionTriesLimitException());
    }
  }

}
