package com.renyibang.taskapi.dao.daoImpl;

import com.renyibang.global.client.UserClient;
import com.renyibang.taskapi.dao.TaskCommentDao;
import com.renyibang.taskapi.entity.Task;
import com.renyibang.taskapi.entity.TaskComment;
import com.renyibang.taskapi.entity.TaskCommentLike;
import com.renyibang.taskapi.repository.TaskCommentLikeRepository;
import com.renyibang.taskapi.repository.TaskCommentRepository;
import com.renyibang.taskapi.repository.TaskRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class TaskCommentDaoImpl implements TaskCommentDao {
  @Autowired TaskRepository taskRepository;
  @Autowired TaskCommentLikeRepository taskCommentLikeRepository;
  @Autowired UserClient userClient;
  @Autowired private TaskCommentRepository taskCommentRepository;

  @Override
  public Page<TaskComment> getTaskComments(long taskId, Pageable pageable) {
    return taskCommentRepository.findByTaskTaskId(taskId, pageable);
  }

  @Override
  public String likeCommentByTaskCommentId(long taskCommentId, long likerId) {
    try {
      TaskComment taskComment = taskCommentRepository.findById(taskCommentId).orElse(null);
      if (taskComment == null) {
        return "评论不存在！";
      }

      if (!userClient.getUserExist(likerId)) {
        return "用户不存在！";
      }

      if (taskCommentLikeRepository.existsByLikerIdAndTaskComment(likerId, taskComment)) {
        return "用户已点赞过该评论！";
      } else {
        taskComment.setLikedNumber(taskComment.getLikedNumber() + 1);
        taskCommentRepository.save(taskComment);

        TaskCommentLike taskCommentLike = new TaskCommentLike();
        taskCommentLike.setLikerId(likerId);
        taskCommentLike.setTaskComment(taskComment);
        taskCommentLikeRepository.save(taskCommentLike);

        return "点赞成功！";
      }
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public String unlikeCommentByTaskCommentId(long taskCommentId, long unlikerId) {
    try {
      TaskComment taskComment = taskCommentRepository.findById(taskCommentId).orElse(null);
      if (taskComment == null) {
        return "评论不存在！";
      }

      if (!userClient.getUserExist(unlikerId)) {
        return "用户不存在！";
      }

      TaskCommentLike taskCommentLike =
          taskCommentLikeRepository.findByLikerIdAndTaskComment(unlikerId, taskComment);

      if (taskCommentLike == null) {
        return "用户未点赞过该评论！";
      } else {
        // 存在并发问题!!
        taskComment.setLikedNumber(taskComment.getLikedNumber() - 1);
        taskCommentRepository.save(taskComment);

        taskCommentLikeRepository.delete(taskCommentLike);
        return "取消点赞成功！";
      }
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public String putComment(long taskId, long userId, String content, byte rating) {
    try {
      if (!userClient.getUserExist(userId)) {
        return "用户不存在！";
      }

      Task task = taskRepository.findById(taskId).orElse(null);
      if (task == null) {
        return "任务不存在！";
      }

      if (taskCommentRepository.existsByTaskAndCommenterId(task, userId)) {
        return "用户已经评论过该任务！";
      }

      TaskComment taskComment = new TaskComment();
      taskComment.setCommenterId(userId);
      taskComment.setTask(task);
      taskComment.setContent(content);
      taskComment.setCreatedAt(LocalDateTime.now());
      taskComment.setRating(rating);

      taskCommentRepository.save(taskComment);

      return "发布评论成功！";
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public String deleteComment(long taskCommentId, long userId) {
    try {
      if (!userClient.getUserExist(userId)) {
        return "用户不存在！";
      }

      TaskComment taskComment = taskCommentRepository.findById(taskCommentId).orElse(null);
      if (taskComment == null) {
        return "评论不存在！";
      }

      if (taskComment.getCommenterId() != userId) {
        return "该评论不是由此用户发布！";
      }

      taskCommentRepository.deleteById(taskCommentId);

      return "删除评论成功！";
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public boolean isLiked(long taskCommentId, long likerId) {
    TaskComment taskComment = taskCommentRepository.findById(taskCommentId).orElse(null);
    if (taskComment == null) {
      return false;
    }

    return taskCommentLikeRepository.existsByLikerIdAndTaskComment(likerId, taskComment);
  }
}
