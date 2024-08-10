package com.renyibang.taskapi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.util.Response;
import com.renyibang.taskapi.service.TaskService;
import com.renyibang.taskapi.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/task")
public class TaskController {
  @Autowired TaskService taskService;

  @GetMapping("/search")
  public JSONObject searchTask(
      String keyword,
      int pageSize,
      int pageIndex,
      String order,
      String timeBegin,
      String timeEnd,
      long priceLow,
      long priceHigh,
      @RequestHeader long userId) {
    Sort sort;
    if (Objects.equals(order, "time") && !keyword.isEmpty()) sort = Sort.by("created_at").descending();
    else if (Objects.equals(order, "time")) sort = Sort.by("createdAt").descending();
    else if (Objects.equals(order, "rating")) sort = Sort.by("rating").descending();
    else return ResponseUtil.error("排序类型错误");
    if (pageSize <= 0 || pageIndex < 0) return ResponseUtil.error("分页错误");
    return taskService.searchTaskByPaging(
        keyword,
        PageRequest.of(pageIndex, pageSize, sort),
        timeBegin,
        timeEnd,
        priceLow,
        priceHigh,
        userId);
  }

  @GetMapping("/{taskId}")
  public JSONObject getTaskInfo(@PathVariable long taskId, @RequestHeader long userId) {
    return taskService.getTaskInfo(taskId, userId);
  }

  @GetMapping("/{taskId}/comment")
  public JSONObject getTaskComment(
      @PathVariable long taskId,
      int pageSize,
      int pageIndex,
      String order,
      @RequestHeader long userId) {
    Sort sort;
    if (Objects.equals(order, "likes")) sort = Sort.by("likedNumber").descending();
    else if (Objects.equals(order, "time")) sort = Sort.by("createdAt").descending();
    else return ResponseUtil.error("排序类型错误");
    if (pageSize <= 0 || pageIndex < 0) return ResponseUtil.error("分页错误");
    return taskService.getTaskComments(taskId, PageRequest.of(pageIndex, pageSize, sort), userId);
  }

  @GetMapping("/{taskId}/message")
  public JSONObject getTaskMessage(
      @PathVariable long taskId,
      int pageSize,
      int pageIndex,
      String order,
      @RequestHeader long userId) {
    Sort sort;
    if (Objects.equals(order, "likes")) sort = Sort.by("likedNumber").descending();
    else if (Objects.equals(order, "time")) sort = Sort.by("createdAt").descending();
    else return ResponseUtil.error("排序类型错误");
    if (pageSize <= 0 || pageIndex < 0) return ResponseUtil.error("分页错误");
    return taskService.getTaskMessages(taskId, PageRequest.of(pageIndex, pageSize, sort), userId);
  }

  @PutMapping("/comment/{taskCommentId}/like")
  public JSONObject likeComment(@PathVariable long taskCommentId, @RequestHeader long userId) {
    return taskService.likeComment(taskCommentId, userId);
  }

  @DeleteMapping("/comment/{taskCommentId}/unlike")
  public JSONObject unlikeComment(@PathVariable long taskCommentId, @RequestHeader long userId) {
    return taskService.unlikeComment(taskCommentId, userId);
  }

  @PutMapping("/message/{taskMessageId}/like")
  public JSONObject likeMessage(@PathVariable long taskMessageId, @RequestHeader long userId) {
    return taskService.likeMessage(taskMessageId, userId);
  }

  @DeleteMapping("/message/{taskMessageId}/unlike")
  public JSONObject unlikeMessage(@PathVariable long taskMessageId, @RequestHeader long userId) {
    return taskService.unlikeMessage(taskMessageId, userId);
  }

  @PutMapping("/{taskId}/collect")
  public JSONObject collectTask(@PathVariable long taskId, @RequestHeader long userId) {
    return taskService.collectTask(taskId, userId);
  }

  @DeleteMapping("/{taskId}/uncollect")
  public JSONObject uncollectTask(@PathVariable long taskId, @RequestHeader long userId) {
    return taskService.uncollectTask(taskId, userId);
  }

  @PutMapping("/{taskId}/access")
  public JSONObject accessTask(@PathVariable long taskId, @RequestHeader long userId) {
    return taskService.accessTask(taskId, userId);
  }

  @DeleteMapping("/{taskId}/unaccess")
  public JSONObject unaccessTask(@PathVariable long taskId, @RequestHeader long userId) {
    return taskService.unaccessTask(taskId, userId);
  }

  @PutMapping("/{taskId}/message")
  public JSONObject publishMessage(
      @PathVariable long taskId, @RequestBody JSONObject body, @RequestHeader long userId) {
    return taskService.publishMessage(taskId, userId, body);
  }

  @DeleteMapping("/message/{taskMessageId}")
  public JSONObject deleteMessage(@PathVariable long taskMessageId, @RequestHeader long userId) {
    return taskService.deleteMessage(taskMessageId, userId);
  }

  @PutMapping("/{taskId}/comment")
  public JSONObject publishComment(
      @PathVariable long taskId, @RequestBody JSONObject body, @RequestHeader long userId) {
    return taskService.publishComment(taskId, userId, body);
  }

  @DeleteMapping("/comment/{taskCommentId}")
  public JSONObject deleteComment(@PathVariable long taskCommentId, @RequestHeader long userId) {
    return taskService.deleteComment(taskCommentId, userId);
  }

  @PostMapping("/issue")
  public JSONObject publishTask(@RequestBody JSONObject body, @RequestHeader long userId) {
    return taskService.publishTask(userId, body);
  }

  @GetMapping("/getTask/{taskId}")
  public JSONObject getTaskDtoById(@PathVariable Long taskId) {
    return taskService.getTaskDtoById(taskId);
  }

  @GetMapping("/{taskId}/ownerId")
  public JSONObject getTaskOwnerId(@PathVariable long taskId) {
    return taskService.getTaskOwnerId(taskId);
  }

  @GetMapping("/initiator/self")
  public JSONObject getMyTask(int pageSize, int pageIndex, @RequestHeader long userId) {
    if (pageSize <= 0 || pageIndex < 0) return ResponseUtil.error("分页错误");
    return taskService.getMyTask(PageRequest.of(pageIndex, pageSize), userId);
  }

  @GetMapping("/recipient/self")
  public JSONObject getMyAccessedTask(int pageSize, int pageIndex, @RequestHeader long userId) {
    if (pageSize <= 0 || pageIndex < 0) return ResponseUtil.error("分页错误");
    return taskService.getMyAccessedTask(PageRequest.of(pageIndex, pageSize), userId);
  }

  @GetMapping("/{taskId}/select/info")
  public JSONObject getTaskAccessorInfo(@PathVariable long taskId, @RequestHeader long userId, int pageSize, int pageIndex) {
    return taskService.getTaskAccessorInfo(taskId, userId, PageRequest.of(pageIndex, pageSize));
  }

  @GetMapping("/{taskId}/select/success")
  public JSONObject getTaskAccessorSuccess(@PathVariable long taskId, @RequestHeader long userId, int pageSize, int pageIndex) {
    return taskService.getTaskAccessorSuccess(taskId, userId, PageRequest.of(pageIndex, pageSize));
  }

  @GetMapping("/{taskId}/select/fail")
  public JSONObject getTaskAccessorFail(@PathVariable long taskId, @RequestHeader long userId, int pageSize, int pageIndex) {
    return taskService.getTaskAccessorFail(taskId, userId, PageRequest.of(pageIndex, pageSize));
  }

  @DeleteMapping("/{taskId}/cancel")
  public JSONObject cancelTask(@PathVariable long taskId, @RequestHeader long userId) {
    return taskService.cancelTask(taskId, userId);
  }

  @PutMapping("/{taskId}/select/confirm")
  public JSONObject confirmAccessors(@PathVariable long taskId, @RequestHeader long userId, @RequestBody JSONObject body) {
    return taskService.confirmAccessors(taskId, userId, body);
  }

  @PutMapping("/{taskId}/select/deny")
  public JSONObject denyAccessors(@PathVariable long taskId, @RequestHeader long userId, @RequestBody JSONObject body) {
    return taskService.denyAccessors(taskId, userId, body);
  }

  @GetMapping("/mycollect")
  public JSONObject getMyCollect(int pageSize, int pageIndex, @RequestHeader long userId) {
    if (pageSize <= 0 || pageIndex < 0) return ResponseUtil.error("分页错误");
    return taskService.getMyCollect(PageRequest.of(pageIndex, pageSize), userId);
  }

  @GetMapping("/health")
  public Response health() {
    return Response.success();
  }
}
