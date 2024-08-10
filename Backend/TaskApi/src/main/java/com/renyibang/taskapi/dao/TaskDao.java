package com.renyibang.taskapi.dao;

import com.renyibang.taskapi.entity.Task;

import com.renyibang.taskapi.entity.TaskAccess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskDao {
  Task findById(long taskId);

  Page<Task> searchTaskByPaging(String keyword, Pageable pageable, LocalDateTime beginDateTime, LocalDateTime endDateTime, long priceLow, long priceHigh);

  String collectTaskByTaskId(long taskId, long collectorId);

  String uncollectTaskByTaskId(long taskId, long uncollectorId);

  String accessTaskByTaskId(long taskId, long accessorId);

  String unaccessTaskByTaskId(long taskId, long unaccessorId);

  String publishTask(long userId, String title, String description, long price, int maxAccess, List<String> requestImages);

  boolean isCollected(long taskId, long collectorId);

  boolean isAccessed(long taskId, long userId);
  
  Page<Task> getMyTask(long userId, Pageable pageable);

  Object getAccessingNumber(Task task);

  Object getSucceedNumber(Task task);

  Object getFailedNumber(Task task);

  Page<Task> getMyAccessedTask(long userId, Pageable pageable);

  Page<TaskAccess> getTaskAccessByTask(Task task, Pageable pageable);

  Page<TaskAccess> getTaskAccessSuccessByTask(Task task, Pageable pageable);

  Page<TaskAccess> getTaskAccessFailByTask(Task task, Pageable pageable);

  String cancelTask(long taskId, long userId);

  String confirmAccessors(long taskId, long userId, List<Long> accessors);

  String denyAccessors(long taskId, long userId, List<Long> accessors);

  Page<Task> getMyCollect(long userId, Pageable pageable);
}
