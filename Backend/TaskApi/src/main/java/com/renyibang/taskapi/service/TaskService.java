package com.renyibang.taskapi.service;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface TaskService {
  JSONObject searchTaskByPaging(
      String keyword,
      Pageable pageable,
      String timeBegin,
      String timeEnd,
      long priceLow,
      long priceHigh,
      long userId);

  JSONObject getTaskInfo(long taskId, long userId);

  JSONObject getTaskComments(long taskId, Pageable pageable, long userId);

  JSONObject getTaskMessages(long taskId, Pageable pageable, long userId);

  JSONObject likeComment(long taskCommentId, long likerId);

  JSONObject unlikeComment(long taskCommentId, long unlikerId);

  JSONObject likeMessage(long taskMessageId, long likerId);

  JSONObject unlikeMessage(long taskMessageId, long unlikerId);

  JSONObject collectTask(long taskId, long collectorId);

  JSONObject uncollectTask(long taskId, long uncollectorId);

  JSONObject accessTask(long taskId, long accessorId);

  JSONObject unaccessTask(long taskId, long unaccessorId);

  JSONObject publishMessage(long taskId, long userId, JSONObject body);

  JSONObject deleteMessage(long taskMessageId, long userId);

  JSONObject publishComment(long taskId, long userId, JSONObject body);

  JSONObject deleteComment(long taskCommentId, long userId);

  JSONObject publishTask(long userId, JSONObject body);

  JSONObject getTaskDtoById(Long taskId);

  JSONObject getTaskOwnerId(long taskId);

  JSONObject getMyTask(Pageable pageable, long userId);

  JSONObject getMyAccessedTask(Pageable pageable, long userId);

  JSONObject getTaskAccessorInfo(long taskId, long userId, Pageable pageable);

  JSONObject cancelTask(long taskId, long userId);

  JSONObject confirmAccessors(long taskId, long userId, JSONObject body);

  JSONObject getTaskAccessorSuccess(long taskId, long userId, Pageable pageable);

  JSONObject getTaskAccessorFail(long taskId, long userId, Pageable pageable);

  JSONObject denyAccessors(long taskId, long userId, JSONObject body);

  JSONObject getMyCollect(Pageable pageable, long userId);
}
