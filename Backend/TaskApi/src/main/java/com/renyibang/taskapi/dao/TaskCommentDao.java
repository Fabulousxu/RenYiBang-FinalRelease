package com.renyibang.taskapi.dao;

import com.renyibang.taskapi.entity.TaskComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCommentDao {
  Page<TaskComment> getTaskComments(long taskId, Pageable pageable);

  String likeCommentByTaskCommentId(long taskCommentId, long likerId);

  String unlikeCommentByTaskCommentId(long taskCommentId, long unlikerId);

  String putComment(long taskId, long userId, String content, byte rating);

  String deleteComment(long taskCommentId, long userId);

  boolean isLiked(long taskCommentId, long likerId);
}
