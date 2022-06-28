package com.demidovn.fruitbounty.server.services.operations.handlers;

import com.demidovn.fruitbounty.gameapi.model.Game;
import com.demidovn.fruitbounty.server.MetricsConsts;
import com.demidovn.fruitbounty.server.dto.operations.request.OperationType;
import com.demidovn.fruitbounty.server.dto.operations.request.RequestOperation;
import com.demidovn.fruitbounty.server.persistence.entities.User;
import com.demidovn.fruitbounty.server.services.UserService;
import com.demidovn.fruitbounty.server.services.feedback.FeedbackService;
import com.demidovn.fruitbounty.server.services.game.UserGames;
import com.demidovn.fruitbounty.server.services.metrics.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SendFeedbackOperationHandler implements OperationHandler {

  @Autowired
  @Qualifier("serverConversionService")
  private ConversionService conversionService;

  @Autowired
  private UserService userService;

  @Autowired
  private UserGames userGames;

  @Autowired
  private FeedbackService feedbackService;

  @Autowired
  private StatService statService;

  @Override
  public OperationType getOperationType() {
    return OperationType.SendFeedback;
  }

  @Override
  public void process(RequestOperation requestOperation) {
    statService.incCounter(MetricsConsts.OTHER.FEEDBACK_SENT_STAT);

    String msg = conversionService.convert(requestOperation, String.class);
    User user = userService.get(requestOperation.getConnection().getUserId());
    Optional<Game> game = userGames.getCurrentGame(user.getId());

    feedbackService.addFeedback(msg, user, game);
  }

}
