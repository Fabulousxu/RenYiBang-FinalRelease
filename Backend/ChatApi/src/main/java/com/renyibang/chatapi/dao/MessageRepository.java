package com.renyibang.chatapi.dao;

import com.renyibang.chatapi.entity.Message;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
  Page<Message> findByChatIdAndCreatedAtBeforeOrderByCreatedAtDesc(
      String chatId, LocalDateTime createdAt, Pageable pageable);
}
