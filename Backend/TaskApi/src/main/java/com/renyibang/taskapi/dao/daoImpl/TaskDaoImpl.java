package com.renyibang.taskapi.dao.daoImpl;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.client.OrderClient;
import com.renyibang.global.client.UserClient;
import com.renyibang.taskapi.dao.TaskDao;
import com.renyibang.taskapi.entity.Task;
import com.renyibang.taskapi.entity.TaskAccess;
import com.renyibang.taskapi.entity.TaskCollect;
import com.renyibang.taskapi.enums.TaskAccessStatus;
import com.renyibang.taskapi.enums.TaskStatus;
import com.renyibang.taskapi.repository.TaskAccessRepository;
import com.renyibang.taskapi.repository.TaskCollectRepository;
import com.renyibang.taskapi.repository.TaskRepository;
import com.renyibang.taskapi.util.ImageUtil;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class TaskDaoImpl implements TaskDao {
  @Autowired TaskRepository taskRepository;

  @Autowired TaskCollectRepository taskCollectRepository;

  @Autowired TaskAccessRepository taskAccessRepository;

  @Autowired UserClient userClient;

  @Autowired OrderClient orderClient;

  @Override
  public Task findById(long taskId) {
    return taskRepository.findById(taskId).orElse(null);
  }

  @Override
  public Page<Task> searchTaskByPaging(
      String keyword,
      Pageable pageable,
      LocalDateTime beginDateTime,
      LocalDateTime endDateTime,
      long priceLow,
      long priceHigh) {
    if (!keyword.isEmpty()) {
      return taskRepository.searchTasks(
              keyword + "*", priceLow, priceHigh, beginDateTime, endDateTime, TaskStatus.DELETE, pageable);
    } else {
      return taskRepository.findByPriceBetweenAndCreatedAtBetweenAndStatusNot(
          priceLow, priceHigh, beginDateTime, endDateTime, TaskStatus.DELETE, pageable);
    }
  }

  @Override
  public String collectTaskByTaskId(long taskId, long collectorId) {
    try {
      if (!userClient.getUserExist(collectorId)) {
        return "用户不存在！";
      }

      Task task = taskRepository.findById(taskId).orElse(null);
      if (task == null) {
        return "任务不存在！";
      }

      if (task.getStatus() == TaskStatus.DELETE) {
        return "该任务已被删除！";
      }

      if (taskCollectRepository.existsByCollectorIdAndAndTask(collectorId, task)) {
        return "用户已收藏该任务！";
      }

      task.setCollectedNumber(task.getCollectedNumber() + 1);
      TaskCollect taskCollect = new TaskCollect();
      taskCollect.setTask(task);
      taskCollect.setCollectorId(collectorId);
      taskCollect.setCreatedAt(LocalDateTime.now());

      taskCollectRepository.save(taskCollect);

      return "收藏成功！";
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public String uncollectTaskByTaskId(long taskId, long uncollectorId) {
    try {
      if (!userClient.getUserExist(uncollectorId)) {
        return "用户不存在！";
      }

      Task task = taskRepository.findById(taskId).orElse(null);
      if (task == null) {
        return "任务不存在！";
      }

//      if (task.getStatus() == TaskStatus.DELETE) {
//        return "该任务已被删除！";
//      }

      TaskCollect taskCollect = taskCollectRepository.findByTaskAndCollectorId(task, uncollectorId);

      if (taskCollect == null) {
        return "用户未收藏该任务！";
      }

      task.setCollectedNumber(task.getCollectedNumber() - 1);
      taskCollectRepository.delete(taskCollect);

      return "取消收藏成功！";
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public String accessTaskByTaskId(long taskId, long accessorId) {
    try {
      Task task = taskRepository.findById(taskId).orElse(null);
      if (task == null) {
        return "任务不存在！";
      }

      if (task.getOwnerId() == accessorId) {
        return "不能接取自己发布的任务！";
      }

      if (task.getStatus() == TaskStatus.DELETE) {
        return "该任务已被删除！";
      } else if (task.getStatus() == TaskStatus.REMOVE) {
        return "该任务已被下架！";
      }

      if (!userClient.getUserExist(accessorId)) {
        return "用户不存在！";
      }

      if (taskAccessRepository.existsByAccessorIdAndTask(accessorId, task)) {
        return "用户已经接取该任务！";
      }

      if (!task.accessNotFull()) {
        return "该任务接取已达上限！";
      }

      if (task.getStatus() == TaskStatus.DELETE) {
        return "该任务已被删除！";
      } else if (task.getStatus() == TaskStatus.REMOVE) {
        return "该任务已被下架！";
      }

      TaskAccess taskAccess = new TaskAccess();
      taskAccess.setTask(task);
      taskAccess.setAccessorId(accessorId);
      taskAccess.setCreatedAt(LocalDateTime.now());
      taskAccess.setTaskAccessStatus(TaskAccessStatus.ACCESSING);

      taskAccessRepository.save(taskAccess);
      return "接取任务成功！";
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public String unaccessTaskByTaskId(long taskId, long unaccessorId) {
    try {
      Task task = taskRepository.findById(taskId).orElse(null);
      if (task == null) {
        return "任务不存在！";
      }

//      if (task.getStatus() == TaskStatus.DELETE) {
//        return "该任务已被删除！";
//      }

      if (!userClient.getUserExist(unaccessorId)) {
        return "用户不存在！";
      }

      TaskAccess taskAccess = taskAccessRepository.findByTaskAndAccessorId(task, unaccessorId);

      if (taskAccess == null) {
        return "用户未接取该任务！";
      }

      taskAccessRepository.delete(taskAccess);
      return "取消接取任务成功！";
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public String publishTask(
      long userId, String title, String description, long price, int maxAccess, List<String> requestImages) {
    try {
      if (!userClient.getUserExist(userId)) {
        return "用户不存在！";
      }

      String imagesURL = ImageUtil.mergeImages(requestImages);

      Task task = new Task();
      task.setTitle(title);
      task.setPrice(price);
      task.setOwnerId(userId);
      task.setImages(imagesURL);
      task.setDescription(description);
      task.setCreatedAt(LocalDateTime.now());
      task.setMaxAccess(maxAccess);

      taskRepository.save(task);

      return "任务发布成功！";
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public boolean isCollected(long taskId, long collectorId) {
    return taskCollectRepository.existsByCollectorIdAndAndTask(
        collectorId, taskRepository.findById(taskId).orElse(null));
  }

  @Override
  public boolean isAccessed(long taskId, long userId) {
    return taskAccessRepository.existsByAccessorIdAndTask(userId, taskRepository.findById(taskId).orElse(null));
  }

  @Override
  public Page<Task> getMyTask(long userId, Pageable pageable) {
    Page<Task> page = taskRepository.findByOwnerId(userId, pageable);
    List<Task> filteredTasks = page.stream()
        .filter(task -> task.getStatus() == TaskStatus.NORMAL)
        .collect(Collectors.toList());
    return new PageImpl<>(filteredTasks, pageable, filteredTasks.size());
  }

  @Override
  public Object getAccessingNumber(Task task) {
    return taskAccessRepository.countByTaskAndTaskAccessStatus(task, TaskAccessStatus.ACCESSING);
  }

  @Override
  public Object getSucceedNumber(Task task) {
    return taskAccessRepository.countByTaskAndTaskAccessStatus(task, TaskAccessStatus.ACCESS_SUCCESS);
  }

  @Override
  public Object getFailedNumber(Task task) {
    return taskAccessRepository.countByTaskAndTaskAccessStatus(task, TaskAccessStatus.ACCESS_FAIL);
  }

  @Override
  public Page<Task> getMyAccessedTask(long userId, Pageable pageable) {
    return taskRepository.findByAccessorId(userId, pageable);
  }

  @Override
  public Page<TaskAccess> getTaskAccessByTask(Task task, Pageable pageable) {
    return taskAccessRepository.findByTaskAndTaskAccessStatus(task, pageable, TaskAccessStatus.ACCESSING);
  }

  @Override
  public Page<TaskAccess> getTaskAccessSuccessByTask(Task task, Pageable pageable) {
    return taskAccessRepository.findByTaskAndTaskAccessStatus(task, pageable, TaskAccessStatus.ACCESS_SUCCESS);
  }

  @Override
  public Page<TaskAccess> getTaskAccessFailByTask(Task task, Pageable pageable) {
    return taskAccessRepository.findByTaskAndTaskAccessStatus(task, pageable, TaskAccessStatus.ACCESS_FAIL);
  }

  @Override
  public String cancelTask(long taskId, long userId) {
    Task task = taskRepository.findById(taskId).orElse(null);
    if (task == null) {
      return "任务不存在！";
    }

    if (task.getOwnerId() != userId) {
      return "只有任务发布者才能取消该任务！";
    }

    if (task.getStatus() == TaskStatus.DELETE) {
      return "该任务已被删除！";
    }

    task.setStatus(TaskStatus.DELETE);
    taskRepository.save(task);

    return "取消任务成功！";
  }

  @Override
  public String confirmAccessors(long taskId, long userId, List<Long> accessors) {
    Task task = taskRepository.findById(taskId).orElse(null);
    if (task == null) {
      return "任务不存在！";
    }

    if (task.getOwnerId() != userId) {
      return "只有任务发布者才能确认接取者！";
    }

    if (task.getStatus() == TaskStatus.DELETE) {
      return "该任务已被删除！";
    }

    if (task.getStatus() == TaskStatus.REMOVE) {
      return "该任务已被下架！";
    }

    if (task.getMaxAccess() < accessors.size()) {
      return "接取者数量超过最大接取数！";
    }

    Set<TaskAccess> confirmAccessors = new HashSet<>();

    for (long accessorId : accessors) {
      TaskAccess taskAccess = taskAccessRepository.findByTaskAndAccessorId(task, accessorId);
      if (taskAccess == null) {
        return "接取者" + accessorId +"不存在！";
      }

      else if (taskAccess.getTaskAccessStatus() != TaskAccessStatus.ACCESSING) {
        return "接取者状态异常！";
      }

      confirmAccessors.add(taskAccess);
    }

    JSONObject orderRequest = new JSONObject();
    orderRequest.put("taskId", taskId);
    orderRequest.put("ownerId", userId);
    orderRequest.put("accessors", accessors);
    orderRequest.put("cost", task.getPrice());

    JSONObject result = orderClient.createTaskOrder(orderRequest, userId);
    if(Objects.equals(false, result.get("ok")))
    {
      return "创建订单失败！";
    }

    for (TaskAccess taskAccess : confirmAccessors) {
      taskAccess.setTaskAccessStatus(TaskAccessStatus.ACCESS_SUCCESS);
      taskAccessRepository.save(taskAccess);
    }

    return "确认接取者成功！";
  }

  @Override
  public String denyAccessors(long taskId, long userId, List<Long> accessors){
    Task task = taskRepository.findById(taskId).orElse(null);
    if (task == null) {
      return "任务不存在！";
    }

    if (task.getOwnerId() != userId) {
      return "只有任务发布者才能拒绝接取者！";
    }

    if (task.getStatus() == TaskStatus.DELETE) {
      return "该任务已被删除！";
    }

    if (task.getStatus() == TaskStatus.REMOVE) {
      return "该任务已被下架！";
    }

    for (long accessorId : accessors) {
      TaskAccess taskAccess = taskAccessRepository.findByTaskAndAccessorId(task, accessorId);
      if (taskAccess == null) {
        return "接取者" + accessorId +"不存在！";
      }

      else if (taskAccess.getTaskAccessStatus() != TaskAccessStatus.ACCESSING) {
        return "接取者状态异常！";
      }

      taskAccess.setTaskAccessStatus(TaskAccessStatus.ACCESS_FAIL);
      taskAccessRepository.save(taskAccess);
    }

    return "拒绝接取者成功！";
  }

  @Override
  public Page<Task> getMyCollect(long userId, Pageable pageable)
  {
    Page<TaskCollect> taskCollects = taskCollectRepository.findByCollectorId(userId, pageable);
    List<Task> tasks = taskCollects.stream().map(TaskCollect::getTask).collect(Collectors.toList());
    return new PageImpl<>(tasks, pageable, tasks.size());
  }
}
