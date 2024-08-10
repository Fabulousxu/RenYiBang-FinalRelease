package com.renyibang.chatapi.dao;

import com.renyibang.chatapi.entity.Chat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
  List<Chat> findByOfOwnerIdOrChatterIdOrderByLastMessageCreatedAtDesc(
      long ofOwnerId, long chatterId);

  Optional<Chat> findByTypeAndOfIdAndChatterId(byte type, long ofId, long chatterId);
}
