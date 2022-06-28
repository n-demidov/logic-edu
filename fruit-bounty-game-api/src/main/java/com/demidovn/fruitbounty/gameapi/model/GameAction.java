package com.demidovn.fruitbounty.gameapi.model;

import lombok.Data;

@Data
public class GameAction {

  private Game game;
  private long actionedPlayerId;
  private long createdTime;

  private GameActionType type;
  private String answer;
  private String hook;

}
