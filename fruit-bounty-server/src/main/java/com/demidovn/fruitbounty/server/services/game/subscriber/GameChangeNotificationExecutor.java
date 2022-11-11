package com.demidovn.fruitbounty.server.services.game.subscriber;

import com.demidovn.fruitbounty.gameapi.model.Game;
import com.demidovn.fruitbounty.gameapi.model.Player;
import com.demidovn.fruitbounty.gameapi.model.backend.QuestType;
import com.demidovn.fruitbounty.server.AppConfigs;
import com.demidovn.fruitbounty.server.MetricsConsts;
import com.demidovn.fruitbounty.server.dto.operations.response.ResponseOperation;
import com.demidovn.fruitbounty.server.persistence.entities.User;
import com.demidovn.fruitbounty.server.services.ClientNotifier;
import com.demidovn.fruitbounty.server.services.UserService;
import com.demidovn.fruitbounty.server.services.game.GameNotifier;
import com.demidovn.fruitbounty.server.services.game.UserGames;
import java.time.Instant;

import com.demidovn.fruitbounty.server.services.metrics.StatService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GameChangeNotificationExecutor implements Runnable {

  @Autowired
  private UserService userService;

  @Autowired
  private ClientNotifier clientNotifier;

  @Autowired
  private UserGames userGames;

  @Autowired
  private GameNotifier gameNotifier;

  @Autowired
  private StatService statService;

  @Setter
  private Game game;

  @Override
  public void run() {
    log.trace("run game={}", game);

    ResponseOperation gameNotification = gameNotifier.createGameChangedNotification(game);

    for (Player player : game.getPlayers()) {
      gameNotifier.sendToClient(player, gameNotification);

      updateUserIfFinished(player);
    }

    notifyGameServiceIfFinished();
  }

  private void updateUserIfFinished(Player player) {
    if (game.isFinished()) {
      User user = userService.get(player.getId());

      updateUser(player, user, game);
      userService.update(user);

      clientNotifier.sendSelfUserInfo(user);
    }

    if (game.isWin()) {
      statService.incCounter(MetricsConsts.GAME.WIN_ALL_STAT);
      statService.incCounter(MetricsConsts.GAME.WIN_CONCRETE_STAT + game.getQuestType());
      if (game.getQuestType() == QuestType.MISSION) {
        statService.incCounter(MetricsConsts.GAME.WIN_CONCRETE_STAT + game.getQuestType() + MetricsConsts.SEPARATOR + game.getMissionNumber());
      }
    }
  }

  private void updateUser(Player player, User user, Game game) {
    user.setScore(findNewUserScore(player, user));

    if (game.isWin()) {
      user.setWins(user.getWins() + 1);

      if (game.getQuestType() == QuestType.MISSION) {
        user.setMission(user.getMission() + 1);
      }
    } else {
      user.setDefeats(user.getDefeats() + 1);
    }

    user.setLastGameEnded(Instant.now().toEpochMilli());
  }

  private int findNewUserScore(Player player, User user) {
    int newUserScore = user.getScore() + player.getAddedScore();

    if (newUserScore < AppConfigs.MIN_USER_SCORE) {
      newUserScore = AppConfigs.MIN_USER_SCORE;
    }
    return newUserScore;
  }

  private void notifyGameServiceIfFinished() {
    if (game.isFinished()) {
      userGames.gameFinished(game);
    }
  }

}
