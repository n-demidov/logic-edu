package com.demidovn.fruitbounty.game;

import com.demidovn.fruitbounty.game.model.quest.NonTargetAnswers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameOptions {

  public static final String MISSIONS_CONFIG = "game/missions.yaml";
  public static final String COMMON_SOURCES_CONFIG = "game/common_sources.yaml";
  public static final String BRIEF_1_CONFIG = "game/brief_1.yaml";
  public static final String TABLE_1_CONFIG = "game/table_1.yaml";
  public static final String TABLE_2_CONFIG = "game/table_2.yaml";
  public static final String TABLE_3_CONFIG = "game/table_3.yaml";
  public static final String TABLE_3_2_CONFIG = "game/table_3_2.yaml";
  public static final String BRIEF_2_CONFIG = "game/brief_2.yaml";
  public static final String BRIEF_3_CONFIG = "game/brief_3.yaml";
  public static final String BRIEF_100_CONFIG = "game/brief_100.yaml";
  public static final String COMPOSITE_SOURCES_CONFIG = "game/sources.yaml";
  public static final String COMPOSITE_CONFIG = "game/composite.yaml";
  public static final String COMPOSITE2_CONFIG = "game/composite2.yaml";

  public static int INFINITY_REPEATS_LIMIT = -1;
  public static int MAX_COMPLEXITY_COUNTER = 6;

  public static int GENERATE_QUESTS_TRIES_LIMIT = 100;
  public static int GENERATE_QUESTS_SENTENCES_TRIES_LIMIT = 100;
  public static int GENERATE_MISSIONS_TRIES_LIMIT = 100;

  public static final String CONFIG_PROPERTIES = "config.properties";

  public static class GameRating {
    public static int MISSION = 0;
    public static int BRIEF_1 = 5;
    public static int BRIEF_2 = 15;
    public static int BRIEF_3 = 30;
    public static int BRIEF_100 = 500;
    public static int TABLE_1 = 3;
    public static int TABLE_2 = 8;
    public static int TABLE_3 = 16;
    public static int COMPOSITE = 2000;
  }

  public static class Globalquest_1 {
    public static int MIN_SUBJECTS = 3;
    public static int MAX_SUBJECTS = 3;
    public static int MIN_OBJECTS_GROUPS = 2;
    public static int MAX_OBJECTS_GROUPS = 2;
    public static int MAX_OBJECTS = 3;
  }

  public static class Globalquest_2 {
    public static int MIN_SUBJECTS = 2;
    public static int MAX_SUBJECTS = 5;
    public static int MIN_OBJECTS_GROUPS = 1;
    public static int MAX_OBJECTS_GROUPS = 3;
    public static int MAX_OBJECTS = 6;
  }

  public static class SubquestGeneral {
    public static int repeatsLimit = GameOptions.INFINITY_REPEATS_LIMIT;
    public static int rightAnswersNum = 1;
    public static NonTargetAnswers nonTargetAnswers = NonTargetAnswers.ONLY_ONE_ANSWER_FOR_MENTIONED_IN_STATEMENTS;
    public static int minAnswerInfers = 0;
    public static int maxAnswerInfers = 99;
  }

  public static class Subquest1 {
    public static int minSentencesNum = 2;
    public static int maxSentencesNum = 5;
    public static int minStatementsInSentence = 1;
    public static int maxStatementsInSentence = 1;
  }

  public static class Subquest2 {
    public static int minSentencesNum = 1;
    public static int maxSentencesNum = 3;
    public static int minStatementsInSentence = 1;
    public static int maxStatementsInSentence = 3;
  }

  public static final int CELL_TYPES_COUNT = 9;
  public static final int CELL_TYPES_MIN = 1;

  public static final String GAME_LOOP_SCHEDULE_DELAY = "30";
  public static final int MOVE_TIME_DELAY_CORRECTION = 800;

  public static class TimeGameMs {
    public static final int DEFAULT = 1000 * 60 * 20;
    public static final int COMPOSITE = 1000 * 60 * 120;
  }

  public static final int MAX_GAME_MISSED_MOVES = 3;

  public static final List<Integer> ALL_CELL_TYPES;

  static {
    List<Integer> possibleCellTypes = new ArrayList<>();

    for (int i = GameOptions.CELL_TYPES_MIN; i <= GameOptions.CELL_TYPES_COUNT; i++) {
      possibleCellTypes.add(i);
    }

    ALL_CELL_TYPES = Collections.unmodifiableList(possibleCellTypes);
  }

}
