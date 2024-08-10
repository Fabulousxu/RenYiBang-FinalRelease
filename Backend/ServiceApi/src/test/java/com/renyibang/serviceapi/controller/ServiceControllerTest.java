package com.renyibang.serviceapi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.serviceapi.service.ServiceService;
import com.renyibang.serviceapi.util.ResponseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ServiceControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ServiceService serviceService;

    @InjectMocks
    private ServiceController serviceController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(serviceController).build();
    }

    @Test
    public void searchServiceTest1() throws Exception {
        when(serviceService.searchServiceByPaging(anyString(), any(), anyString(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/search")
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
    public void searchServiceTest2() throws Exception {
        when(serviceService.searchServiceByPaging(anyString(), any(), anyString(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/search")
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
    public void searchServiceTest3() throws Exception {
        when(serviceService.searchServiceByPaging(anyString(), any(), anyString(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/search")
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
    public void searchServiceTest4() throws Exception {
        when(serviceService.searchServiceByPaging(anyString(), any(), anyString(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/search")
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
    public void searchServiceTest5() throws Exception {
        when(serviceService.searchServiceByPaging(anyString(), any(), anyString(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/search")
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
    public void searchServiceTest6() throws Exception {
        when(serviceService.searchServiceByPaging(anyString(), any(), anyString(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/search")
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
    public void getServiceInfoTest() throws Exception {
        when(serviceService.getServiceInfo(anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getServiceCommentTest1() throws Exception {
        when(serviceService.getServiceComments(anyLong(), any(),  anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/1/comment")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .param("order", "likes")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getServiceCommentTest2() throws Exception {
        when(serviceService.getServiceComments(anyLong(), any(),  anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/1/comment")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .param("order", "time")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getServiceCommentTest3() throws Exception {
        when(serviceService.getServiceComments(anyLong(), any(),  anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/1/comment")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .param("order", "other")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getServiceCommentTest4() throws Exception {
        when(serviceService.getServiceComments(anyLong(), any(),  anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/1/comment")
                                .param("pageSize", "0")
                                .param("pageIndex", "1")
                                .param("order", "likes")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getServiceCommentTest5() throws Exception {
        when(serviceService.getServiceComments(anyLong(), any(),  anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/1/comment")
                                .param("pageSize", "1")
                                .param("pageIndex", "-1")
                                .param("order", "likes")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getServiceMessageTest1() throws Exception {
        when(serviceService.getServiceMessages(anyLong(), any(),  anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/1/message")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .param("order", "likes")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getServiceMessageTest2() throws Exception {
        when(serviceService.getServiceMessages(anyLong(), any(),  anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/1/message")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .param("order", "time")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getServiceMessageTest3() throws Exception {
        when(serviceService.getServiceMessages(anyLong(), any(),  anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/1/message")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .param("order", "other")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getServiceMessageTest4() throws Exception {
        when(serviceService.getServiceMessages(anyLong(), any(),  anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/1/message")
                                .param("pageSize", "0")
                                .param("pageIndex", "1")
                                .param("order", "likes")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getServiceMessageTest5() throws Exception {
        when(serviceService.getServiceMessages(anyLong(), any(),  anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/1/message")
                                .param("pageSize", "1")
                                .param("pageIndex", "-1")
                                .param("order", "likes")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void likeCommentTest() throws Exception {
        when(serviceService.likeComment(anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        put("/api/service/comment/1/like")
                                .header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void unlikeCommentTest() throws Exception {
        when(serviceService.unlikeComment(anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        delete("/api/service/comment/1/unlike")
                                .header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void likeMessageTest() throws Exception {
        when(serviceService.likeMessage(anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        put("/api/service/message/1/like")
                                .header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void unlikeMessageTest() throws Exception {
        when(serviceService.unlikeMessage(anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        delete("/api/service/message/1/unlike")
                                .header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void collectServiceTest() throws Exception {
        when(serviceService.collectService(anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        put("/api/service/1/collect")
                                .header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void uncollectServiceTest() throws Exception {
        when(serviceService.uncollectService(anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        delete("/api/service/1/uncollect")
                                .header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void accessServiceTest() throws Exception {
        when(serviceService.accessService(anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        put("/api/service/1/access")
                                .header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void unaccessServiceTest() throws Exception {
        when(serviceService.unaccessService(anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        delete("/api/service/1/unaccess")
                                .header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void publishMessageTest() throws Exception {
        when(serviceService.publishMessage(anyLong(), anyLong(), any(JSONObject.class)))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        put("/api/service/1/message")
                                .header("userId", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void deleteMessageTest() throws Exception {
        when(serviceService.deleteMessage(anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        delete("/api/service/message/1")
                                .header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void publishCommentTest() throws Exception {
        when(serviceService.publishComment(anyLong(), anyLong(), any(JSONObject.class)))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        put("/api/service/1/comment")
                                .header("userId", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void deleteCommentTest() throws Exception {
        when(serviceService.deleteComment(anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        delete("/api/service/comment/1")
                                .header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void publishServiceTest() throws Exception {
        when(serviceService.publishService(anyLong(), any(JSONObject.class)))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        post("/api/service/issue")
                                .header("userId", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getServiceDtoByIdTest() throws Exception {
        when(serviceService.getServiceDtoById(anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/getService/1")
                                .header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getServiceOwnerIdTest() throws Exception {
        when(serviceService.getServiceOwnerId(anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/1/ownerId")
                                .header("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getMyServiceTest1() throws Exception {
        when(serviceService.getMyService(any(Pageable.class), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/initiator/self")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getMyServiceTest2() throws Exception {
        when(serviceService.getMyService(any(Pageable.class), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/initiator/self")
                                .param("pageSize", "0")
                                .param("pageIndex", "1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getMyServiceTest3() throws Exception {
        when(serviceService.getMyService(any(Pageable.class), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/initiator/self")
                                .param("pageSize", "1")
                                .param("pageIndex", "-1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getMyAccessedServiceTest1() throws Exception {
        when(serviceService.getMyAccessedService(any(Pageable.class), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/recipient/self")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getMyAccessedServiceTest2() throws Exception {
        when(serviceService.getMyAccessedService(any(Pageable.class), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/recipient/self")
                                .param("pageSize", "0")
                                .param("pageIndex", "1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getMyAccessedServiceTest3() throws Exception {
        when(serviceService.getMyAccessedService(any(Pageable.class), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/recipient/self")
                                .param("pageSize", "1")
                                .param("pageIndex", "-1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getServiceAccessorInfoTest() throws Exception {
        when(serviceService.getServiceAccessorInfo(anyLong(), anyLong(), any(Pageable.class)))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/1/select/info")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getServiceAccessorSuccessTest() throws Exception {
        when(serviceService.getServiceAccessorSuccess(anyLong(), anyLong(), any(Pageable.class)))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/1/select/success")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getServiceAccessorFailTest() throws Exception {
        when(serviceService.getServiceAccessorFail(anyLong(), anyLong(), any(Pageable.class)))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/1/select/fail")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void cancelServiceTest() throws Exception {
        when(serviceService.cancelService(anyLong(), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        delete("/api/service/1/cancel")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void confirmAccessorsTest() throws Exception {
        when(serviceService.confirmAccessors(anyLong(), anyLong(), any(JSONObject.class)))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        put("/api/service/1/select/confirm")
                                .header("userId", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void denyAccessorsTest() throws Exception {
        when(serviceService.denyAccessors(anyLong(), anyLong(), any(JSONObject.class)))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        put("/api/service/1/select/deny")
                                .header("userId", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getMyCollectTest1() throws Exception {
        when(serviceService.getMyCollect(any(Pageable.class), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/mycollect")
                                .param("pageSize", "1")
                                .param("pageIndex", "1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void getMyCollectTest2() throws Exception {
        when(serviceService.getMyCollect(any(Pageable.class), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/mycollect")
                                .param("pageSize", "0")
                                .param("pageIndex", "1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void getMyCollectTest3() throws Exception {
        when(serviceService.getMyCollect(any(Pageable.class), anyLong()))
                .thenReturn(ResponseUtil.success());
        mockMvc
                .perform(
                        get("/api/service/mycollect")
                                .param("pageSize", "1")
                                .param("pageIndex", "-1")
                                .header("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }
}