package com.renyibang.chatapi.service;

import com.renyibang.global.util.Response;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {
  Response getChats(long userId);

  Response initiateChat(long userId, byte type, long ofId);

  Response getChatHistory(long userId, String chatId, String lastMessageId, int count);
}
