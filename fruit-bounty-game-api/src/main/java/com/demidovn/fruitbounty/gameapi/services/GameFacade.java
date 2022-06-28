package com.demidovn.fruitbounty.gameapi.services;

import com.demidovn.fruitbounty.gameapi.model.Game;
import com.demidovn.fruitbounty.gameapi.model.GameAction;
import com.demidovn.fruitbounty.gameapi.model.backend.GameDescription;

import java.util.List;

public interface GameFacade {

  Game startGame(GameDescription gameDescription);

  void processGameAction(GameAction gameAction);

  List<Game> getAllGames();

}
