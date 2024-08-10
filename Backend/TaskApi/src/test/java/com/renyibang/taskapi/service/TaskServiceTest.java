package com.renyibang.taskapi.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.client.UserClient;
import com.renyibang.taskapi.dao.TaskCommentDao;
import com.renyibang.taskapi.dao.TaskDao;
import com.renyibang.taskapi.dao.TaskMessageDao;
import com.renyibang.taskapi.entity.Task;
import com.renyibang.taskapi.entity.TaskAccess;
import com.renyibang.taskapi.entity.TaskComment;
import com.renyibang.taskapi.entity.TaskMessage;
import com.renyibang.taskapi.service.serviceImpl.TaskServiceImpl;
import com.renyibang.taskapi.enums.TaskAccessStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TaskServiceTest {
  @InjectMocks
  private TaskServiceImpl taskService;

  @Mock
  private TaskDao taskDao;

  @Mock
    private TaskMessageDao taskMessageDao;

    @Mock
    private TaskCommentDao taskCommentDao;

    @Mock
    private UserClient userClient;

    private MockMvc mockMvc;

    private Task task;
    private Pageable pageable;
    private DateTimeFormatter formatter;
    private List<Task> taskList;
    private Page<Task> taskPage;
    private JSONObject successResponse;
    private JSONObject errorResponse;
    private JSONObject userInfosForTest;
    private JSONObject userInfoForTest;
    private ArrayList<JSONObject> userInfoList;


    @BeforeEach
    public void setUp() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        mockMvc = MockMvcBuilders.standaloneSetup(taskService).build();
        task = new Task();
        task.setOwnerId(1L);
        task.setTitle("test");
        task.setDescription("test");
        task.setPrice(1000);
        task.setCreatedAt(LocalDateTime.parse("2024-06-01 01:00:00", formatter));
        task.setImages("test");
        taskList = new ArrayList<>();
        taskList.add(task);
        taskPage = new PageImpl<>(taskList);
        pageable = PageRequest.of(0, 10);

      successResponse = new JSONObject();
      errorResponse = new JSONObject();
      successResponse.put("ok", true);
      successResponse.put("data", "111");
      errorResponse.put("ok", false);
      errorResponse.put("data", "111");
      userInfoForTest = new JSONObject();
      userInfoForTest.put("ok", true);
      userInfoList = new ArrayList<>();
      userInfoList.add(userInfoForTest);
      userInfosForTest = new JSONObject();
      userInfosForTest.put("ok", true);
      userInfosForTest.put("data", userInfoList);
    }

    @Test
  public void testSearchTaskByPaging() {
      JSONArray userIds = new JSONArray();
      userIds.add(1L);
      JSONObject userObject = new JSONObject();
      userObject.put("ok", true);
      userObject.put("data", userIds);
      String formattedBegintime = "2024-07-01 00:00:00";
      String formattedEndtime = "2024-07-02 00:00:00";

      when(taskDao.searchTaskByPaging(anyString(),any(Pageable.class),any(LocalDateTime.class),any(LocalDateTime.class),anyLong(),anyLong())).thenReturn(taskPage);
      when(userClient.getUserInfos(anyList())).thenReturn(userObject);

      JSONObject result = taskService.searchTaskByPaging("test", pageable, formattedBegintime, formattedEndtime, 1L, 1000L, 1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testSearchTaskByPagingWithEmptyUserIds() {
      JSONArray userIds = new JSONArray();
      JSONObject userObject = new JSONObject();
      userObject.put("ok", true);
      userObject.put("data", userIds);
      Page<Task> tasks = new PageImpl<>(Collections.emptyList());
      String formattedBegintime = "2024-07-01 00:00:00";
      String formattedEndtime = "2024-07-02 00:00:00";
      when(taskDao.searchTaskByPaging(anyString(),any(Pageable.class),any(LocalDateTime.class),any(LocalDateTime.class),anyLong(),anyLong())).thenReturn(tasks);
      when(userClient.getUserInfos(anyList())).thenReturn(userObject);

      JSONObject result = taskService.searchTaskByPaging("test", pageable, formattedBegintime, formattedEndtime, 1L, 1000L, 1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testSearchTaskByPagingWithErrorResponse() {
      JSONArray userIds = new JSONArray();
      userIds.add(1L);
      JSONObject userObject = new JSONObject();
      userObject.put("ok", false);
      userObject.put("data", userIds);
      String formattedBegintime = "2024-07-01 00:00:00";
      String formattedEndtime = "2024-07-02 00:00:00";

      when(taskDao.searchTaskByPaging(anyString(),any(Pageable.class),any(LocalDateTime.class),any(LocalDateTime.class),anyLong(),anyLong())).thenReturn(taskPage);
      when(userClient.getUserInfos(anyList())).thenReturn(userObject);

      JSONObject result = taskService.searchTaskByPaging("test", pageable, formattedBegintime, formattedEndtime, 1L, 1000L, 1L);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testSearchTaskByPagingWithInvalidParams() {
      JSONObject result = taskService.searchTaskByPaging("test", pageable, "invalid", "invalid", -1L, -2L, 1L);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testGetTaskInfo() {
        when(taskDao.findById(anyLong())).thenReturn(task);
        when(userClient.getUserInfo(anyLong())).thenReturn(successResponse);
        JSONObject result = taskService.getTaskInfo(1L, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetTaskInfoWithEmptyTask() {
        when(taskDao.findById(anyLong())).thenReturn(null);
        JSONObject result = taskService.getTaskInfo(1L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetTaskInfoWithEmptyUser() {
        when(taskDao.findById(anyLong())).thenReturn(task);
        when(userClient.getUserInfo(anyLong())).thenReturn(errorResponse);
        JSONObject result = taskService.getTaskInfo(1L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetTaskComments() {
      TaskComment taskComment = new TaskComment();
      taskComment.setCommenterId(1L);
      taskComment.setTask(task);
      taskComment.setContent("test");
      taskComment.setRating((byte) 50);
      taskComment.setCreatedAt(LocalDateTime.parse("2024-06-01 01:00:00", formatter));
      Page<TaskComment> taskCommentPage = new PageImpl<>(List.of(taskComment));
      when(taskCommentDao.getTaskComments(anyLong(), any(Pageable.class))).thenReturn(taskCommentPage);
      when(userClient.getUserInfos(anyList())).thenReturn(userInfosForTest);
      when(taskCommentDao.isLiked(anyLong(), anyLong())).thenReturn(true);

      JSONObject result = taskService.getTaskComments(1L, pageable, 1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testGetTaskCommentsWithEmptyComments() {
      Page<TaskComment> taskCommentPage = new PageImpl<>(Collections.emptyList());
      when(taskCommentDao.getTaskComments(anyLong(), any(Pageable.class))).thenReturn(taskCommentPage);

      JSONObject result = taskService.getTaskComments(1L, pageable, 1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testGetTaskCommentsWithEmptyUser() {
      TaskComment taskComment = new TaskComment();
      taskComment.setCommenterId(1L);
      Page<TaskComment> taskCommentPage = new PageImpl<>(List.of(taskComment));
      when(taskCommentDao.getTaskComments(anyLong(), any(Pageable.class))).thenReturn(taskCommentPage);
      when(userClient.getUserInfo(anyLong())).thenReturn(errorResponse);

      JSONObject result = taskService.getTaskComments(1L, pageable, 1L);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testGetTaskMessages() {
      TaskMessage taskMessage = new TaskMessage();
      taskMessage.setMessagerId(1L);
      taskMessage.setTask(task);
      taskMessage.setContent("test");
      taskMessage.setCreatedAt(LocalDateTime.parse("2024-06-01 01:00:00", formatter));
      Page<TaskMessage> taskMessagePage = new PageImpl<>(List.of(taskMessage));
      when(taskMessageDao.getTaskMessages(anyLong(), any(Pageable.class))).thenReturn(taskMessagePage);
      when(userClient.getUserInfos(anyList())).thenReturn(userInfosForTest);

      JSONObject result = taskService.getTaskMessages(1L, pageable, 1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testGetTaskMessagesWithEmptyMessages() {
      TaskMessage taskMessage = new TaskMessage();
      Page<TaskMessage> taskMessagePage = new PageImpl<>(Collections.emptyList());
      when(taskMessageDao.getTaskMessages(anyLong(), any(Pageable.class))).thenReturn(taskMessagePage);

      JSONObject result = taskService.getTaskMessages(1L, pageable, 1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testGetTaskMessagesWithEmptyUser() {
      TaskMessage taskMessage = new TaskMessage();
      taskMessage.setMessagerId(1L);
      Page<TaskMessage> taskMessagePage = new PageImpl<>(List.of(taskMessage));
      when(taskMessageDao.getTaskMessages(anyLong(), any(Pageable.class))).thenReturn(taskMessagePage);
      when(userClient.getUserInfo(anyLong())).thenReturn(errorResponse);

      JSONObject result = taskService.getTaskMessages(1L, pageable, 1L);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testLikeCommentSuccess() {
      when(taskCommentDao.likeCommentByTaskCommentId(anyLong(),anyLong())).thenReturn("点赞成功！");
      JSONObject result = taskService.likeComment(1L, 1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testLikeCommentFailed() {
      when(taskCommentDao.likeCommentByTaskCommentId(anyLong(),anyLong())).thenReturn("点赞失败！");
      JSONObject result = taskService.likeComment(1L, 1L);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testUnlikeCommentSuccess() {
      when(taskCommentDao.unlikeCommentByTaskCommentId(anyLong(),anyLong())).thenReturn("取消点赞成功！");
      JSONObject result = taskService.unlikeComment(1L, 1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testUnlikeCommentFailed() {
      when(taskCommentDao.unlikeCommentByTaskCommentId(anyLong(),anyLong())).thenReturn("取消点赞失败！");
      JSONObject result = taskService.unlikeComment(1L, 1L);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testLikeMessageSuccess() {
      when(taskMessageDao.likeMessageByTaskMessageId(anyLong(),anyLong())).thenReturn("点赞成功！");
      JSONObject result = taskService.likeMessage(1L, 1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testLikeMessageFailed() {
      when(taskMessageDao.likeMessageByTaskMessageId(anyLong(),anyLong())).thenReturn("点赞失败！");
      JSONObject result = taskService.likeMessage(1L, 1L);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testUnlikeMessageSuccess() {
      when(taskMessageDao.unlikeMessageByTaskMessageId(anyLong(),anyLong())).thenReturn("取消点赞成功！");
      JSONObject result = taskService.unlikeMessage(1L, 1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testUnlikeMessageFailed() {
      when(taskMessageDao.unlikeMessageByTaskMessageId(anyLong(),anyLong())).thenReturn("取消点赞失败！");
      JSONObject result = taskService.unlikeMessage(1L, 1L);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testCollectTaskSuccess() {
      when(taskDao.collectTaskByTaskId(anyLong(),anyLong())).thenReturn("收藏成功！");
      JSONObject result = taskService.collectTask(1L, 1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testCollectTaskFailed() {
      when(taskDao.collectTaskByTaskId(anyLong(),anyLong())).thenReturn("收藏失败！");
      JSONObject result = taskService.collectTask(1L, 1L);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testUncollectTaskSuccess() {
      when(taskDao.uncollectTaskByTaskId(anyLong(),anyLong())).thenReturn("取消收藏成功！");
      JSONObject result = taskService.uncollectTask(1L, 1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testUncollectTaskFailed() {
      when(taskDao.uncollectTaskByTaskId(anyLong(),anyLong())).thenReturn("取消收藏失败！");
      JSONObject result = taskService.uncollectTask(1L, 1L);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testAccessTaskSuccess() {
      when(taskDao.accessTaskByTaskId(anyLong(),anyLong())).thenReturn("接取任务成功！");
      JSONObject result = taskService.accessTask(1L, 1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testAccessTaskFailed() {
      when(taskDao.accessTaskByTaskId(anyLong(),anyLong())).thenReturn("接取任务失败！");
      JSONObject result = taskService.accessTask(1L, 1L);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testUnaccessTaskSuccess() {
      when(taskDao.unaccessTaskByTaskId(anyLong(),anyLong())).thenReturn("取消接取任务成功！");
      JSONObject result = taskService.unaccessTask(1L, 1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testUnaccessTaskFailed() {
      when(taskDao.unaccessTaskByTaskId(anyLong(),anyLong())).thenReturn("取消接取任务失败！");
      JSONObject result = taskService.unaccessTask(1L, 1L);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishMessageSuccess() {
      JSONObject body = new JSONObject();
      body.put("content", "test");
      when(taskMessageDao.putMessage(anyLong(),anyLong(),anyString())).thenReturn("发布留言成功！");
      JSONObject result = taskService.publishMessage(1L, 1L, body);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testPublishMessageFailed() {
      JSONObject body = new JSONObject();
      body.put("content", "test");
      when(taskMessageDao.putMessage(anyLong(),anyLong(),anyString())).thenReturn("发布留言失败！");
      JSONObject result = taskService.publishMessage(1L, 1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishMessageWithEmptyBody() {
      JSONObject body = new JSONObject();
      when(taskMessageDao.putMessage(anyLong(),anyLong(),anyString())).thenReturn("发布留言失败！");
      JSONObject result = taskService.publishMessage(1L, 1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishMessageWithEmptyContent() {
      JSONObject body = new JSONObject();
      body.put("content", "");
      when(taskMessageDao.putMessage(anyLong(),anyLong(),anyString())).thenReturn("发布留言失败！");
      JSONObject result = taskService.publishMessage(1L, 1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testDeleteMessageSuccess() {
      when(taskMessageDao.deleteMessage(anyLong(),anyLong())).thenReturn("删除留言成功！");
      JSONObject result = taskService.deleteMessage(1L, 1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testDeleteMessageFailed() {
      when(taskMessageDao.deleteMessage(anyLong(),anyLong())).thenReturn("删除留言失败！");
      JSONObject result = taskService.deleteMessage(1L, 1L);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishCommentSuccess() {
      JSONObject body = new JSONObject();
      body.put("content", "test");
      body.put("rating", 1);
      when(taskCommentDao.putComment(anyLong(),anyLong(),anyString(),anyByte())).thenReturn("发布评论成功！");
      JSONObject result = taskService.publishComment(1L, 1L, body);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testPublishCommentFailed() {
      JSONObject body = new JSONObject();
      body.put("content", "test");
      body.put("rating", 1);
      when(taskCommentDao.putComment(anyLong(),anyLong(),anyString(),anyByte())).thenReturn("发布评论失败！");
      JSONObject result = taskService.publishComment(1L, 1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishCommentWithEmptyBody() {
      JSONObject body = new JSONObject();
      when(taskCommentDao.putComment(anyLong(),anyLong(),anyString(),anyByte())).thenReturn("发布评论失败！");
      JSONObject result = taskService.publishComment(1L, 1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishCommentWithEmptyContent() {
      JSONObject body = new JSONObject();
      body.put("content", "");
      body.put("rating", 1);
      when(taskCommentDao.putComment(anyLong(),anyLong(),anyString(),anyByte())).thenReturn("发布评论失败！");
      JSONObject result = taskService.publishComment(1L, 1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishCommentWithEmptyRating() {
      JSONObject body = new JSONObject();
      body.put("content", "test");
      when(taskCommentDao.putComment(anyLong(),anyLong(),anyString(),anyByte())).thenReturn("发布评论失败！");
      JSONObject result = taskService.publishComment(1L, 1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testDeleteCommentSuccess() {
      when(taskCommentDao.deleteComment(anyLong(),anyLong())).thenReturn("删除评论成功！");
      JSONObject result = taskService.deleteComment(1L, 1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testDeleteCommentFailed() {
      when(taskCommentDao.deleteComment(anyLong(),anyLong())).thenReturn("删除评论失败！");
      JSONObject result = taskService.deleteComment(1L, 1L);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishTaskSuccess() {
      String images = "test";
      ArrayList<String> imageList = new ArrayList<>();
      imageList.add(images);
      JSONObject body = new JSONObject();
      body.put("title", "test");
      body.put("description", "test");
      body.put("price", 1000);
      body.put("maxAccess", 1);
      body.put("images", imageList);
      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("任务发布成功！");
      JSONObject result = taskService.publishTask(1L, body);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testPublishTaskFailed() {
      String images = "test";
      ArrayList<String> imageList = new ArrayList<>();
      imageList.add(images);
      JSONObject body = new JSONObject();
      body.put("title", "test");
      body.put("description", "test");
      body.put("price", 1000);
      body.put("maxAccess", 1);
      body.put("images", imageList);
      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务失败！");
      JSONObject result = taskService.publishTask(1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishTaskWithEmptyBody() {
      JSONObject body = new JSONObject();
      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务失败！");
      JSONObject result = taskService.publishTask(1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishTaskWithEmptyTitle() {
      JSONObject body = new JSONObject();
      body.put("title", "");
      body.put("description", "test");
      body.put("price", 1000);
      body.put("maxAccess", 1);
      body.put("images", "test");
      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务失败！");
      JSONObject result = taskService.publishTask(1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishTaskWithEmptyDescription() {
      JSONObject body = new JSONObject();
      body.put("title", "test");
      body.put("description", "");
      body.put("price", 1000);
      body.put("maxAccess", 1);
      body.put("images", "test");
      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务失败！");
      JSONObject result = taskService.publishTask(1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishTaskWithIntegerPrice() {
      JSONObject body = new JSONObject();
      body.put("title", "test");
      body.put("description", "test");
      body.put("price", 10000L);
      body.put("maxAccess", 1);
      body.put("images", "test");
      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务失败！");
      JSONObject result = taskService.publishTask(1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishTaskWithLongPrice() {
      JSONObject body = new JSONObject();
      body.put("title", "test");
      body.put("description", "test");
      body.put("price", 10000000000L);
      body.put("maxAccess", 1);
      body.put("images", "test");
      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务失败！");
      JSONObject result = taskService.publishTask(1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishTaskWithStringPrice() {
      JSONObject body = new JSONObject();
      body.put("title", "test");
      body.put("description", "test");
      body.put("price", "test");
      body.put("maxAccess", 1);
      body.put("images", "test");
      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务失败！");
      JSONObject result = taskService.publishTask(1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishTaskWithNegativePrice() {
      JSONObject body = new JSONObject();
      body.put("title", "test");
      body.put("description", "test");
      body.put("price", -1000);
      body.put("maxAccess", 1);
      body.put("images", "test");
      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务失败！");
      JSONObject result = taskService.publishTask(1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishTaskWithStringMaxAccess() {
      JSONObject body = new JSONObject();
      body.put("title", "test");
      body.put("description", "test");
      body.put("price", 1000);
      body.put("maxAccess", "test");
      body.put("images", "test");
      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务失败！");
      JSONObject result = taskService.publishTask(1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishTaskWithNegativeMaxAccess() {
      JSONObject body = new JSONObject();
      body.put("title", "test");
      body.put("description", "test");
      body.put("price", 1000);
      body.put("maxAccess", -1);
      body.put("images", "test");
      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务失败！");
      JSONObject result = taskService.publishTask(1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testGetTaskDtoByIdSuccess() {
      when(taskDao.findById(anyLong())).thenReturn(task);
      when(userClient.getUserInfo(anyLong())).thenReturn(successResponse);
      JSONObject result = taskService.getTaskDtoById(1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testGetTaskDtoByIdFailed() {
      when(taskDao.findById(anyLong())).thenReturn(null);
      JSONObject result = taskService.getTaskDtoById(1L);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testGetTaskOwnerIdSuccess() {
      when(taskDao.findById(anyLong())).thenReturn(task);
      JSONObject result = taskService.getTaskOwnerId(1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testGetTaskOwnerIdFailed() {
      when(taskDao.findById(anyLong())).thenReturn(null);
      JSONObject result = taskService.getTaskOwnerId(1L);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testGetMyTask() {
        when(taskDao.getMyTask(anyLong(), any(Pageable.class))).thenReturn(taskPage);
        JSONObject result = taskService.getMyTask(pageable, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetMyAccessedTask() {
        when(taskDao.getMyAccessedTask(anyLong(), any(Pageable.class))).thenReturn(taskPage);
        JSONObject result = taskService.getMyAccessedTask(pageable, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetTaskAccessorInfo1() {
      when(taskDao.findById(anyLong())).thenReturn(null);
      JSONObject result = taskService.getTaskAccessorInfo(1L, 1L, pageable);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testGetTaskAccessorInfo2() {
      Task newtask = new Task();
      newtask.setOwnerId(2L);
      when(taskDao.findById(anyLong())).thenReturn(newtask);
      JSONObject result = taskService.getTaskAccessorInfo(1L, 1L, pageable);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testGetTaskAccessorInfo3() {
      Task newtask = new Task();
      newtask.setOwnerId(1L);
      when(taskDao.findById(anyLong())).thenReturn(newtask);
      when(taskDao.getTaskAccessByTask(any(Task.class), any(Pageable.class))).thenReturn(null);
      JSONObject result = taskService.getTaskAccessorInfo(1L, 1L, pageable);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testGetTaskAccessorInfo4() {
      Task newtask = new Task();
      newtask.setOwnerId(1L);
      when(taskDao.findById(anyLong())).thenReturn(newtask);

      List<TaskAccess> taskAccessList = Collections.emptyList();
      Page<TaskAccess> taskAccessPage = new PageImpl<>(taskAccessList);
      when(taskDao.getTaskAccessByTask(any(Task.class), any(Pageable.class))).thenReturn(taskAccessPage);

      JSONObject result = taskService.getTaskAccessorInfo(1L, 1L, pageable);
      System.out.println(result);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testGetTaskAccessorInfo5() {
      Task newtask = new Task();
      newtask.setOwnerId(1L);
      when(taskDao.findById(anyLong())).thenReturn(newtask);
      TaskAccess taskAccess = new TaskAccess();
      taskAccess.setAccessorId(1L);
      List<TaskAccess> taskAccessList = new ArrayList<>();
      taskAccessList.add(taskAccess);
      Page<TaskAccess> taskAccessPage = new PageImpl<>(taskAccessList);
      when(taskDao.getTaskAccessByTask(any(Task.class), any(Pageable.class))).thenReturn(taskAccessPage);
      when(userClient.getAccessorInfos(anyList())).thenReturn(errorResponse);
      JSONObject result = taskService.getTaskAccessorInfo(1L, 1L, pageable);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testGetTaskAccessorInfo6() {
      when(taskDao.findById(anyLong())).thenReturn(task);
      TaskAccess taskAccess = new TaskAccess();
      taskAccess.setAccessorId(1L);
      taskAccess.setTaskAccessId(1L);
      taskAccess.setTaskAccessStatus(TaskAccessStatus.ACCESSING);
      List<TaskAccess> taskAccessList = new ArrayList<>();
      taskAccessList.add(taskAccess);
      Page<TaskAccess> taskAccessPage = new PageImpl<>(taskAccessList);
      when(taskDao.getTaskAccessByTask(any(Task.class), any(Pageable.class))).thenReturn(taskAccessPage);

      JSONObject newmessage = new JSONObject();
      newmessage.put("ok", true);
      newmessage.put("data", new ArrayList<>());
      when(userClient.getAccessorInfos(anyList())).thenReturn(newmessage);
      JSONObject result = taskService.getTaskAccessorInfo(1L, 1L, pageable);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testGetTaskAccessorSuccess1() {
      when(taskDao.findById(anyLong())).thenReturn(null);
      JSONObject result = taskService.getTaskAccessorSuccess(1L, 1L, pageable);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testGetTaskAccessorSuccess2() {
      Task newtask = new Task();
      newtask.setOwnerId(2L);
      when(taskDao.findById(anyLong())).thenReturn(newtask);
      JSONObject result = taskService.getTaskAccessorSuccess(1L, 1L, pageable);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testGetTaskAccessorSuccess3() {
      Task newtask = new Task();
      newtask.setOwnerId(1L);
      when(taskDao.findById(anyLong())).thenReturn(newtask);
      when(taskDao.getTaskAccessSuccessByTask(any(Task.class), any(Pageable.class))).thenReturn(null);
      JSONObject result = taskService.getTaskAccessorSuccess(1L, 1L, pageable);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testGetTaskAccessorSuccess4() {
      Task newtask = new Task();
      newtask.setOwnerId(1L);
      when(taskDao.findById(anyLong())).thenReturn(newtask);

      List<TaskAccess> taskAccessList = Collections.emptyList();
      Page<TaskAccess> taskAccessPage = new PageImpl<>(taskAccessList);
      when(taskDao.getTaskAccessSuccessByTask(any(Task.class), any(Pageable.class))).thenReturn(taskAccessPage);
      JSONObject result = taskService.getTaskAccessorSuccess(1L, 1L, pageable);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testGetTaskAccessorSuccess5() {
      Task newtask = new Task();
      newtask.setOwnerId(1L);
      when(taskDao.findById(anyLong())).thenReturn(newtask);
      TaskAccess taskAccess = new TaskAccess();
      taskAccess.setAccessorId(1L);
      List<TaskAccess> taskAccessList = new ArrayList<>();
      taskAccessList.add(taskAccess);
      Page<TaskAccess> taskAccessPage = new PageImpl<>(taskAccessList);
      when(taskDao.getTaskAccessSuccessByTask(any(Task.class), any(Pageable.class))).thenReturn(taskAccessPage);
      when(userClient.getAccessorInfos(anyList())).thenReturn(errorResponse);
      JSONObject result = taskService.getTaskAccessorSuccess(1L, 1L, pageable);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testGetTaskAccessorSuccess6() {
      when(taskDao.findById(anyLong())).thenReturn(task);
      TaskAccess taskAccess = new TaskAccess();
      taskAccess.setAccessorId(1L);
      taskAccess.setTaskAccessId(1L);
      taskAccess.setTaskAccessStatus(TaskAccessStatus.ACCESSING);
      List<TaskAccess> taskAccessList = new ArrayList<>();
      taskAccessList.add(taskAccess);
      Page<TaskAccess> taskAccessPage = new PageImpl<>(taskAccessList);
      when(taskDao.getTaskAccessSuccessByTask(any(Task.class), any(Pageable.class))).thenReturn(taskAccessPage);

      JSONObject newmessage = new JSONObject();
      newmessage.put("ok", true);
      newmessage.put("data", new ArrayList<>());
      when(userClient.getAccessorInfos(anyList())).thenReturn(newmessage);
      JSONObject result = taskService.getTaskAccessorSuccess(1L, 1L, pageable);
      System.out.println(result);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testGetTaskAccessorFail1() {
      when(taskDao.findById(anyLong())).thenReturn(null);
      JSONObject result = taskService.getTaskAccessorFail(1L, 1L, pageable);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testGetTaskAccessorFail2() {
      Task newtask = new Task();
      newtask.setOwnerId(2L);
      when(taskDao.findById(anyLong())).thenReturn(newtask);
      JSONObject result = taskService.getTaskAccessorFail(1L, 1L, pageable);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testGetTaskAccessorFail3() {
      Task newtask = new Task();
      newtask.setOwnerId(1L);
      when(taskDao.findById(anyLong())).thenReturn(newtask);
      when(taskDao.getTaskAccessFailByTask(any(Task.class), any(Pageable.class))).thenReturn(null);
      JSONObject result = taskService.getTaskAccessorFail(1L, 1L, pageable);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testGetTaskAccessorFail4() {
      Task newtask = new Task();
      newtask.setOwnerId(1L);
      when(taskDao.findById(anyLong())).thenReturn(newtask);

      List<TaskAccess> taskAccessList = Collections.emptyList();
      Page<TaskAccess> taskAccessPage = new PageImpl<>(taskAccessList);
      when(taskDao.getTaskAccessFailByTask(any(Task.class), any(Pageable.class))).thenReturn(taskAccessPage);
      JSONObject result = taskService.getTaskAccessorFail(1L, 1L, pageable);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testGetTaskAccessorFail5() {
      Task newtask = new Task();
      newtask.setOwnerId(1L);
      when(taskDao.findById(anyLong())).thenReturn(newtask);
      TaskAccess taskAccess = new TaskAccess();
      taskAccess.setAccessorId(1L);
      List<TaskAccess> taskAccessList = new ArrayList<>();
      taskAccessList.add(taskAccess);
      Page<TaskAccess> taskAccessPage = new PageImpl<>(taskAccessList);
      when(taskDao.getTaskAccessFailByTask(any(Task.class), any(Pageable.class))).thenReturn(taskAccessPage);
      when(userClient.getAccessorInfos(anyList())).thenReturn(errorResponse);
      JSONObject result = taskService.getTaskAccessorFail(1L, 1L, pageable);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testGetTaskAccessorFail6() {
      when(taskDao.findById(anyLong())).thenReturn(task);
      TaskAccess taskAccess = new TaskAccess();
      taskAccess.setAccessorId(1L);
      taskAccess.setTaskAccessId(1L);
      taskAccess.setTaskAccessStatus(TaskAccessStatus.ACCESSING);
      List<TaskAccess> taskAccessList = new ArrayList<>();
      taskAccessList.add(taskAccess);
      Page<TaskAccess> taskAccessPage = new PageImpl<>(taskAccessList);
      when(taskDao.getTaskAccessFailByTask(any(Task.class), any(Pageable.class))).thenReturn(taskAccessPage);

      JSONObject newmessage = new JSONObject();
      newmessage.put("ok", true);
      newmessage.put("data", new ArrayList<>());
      when(userClient.getAccessorInfos(anyList())).thenReturn(newmessage);
      JSONObject result = taskService.getTaskAccessorFail(1L, 1L, pageable);
      System.out.println(result);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testCancelTaskSuccess() {
      when(taskDao.cancelTask(anyLong(),anyLong())).thenReturn("取消任务成功！");
      JSONObject result = taskService.cancelTask(1L, 1L);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testCancelTaskFailed() {
      when(taskDao.cancelTask(anyLong(),anyLong())).thenReturn("取消任务失败！");
      JSONObject result = taskService.cancelTask(1L, 1L);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testConfirmAccessors1() {
      JSONObject body = new JSONObject();
      List<Long> userIds = Collections.emptyList();
      body.put("userList", userIds);

      JSONObject result = taskService.confirmAccessors(1L, 1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testConfirmAccessors2() {
      long accessorId = 1L;
      List<Long> accessors = new ArrayList<>();
      accessors.add(accessorId);
      JSONObject body = new JSONObject();
      body.put("userList", accessors);
      when(taskDao.confirmAccessors(anyLong(), anyLong(), anyList())).thenReturn("确认接取者成功！");
      JSONObject result = taskService.confirmAccessors(1L, 1L, body);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testConfirmAccessors3() {
      long accessorId = 1L;
      List<Long> accessors = new ArrayList<>();
      accessors.add(accessorId);
      JSONObject body = new JSONObject();
      body.put("userList", accessors);
      when(taskDao.confirmAccessors(anyLong(), anyLong(), anyList())).thenReturn("确认接取者失败！");
      JSONObject result = taskService.confirmAccessors(1L, 1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testDenyAccessors1() {
      JSONObject body = new JSONObject();
      List<Long> userIds = Collections.emptyList();
      body.put("userList", userIds);

      JSONObject result = taskService.denyAccessors(1L, 1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testDenyAccessors2() {
      long accessorId = 1L;
      List<Long> accessors = new ArrayList<>();
      accessors.add(accessorId);
      JSONObject body = new JSONObject();
      body.put("userList", accessors);
      when(taskDao.denyAccessors(anyLong(), anyLong(), anyList())).thenReturn("拒绝接取者成功！");
      JSONObject result = taskService.denyAccessors(1L, 1L, body);
      assert result.get("ok").equals(true);
    }

    @Test
    public void testDenyAccessors3() {
      long accessorId = 1L;
      List<Long> accessors = new ArrayList<>();
      accessors.add(accessorId);
      JSONObject body = new JSONObject();
      body.put("userList", accessors);
      when(taskDao.denyAccessors(anyLong(), anyLong(), anyList())).thenReturn("拒绝接取者失败！");
      JSONObject result = taskService.denyAccessors(1L, 1L, body);
      assert result.get("ok").equals(false);
    }

    @Test
    public void testGetMyCollect() {
      when(taskDao.getMyCollect(anyLong(), any(Pageable.class))).thenReturn(taskPage);
      JSONObject result = taskService.getMyCollect(pageable, 1L);
      assert result.get("ok").equals(true);
    }

  @Test
  public void testSearchTaskByPagingWithException() {
    String formattedBegintime = "2024-07-01 00:00:00";
    String formattedEndtime = "2024-07-02 00:00:00";

    // 模拟 taskDao.searchTaskByPaging 方法抛出异常
    when(taskDao.searchTaskByPaging(anyString(), any(Pageable.class), any(LocalDateTime.class), any(LocalDateTime.class), anyLong(), anyLong()))
            .thenThrow(new RuntimeException("模拟异常"));

    JSONObject result = taskService.searchTaskByPaging("test", pageable, formattedBegintime, formattedEndtime, 1L, 1000L, 1L);

    // 断言返回结��的 ok 属性为 false，并且包含异常信息
    assert result.get("ok").equals(false);
    assert result.get("message").toString().contains("模拟异常");
  }

  @Test
  public void testGetTaskInfoWithException() {
    // 模拟 taskDao.findById 方法抛出异常
    when(taskDao.findById(anyLong())).thenThrow(new RuntimeException("模拟异常"));

    JSONObject result = taskService.getTaskInfo(1L, 1L);

    // 断言返回结��的 ok 属性为 false，并且包含异常信息
    assert result.get("ok").equals(false);
    assert result.get("message").toString().contains("模拟异常");
  }

  @Test
  public void testGetTaskCommentsWithException() {
    // 模拟 taskCommentDao.getTaskComments 方法抛出异常
    when(taskCommentDao.getTaskComments(anyLong(), any(Pageable.class))).thenThrow(new RuntimeException("模拟异常"));

    JSONObject result = taskService.getTaskComments(1L, pageable, 1L);

    // 断言返回结��的 ok 属性为 false，并且包含异常信息
    assert result.get("ok").equals(false);
    assert result.get("message").toString().contains("模拟异常");
  }

  @Test
  public void testGetTaskMessagesWithException() {
    // 模拟 taskMessageDao.getTaskMessages 方法抛出异常
    when(taskMessageDao.getTaskMessages(anyLong(), any(Pageable.class))).thenThrow(new RuntimeException("模拟异常"));

    JSONObject result = taskService.getTaskMessages(1L, pageable, 1L);

    // 断言返回结��的 ok 属性为 false，并且包含异常信息
    assert result.get("ok").equals(false);
    assert result.get("message").toString().contains("模拟异常");
  }

  @Test
  public void testLikeCommentWithException() {
    // 模拟 taskCommentDao.likeCommentByTaskCommentId 方法抛出异常
    when(taskCommentDao.likeCommentByTaskCommentId(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

    JSONObject result = taskService.likeComment(1L, 1L);

    // 断言返回结��的 ok 属性为 false，并且包含异常信息
    assert result.get("ok").equals(false);
    assert result.get("message").toString().contains("模拟异常");
  }

  @Test
  public void testUnlikeCommentWithException() {
    // 模拟 taskCommentDao.unlikeCommentByTaskCommentId 方法抛出异常
    when(taskCommentDao.unlikeCommentByTaskCommentId(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

    JSONObject result = taskService.unlikeComment(1L, 1L);

    // 断言返回结��的 ok 属性为 false，并且包含异常信息
    assert result.get("ok").equals(false);
    assert result.get("message").toString().contains("模拟异常");
  }

  @Test
  public void testLikeMessageWithException() {
    // 模拟 taskMessageDao.likeMessageByTaskMessageId 方法抛出异常
    when(taskMessageDao.likeMessageByTaskMessageId(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

    JSONObject result = taskService.likeMessage(1L, 1L);

    // 断言返回结��的 ok 属性为 false，并且包含异常信息
    assert result.get("ok").equals(false);
    assert result.get("message").toString().contains("模拟异常");
  }

  @Test
  public void testUnlikeMessageWithException() {
    // 模拟 taskMessageDao.unlikeMessageByTaskMessageId 方法抛出异常
    when(taskMessageDao.unlikeMessageByTaskMessageId(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

    JSONObject result = taskService.unlikeMessage(1L, 1L);

    // 断言返回结��的 ok 属性为 false，并且包含异常信息
    assert result.get("ok").equals(false);
    assert result.get("message").toString().contains("模拟异常");
  }

  @Test
  public void testCollectTaskWithException() {
    // 模拟 taskDao.collectTaskByTaskId 方法抛出异常
    when(taskDao.collectTaskByTaskId(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

    JSONObject result = taskService.collectTask(1L, 1L);

    // 断言返回结��的 ok 属性为 false，并且包含异常信息
    assert result.get("ok").equals(false);
    assert result.get("message").toString().contains("模拟异常");
  }

  @Test
  public void testUncollectTaskWithException() {
    // 模拟 taskDao.uncollectTaskByTaskId 方法抛出异常
    when(taskDao.uncollectTaskByTaskId(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

    JSONObject result = taskService.uncollectTask(1L, 1L);

    // 断言返回结��的 ok 属性为 false，并且包含异常信息
    assert result.get("ok").equals(false);
    assert result.get("message").toString().contains("模拟异常");
  }

  @Test
  public void testAccessTaskWithException() {
    // 模拟 taskDao.accessTaskByTaskId 方法抛出异常
    when(taskDao.accessTaskByTaskId(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

    JSONObject result = taskService.accessTask(1L, 1L);

    // 断言返回结��的 ok 属性为 false，并且包含异常信息
    assert result.get("ok").equals(false);
    assert result.get("message").toString().contains("模拟异常");
  }

  @Test
  public void testUnaccessTaskWithException() {
    // 模拟 taskDao.unaccessTaskByTaskId 方法抛出异常
    when(taskDao.unaccessTaskByTaskId(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

    JSONObject result = taskService.unaccessTask(1L, 1L);

    // 断言返回结��的 ok 属性为 false，并且包含异常信息
    assert result.get("ok").equals(false);
    assert result.get("message").toString().contains("模拟异常");
  }

  @Test
  public void testPublishMessageWithException() {
    // 模拟 taskMessageDao.putMessage 方法抛出异常
    when(taskMessageDao.putMessage(anyLong(), anyLong(), anyString())).thenThrow(new RuntimeException("模拟异常"));

    JSONObject body = new JSONObject();
    body.put("content", "test");
    JSONObject result = taskService.publishMessage(1L, 1L, body);

    // 断言返回结��的 ok 属性为 false，并且包含异常信息
    assert result.get("ok").equals(false);
    assert result.get("message").toString().contains("模拟异常");
  }

  @Test
  public void testDeleteMessageWithException() {
    // 模拟 taskMessageDao.deleteMessage 方法抛出异常
    when(taskMessageDao.deleteMessage(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

    JSONObject result = taskService.deleteMessage(1L, 1L);

    // 断言返回结��的 ok 属性为 false，并且包含异常信息
    assert result.get("ok").equals(false);
    assert result.get("message").toString().contains("模拟异常");
  }

  @Test
  public void testPublishCommentWithException() {
    // 模拟 taskCommentDao.putComment 方法抛出异常
    when(taskCommentDao.putComment(anyLong(), anyLong(), anyString(), anyByte())).thenThrow(new RuntimeException("模拟异常"));

    JSONObject body = new JSONObject();
    body.put("content", "test");
    body.put("rating", 1);
    JSONObject result = taskService.publishComment(1L, 1L, body);

    // 断言返回结��的 ok 属性为 false，并且包含异常信息
    assert result.get("ok").equals(false);
    assert result.get("message").toString().contains("模拟异常");
  }

  @Test
  public void testDeleteCommentWithException() {
    // 模拟 taskCommentDao.deleteComment 方法抛出异常
    when(taskCommentDao.deleteComment(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

    JSONObject result = taskService.deleteComment(1L, 1L);

    // 断言返回结��的 ok 属性为 false，并且包含异常信息
    assert result.get("ok").equals(false);
    assert result.get("message").toString().contains("模拟异常");
  }

  @Test
  public void testPublishTaskWithException() {
    // 模拟 taskDao.publishTask 方法抛出异常
    when(taskDao.publishTask(anyLong(), anyString(), anyString(), anyLong(), anyInt(), anyList())).thenThrow(new RuntimeException("模拟异常"));

    JSONObject body = new JSONObject();
    body.put("title", "test");
    body.put("description", "test");
    body.put("price", 1000);
    body.put("maxAccess", 1);
    body.put("images", "test");
    JSONObject result = taskService.publishTask(1L, body);

    // 断言返回结��的 ok 属性为 false，并且包含异常信息
    assert result.get("ok").equals(false);
  }

  @Test
  public void testGetMyTaskWithException() {
    // 模拟 taskDao.getMyTask 方法抛出异常
    when(taskDao.getMyTask(anyLong(), any(Pageable.class))).thenThrow(new RuntimeException("模拟异常"));
    JSONObject result = taskService.getMyTask(pageable, 1L);
    assert result.get("ok").equals(false);
  }

  @Test
  public void testMyAccessedTaskWithException() {
    // 模拟 taskDao.getMyAccessedTask 方法抛出异常
    when(taskDao.getMyAccessedTask(anyLong(), any(Pageable.class))).thenThrow(new RuntimeException("模拟异常"));
    JSONObject result = taskService.getMyAccessedTask(pageable, 1L);
    assert result.get("ok").equals(false);
  }

  @Test
  public void testGetTaskAccessorInfoWithException() {
    // 模拟 taskDao.findById 方法抛出异常
    when(taskDao.findById(anyLong())).thenThrow(new RuntimeException("模拟异常"));
    JSONObject result = taskService.getTaskAccessorInfo(1L, 1L, pageable);
    assert result.get("ok").equals(false);
  }

  @Test
  public void testGetTaskAccessorSuccessWithException() {
    // 模拟 taskDao.findById 方法抛出异常
    when(taskDao.findById(anyLong())).thenThrow(new RuntimeException("模拟异常"));
    JSONObject result = taskService.getTaskAccessorSuccess(1L, 1L, pageable);
    assert result.get("ok").equals(false);
  }

  @Test
  public void testGetTaskAccessorFailWithException() {
    // 模拟 taskDao.findById 方法抛出异常
    when(taskDao.findById(anyLong())).thenThrow(new RuntimeException("模拟异常"));
    JSONObject result = taskService.getTaskAccessorFail(1L, 1L, pageable);
    assert result.get("ok").equals(false);
  }

  @Test
  public void testCancelTaskWithException() {
    // 模拟 taskDao.cancelTask 方法抛出异常
    when(taskDao.cancelTask(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));
    JSONObject result = taskService.cancelTask(1L, 1L);
    assert result.get("ok").equals(false);
  }

  @Test
  public void testConfirmAccessorsWithException() {
    // 模拟 taskDao.confirmAccessors 方法抛出异常
    when(taskDao.confirmAccessors(anyLong(), anyLong(), anyList())).thenThrow(new RuntimeException("模拟异常"));
    JSONObject result = taskService.confirmAccessors(1L, 1L, new JSONObject());
    assert result.get("ok").equals(false);
  }

  @Test
  public void testDenyAccessorsWithException() {
    // 模拟 taskDao.denyAccessors 方法抛出异常
    when(taskDao.denyAccessors(anyLong(), anyLong(), anyList())).thenThrow(new RuntimeException("模拟异常"));
    JSONObject result = taskService.denyAccessors(1L, 1L, new JSONObject());
    assert result.get("ok").equals(false);
  }

  @Test
  public void testGetMyCollectWithException() {
    // 模拟 taskDao.getMyCollect 方法抛出异常
    when(taskDao.getMyCollect(anyLong(), any(Pageable.class))).thenThrow(new RuntimeException("模拟异常"));
    JSONObject result = taskService.getMyCollect(pageable, 1L);
    assert result.get("ok").equals(false);
  }

}
