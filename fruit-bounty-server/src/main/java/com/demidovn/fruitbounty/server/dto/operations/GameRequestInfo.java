package com.demidovn.fruitbounty.server.dto.operations;

import com.demidovn.fruitbounty.gameapi.model.backend.QuestType;
import lombok.Data;

@Data
public class GameRequestInfo {

    private final long userId;
    private final QuestType questType;
    private long iterations;

    public GameRequestInfo(long userId, QuestType questType) {
        this.userId = userId;
        this.questType = questType;
    }

}
