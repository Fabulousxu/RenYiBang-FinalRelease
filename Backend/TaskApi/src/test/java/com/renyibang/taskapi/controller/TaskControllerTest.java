package com.renyibang.taskapi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.util.ResponseUtil;
import com.renyibang.taskapi.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    private TaskController taskController;
    @Mock
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    public void searchTaskTest1() throws Exception {
        when(taskService.searchTaskByPaging(
                anyString(), any(), anyString(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/search")
                                .param("keyword", "test")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .param("order", "time")
                                .param("timeBegin", "")
                                .param("timeEnd", "")
                                .param("priceLow", "0")
                                .param("priceHigh", "-1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void searchTaskTest2() throws Exception {
        when(taskService.searchTaskByPaging(
                anyString(), any(), anyString(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/search")
                                .param("keyword", "test")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .param("order", "rating")
                                .param("timeBegin", "")
                                .param("timeEnd", "")
                                .param("priceLow", "0")
                                .param("priceHigh", "-1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void searchTaskTest3() throws Exception {
        when(taskService.searchTaskByPaging(
                anyString(), any(), anyString(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/search")
                                .param("keyword", "test")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .param("order", "other")
                                .param("timeBegin", "")
                                .param("timeEnd", "")
                                .param("priceLow", "0")
                                .param("priceHigh", "-1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void searchTaskTest4() throws Exception {
        when(taskService.searchTaskByPaging(
                anyString(), any(), anyString(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/search")
                                .param("keyword", "test")
                                .param("pageSize", "0")
                                .param("pageIndex", "1")
                                .param("order", "time")
                                .param("timeBegin", "")
                                .param("timeEnd", "")
                                .param("priceLow", "0")
                                .param("priceHigh", "-1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void searchTaskTest5() throws Exception {
        when(taskService.searchTaskByPaging(
                anyString(), any(), anyString(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/search")
                                .param("keyword", "test")
                                .param("pageSize", "1")
                                .param("pageIndex", "-1")
                                .param("order", "time")
                                .param("timeBegin", "")
                                .param("timeEnd", "")
                                .param("priceLow", "0")
                                .param("priceHigh", "-1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void searchTaskTest6() throws Exception {
        when(taskService.searchTaskByPaging(
                anyString(), any(), anyString(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/search")
                                .param("keyword", "")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .param("order", "time")
                                .param("timeBegin", "")
                                .param("timeEnd", "")
                                .param("priceLow", "0")
                                .param("priceHigh", "-1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getTaskInfoTest() throws Exception {
        when(taskService.getTaskInfo(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getTaskCommentTest1() throws Exception {
        when(taskService.getTaskComments(anyLong(), any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/1/comment")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .param("order", "likes")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getTaskCommentTest2() throws Exception {
        when(taskService.getTaskComments(anyLong(), any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/1/comment")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .param("order", "time")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getTaskCommentTest3() throws Exception {
        when(taskService.getTaskComments(anyLong(), any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/1/comment")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .param("order", "other")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getTaskCommentTest4() throws Exception {
        when(taskService.getTaskComments(anyLong(), any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/1/comment")
                                .param("pageSize", "0")
                                .param("pageIndex", "1")
                                .param("order", "likes")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getTaskCommentTest5() throws Exception {
        when(taskService.getTaskComments(anyLong(), any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/1/comment")
                                .param("pageSize", "1")
                                .param("pageIndex", "-1")
                                .param("order", "likes")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getTaskMessageTest1() throws Exception {
        when(taskService.getTaskMessages(anyLong(), any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/1/message")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .param("order", "likes")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getTaskMessageTest2() throws Exception {
        when(taskService.getTaskMessages(anyLong(), any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/1/message")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .param("order", "time")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getTaskMessageTest3() throws Exception {
        when(taskService.getTaskMessages(anyLong(), any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/1/message")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .param("order", "other")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getTaskMessageTest4() throws Exception {
        when(taskService.getTaskMessages(anyLong(), any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/1/message")
                                .param("pageSize", "0")
                                .param("pageIndex", "1")
                                .param("order", "likes")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getTaskMessageTest5() throws Exception {
        when(taskService.getTaskMessages(anyLong(), any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/1/message")
                                .param("pageSize", "1")
                                .param("pageIndex", "-1")
                                .param("order", "likes")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void likeCommentTest() throws Exception {
        when(taskService.likeComment(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(put("/api/task/comment/1/like").header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void unlikeCommentTest() throws Exception {
        when(taskService.unlikeComment(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(delete("/api/task/comment/1/unlike").header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void likeMessageTest() throws Exception {
        when(taskService.likeMessage(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(put("/api/task/message/1/like").header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void unlikeMessageTest() throws Exception {
        when(taskService.unlikeMessage(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(delete("/api/task/message/1/unlike").header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void collectTaskTest() throws Exception {
        when(taskService.collectTask(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(put("/api/task/1/collect").header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void uncollectTaskTest() throws Exception {
        when(taskService.uncollectTask(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(delete("/api/task/1/uncollect").header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void accessTaskTest() throws Exception {
        when(taskService.accessTask(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(put("/api/task/1/access").header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void unaccessTaskTest() throws Exception {
        when(taskService.unaccessTask(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(delete("/api/task/1/unaccess").header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void publishMessageTest() throws Exception {
        when(taskService.publishMessage(anyLong(), anyLong(), any(JSONObject.class)))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        put("/api/task/1/message")
                                .header("userId", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void deleteMessageTest() throws Exception {
        when(taskService.deleteMessage(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(delete("/api/task/message/1").header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void publishCommentTest() throws Exception {
        when(taskService.publishComment(anyLong(), anyLong(), any(JSONObject.class)))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        put("/api/task/1/comment")
                                .header("userId", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void deleteCommentTest() throws Exception {
        when(taskService.deleteComment(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(delete("/api/task/comment/1").header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void publishTaskTest() throws Exception {
        when(taskService.publishTask(anyLong(), any(JSONObject.class)))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        post("/api/task/issue")
                                .header("userId", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getTaskDtoByIdTest() throws Exception {
        when(taskService.getTaskDtoById(anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(get("/api/task/getTask/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getTaskOwnerIdTest() throws Exception {
        when(taskService.getTaskOwnerId(anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(get("/api/task/1/ownerId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getMyTaskTest1() throws Exception {
        when(taskService.getMyTask(any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/initiator/self")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getMyTaskTest2() throws Exception {
        when(taskService.getMyTask(any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/initiator/self")
                                .param("pageSize", "0")
                                .param("pageIndex", "1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getMyTaskTest3() throws Exception {
        when(taskService.getMyTask(any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/initiator/self")
                                .param("pageSize", "1")
                                .param("pageIndex", "-1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getMyAccessedTaskTest1() throws Exception {
        when(taskService.getMyAccessedTask(any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/recipient/self")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getMyAccessedTaskTest2() throws Exception {
        when(taskService.getMyAccessedTask(any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/recipient/self")
                                .param("pageSize", "0")
                                .param("pageIndex", "1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getMyAccessedTaskTest3() throws Exception {
        when(taskService.getMyAccessedTask(any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/recipient/self")
                                .param("pageSize", "1")
                                .param("pageIndex", "-1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getTaskAccessorInfoTest() throws Exception {
        when(taskService.getTaskAccessorInfo(anyLong(), anyLong(), any())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/1/select/info")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getTaskAccessorSuccessTest() throws Exception {
        when(taskService.getTaskAccessorSuccess(anyLong(), anyLong(), any())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/1/select/success")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getTaskAccessorFailTest() throws Exception {
        when(taskService.getTaskAccessorFail(anyLong(), anyLong(), any())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/1/select/fail")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void cancelTaskTest() throws Exception {
        when(taskService.cancelTask(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(delete("/api/task/1/cancel").header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void confirmAccessorsTest() throws Exception {
        when(taskService.confirmAccessors(anyLong(), anyLong(), any(JSONObject.class)))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        put("/api/task/1/select/confirm")
                                .header("userId", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void denyAccessorsTest() throws Exception {
        when(taskService.denyAccessors(anyLong(), anyLong(), any(JSONObject.class)))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        put("/api/task/1/select/deny")
                                .header("userId", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getMyCollectTest1() throws Exception {
        when(taskService.getMyCollect(any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/mycollect")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getMyCollectTest2() throws Exception {
        when(taskService.getMyCollect(any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/mycollect")
                                .param("pageSize", "0")
                                .param("pageIndex", "1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getMyCollectTest3() throws Exception {
        when(taskService.getMyCollect(any(), anyLong())).thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/task/mycollect")
                                .param("pageSize", "1")
                                .param("pageIndex", "-1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }
}
