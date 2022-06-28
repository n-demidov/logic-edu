package com.demidovn.fruitbounty.game.services.game;

import com.demidovn.fruitbounty.game.services.game.rules.ending.GameEndingByMoving;
import com.demidovn.fruitbounty.game.services.game.rules.ending.GameEndingBySurrendering;
import com.demidovn.fruitbounty.game.services.game.rules.MoveCorrectness;
import com.demidovn.fruitbounty.gameapi.model.Game;
import com.demidovn.fruitbounty.gameapi.model.GameAction;
import org.springframework.stereotype.Component;

@Component
public class GameRules {

  private final MoveCorrectness moveCorrectness = new MoveCorrectness();
  private final GameEndingBySurrendering gameEndingBySurrendering = new GameEndingBySurrendering();
  private final GameEndingByMoving gameEndingByAnswer = new GameEndingByMoving();

  public boolean isMoveValid(GameAction gameAction) {
    return moveCorrectness.isMoveValid(gameAction);
  }

  public void checkGameEndingBySurrendering(Game game) {
    gameEndingBySurrendering.checkGameEndingBySurrendering(game);
  }

  public void finishGameByAnswer(GameAction gameAction) {
    gameEndingByAnswer.checkGameEndingByMoving(gameAction);
  }
}
