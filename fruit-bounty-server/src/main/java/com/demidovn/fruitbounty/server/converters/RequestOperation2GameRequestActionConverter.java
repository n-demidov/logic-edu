package com.demidovn.fruitbounty.server.converters;

import com.demidovn.fruitbounty.server.dto.operations.request.GameRequestAction;
import com.demidovn.fruitbounty.gameapi.model.backend.QuestType;
import com.demidovn.fruitbounty.server.dto.operations.request.RequestOperation;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class RequestOperation2GameRequestActionConverter
  implements FruitServerConverter<RequestOperation, GameRequestAction> {

  private static final String GAME_REQUEST_ACTION_TYPE = "ack";
  private static final String SUBMIT_GAME_REQUEST_VALUE = "y";
  private static final String GAME_TYPE = "gameType";

  @Override
  public GameRequestAction convert(RequestOperation requestOperation) {
    String ackRaw = requestOperation.getData().get(GAME_REQUEST_ACTION_TYPE);
    boolean ack = Objects.equals(ackRaw, SUBMIT_GAME_REQUEST_VALUE);

    String gameTypeRaw = requestOperation.getData().get(GAME_TYPE);
    QuestType questType = null;
    if (ack) {
      questType = QuestType.valueOf(gameTypeRaw);
    }

    return new GameRequestAction(ack, questType);
  }

}
