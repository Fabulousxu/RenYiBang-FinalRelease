package com.renyibang.chatapi.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.renyibang.chatapi.dao.ChatRepository;
import com.renyibang.chatapi.dao.MessageRepository;
import com.renyibang.chatapi.entity.Chat;
import com.renyibang.chatapi.entity.Message;
import com.renyibang.chatapi.service.ChatService;
import com.renyibang.global.client.ServiceClient;
import com.renyibang.global.client.TaskClient;
import com.renyibang.global.client.UserClient;
import com.renyibang.global.util.Response;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {
  @Autowired private ChatRepository chatRepository;
  @Autowired private MessageRepository messageRepository;
  @Autowired private UserClient userClient;
  @Autowired private TaskClient taskClient;
  @Autowired private ServiceClient serviceClient;

  private JSONObject getChatJson(Chat chat, long userId) {
    long chatterId = chat.getOfOwnerId() == userId ? chat.getChatterId() : chat.getOfOwnerId();
    JSONObject chatterJson = new JSONObject();
    JSONObject res = userClient.getUserInfo(chatterId);
    if (res.getBooleanValue("ok")) chatterJson = res.getJSONObject("data");
    JSONObject chatJson = JSONObject.from(chat);
    chatJson.put("type", chat.getType() == 0 ? "task" : "service");
    chatJson.put("ofId", chat.getOfId());
    chatJson.put("chatter", chatterJson);
    if (userId == chat.getLastMessageSenderId()) chatJson.put("unreadCount", 0);
    return chatJson;
  }

  @Override
  public Response getChats(long userId) {
    List<Chat> chats =
        chatRepository.findByOfOwnerIdOrChatterIdOrderByLastMessageCreatedAtDesc(userId, userId);
    chats.sort((a, b) -> b.getLastMessageCreatedAt().compareTo(a.getLastMessageCreatedAt()));
    JSONArray chatArray = new JSONArray();
    for (Chat chat : chats) chatArray.add(getChatJson(chat, userId));
    JSONObject selfJson = new JSONObject();
    JSONObject res = userClient.getUserInfo(userId);
    if (res.getBooleanValue("ok")) selfJson = res.getJSONObject("data");
    return Response.success(JSONObject.of("self", selfJson, "chats", chatArray));
  }

  @Override
  public Response initiateChat(long userId, byte type, long ofId) {
    Chat chat = chatRepository.findByTypeAndOfIdAndChatterId(type, ofId, userId).orElse(null);
    if (chat == null) {
      JSONObject res =
          type == 0 ? taskClient.getTaskOwnerId(ofId) : serviceClient.getServiceOwnerId(ofId);
      if (!res.getBooleanValue("ok")) return Response.error(res.getString("message"));
      long ofOwnerId = res.getLongValue("data");
      chat = new Chat(type, ofId, ofOwnerId, userId);
      chatRepository.save(chat);
    }
    return Response.success(JSONObject.of("chatId", chat.getChatId()));
  }

  @Override
  public Response getChatHistory(long userId, String chatId, String lastMessageId, int count) {
    Chat chat = chatRepository.findById(chatId).orElse(null);
    if (chat == null) return Response.error("聊天不存在");
    if (chat.getOfOwnerId() != userId && chat.getChatterId() != userId)
      return Response.error("无权查看聊天记录");
    LocalDateTime lastMessageCreatedAt = null;
    if (lastMessageId.isEmpty()) {
      lastMessageCreatedAt = LocalDateTime.now();
      if (chat.getLastMessageSenderId() != userId) {
        chat.setUnreadCount(0);
        chatRepository.save(chat);
      }
    } else {
      Message lastMessage = messageRepository.findById(lastMessageId).orElse(null);
      lastMessageCreatedAt = lastMessage == null ? LocalDateTime.now() : lastMessage.getCreatedAt();
    }
    Page<Message> messages =
        messageRepository.findByChatIdAndCreatedAtBeforeOrderByCreatedAtDesc(
            chatId, lastMessageCreatedAt, PageRequest.of(0, count));
    return Response.success(messages.stream().toList());
  }
}
