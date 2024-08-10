package com.renyibang.serviceapi.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.client.UserClient;
import com.renyibang.serviceapi.dao.ServiceCommentDao;
import com.renyibang.serviceapi.dao.ServiceDao;
import com.renyibang.serviceapi.dao.ServiceMessageDao;
import com.renyibang.serviceapi.entity.Service;
import com.renyibang.serviceapi.entity.ServiceAccess;
import com.renyibang.serviceapi.entity.ServiceComment;
import com.renyibang.serviceapi.entity.ServiceMessage;
import com.renyibang.serviceapi.service.serviceImpl.ServiceServiceImpl;
import com.renyibang.serviceapi.enums.ServiceAccessStatus;
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
public class ServiceServiceImplTest {
    @InjectMocks
    private ServiceServiceImpl serviceService;

    @Mock
    private ServiceDao serviceDao;

    @Mock
    private ServiceCommentDao serviceCommentDao;

    @Mock
    private ServiceMessageDao serviceMessageDao;

    @Mock
    private UserClient userClient;

    private MockMvc mockMvc;

    private Service service;
    private Pageable pageable;
    private DateTimeFormatter formatter;
    private List<Service> serviceList;
    private Page<Service> servicePage;
    private JSONObject successResponse;
    private JSONObject errorResponse;
    private JSONObject userInfosForTest;
    private JSONObject userInfoForTest;
    private  ArrayList<JSONObject> userInfoList;

    @BeforeEach
    public void setUp() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        mockMvc = MockMvcBuilders.standaloneSetup(serviceService).build();
        service = new Service();
        service.setOwnerId(1L);
        service.setTitle("test");
        service.setDescription("test");
        service.setPrice(1000);
        service.setCreatedAt(LocalDateTime.parse("2024-06-01 12:00:00", formatter));
        service.setImages("test");
        serviceList = new ArrayList<>();
        serviceList.add(service);
        servicePage = new PageImpl<>(serviceList);
        pageable = PageRequest.of(0, 10);

        successResponse = new JSONObject();
        errorResponse = new JSONObject();
        successResponse.put("ok", true);
        errorResponse.put("ok", false);
        successResponse.put("data", "111");
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
    public void testSearchServiceByPaging() {
        JSONArray userIds = new JSONArray();
        userIds.add(1L);
        JSONObject userObject = new JSONObject();
        userObject.put("ok", true);
        userObject.put("data", userIds);
        String formattedBegintime = "2024-07-01 00:00:00";
        String formattedEndtime = "2024-07-02 00:00:00";

        when(serviceDao.searchServiceByPaging(anyString(),any(Pageable.class),any(LocalDateTime.class),any(LocalDateTime.class),anyLong(),anyLong())).thenReturn(servicePage);
        when(userClient.getUserInfos(anyList())).thenReturn(userObject);

        JSONObject result = serviceService.searchServiceByPaging("test", pageable, formattedBegintime, formattedEndtime, 1L, 1000L, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testSearchServiceByPagingWithEmptyUserIds() {
        JSONArray userIds = new JSONArray();
        JSONObject userObject = new JSONObject();
        userObject.put("ok", true);
        userObject.put("data", userIds);
        Page<Service> services = new PageImpl<>(Collections.emptyList());
        String formattedBegintime = "2024-07-01 00:00:00";
        String formattedEndtime = "2024-07-02 00:00:00";
        when(serviceDao.searchServiceByPaging(anyString(),any(Pageable.class),any(LocalDateTime.class),any(LocalDateTime.class),anyLong(),anyLong())).thenReturn(services);
        when(userClient.getUserInfos(anyList())).thenReturn(userObject);

        JSONObject result = serviceService.searchServiceByPaging("test", pageable, formattedBegintime, formattedEndtime, 1L, 1000L, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testSearchServiceByPagingWithErrorResponse() {
        JSONArray userIds = new JSONArray();
        userIds.add(1L);
        JSONObject userObject = new JSONObject();
        userObject.put("ok", false);
        userObject.put("data", userIds);
        String formattedBegintime = "2024-07-01 00:00:00";
        String formattedEndtime = "2024-07-02 00:00:00";

        when(serviceDao.searchServiceByPaging(anyString(),any(Pageable.class),any(LocalDateTime.class),any(LocalDateTime.class),anyLong(),anyLong())).thenReturn(servicePage);
        when(userClient.getUserInfos(anyList())).thenReturn(userObject);

        JSONObject result = serviceService.searchServiceByPaging("test", pageable, formattedBegintime, formattedEndtime, 1L, 1000L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testSearchServiceByPagingWithInvalidParams() {
        JSONObject result = serviceService.searchServiceByPaging("test", pageable, "invalid", "invalid", -1L, -2L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testSearchServiceByPagingWithBeginIsAfterEnd() {
        String formattedBegintime = "2024-07-02 00:00:00";
        String formattedEndtime = "2024-07-01 00:00:00";
        JSONObject result = serviceService.searchServiceByPaging("test", pageable, formattedBegintime, formattedEndtime, 1L, 1000L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testSearchServiceByPagingWithNegativePrice() {
        String formattedBegintime = "2024-07-01 00:00:00";
        String formattedEndtime = "2024-07-02 00:00:00";
        JSONObject result = serviceService.searchServiceByPaging("test", pageable, formattedBegintime, formattedEndtime, -1000L, -1000L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testSearchServiceByPagingWithLowerBoundPrice() {
        String formattedBegintime = "2024-07-01 00:00:00";
        String formattedEndtime = "2024-07-02 00:00:00";
        JSONObject result = serviceService.searchServiceByPaging("test", pageable, formattedBegintime, formattedEndtime, 1L, 0L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceInfo() {
        when(serviceDao.findById(anyLong())).thenReturn(service);
        when(userClient.getUserInfo(anyLong())).thenReturn(userInfoForTest);
        JSONObject result = serviceService.getServiceInfo(1L, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceInfoWithEmptyService() {
        when(serviceDao.findById(anyLong())).thenReturn(null);
        JSONObject result = serviceService.getServiceInfo(1L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceInfoWithEmptyUser() {
        when(serviceDao.findById(anyLong())).thenReturn(service);
        when(userClient.getUserInfo(anyLong())).thenReturn(errorResponse);
        JSONObject result = serviceService.getServiceInfo(1L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceComments() {
        ServiceComment serviceComment = new ServiceComment();
        serviceComment.setCommenterId(1L);
        serviceComment.setService(service);
        serviceComment.setContent("test");
        serviceComment.setRating((byte) 50);
        serviceComment.setCreatedAt(LocalDateTime.parse("2024-06-01 12:00:00", formatter));
        Page<ServiceComment> serviceCommentPage = new PageImpl<>(Collections.singletonList(serviceComment));
        when(serviceCommentDao.getServiceComments(anyLong(), any(Pageable.class))).thenReturn(serviceCommentPage);
        when(userClient.getUserInfos(anyList())).thenReturn(userInfosForTest);
        when(serviceCommentDao.isLiked(anyLong(), anyLong())).thenReturn(true);

        JSONObject result = serviceService.getServiceComments(1L, pageable, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceCommentsWithEmptyComments() {
        Page<ServiceComment> serviceCommentPage = new PageImpl<>(Collections.emptyList());
        when(serviceCommentDao.getServiceComments(anyLong(), any(Pageable.class))).thenReturn(serviceCommentPage);
        JSONObject result = serviceService.getServiceComments(1L, pageable, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceCommentsWithEmptyUser() {
        ServiceComment serviceComment = new ServiceComment();
        serviceComment.setCommenterId(1L);
        serviceComment.setService(service);
        serviceComment.setContent("test");
        serviceComment.setRating((byte) 50);
        serviceComment.setCreatedAt(LocalDateTime.parse("2024-06-01 12:00:00", formatter));
        Page<ServiceComment> serviceCommentPage = new PageImpl<>(Collections.singletonList(serviceComment));
        when(serviceCommentDao.getServiceComments(anyLong(), any(Pageable.class))).thenReturn(serviceCommentPage);
        when(userClient.getUserInfos(anyList())).thenReturn(errorResponse);

        JSONObject result = serviceService.getServiceComments(1L, pageable, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceMessages() {
        ServiceMessage serviceMessage = new ServiceMessage();
        serviceMessage.setMessagerId(1L);
        serviceMessage.setService(service);
        serviceMessage.setContent("test");
        serviceMessage.setCreatedAt(LocalDateTime.parse("2024-06-01 12:00:00", formatter));
        Page<ServiceMessage> serviceMessagePage = new PageImpl<>(Collections.singletonList(serviceMessage));
        when(serviceMessageDao.getServiceMessages(anyLong(), any(Pageable.class))).thenReturn(serviceMessagePage);
        when(userClient.getUserInfos(anyList())).thenReturn(userInfosForTest);

        JSONObject result = serviceService.getServiceMessages(1L,  pageable, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceMessagesWithEmptyMessages() {
        Page<ServiceMessage> serviceMessagePage = new PageImpl<>(Collections.emptyList());
        when(serviceMessageDao.getServiceMessages(anyLong(), any(Pageable.class))).thenReturn(serviceMessagePage);
        JSONObject result = serviceService.getServiceMessages(1L, pageable, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceMessagesWithEmptyUser() {
        ServiceMessage serviceMessage = new ServiceMessage();
        serviceMessage.setMessagerId(1L);
        serviceMessage.setService(service);
        serviceMessage.setContent("test");
        serviceMessage.setCreatedAt(LocalDateTime.parse("2024-06-01 12:00:00", formatter));
        Page<ServiceMessage> serviceMessagePage = new PageImpl<>(Collections.singletonList(serviceMessage));
        when(serviceMessageDao.getServiceMessages(anyLong(), any(Pageable.class))).thenReturn(serviceMessagePage);
        when(userClient.getUserInfos(anyList())).thenReturn(errorResponse);

        JSONObject result = serviceService.getServiceMessages(1L, pageable, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testLikeCommentSuccess() {
        when(serviceCommentDao.likeCommentByServiceCommentId(anyLong(), anyLong())).thenReturn("点赞成功！");
        JSONObject result = serviceService.likeComment(1L, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testLikeCommentFailed() {
        when(serviceCommentDao.likeCommentByServiceCommentId(anyLong(), anyLong())).thenReturn("点赞失败！");
        JSONObject result = serviceService.likeComment(1L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testUnlikeCommentSuccess() {
        when(serviceCommentDao.unlikeCommentByServiceCommentId(anyLong(), anyLong())).thenReturn("取消点赞成功！");
        JSONObject result = serviceService.unlikeComment(1L, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testUnlikeCommentFailed() {
        when(serviceCommentDao.unlikeCommentByServiceCommentId(anyLong(), anyLong())).thenReturn("取消点赞失败！");
        JSONObject result = serviceService.unlikeComment(1L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testLikeMessageSuccess() {
        when(serviceMessageDao.likeMessageByServiceMessageId(anyLong(), anyLong())).thenReturn("点赞成功！");
        JSONObject result = serviceService.likeMessage(1L, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testLikeMessageFailed() {
        when(serviceMessageDao.likeMessageByServiceMessageId(anyLong(), anyLong())).thenReturn("点赞失败！");
        JSONObject result = serviceService.likeMessage(1L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testUnlikeMessageSuccess() {
        when(serviceMessageDao.unlikeMessageByServiceMessageId(anyLong(), anyLong())).thenReturn("取消点赞成功！");
        JSONObject result = serviceService.unlikeMessage(1L, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testUnlikeMessageFailed() {
        when(serviceMessageDao.unlikeMessageByServiceMessageId(anyLong(), anyLong())).thenReturn("取消点赞失败！");
        JSONObject result = serviceService.unlikeMessage(1L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testCollectServiceSuccess() {
        when(serviceDao.collectServiceByServiceId(anyLong(), anyLong())).thenReturn("收藏成功！");
        JSONObject result = serviceService.collectService(1L, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testCollectServiceFailed() {
        when(serviceDao.collectServiceByServiceId(anyLong(), anyLong())).thenReturn("收藏失败！");
        JSONObject result = serviceService.collectService(1L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testUncollectServiceSuccess() {
        when(serviceDao.uncollectServiceByServiceId(anyLong(), anyLong())).thenReturn("取消收藏成功！");
        JSONObject result = serviceService.uncollectService(1L, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testUncollectServiceFailed() {
        when(serviceDao.uncollectServiceByServiceId(anyLong(), anyLong())).thenReturn("取消收藏失败！");
        JSONObject result = serviceService.uncollectService(1L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testAccessServiceSuccess() {
        when(serviceDao.accessServiceByServiceId(anyLong(), anyLong())).thenReturn("接取服务成功！");
        JSONObject result = serviceService.accessService(1L, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testAccessServiceFailed() {
        when(serviceDao.accessServiceByServiceId(anyLong(), anyLong())).thenReturn("接取服务失败！");
        JSONObject result = serviceService.accessService(1L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testUnaccessServiceSuccess() {
        when(serviceDao.unaccessServiceByServiceId(anyLong(), anyLong())).thenReturn("取消接取服务成功！");
        JSONObject result = serviceService.unaccessService(1L, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testUnaccessServiceFailed() {
        when(serviceDao.unaccessServiceByServiceId(anyLong(), anyLong())).thenReturn("取消接取服务失败！");
        JSONObject result = serviceService.unaccessService(1L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishMessageSuccess() {
        JSONObject body = new JSONObject();
        body.put("content", "test");
        when(serviceMessageDao.putMessage(anyLong(), anyLong(), anyString())).thenReturn("发布留言成功！");
        JSONObject result = serviceService.publishMessage(1L, 1L, body);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testPublishMessageFailed() {
        JSONObject body = new JSONObject();
        body.put("content", "test");
        when(serviceMessageDao.putMessage(anyLong(), anyLong(), anyString())).thenReturn("发布留言失败！");
        JSONObject result = serviceService.publishMessage(1L, 1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishMessageWithEmptyBody() {
        JSONObject body = new JSONObject();
        when(serviceMessageDao.putMessage(anyLong(), anyLong(), anyString())).thenReturn("发布留言失败！");
        JSONObject result = serviceService.publishMessage(1L, 1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishMessageWithEmptyContent() {
        JSONObject body = new JSONObject();
        body.put("content", "");
        when(serviceMessageDao.putMessage(anyLong(), anyLong(), anyString())).thenReturn("发布留言失败！");
        JSONObject result = serviceService.publishMessage(1L, 1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testDeleteMessageSuccess() {
        when(serviceMessageDao.deleteMessage(anyLong(), anyLong())).thenReturn("删除留言成功！");
        JSONObject result = serviceService.deleteMessage(1L, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testDeleteMessageFailed() {
        when(serviceMessageDao.deleteMessage(anyLong(), anyLong())).thenReturn("删除留言失败！");
        JSONObject result = serviceService.deleteMessage(1L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishCommentSuccess() {
        JSONObject body = new JSONObject();
        body.put("content", "test");
        body.put("rating", 50);
        when(serviceCommentDao.putComment(anyLong(), anyLong(), anyString(), anyByte())).thenReturn("发布评论成功！");
        JSONObject result = serviceService.publishComment(1L, 1L, body);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testPublishCommentFailed() {
        JSONObject body = new JSONObject();
        body.put("content", "test");
        body.put("rating", 50);
        when(serviceCommentDao.putComment(anyLong(), anyLong(), anyString(), anyByte())).thenReturn("发布评论失败！");
        JSONObject result = serviceService.publishComment(1L, 1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishCommentWithEmptyBody() {
        JSONObject body = new JSONObject();
        when(serviceCommentDao.putComment(anyLong(), anyLong(), anyString(), anyByte())).thenReturn("发布评论失败！");
        JSONObject result = serviceService.publishComment(1L, 1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishCommentWithEmptyContent() {
        JSONObject body = new JSONObject();
        body.put("content", "");
        body.put("rating", 50);
        when(serviceCommentDao.putComment(anyLong(), anyLong(), anyString(), anyByte())).thenReturn("发布评论失败！");
        JSONObject result = serviceService.publishComment(1L, 1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishCommentWithEmptyRating() {
        JSONObject body = new JSONObject();
        body.put("content", "test");
        when(serviceCommentDao.putComment(anyLong(), anyLong(), anyString(), anyByte())).thenReturn("发布评论失败！");
        JSONObject result = serviceService.publishComment(1L, 1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testDeleteCommentSuccess() {
        when(serviceCommentDao.deleteComment(anyLong(), anyLong())).thenReturn("删除评论成功！");
        JSONObject result = serviceService.deleteComment(1L, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testDeleteCommentFailed() {
        when(serviceCommentDao.deleteComment(anyLong(), anyLong())).thenReturn("删除评论失败！");
        JSONObject result = serviceService.deleteComment(1L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceSuccess() {
        String images = "test";
        ArrayList<String> imageList = new ArrayList<>();
        imageList.add(images);
        JSONObject body = new JSONObject();
        body.put("title", "test");
        body.put("description", "test");
        body.put("price", 1000);
        body.put("maxAccess", 1);
        body.put("images", imageList);
        when(serviceDao.publishService(anyLong(), anyString(), anyString(), anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("服务发布成功！");
        JSONObject result = serviceService.publishService(1L, body);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testPublishServiceFailed() {
        String images = "test";
        ArrayList<String> imageList = new ArrayList<>();
        imageList.add(images);
        JSONObject body = new JSONObject();
        body.put("title", "test");
        body.put("description", "test");
        body.put("price", 1000);
        body.put("maxAccess", 1);
        body.put("images", imageList);
        when(serviceDao.publishService(anyLong(), anyString(), anyString(), anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务失败！");
        JSONObject result = serviceService.publishService(1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceWithEmptyBody() {
        JSONObject body = new JSONObject();
        when(serviceDao.publishService(anyLong(), anyString(), anyString(), anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务失败！");
        JSONObject result = serviceService.publishService(1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceWithEmptyTitle() {
        JSONObject body = new JSONObject();
        body.put("title", "");
        body.put("description", "test");
        body.put("price", 1000);
        body.put("maxAccess", 1);
        body.put("images", "test");
        when(serviceDao.publishService(anyLong(), anyString(), anyString(), anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务失败！");
        JSONObject result = serviceService.publishService(1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceWithEmptyDescription() {
        JSONObject body = new JSONObject();
        body.put("title", "test");
        body.put("description", "");
        body.put("price", 1000);
        body.put("maxAccess", 1);
        body.put("images", "test");
        when(serviceDao.publishService(anyLong(), anyString(), anyString(), anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务失败！");
        JSONObject result = serviceService.publishService(1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceWithIntegerPrice() {
        JSONObject body = new JSONObject();
        body.put("title", "test");
        body.put("description", "test");
        body.put("price", 0);
        body.put("maxAccess", 1);
        body.put("images", "test");
        when(serviceDao.publishService(anyLong(), anyString(), anyString(), anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务失败！");
        JSONObject result = serviceService.publishService(1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceWithLongPrice() {
        JSONObject body = new JSONObject();
        body.put("title", "test");
        body.put("description", "test");
        body.put("price", 10000000000L);
        body.put("maxAccess", 1);
        body.put("images", "test");
        when(serviceDao.publishService(anyLong(), anyString(), anyString(), anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务失败！");
        JSONObject result = serviceService.publishService(1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceWithStringPrice() {
        JSONObject body = new JSONObject();
        body.put("title", "test");
        body.put("description", "test");
        body.put("price", "test");
        body.put("maxAccess", 1);
        body.put("images", "test");
        when(serviceDao.publishService(anyLong(), anyString(), anyString(), anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务失败！");
        JSONObject result = serviceService.publishService(1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceWithNegativePrice() {
        JSONObject body = new JSONObject();
        body.put("title", "test");
        body.put("description", "test");
        body.put("price", -1);
        body.put("maxAccess", 1);
        body.put("images", "test");
        when(serviceDao.publishService(anyLong(), anyString(), anyString(), anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务失败！");
        JSONObject result = serviceService.publishService(1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceWithStringMaxAccess() {
        JSONObject body = new JSONObject();
        body.put("title", "test");
        body.put("description", "test");
        body.put("price", 1000);
        body.put("maxAccess", "test");
        body.put("images", "test");
        when(serviceDao.publishService(anyLong(), anyString(), anyString(), anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务失败！");
        JSONObject result = serviceService.publishService(1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceWithNegativeMaxAccess() {
        JSONObject body = new JSONObject();
        body.put("title", "test");
        body.put("description", "test");
        body.put("price", 1000);
        body.put("maxAccess", -1);
        body.put("images", "test");
        when(serviceDao.publishService(anyLong(), anyString(), anyString(), anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务失败！");
        JSONObject result = serviceService.publishService(1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceDtoByIdSuccess() {
        when(serviceDao.findById(anyLong())).thenReturn(service);
        when(userClient.getUserInfo(anyLong())).thenReturn(userInfoForTest);
        JSONObject result = serviceService.getServiceDtoById(1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceDtoByIdFailed() {
        when(serviceDao.findById(anyLong())).thenReturn(null);
        JSONObject result = serviceService.getServiceDtoById(1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceOwnerIdSuccess() {
        when(serviceDao.findById(anyLong())).thenReturn(service);
        JSONObject result = serviceService.getServiceOwnerId(1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceOwnerIdFailed() {
        when(serviceDao.findById(anyLong())).thenReturn(null);
        JSONObject result = serviceService.getServiceOwnerId(1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetMyService() {
        when(serviceDao.getMyService(anyLong(), any(Pageable.class))).thenReturn(servicePage);
        JSONObject result = serviceService.getMyService(pageable, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetMyAccessedService() {
        when(serviceDao.getMyAccessedService(anyLong(), any(Pageable.class))).thenReturn(servicePage);
        JSONObject result = serviceService.getMyAccessedService(pageable, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceAccessorInfo1() {
        when(serviceDao.findById(anyLong())).thenReturn(null);
        JSONObject result = serviceService.getServiceAccessorInfo(1L, 1L, pageable);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceAccessorInfo2() {
        Service newservice = new Service();
        newservice.setOwnerId(2L);
        when(serviceDao.findById(anyLong())).thenReturn(newservice);
        JSONObject result = serviceService.getServiceAccessorInfo(1L, 1L, pageable);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceAccessorInfo3() {
        Service newservice = new Service();
        newservice.setOwnerId(1L);
        when(serviceDao.findById(anyLong())).thenReturn(newservice);
        when(serviceDao.getMyAccessedService(anyLong(), any(Pageable.class))).thenReturn(null);
        JSONObject result = serviceService.getServiceAccessorInfo(1L, 1L, pageable);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceAccessorInfo4() {
        Service newservice = new Service();
        newservice.setOwnerId(1L);
        when(serviceDao.findById(anyLong())).thenReturn(newservice);

        List<ServiceAccess> serviceAccessList = Collections.emptyList();
        Page<ServiceAccess> serviceAccessPage = new PageImpl<>(serviceAccessList);
        when(serviceDao.getServiceAccessByService(any(Service.class), any(Pageable.class))).thenReturn(serviceAccessPage);
        when(userClient.getUserInfos(anyList())).thenReturn(userInfosForTest);
        JSONObject result = serviceService.getServiceAccessorInfo(1L, 1L, pageable);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceAccessorInfo5() {
        Service newservice = new Service();
        newservice.setOwnerId(1L);
        when(serviceDao.findById(anyLong())).thenReturn(newservice);

        ServiceAccess serviceAccess = new ServiceAccess();
        serviceAccess.setAccessorId(1L);
        List<ServiceAccess> serviceAccessList = new ArrayList<>();
        serviceAccessList.add(serviceAccess);
        Page<ServiceAccess> serviceAccessPage = new PageImpl<>(serviceAccessList);
        when(serviceDao.getServiceAccessByService(any(Service.class), any(Pageable.class))).thenReturn(serviceAccessPage);
        when(userClient.getAccessorInfos(anyList())).thenReturn(errorResponse);
        JSONObject result = serviceService.getServiceAccessorInfo(1L, 1L, pageable);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceAccessorInfo6() {
        when(serviceDao.findById(anyLong())).thenReturn(service);
        ServiceAccess serviceAccess = new ServiceAccess();
        serviceAccess.setAccessorId(1L);
        serviceAccess.setServiceAccessId(1L);
        serviceAccess.setServiceAccessStatus(ServiceAccessStatus.ACCESSING);
        List<ServiceAccess> serviceAccessList = new ArrayList<>();
        serviceAccessList.add(serviceAccess);
        Page<ServiceAccess> serviceAccessPage = new PageImpl<>(serviceAccessList);
        when(serviceDao.getServiceAccessByService(any(Service.class), any(Pageable.class))).thenReturn(serviceAccessPage);
        JSONObject newmessage = new JSONObject();
        newmessage.put("ok", true);
        newmessage.put("data", new ArrayList<>());
        when(userClient.getAccessorInfos(anyList())).thenReturn(newmessage);
        JSONObject result = serviceService.getServiceAccessorInfo(1L, 1L, pageable);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceAccessorSuccess1() {
        when(serviceDao.findById(anyLong())).thenReturn(null);
        JSONObject result = serviceService.getServiceAccessorSuccess(1L, 1L, pageable);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceAccessorSuccess2() {
        Service newservice = new Service();
        newservice.setOwnerId(2L);
        when(serviceDao.findById(anyLong())).thenReturn(newservice);
        JSONObject result = serviceService.getServiceAccessorSuccess(1L, 1L, pageable);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceAccessorSuccess3() {
        Service newservice = new Service();
        newservice.setOwnerId(1L);
        when(serviceDao.findById(anyLong())).thenReturn(newservice);
        when(serviceDao.getMyAccessedService(anyLong(), any(Pageable.class))).thenReturn(null);
        JSONObject result = serviceService.getServiceAccessorSuccess(1L, 1L, pageable);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceAccessorSuccess4() {
        Service newservice = new Service();
        newservice.setOwnerId(1L);
        when(serviceDao.findById(anyLong())).thenReturn(newservice);

        List<ServiceAccess> serviceAccessList = Collections.emptyList();
        Page<ServiceAccess> serviceAccessPage = new PageImpl<>(serviceAccessList);
        when(serviceDao.getServiceAccessSuccessByService(any(Service.class), any(Pageable.class))).thenReturn(serviceAccessPage);
        JSONObject result = serviceService.getServiceAccessorSuccess(1L, 1L, pageable);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceAccessorSuccess5() {
        Service newservice = new Service();
        newservice.setOwnerId(1L);
        when(serviceDao.findById(anyLong())).thenReturn(newservice);

        ServiceAccess serviceAccess = new ServiceAccess();
        serviceAccess.setAccessorId(1L);
        List<ServiceAccess> serviceAccessList = new ArrayList<>();
        serviceAccessList.add(serviceAccess);
        Page<ServiceAccess> serviceAccessPage = new PageImpl<>(serviceAccessList);
        when(serviceDao.getServiceAccessByService(any(Service.class), any(Pageable.class))).thenReturn(serviceAccessPage);
        when(userClient.getUserInfos(anyList())).thenReturn(errorResponse);
        JSONObject result = serviceService.getServiceAccessorSuccess(1L, 1L, pageable);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceAccessorSuccess6() {
        when(serviceDao.findById(anyLong())).thenReturn(service);
        ServiceAccess serviceAccess = new ServiceAccess();
        serviceAccess.setAccessorId(1L);
        serviceAccess.setServiceAccessId(1L);
        serviceAccess.setServiceAccessStatus(ServiceAccessStatus.ACCESSING);
        List<ServiceAccess> serviceAccessList = new ArrayList<>();
        serviceAccessList.add(serviceAccess);
        Page<ServiceAccess> serviceAccessPage = new PageImpl<>(serviceAccessList);
        when(serviceDao.getServiceAccessSuccessByService(any(Service.class), any(Pageable.class))).thenReturn(serviceAccessPage);
        JSONObject newmessage = new JSONObject();
        newmessage.put("ok", true);
        newmessage.put("data", new ArrayList<>());
        when(userClient.getAccessorInfos(anyList())).thenReturn(newmessage);

        JSONObject result = serviceService.getServiceAccessorSuccess(1L, 1L, pageable);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceAccessorFail1() {
        when(serviceDao.findById(anyLong())).thenReturn(null);
        JSONObject result = serviceService.getServiceAccessorFail(1L, 1L, pageable);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceAccessorFail2() {
        Service newservice = new Service();
        newservice.setOwnerId(2L);
        when(serviceDao.findById(anyLong())).thenReturn(newservice);
        JSONObject result = serviceService.getServiceAccessorFail(1L, 1L, pageable);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceAccessorFail3() {
        Service newservice = new Service();
        newservice.setOwnerId(1L);
        when(serviceDao.findById(anyLong())).thenReturn(newservice);
        when(serviceDao.getMyAccessedService(anyLong(), any(Pageable.class))).thenReturn(null);
        JSONObject result = serviceService.getServiceAccessorFail(1L, 1L, pageable);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceAccessorFail4() {
        Service newservice = new Service();
        newservice.setOwnerId(1L);
        when(serviceDao.findById(anyLong())).thenReturn(newservice);

        List<ServiceAccess> serviceAccessList = Collections.emptyList();
        Page<ServiceAccess> serviceAccessPage = new PageImpl<>(serviceAccessList);
        when(serviceDao.getServiceAccessFailByService(any(Service.class), any(Pageable.class))).thenReturn(serviceAccessPage);
        when(userClient.getUserInfos(anyList())).thenReturn(userInfosForTest);
        JSONObject result = serviceService.getServiceAccessorFail(1L, 1L, pageable);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceAccessorFail5() {
        Service newservice = new Service();
        newservice.setOwnerId(1L);
        when(serviceDao.findById(anyLong())).thenReturn(newservice);

        ServiceAccess serviceAccess = new ServiceAccess();
        serviceAccess.setAccessorId(1L);
        List<ServiceAccess> serviceAccessList = new ArrayList<>();
        serviceAccessList.add(serviceAccess);
        Page<ServiceAccess> serviceAccessPage = new PageImpl<>(serviceAccessList);
        when(serviceDao.getServiceAccessFailByService(any(Service.class), any(Pageable.class))).thenReturn(serviceAccessPage);
        when(userClient.getUserInfos(anyList())).thenReturn(errorResponse);
        JSONObject result = serviceService.getServiceAccessorFail(1L, 1L, pageable);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceAccessorFail6() {
        when(serviceDao.findById(anyLong())).thenReturn(service);
        ServiceAccess serviceAccess = new ServiceAccess();
        serviceAccess.setAccessorId(1L);
        serviceAccess.setServiceAccessId(1L);
        serviceAccess.setServiceAccessStatus(ServiceAccessStatus.ACCESSING);
        List<ServiceAccess> serviceAccessList = new ArrayList<>();
        serviceAccessList.add(serviceAccess);
        Page<ServiceAccess> serviceAccessPage = new PageImpl<>(serviceAccessList);
        when(serviceDao.getServiceAccessFailByService(any(Service.class), any(Pageable.class))).thenReturn(serviceAccessPage);
        JSONObject newmessage = new JSONObject();
        newmessage.put("ok", true);
        newmessage.put("data", new ArrayList<>());
        when(userClient.getAccessorInfos(anyList())).thenReturn(newmessage);

        JSONObject result = serviceService.getServiceAccessorFail(1L, 1L, pageable);
        System.out.println(result);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testCancelServiceSuccess() {
        when(serviceDao.cancelService(anyLong(), anyLong())).thenReturn("取消服务成功！");
        JSONObject result = serviceService.cancelService(1L, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testCancelServiceFailed() {
        when(serviceDao.cancelService(anyLong(), anyLong())).thenReturn("取消服务失败！");
        JSONObject result = serviceService.cancelService(1L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testConfirmAccessors1() {
        JSONObject body = new JSONObject();
        List<Long> userIds = Collections.emptyList();
        body.put("userList", userIds);

        JSONObject result = serviceService.confirmAccessors(1L, 1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testConfirmAccessors2() {
        JSONObject body = new JSONObject();
        List<Long> userIds = new ArrayList<>();
        userIds.add(1L);
        body.put("userList", userIds);

        when(serviceDao.confirmAccessors(anyLong(), anyLong(), anyList())).thenReturn("确认接取者成功！");
        JSONObject result = serviceService.confirmAccessors(1L, 1L, body);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testConfirmAccessors3() {
        JSONObject body = new JSONObject();
        List<Long> userIds = new ArrayList<>();
        userIds.add(1L);
        body.put("userList", userIds);

        when(serviceDao.confirmAccessors(anyLong(), anyLong(), anyList())).thenReturn("确认接取者失败！");
        JSONObject result = serviceService.confirmAccessors(1L, 1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testDenyAccessors1() {
        JSONObject body = new JSONObject();
        List<Long> userIds = Collections.emptyList();
        body.put("userList", userIds);

        JSONObject result = serviceService.denyAccessors(1L, 1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testDenyAccessors2() {
        JSONObject body = new JSONObject();
        List<Long> userIds = new ArrayList<>();
        userIds.add(1L);
        body.put("userList", userIds);

        when(serviceDao.denyAccessors(anyLong(), anyLong(), anyList())).thenReturn("拒绝接取者成功！");
        JSONObject result = serviceService.denyAccessors(1L, 1L, body);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testDenyAccessors3() {
        JSONObject body = new JSONObject();
        List<Long> userIds = new ArrayList<>();
        userIds.add(1L);
        body.put("userList", userIds);

        when(serviceDao.denyAccessors(anyLong(), anyLong(), anyList())).thenReturn("拒绝接取者失败！");
        JSONObject result = serviceService.denyAccessors(1L, 1L, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetMyCollect() {
        when(serviceDao.getMyCollect(anyLong(), any(Pageable.class))).thenReturn(servicePage);
        JSONObject result = serviceService.getMyCollect(pageable, 1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testSearchServiceByPagingWithException() {
        String formattedBegintime = "2024-07-01 00:00:00";
        String formattedEndtime = "2024-07-02 00:00:00";
        when(serviceDao.searchServiceByPaging(anyString(), any(Pageable.class), any(LocalDateTime.class), any(LocalDateTime.class), anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));
        JSONObject result = serviceService.searchServiceByPaging("test", pageable, formattedBegintime, formattedEndtime, 1L, 1000L, 1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceInfoWithException() {
        when(serviceDao.findById(anyLong())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.getServiceInfo(1L, 1L);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceCommentsWithException() {
        when(serviceCommentDao.getServiceComments(anyLong(), any(Pageable.class))).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.getServiceComments(1L, pageable, 1L);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceMessagesWithException() {
        when(serviceMessageDao.getServiceMessages(anyLong(), any(Pageable.class))).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.getServiceMessages(1L, pageable, 1L);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testLikeCommentWithException() {
        when(serviceCommentDao.likeCommentByServiceCommentId(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.likeComment(1L, 1L);

        assert result.get("ok").equals(false);
    }


    @Test
    public void testUnlikeCommentWithException() {
        when(serviceCommentDao.unlikeCommentByServiceCommentId(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.unlikeComment(1L, 1L);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testLikeMessageWithException() {
        when(serviceMessageDao.likeMessageByServiceMessageId(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.likeMessage(1L, 1L);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testUnlikeMessageWithException() {
        when(serviceMessageDao.unlikeMessageByServiceMessageId(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.unlikeMessage(1L, 1L);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testCollectServiceWithException() {
        when(serviceDao.collectServiceByServiceId(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.collectService(1L, 1L);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testUncollectServiceWithException() {
        when(serviceDao.uncollectServiceByServiceId(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.uncollectService(1L, 1L);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testAccessServiceWithException() {
        when(serviceDao.accessServiceByServiceId(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.accessService(1L, 1L);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testUnaccessServiceWithException() {
        when(serviceDao.unaccessServiceByServiceId(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.unaccessService(1L, 1L);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishMessageWithException() {
        JSONObject body = new JSONObject();
        body.put("content", "test");
        when(serviceMessageDao.putMessage(anyLong(), anyLong(), anyString())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.publishMessage(1L, 1L, body);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testDeleteMessageWithException() {
        when(serviceMessageDao.deleteMessage(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.deleteMessage(1L, 1L);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishCommentWithException() {
        JSONObject body = new JSONObject();
        body.put("content", "test");
        body.put("rating", 50);
        when(serviceCommentDao.putComment(anyLong(), anyLong(), anyString(), anyByte())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.publishComment(1L, 1L, body);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testDeleteCommentWithException() {
        when(serviceCommentDao.deleteComment(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.deleteComment(1L, 1L);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceWithException() {
        JSONObject body = new JSONObject();
        body.put("title", "test");
        body.put("description", "test");
        body.put("price", 1000);
        body.put("maxAccess", 1);
        body.put("images", "test");
        when(serviceDao.publishService(anyLong(), anyString(), anyString(), anyLong(), anyInt(), ArgumentMatchers.<List<String>>any())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.publishService(1L, body);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetMyServiceWithException() {
        when(serviceDao.getMyService(anyLong(), any(Pageable.class))).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.getMyService(pageable, 1L);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetMyAccessedServiceWithException() {
        when(serviceDao.getMyAccessedService(anyLong(), any(Pageable.class))).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.getMyAccessedService(pageable, 1L);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceAccessorInfoWithException() {
        when(serviceDao.findById(anyLong())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.getServiceAccessorInfo(1L, 1L, pageable);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceAccessorSuccessWithException() {
        when(serviceDao.findById(anyLong())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.getServiceAccessorSuccess(1L, 1L, pageable);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceAccessorFailWithException() {
        when(serviceDao.findById(anyLong())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.getServiceAccessorFail(1L, 1L, pageable);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testCancelServiceWithException() {
        when(serviceDao.cancelService(anyLong(), anyLong())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.cancelService(1L, 1L);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testConfirmAccessorsWithException() {
        JSONObject body = new JSONObject();
        List<Long> userIds = new ArrayList<>();
        userIds.add(1L);
        body.put("userList", userIds);

        when(serviceDao.confirmAccessors(anyLong(), anyLong(), anyList())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.confirmAccessors(1L, 1L, body);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testDenyAccessorsWithException() {
        JSONObject body = new JSONObject();
        List<Long> userIds = new ArrayList<>();
        userIds.add(1L);
        body.put("userList", userIds);

        when(serviceDao.denyAccessors(anyLong(), anyLong(), anyList())).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.denyAccessors(1L, 1L, body);

        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetMyCollectWithException() {
        when(serviceDao.getMyCollect(anyLong(), any(Pageable.class))).thenThrow(new RuntimeException("模拟异常"));

        JSONObject result = serviceService.getMyCollect(pageable, 1L);

        assert result.get("ok").equals(false);
    }
}
