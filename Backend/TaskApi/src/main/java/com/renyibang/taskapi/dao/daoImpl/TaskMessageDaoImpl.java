package com.renyibang.taskapi.dao.daoImpl;

import com.renyibang.global.client.UserClient;
import com.renyibang.taskapi.dao.TaskMessageDao;
import com.renyibang.taskapi.entity.Task;
import com.renyibang.taskapi.entity.TaskMessage;
import com.renyibang.taskapi.entity.TaskMessageLike;
import com.renyibang.taskapi.repository.TaskMessageLikeRepository;
import com.renyibang.taskapi.repository.TaskMessageRepository;
import com.renyibang.taskapi.repository.TaskRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class TaskMessageDaoImpl implements TaskMessageDao {
  @Autowired TaskRepository taskRepository;
  @Autowired TaskMessageLikeRepository taskMessageLikeRepository;
  @Autowired UserClient userClient;
  @Autowired private TaskMessageRepository taskMessageRepository;

  @Override
  public Page<TaskMessage> getTaskMessages(long taskId, Pageable pageable) {
    return taskMessageRepository.findByTaskTaskId(taskId, pageable);
  }

  @Override
  public String likeMessageByTaskMessageId(long taskMessageId, long likerId) {
    try {
      TaskMessage taskMessage = taskMessageRepository.findById(taskMessageId).orElse(null);
      if (taskMessage == null) {
        return "留言不存在！";
      }

      if (!userClient.getUserExist(likerId)) {
        return "用户不存在！";
      }

      if (taskMessageLikeRepository.existsByLikerIdAndTaskMessage(likerId, taskMessage)) {
        return "用户已点赞过该留言！";
      } else {
        taskMessage.setLikedNumber(taskMessage.getLikedNumber() + 1);
        taskMessageRepository.save(taskMessage);

        TaskMessageLike taskMessageLike = new TaskMessageLike();
        taskMessageLike.setLikerId(likerId);
        taskMessageLike.setTaskMessage(taskMessage);
        taskMessageLikeRepository.save(taskMessageLike);

        return "点赞成功！";
      }
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public String unlikeMessageByTaskMessageId(long taskMessageId, long unlikerId) {
    try {
      TaskMessage taskMessage = taskMessageRepository.findById(taskMessageId).orElse(null);
      if (taskMessage == null) {
        return "留言不存在！";
      }

      if (!userClient.getUserExist(unlikerId)) {
        return "用户不存在！";
      }

      TaskMessageLike taskMessageLike =
          taskMessageLikeRepository.findByLikerIdAndTaskMessage(unlikerId, taskMessage);

      if (taskMessageLike == null) {
        return "用户未点赞过该留言！";
      } else {
        // 存在并发问题!!
        taskMessage.setLikedNumber(taskMessage.getLikedNumber() - 1);
        taskMessageRepository.save(taskMessage);

        taskMessageLikeRepository.delete(taskMessageLike);
        return "取消点赞成功！";
      }
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public String putMessage(long taskId, long userId, String content) {
    try {
      if (!userClient.getUserExist(userId)) {
        return "用户不存在！";
      }

      Task task = taskRepository.findById(taskId).orElse(null);
      if (task == null) {
        return "任务不存在！";
      }

      TaskMessage taskMessage = new TaskMessage();
      taskMessage.setTask(task);
      taskMessage.setMessagerId(userId);
      taskMessage.setContent(content);
      taskMessage.setCreatedAt(LocalDateTime.now());
      taskMessageRepository.save(taskMessage);

      return "发布留言成功！";
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public String deleteMessage(long taskMessageId, long userId) {
    try {
      if (!userClient.getUserExist(userId)) {
        return "用户不存在！";
      }

      TaskMessage taskMessage = taskMessageRepository.findById(taskMessageId).orElse(null);
      if (taskMessage == null) {
        return "留言不存在！";
      }

      if (taskMessage.getMessagerId() != userId) {
        return "该留言不是由此用户发布！";
      }

      taskMessageRepository.deleteById(taskMessageId);

      return "删除留言成功！";
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public boolean isLiked(long taskMessageId, long likerId) {
    TaskMessage taskMessage = taskMessageRepository.findById(taskMessageId).orElse(null);
    if (taskMessage == null) {
      return false;
    }

    return taskMessageLikeRepository.existsByLikerIdAndTaskMessage(likerId, taskMessage);
  }
}
