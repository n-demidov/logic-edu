package com.demidovn.fruitbounty.game.model.singleplay;

import com.demidovn.fruitbounty.game.GameOptions;
import com.demidovn.fruitbounty.gameapi.model.Game;
import com.demidovn.fruitbounty.gameapi.model.Player;
import com.demidovn.fruitbounty.gameapi.model.backend.QuestType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class CompositeGame extends Game {

    // Only backend vars //
    @JsonIgnore
    private final GameCompositeInfo gameCompositeInfo;

    @JsonIgnore
    private String globalAnswers;  // Only for log
    @JsonIgnore
    private List<List<Integer>> globalAnswersIdxs;  // Only for log

    public CompositeGame(QuestType questType, List<Player> players, GameCompositeInfo gameCompositeInfo, long timePerGame) {
        super(questType, GameOptions.GameRating.COMPOSITE, players, timePerGame);
        this.gameCompositeInfo = gameCompositeInfo;
    }

    public GameCompositeInfo getGameCompositeInfo() {
        return gameCompositeInfo;
    }

    @Override
    public String toString() {
        return super.toString() +
                " CompositeGame{" +
                "gameCompositeInfo=" + gameCompositeInfo +
                ", globalAnswers=" + globalAnswers +
                ", globalAnswersIdxs=" + globalAnswersIdxs +
                "} ";
    }
}
