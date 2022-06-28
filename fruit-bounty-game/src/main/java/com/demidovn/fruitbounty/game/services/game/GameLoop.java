package com.demidovn.fruitbounty.game.services.game;

import com.demidovn.fruitbounty.game.GameOptions;
import com.demidovn.fruitbounty.game.model.GameProcessingContext;
import com.demidovn.fruitbounty.game.services.DefaultGameEventsSubscriptions;
import com.demidovn.fruitbounty.game.services.DefaultGameFacade;
import com.demidovn.fruitbounty.game.services.game.generating.singleplay.CompositeGameNextQuestHandler;
import com.demidovn.fruitbounty.gameapi.model.Game;
import com.demidovn.fruitbounty.gameapi.model.GameAction;
import com.demidovn.fruitbounty.gameapi.model.GameActionType;
import com.demidovn.fruitbounty.gameapi.model.Hook;
import com.demidovn.fruitbounty.gameapi.model.Player;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

@Slf4j
@Component
public class GameLoop {

  private static final int MIN_GAME_ACTIONS_IN_QUEUE_TO_WARNING = 20;

  @Autowired
  private DefaultGameFacade gameFacade;

  @Autowired
  private DefaultGameEventsSubscriptions gameEventsSubscriptions;

  @Autowired
  private GameRules gameRules;

  @Autowired
  private CompositeGameNextQuestHandler compositeGameNextQuestHandler;

  @Scheduled(fixedDelayString = GameOptions.GAME_LOOP_SCHEDULE_DELAY)
  public void gameLoop() {
    List<Game> finishedGames = new ArrayList<>();

    for (Game game : gameFacade.getAllGames()) {
      processGame(game);

      if (game.isFinished()) {
        finishedGames.add(game);
      }
    }

    gameFacade.gamesFinished(finishedGames);
  }

  private void processGame(Game game) {
    try {
      doProcessGame(game);
    } catch (Exception e) {
      log.error("error while process game, missionNumber={}, game={}", game.getMissionNumber(), game, e);
    }
  }

  private void doProcessGame(Game game) {
    GameProcessingContext processContext = new GameProcessingContext();

    int i = 0;
    GameAction gameAction;
    while ((gameAction = game.getGameActions().poll()) != null) {
      processGameAction(gameAction, processContext);

      i++;
      if (i == MIN_GAME_ACTIONS_IN_QUEUE_TO_WARNING) {
        log.warn("{} iteration of processing game-actions, game={}", i, game);
      }
    }

    if (i > MIN_GAME_ACTIONS_IN_QUEUE_TO_WARNING) {
      log.warn("{} game-actions have been processed, game={}", i, game);
    }

    checkForCurrentMoveExpiration(game, processContext);
    notifyIfGameChanged(game, processContext);
//    botService.actionIfBot(game);
  }

  private void processGameAction(GameAction gameAction, GameProcessingContext context) {
    if (gameAction.getGame().isFinished()) {
      return;
    }

    if (gameAction.getType() == GameActionType.ANSWER) {
      processAnswerAction(gameAction, context);
    } else if (gameAction.getType() == GameActionType.NEXT_QUEST) {
      processNextQuestAction(gameAction, context);
    } else if (gameAction.getType() == GameActionType.HOOK) {
      processHookAction(gameAction, context);
    } else if (gameAction.getType() == GameActionType.SURRENDER) {
      processSurrenderAction(gameAction, context);
    } else {
      throw new IllegalArgumentException(String.format(
        "Unknown gameActionType=%s", gameAction.getType()));
    }
  }

  private void processAnswerAction(GameAction gameAction, GameProcessingContext context) {
    gameRules.finishGameByAnswer(gameAction);
    context.markGameChanged();
  }

  private void processNextQuestAction(GameAction gameAction, GameProcessingContext context) {
    compositeGameNextQuestHandler.process(gameAction.getGame());
    context.markGameChanged();
  }

  private void processHookAction(GameAction gameAction, GameProcessingContext context) {
    String hookName = gameAction.getHook();
    Game game = gameAction.getGame();

    Hook hook = findHook(hookName, game);
    if (hook == null) {
      return;
    }

    game.getPages().add(hook.getMiniquestIntro());
    context.markGameChanged();
  }

  private void processSurrenderAction(GameAction gameAction, GameProcessingContext context) {
//    playerSurrendered(gameAction.findActionedPlayer(), gameAction.getGame());

    context.markGameChanged();
  }

  private void playerSurrendered(Player player, Game game) {
    player.setSurrendered(true);

//    if (player.equals(game.getCurrentPlayer())) {
//      gameRules.switchCurrentPlayer(game);
//    }

//    gameRules.checkGameEndingBySurrendering(game);
  }

  private void checkForCurrentMoveExpiration(Game game, GameProcessingContext processContext) {
    if (game.isFinished()) {
      return;
    }

    if (isCurrentMoveExpired(game)) {
      Player currentPlayer = game.getCurrentPlayer();

      currentPlayer.incrementMissedMoves();

      if (currentPlayer.getConsecutivelyMissedMoves() >= GameOptions.MAX_GAME_MISSED_MOVES) {
        playerSurrendered(currentPlayer, game);
      } else {
//        gameRules.switchCurrentPlayer(game);
      }

      processContext.markGameChanged();
    }
  }

  private boolean isCurrentMoveExpired(Game game) {
    return
      game.getCurrentMoveStarted() + game.getTimePerGameMs() < Instant.now().toEpochMilli();
  }

  private void notifyIfGameChanged(Game game, GameProcessingContext processContext) {
    if (processContext.isGameChanged()) {
      gameEventsSubscriptions.notifyGameChanged(game);
    }
  }

  @Nullable
  private Hook findHook(String hookName, Game game) {
    for (Hook hook : game.getHooks()) {
      if (hookName.equals(hook.getName())) {
        return hook;
      }
    }
    return null;
  }

}
