package com.renyibang.chatapi.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.chatapi.dao.ChatRepository;
import com.renyibang.chatapi.dao.MessageRepository;
import com.renyibang.chatapi.entity.Chat;
import com.renyibang.chatapi.entity.Message;
import com.renyibang.chatapi.service.impl.ChatServiceImpl;
import com.renyibang.global.client.ServiceClient;
import com.renyibang.global.client.TaskClient;
import com.renyibang.global.client.UserClient;
import com.renyibang.global.util.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ChatServiceTest {
  @InjectMocks private ChatServiceImpl chatService;
  @Mock private ChatRepository chatRepository;
  @Mock private MessageRepository messageRepository;
  @Mock private UserClient userClient;
  @Mock private TaskClient taskClient;
  @Mock private ServiceClient serviceClient;
  private Chat taskChat;
  private Message message;

  @BeforeEach
  public void setUp() {
    taskChat = new Chat((byte) 0, 1, 1, 2);
    taskChat.setChatId("1");
    taskChat.setLastMessageCreatedAt(LocalDateTime.now());
    Chat serviceChat = new Chat((byte) 1, 1, 1, 2);
    serviceChat.setChatId("2");
    serviceChat.setLastMessageCreatedAt(LocalDateTime.now());
    List<Chat> chats = new ArrayList<>();
    chats.add(taskChat);
    chats.add(serviceChat);
    message = new Message("1", 1, "");
    message.setMessageId("1");
    message.setCreatedAt(LocalDateTime.now());
    Page<Message> messages = new PageImpl<>(List.of(message));
    when(chatRepository.findByOfOwnerIdOrChatterIdOrderByLastMessageCreatedAtDesc(
            anyLong(), anyLong()))
        .thenReturn(chats);
    when(messageRepository.findByChatIdAndCreatedAtBeforeOrderByCreatedAtDesc(
            anyString(), any(LocalDateTime.class), any(Pageable.class)))
        .thenReturn(messages);
  }

  @Test
  public void getChatsTest1() throws Exception {
    when(userClient.getUserInfo(anyLong()))
        .thenReturn(JSONObject.of("ok", true, "data", new JSONObject()));
    Response res = chatService.getChats(1);
  }

  @Test
  public void getChatsTest2() throws Exception {
    when(userClient.getUserInfo(anyLong())).thenReturn(JSONObject.of("ok", false));
    Response res = chatService.getChats(2);
  }

  @Test
  public void initiateChatTest1() throws Exception {
    when(chatRepository.findByTypeAndOfIdAndChatterId(anyByte(), anyLong(), anyLong()))
        .thenReturn(Optional.ofNullable(taskChat));
    Response res = chatService.initiateChat(1, (byte) 0, 1);
  }

  @Test
  public void initiateChatTest2() throws Exception {
    when(chatRepository.findByTypeAndOfIdAndChatterId(anyByte(), anyLong(), anyLong()))
        .thenReturn(Optional.empty());
    when(taskClient.getTaskOwnerId(anyLong())).thenReturn(JSONObject.of("ok", true, "data", 1));
    Response res = chatService.initiateChat(1, (byte) 0, 1);
  }

  @Test
  public void initiateChatTest3() throws Exception {
    when(chatRepository.findByTypeAndOfIdAndChatterId(anyByte(), anyLong(), anyLong()))
        .thenReturn(Optional.empty());
    when(taskClient.getTaskOwnerId(anyLong())).thenReturn(JSONObject.of("ok", false));
    Response res = chatService.initiateChat(1, (byte) 0, 1);
  }

  @Test
  public void initiateChatTest4() throws Exception {
    when(chatRepository.findByTypeAndOfIdAndChatterId(anyByte(), anyLong(), anyLong()))
        .thenReturn(Optional.empty());
    when(serviceClient.getServiceOwnerId(anyLong()))
        .thenReturn(JSONObject.of("ok", true, "data", 1));
    Response res = chatService.initiateChat(1, (byte) 1, 1);
  }

  @Test
  public void getChatHistoryTest1() throws Exception {
    when(chatRepository.findById(anyString())).thenReturn(Optional.ofNullable(taskChat));
    when(messageRepository.findById(anyString())).thenReturn(Optional.ofNullable(message));
    Response res = chatService.getChatHistory(1, "1", "1", 1);
  }

  @Test
  public void getChatHistoryTest2() throws Exception {
    when(chatRepository.findById(anyString())).thenReturn(Optional.ofNullable(taskChat));
    when(messageRepository.findById(anyString())).thenReturn(Optional.empty());
    Response res = chatService.getChatHistory(2, "1", "1", 1);
  }

  @Test
  public void getChatHistoryTest3() throws Exception {
    when(chatRepository.findById(anyString())).thenReturn(Optional.ofNullable(taskChat));
    when(messageRepository.findById(anyString())).thenReturn(Optional.ofNullable(message));
    Response res = chatService.getChatHistory(3, "1", "1", 1);
  }

  @Test
  public void getChatHistoryTest4() throws Exception {
    when(chatRepository.findById(anyString())).thenReturn(Optional.empty());
    Response res = chatService.getChatHistory(1, "1", "1", 1);
  }

  @Test
  public void getChatHistoryTest5() throws Exception {
    when(chatRepository.findById(anyString())).thenReturn(Optional.ofNullable(taskChat));
    when(messageRepository.findById(anyString())).thenReturn(Optional.ofNullable(message));
    Response res = chatService.getChatHistory(1, "1", "", 1);
  }

  @Test
  public void getChatHistoryTest6() throws Exception {
    when(chatRepository.findById(anyString())).thenReturn(Optional.ofNullable(taskChat));
    when(messageRepository.findById(anyString())).thenReturn(Optional.ofNullable(message));
    Response res = chatService.getChatHistory(2, "1", "", 1);
  }
}
