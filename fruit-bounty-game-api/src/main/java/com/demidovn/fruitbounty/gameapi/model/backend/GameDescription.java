package com.demidovn.fruitbounty.gameapi.model.backend;

import com.demidovn.fruitbounty.gameapi.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GameDescription {

    private List<Player> players;
    private QuestType questType;
    private int mission;

}
