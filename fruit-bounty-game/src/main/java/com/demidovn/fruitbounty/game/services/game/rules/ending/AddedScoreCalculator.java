package com.demidovn.fruitbounty.game.services.game.rules.ending;


public class AddedScoreCalculator {

  private static final int DEFAULT_MODIFIER = 5;
  private static final int WINNER_GREATER_SCORE_MODIFIER = 100;
  private static final int WINNER_LESS_SCORE_MODIFIER = 75;
  private static final int MIN_SCORE_PER_WIN = 1;

  public int findWinnerAddedScore(int winnerScore, int looserScore) {
    int playersDiffScore = Math.abs(winnerScore - looserScore);

    if (winnerScore > looserScore) {
      int playersDiffLevel = playersDiffScore / WINNER_GREATER_SCORE_MODIFIER;
      int addedScore = DEFAULT_MODIFIER - playersDiffLevel;
      if (addedScore < MIN_SCORE_PER_WIN) {
        addedScore = MIN_SCORE_PER_WIN;
      }

      return addedScore;
    } else {
      int playersDiffLevel = playersDiffScore / WINNER_LESS_SCORE_MODIFIER;

      return DEFAULT_MODIFIER + playersDiffLevel;
    }
  }
  
}
