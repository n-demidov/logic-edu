package com.demidovn.fruitbounty.game.services.game.rules.ending;


import com.demidovn.fruitbounty.game.model.Pair;
import com.demidovn.fruitbounty.gameapi.model.backend.QuestType;
import java.util.HashMap;
import java.util.Map;

public class FixedAddedScoreCalculator {

  private static final int DEFAULT_MODIFIER = 5;
  private static final int WINNER_GREATER_SCORE_MODIFIER = 100;
  private static final int WINNER_LESS_SCORE_MODIFIER = 75;
  private static final int MIN_SCORE_PER_WIN = 1;

  private static final Map<QuestType, Pair<Integer, Integer>> scoresByQuestType = new HashMap<>();

  static {
    scoresByQuestType.put(QuestType.TABLE_1, new Pair<>(3, -1));
    scoresByQuestType.put(QuestType.TABLE_2, new Pair<>(8, -2));
    scoresByQuestType.put(QuestType.TABLE_3, new Pair<>(20, -4));
    scoresByQuestType.put(QuestType.TABLE_3_2, new Pair<>(20, -4));
  }

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

  public int getWinScore(QuestType questType) {
    return scoresByQuestType.get(questType).getKey();
  }

  public int getLooseScore(QuestType questType) {
    return scoresByQuestType.get(questType).getValue();
  }

}
