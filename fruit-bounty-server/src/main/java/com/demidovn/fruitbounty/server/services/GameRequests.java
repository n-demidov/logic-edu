package com.demidovn.fruitbounty.server.services;

import com.demidovn.fruitbounty.gameapi.model.Game;
import com.demidovn.fruitbounty.server.MetricsConsts;
import com.demidovn.fruitbounty.server.dto.operations.GameRequestInfo;
import com.demidovn.fruitbounty.server.persistence.entities.User;
import com.demidovn.fruitbounty.server.services.game.GameNotifier;
import com.demidovn.fruitbounty.server.services.game.UserGames;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.demidovn.fruitbounty.server.services.metrics.StatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GameRequests {

  @Autowired
  private UserGames userGames;

  @Autowired
  private ConnectionService connectionService;

  @Autowired
  private GameNotifier gameNotifier;

  @Autowired
  private UserService userService;

  @Autowired
  private StatService statService;

  private final Map<Long, GameRequestInfo> gameRequests = new ConcurrentHashMap<>();

  public void userSubmitRequest(GameRequestInfo gameRequestInfo) {
    if (userGames.isUserPlaying(gameRequestInfo.getUserId())) {
      return;
    }

    gameRequests.put(gameRequestInfo.getUserId(), gameRequestInfo);
  }

  public void userCancelRequest(GameRequestInfo gameRequestInfo) {
    gameRequests.remove(gameRequestInfo.getUserId());
  }

  public void processGameRequests() {
    clearNotActualRequests();

    log.trace("Starting of processGameRequests, gameRequests={}", gameRequests.size());

    for (GameRequestInfo gameRequestInfo : gameRequests.values()) {
      processUserRequest(gameRequestInfo);
    }
  }

  private void clearNotActualRequests() {
    Iterator<Long> users = gameRequests.keySet().iterator();
    while (users.hasNext()) {
      Long userId = users.next();

      if (userGames.isUserPlaying(userId) || !connectionService.isUserOnline(userId)) {
        users.remove();
        gameRequests.remove(userId);
      }
    }
  }

  private void processUserRequest(GameRequestInfo gameRequestInfo) {
    User user = userService.get(gameRequestInfo.getUserId());

    log.debug("Creating game for userId={}, mission={}", gameRequestInfo.getUserId(), user.getMission());
    gameRequests.remove(gameRequestInfo.getUserId());

    Game game = userGames.startGame(
            Collections.singletonList(gameRequestInfo.getUserId()),
            gameRequestInfo.getQuestType(),
            user.getMission());
    gameNotifier.notifyThatGameStarted(game);

    statService.incCounter(MetricsConsts.GAME.ALL_STAT);
    statService.incCounter(MetricsConsts.GAME.CONCRETE_STAT + game.getQuestType());
  }

}
