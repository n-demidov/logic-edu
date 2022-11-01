package com.demidovn.fruitbounty.server.services.operations.handlers;

import com.demidovn.fruitbounty.server.dto.operations.request.OperationType;
import com.demidovn.fruitbounty.server.dto.operations.request.RequestOperation;
import com.demidovn.fruitbounty.server.services.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import org.springframework.web.util.HtmlUtils;

@Component
public class SendChatOperationHandler implements OperationHandler {

  @Autowired
  @Qualifier("serverConversionService")
  private ConversionService conversionService;

  @Autowired
  private ChatService chatService;

  @Override
  public OperationType getOperationType() {
    return OperationType.SendChat;
  }

  @Override
  public void process(RequestOperation requestOperation) {
    String chatMessage = conversionService.convert(requestOperation, String.class);
    chatMessage = HtmlUtils.htmlEscape(chatMessage);

    chatService.addNewMassage(chatMessage, requestOperation.getConnection());
  }

}
