package com.demidovn.fruitbounty.gameapi.model;

public enum GameActionType {

  ANSWER,
  SURRENDER,
  HOOK,
  NEXT_QUEST;

  public static boolean contains(String value) {
    for (GameActionType gameActionType : GameActionType.values()) {
      if (gameActionType.name().equals(value)) {
        return true;
      }
    }

    return false;
  }

}
