package com.renyibang.chatapi.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@Document("chat")
public class Chat {
  @MongoId private String chatId;
  @Indexed private byte type; // 0: task, 1: service
  @Indexed private long ofId; // task or service id

  @Indexed
  @JsonIgnore
  @JSONField(serialize = false)
  private long ofOwnerId; // task or service owner id

  @Indexed
  @JsonIgnore
  @JSONField(serialize = false)
  private long chatterId; // chatter id

  private int unreadCount = 0;
  private long lastMessageSenderId;
  private String lastMessageContent = "";

  @CreatedDate
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @Indexed
  private LocalDateTime lastMessageCreatedAt;

  public Chat(byte type, long ofId, long ofOwnerId, long chatterId) {
    this.type = type;
    this.ofId = ofId;
    this.ofOwnerId = ofOwnerId;
    this.chatterId = chatterId;
    this.lastMessageSenderId = chatterId;
  }
}
