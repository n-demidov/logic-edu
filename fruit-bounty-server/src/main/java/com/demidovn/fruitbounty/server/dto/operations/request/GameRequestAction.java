package com.demidovn.fruitbounty.server.dto.operations.request;

import com.demidovn.fruitbounty.gameapi.model.backend.QuestType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameRequestAction {

    private final boolean ack;
    private final QuestType questType;

}
