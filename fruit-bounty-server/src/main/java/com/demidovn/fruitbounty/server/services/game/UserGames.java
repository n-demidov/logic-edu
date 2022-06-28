package com.demidovn.fruitbounty.server.services.game;

import com.demidovn.fruitbounty.gameapi.model.backend.QuestType;
import com.demidovn.fruitbounty.gameapi.model.Game;
import com.demidovn.fruitbounty.gameapi.model.GameAction;
import com.demidovn.fruitbounty.gameapi.model.Player;
import com.demidovn.fruitbounty.gameapi.model.backend.GameDescription;
import com.demidovn.fruitbounty.gameapi.services.GameFacade;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserGames {

  @Autowired
  @Qualifier("serverConversionService")
  private ConversionService conversionService;

  @Autowired
  private GameFacade gameFacade;

  private final Map<Long, Game> userGames = new ConcurrentHashMap<>();

  public Game startGame(List<Long> userIds, QuestType questType, int mission) {
    List<Player> players = convert2Players(userIds);

    Game createdGame = gameFacade.startGame(new GameDescription(players, questType, mission));

    updateUsersGame(userIds, createdGame);

    return createdGame;
  }

  public void processGameAction(GameAction gameAction) {
    gameFacade.processGameAction(gameAction);
  }

  public boolean isUserPlaying(long userId) {
    return userGames.containsKey(userId);
  }

  public Optional<Game> getCurrentGame(long userId) {
    return Optional.ofNullable(userGames.get(userId));
  }

  public void gameFinished(Game game) {
    for (Player player : game.getPlayers()) {
      userGames.remove(player.getId());
      log.trace("Game for user {} was removed; current userGames={}", player.getId(), userGames.size());
    }
  }

  public int countPlayingUsers() {
    return userGames.size();
  }

  private List<Player> convert2Players(List<Long> userIds) {
    return userIds.stream()
        .map(userId -> conversionService.convert(userId, Player.class))
        .collect(Collectors.toList());
  }

  private void updateUsersGame(List<Long> userIds, Game createdGame) {
    userIds
      .forEach(userId -> userGames.put(userId, createdGame));
  }
}
