package com.renyibang.chatapi.component;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.chatapi.dao.ChatRepository;
import com.renyibang.chatapi.dao.MessageRepository;
import com.renyibang.chatapi.entity.Chat;
import com.renyibang.chatapi.entity.Message;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
  private final Map<Long, Set<WebSocketSession>> userSessionMap = new ConcurrentHashMap<>();
  private final Map<WebSocketSession, Long> sessionUserMap = new ConcurrentHashMap<>();
  @Autowired private ChatRepository chatRepository;
  @Autowired private MessageRepository messageRepository;

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    JSONObject json = JSONObject.parseObject(message.getPayload());
    if (json.getString("type") != null && json.getString("type").equals("register")) {
      long userId = json.getLongValue("userId");
      var userSessionSet = userSessionMap.getOrDefault(userId, new HashSet<>());
      userSessionSet.add(session);
      userSessionMap.put(userId, userSessionSet);
      sessionUserMap.put(session, userId);
      return;
    }

    String chatId = json.getString("chatId");
    Chat chat = chatRepository.findById(chatId).orElse(null);
    if (chat == null) return;
    long senderId = sessionUserMap.get(session);
    long receiverId = senderId == chat.getOfOwnerId() ? chat.getChatterId() : chat.getOfOwnerId();
    var senderSessionList = userSessionMap.get(senderId);
    var receiverSessionList = userSessionMap.getOrDefault(receiverId, new HashSet<>());
    Message chatMessage = new Message(chatId, senderId, json.getString("content"));
    messageRepository.save(chatMessage);
    if (receiverSessionList.isEmpty()) chat.setUnreadCount(chat.getUnreadCount() + 1);
    chat.setLastMessageSenderId(senderId);
    chat.setLastMessageContent(chatMessage.getContent());
    chat.setLastMessageCreatedAt(chatMessage.getCreatedAt());
    chatRepository.save(chat);

    var messageJson = JSONObject.from(chatMessage);
    messageJson.put("chatId", chatId);
    var textMessage = new TextMessage(messageJson.toString());
    for (var senderSession : senderSessionList) senderSession.sendMessage(textMessage);
    for (var receiverSession : receiverSessionList) receiverSession.sendMessage(textMessage);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    long userId = sessionUserMap.get(session);
    var userSessionSet = userSessionMap.get(userId);
    userSessionSet.remove(session);
    if (userSessionSet.isEmpty()) userSessionMap.remove(userId);
    sessionUserMap.remove(session);
  }
}
