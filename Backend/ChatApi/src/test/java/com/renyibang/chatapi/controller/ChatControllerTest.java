package com.renyibang.chatapi.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.renyibang.chatapi.service.ChatService;
import com.renyibang.global.util.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ChatControllerTest {
  private MockMvc mockMvc;
  @InjectMocks private ChatController chatController;
  @Mock private ChatService chatService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();
  }

  @Test
  public void getChatsTest() throws Exception {
    when(chatService.getChats(anyLong())).thenReturn(Response.success());
    mockMvc
        .perform(get("/api/chat/list").header("userId", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void initiateChatTest1() throws Exception {
    when(chatService.initiateChat(anyLong(), anyByte(), anyLong())).thenReturn(Response.success());
    mockMvc
        .perform(post("/api/chat/enter/task/1").header("userId", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void initiateChatTest2() throws Exception {
    when(chatService.initiateChat(anyLong(), anyByte(), anyLong())).thenReturn(Response.success());
    mockMvc
        .perform(post("/api/chat/enter/service/1").header("userId", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void initiateChatTest3() throws Exception {
    when(chatService.initiateChat(anyLong(), anyByte(), anyLong())).thenReturn(Response.success());
    mockMvc
        .perform(post("/api/chat/enter/other/1").header("userId", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(false));
  }

  @Test
  public void getChatHistoryTest() throws Exception {
    when(chatService.getChatHistory(anyLong(), anyString(), anyString(), anyInt()))
        .thenReturn(Response.success());
    mockMvc
        .perform(
            get("/api/chat/history")
                .param("chatId", "1")
                .param("lastMessageId", "1")
                .param("count", "1")
                .header("userId", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }
}
