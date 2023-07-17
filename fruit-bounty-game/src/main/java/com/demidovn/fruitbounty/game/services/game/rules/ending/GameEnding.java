package com.demidovn.fruitbounty.game.services.game.rules.ending;

import com.demidovn.fruitbounty.gameapi.model.Game;
import com.demidovn.fruitbounty.gameapi.model.GameAction;
import com.demidovn.fruitbounty.gameapi.model.Player;
import com.demidovn.fruitbounty.gameapi.model.backend.QuestType;

public abstract class GameEnding {
  private final AddedScoreCalculator addedScoreCalculator = new AddedScoreCalculator();

  public void checkGameEndingByMoving(GameAction gameAction) {
    String answer = gameAction.getAnswer();
    Game game = gameAction.getGame();

    if (!game.getAnswerOptions().contains(answer)) {
      return;
    }

    if (game.getRightAnswer().equals(answer)) {
      game.setWin(true);
    } else {

    }

    game.setFinished(true);

    // score
    // todo: only singleplay
    Player player = game.getPlayers().get(0);
    int score = getScore(game, player);
    setScoreToPlayers(game, score);
  }

  protected void finishGame(Game game, Player winner) {

//    game.setFinished(true);
//    game.setWinner(winner);

    if (winner != null) {
//      Player looser = findLooser(players, winner);

//      int winnerAddedScore = addedScoreCalculator.findWinnerAddedScore(winner, looser);

//      winner.setAddedScore(winnerAddedScore);
//      looser.setAddedScore(-winnerAddedScore);
    }
  }

  private int getScore(Game game, Player player) {
    if (game.getQuestType() == QuestType.MISSION) {
      if (game.isWin()) {
        return game.getMissionNumber() + 1;
      } else {
        return 0;
      }
    } else {
      int winnerScore;
      int looserScore;

      if (game.isWin()) {
        winnerScore = player.getScore();
        looserScore = game.getBaseRating();
      } else {
        winnerScore = game.getBaseRating();
        looserScore = player.getScore();
      }

      int winnerAddedScore = addedScoreCalculator.findWinnerAddedScore(winnerScore, looserScore);
      if (game.isWin()) {
        return winnerAddedScore;
      } else {
        return -winnerAddedScore;
      }
    }
  }

  private void setScoreToPlayers(Game game, int score) {
    for (int i = 0; i < game.getPlayers().size(); i++) {
      Player player = game.getPlayers().get(i);
      player.setAddedScore(score);
    }
  }

}
