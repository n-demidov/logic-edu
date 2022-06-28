package com.demidovn.fruitbounty.game.services.game.rules;

import com.demidovn.fruitbounty.gameapi.model.Cell;
import com.demidovn.fruitbounty.gameapi.model.Game;
import com.demidovn.fruitbounty.gameapi.model.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractGameRules {

  protected final Random rand = new Random();
  private final CellsFinder cellsFinder = new CellsFinder();


}
