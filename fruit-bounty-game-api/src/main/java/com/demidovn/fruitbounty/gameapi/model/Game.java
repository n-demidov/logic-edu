package com.demidovn.fruitbounty.gameapi.model;

import com.demidovn.fruitbounty.gameapi.model.backend.QuestType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Data;

@Data
public class Game {

  private final QuestType questType;

  @JsonIgnore
  private int missionNumber;  // Only for log on backend

  @JsonIgnore
  private String rightAnswer;

  @JsonIgnore
  private final Queue<GameAction> gameActions = new ConcurrentLinkedQueue<>();

  @JsonIgnore
  private final int baseRating;

  @JsonIgnore
  private final List<Object> generatedMiniquests = new ArrayList<>();  // Only for log

  // Both: backend and client //
  private final List<Player> players;
  private List<Hook> hooks;

  private final long timePerGameMs;
  @JsonIgnore
  private long currentMoveStarted = Instant.now().toEpochMilli();
  private long clientCurrentMoveTimeLeft;

  private boolean isFinished;
  private boolean win;

  // Only client vars
  private List<String> answerOptions;
  private boolean showTables;
  private List<List<List<String>>> tables;
  private List<String> pages = new ArrayList<>();

  public Game(QuestType questType, int baseRating, List<Player> players, long timePerGameMs) {
    this.questType = questType;
    this.baseRating = baseRating;
    this.players = players;
    this.timePerGameMs = timePerGameMs;
  }

  public Player getCurrentPlayer() {
    return players.get(0);
  }

  @Override
  public String toString() {
    return "Game{" +
            "questType=" + questType +
            ", missionNumber=" + missionNumber +
            ", rightAnswer='" + rightAnswer + '\'' +
            ", generatedMiniquests=" + generatedMiniquests +
            ", players=" + players +
            ", timePerGameMs=" + timePerGameMs +
            ", currentMoveStarted=" + currentMoveStarted +
            ", clientCurrentMoveTimeLeft=" + clientCurrentMoveTimeLeft +
            ", isFinished=" + isFinished +
            ", win=" + win +
            ", answerOptions=" + answerOptions +
            ", pages=" + pages +
            '}';
  }

//  public String toFullString() {
//    return "Game{" +
//            "questType=" + questType +
//            ", players=" + players +
//            ", missionNumber=" + missionNumber +
//            ", timePerMoveMs=" + timePerMoveMs +
//            ", currentMoveStarted=" + currentMoveStarted +
//            ", clientCurrentMoveTimeLeft=" + clientCurrentMoveTimeLeft +
//            ", isFinished=" + isFinished +
//            ", win=" + win +
//            ", gameActions=" + gameActions +
//            '}';
//  }
}
