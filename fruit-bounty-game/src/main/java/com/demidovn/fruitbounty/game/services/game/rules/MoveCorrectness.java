package com.demidovn.fruitbounty.game.services.game.rules;

import com.demidovn.fruitbounty.game.GameOptions;
import com.demidovn.fruitbounty.gameapi.model.GameAction;

import java.time.Instant;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MoveCorrectness extends AbstractGameRules {

  public boolean isMoveValid(GameAction gameAction) {
    return
      isActionBeforeGameExpired(gameAction);
  }

  private boolean isActionBeforeGameExpired(GameAction gameAction) {
    return gameAction.getGame().getCurrentMoveStarted() + gameAction.getGame().getTimePerGameMs()
      + GameOptions.MOVE_TIME_DELAY_CORRECTION > Instant.now().toEpochMilli();
  }

}
