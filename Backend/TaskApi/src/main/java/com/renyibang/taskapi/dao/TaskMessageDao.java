package com.renyibang.taskapi.dao;

import com.renyibang.taskapi.entity.TaskMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskMessageDao {
  Page<TaskMessage> getTaskMessages(long taskId, Pageable pageable);

  String likeMessageByTaskMessageId(long taskMessageId, long likerId);

  String unlikeMessageByTaskMessageId(long taskMessageId, long unlikerId);

  String putMessage(long taskId, long userId, String content);

  String deleteMessage(long taskMessageId, long userId);

  boolean isLiked(long taskMessageId, long likerId);
}
