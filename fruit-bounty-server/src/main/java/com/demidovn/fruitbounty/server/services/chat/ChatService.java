package com.demidovn.fruitbounty.server.services.chat;

import com.demidovn.fruitbounty.server.AppConstants;
import com.demidovn.fruitbounty.server.MetricsConsts;
import com.demidovn.fruitbounty.server.dto.operations.response.ResponseOperation;
import com.demidovn.fruitbounty.server.dto.operations.response.ResponseOperationType;
import com.demidovn.fruitbounty.server.entities.Connection;
import com.demidovn.fruitbounty.server.persistence.entities.User;
import com.demidovn.fruitbounty.server.persistence.entities.chat.Chat;
import com.demidovn.fruitbounty.server.persistence.entities.chat.ChatValue;
import com.demidovn.fruitbounty.server.persistence.entities.chat.ChatValues;
import com.demidovn.fruitbounty.server.persistence.repository.ChatRepository;
import com.demidovn.fruitbounty.server.services.ConnectionService;
import com.demidovn.fruitbounty.server.services.UserService;
import com.demidovn.fruitbounty.server.services.metrics.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Component
public class ChatService {
    private static final String MESSAGE_FORMAT = "[%s] %s: %s";
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatHub chatHub;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private StatService statService;

    private static final Object lock = new Object();

    public void addNewMassage(String chatMessage, Connection senderConnection) {
        sendToAll(chatMessage, senderConnection);
    }

    private void sendToAll(String chatMessage, Connection senderConnection) {
        User user = userService.get(senderConnection.getUserId());
        String currentDateTime = getCurrentDateTime();
        String message = formatMessage(chatMessage, user, currentDateTime);

        chatHub.push(message);
        sendMessageToTopic(message);
        storeChatMessage(message, user, currentDateTime);

        statService.incCounter(MetricsConsts.OTHER.CHAT_SENT_STAT);
    }

    private String getCurrentDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.format(timeFormatter);
    }

    private String formatMessage(String message, User user, String currentDateTime) {
        return String.format(
                MESSAGE_FORMAT,
                currentDateTime,
                user.getPublicName(),
                message);
    }

    private void sendMessageToTopic(String message) {
        ResponseOperation responseOperation = new ResponseOperation(
                ResponseOperationType.SendChat, Collections.singletonList(message));
        connectionService.sendToAll(responseOperation);
    }

    private void storeChatMessage(String message, User user, String currentDateTime) {
        // todo: extract to async thread-pool
        synchronized (lock) {
            Chat chat = loadDbChat();
            addChatMessage(message, user, chat, currentDateTime);
            chatRepository.save(chat);
        }
    }

    private Chat loadDbChat() {
        // Keep all in one row because free DB has a limit of rows :)
        Chat result = chatRepository.findOne(ChatRepository.CHAT_SPECIAL_ROW_ID);
        if (result == null) {
            result = new Chat();
            result.setChatValues(new ChatValues());
        }
        return result;
    }

    private void addChatMessage(String msg, User user, Chat chat, String currentDateTime) {
        ChatValue chatValue = new ChatValue(
                String.valueOf(user.getId()),
                user.getPublicName(),
                currentDateTime,
                msg
        );

        List<ChatValue> chatValues = chat.getChatValues().getChatValues();
        chatValues.add(chatValue);

        if (chatValues.size() > AppConstants.MAX_CHAT_MESSAGES_IN_DB) {
            chatValues.remove(0);
        }
    }

}
