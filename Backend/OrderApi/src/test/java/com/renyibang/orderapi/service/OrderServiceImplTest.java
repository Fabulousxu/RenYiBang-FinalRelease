package com.renyibang.orderapi.service;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.client.ServiceClient;
import com.renyibang.global.client.TaskClient;
import com.renyibang.global.dto.ServiceDTO;
import com.renyibang.global.dto.TaskDTO;
import com.renyibang.orderapi.dao.OrderDao;
import com.renyibang.orderapi.dto.OrderDTO;
import com.renyibang.orderapi.entity.Order;
import com.renyibang.global.dto.UserDTO;
import com.renyibang.global.client.UserClient;
import com.renyibang.orderapi.enums.OrderStatus;
import com.renyibang.orderapi.service.ServiceImpl.OrderServiceImpl;
import com.renyibang.orderapi.util.ResponseUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.aspectj.weaver.ast.Or;
import org.hibernate.grammars.hql.HqlParser.LocalDateTimeContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Pair;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class OrderServiceImplTest {
    @InjectMocks
    private OrderServiceImpl orderService;

    @MockBean
    private OrderDao orderDao;

    @MockBean
    private UserClient userClient;

    @MockBean
    private TaskClient taskClient;

    @MockBean
    private ServiceClient serviceClient;

    private Order validTaskOrder;
    private Order validServiceOrder;
    private UserDTO validOwner;
    private UserDTO validAccessor;
    private TaskDTO validTask;
    private ServiceDTO validService;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.validTaskOrder = new Order(1, (byte) 0, 1, 5, OrderStatus.UNPAID, 100, 3);
        this.validServiceOrder = new Order(2, (byte) 1, 1, 5, OrderStatus.UNPAID, 100, 6);
        this.validOwner = new UserDTO(1, 3500, "Apple", "apple.png");
        this.validAccessor = new UserDTO(5, 4900, "Banana", "banana.png");
        this.validTask = new TaskDTO(3, "Task 1", "Task 1 Description", "Task 1.png", LocalDateTime.of(2021, 1, 1, 0, 0));
        this.validService = new ServiceDTO(6, "Service 1", "Service 1 Description", "Service 1.png", LocalDateTime.of(2021, 1, 1, 0, 0));
    }

    // findByOwnerIdAndType

    @Test
    @DisplayName("Should return empty list when owner not found")
    public void findByOwnerIdAndTypeOwnerNotFound() {
        when(orderDao.findByOwnerIdAndType(99999999L, (byte) 0)).thenReturn(Collections.emptyList());

        // Mock userClient.getUserById 返回用户不存在的错误
        when(userClient.getUserById(99999999L)).thenReturn(ResponseUtil.error("用户不存在！"));

        // 调用服务方法
        List<OrderDTO> result = orderService.findByOwnerIdAndType(99999999L, (byte) 0);

        // 断言结果为空列表
        assertTrue(result.isEmpty());

        // 验证 orderDao.findByOwnerIdAndType 被调用了一次
        verify(orderDao, times(1)).findByOwnerIdAndType(99999999L, (byte) 0);
        // 验证 userClient.getUserById 被调用了一次
        verify(userClient, times(1)).getUserById(99999999L);
    }

    @Test
    @DisplayName("Should return empty list when accessor not found")
    public void findByOwnerIdAndTypeAccessorNotFound() {
        Order order = new Order(1, (byte) 0, 1, 99999999, OrderStatus.UNPAID, 100, 1);
        when(orderDao.findByOwnerIdAndType(1L, (byte) 0)).thenReturn(List.of(order));

        // Mock userClient.getUserById 返回用户不存在的错误
        when(userClient.getUserById(1L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", new UserDTO()));

        // Mock userClient.getUserById 返回用户不存在的错误
        when(userClient.getUserById(99999999L)).thenReturn(ResponseUtil.error("用户不存在！"));

        List<OrderDTO> result = orderService.findByOwnerIdAndType(1L, (byte) 0);

        assertTrue(result.isEmpty());
        verify(orderDao, times(1)).findByOwnerIdAndType(1L, (byte) 0);
        verify(userClient, times(1)).getUserById(1L);
        verify(userClient, times(1)).getUserById(99999999L);
        verify(userClient, times(2)).getUserById(anyLong());
    }

    @Test
    @DisplayName("Should return empty list when task not found and type is 0")
    public void findByOwnerIdAndTypeTaskNotFound() {
        Order order = new Order(1, (byte) 0, 1, 5, OrderStatus.UNPAID, 100, 99999999);
        when(orderDao.findByOwnerIdAndType(1L, (byte) 0)).thenReturn(List.of(order));

        when(userClient.getUserById(anyLong())).thenReturn(ResponseUtil.success("获取当前用户信息成功！", new UserDTO()));
        when(taskClient.getTaskById(99999999L)).thenReturn(ResponseUtil.error("任务不存在！"));

        List<OrderDTO> result = orderService.findByOwnerIdAndType(1L, (byte) 0);

        assertTrue(result.isEmpty());
        verify(orderDao, times(1)).findByOwnerIdAndType(1L, (byte) 0);
        verify(taskClient, times(1)).getTaskById(99999999L);
        verify(userClient, times(2)).getUserById(anyLong());
    }

    @Test
    @DisplayName("Should return empty list when service not found and type is not 0")
    public void findByOwnerIdAndTypeServiceNotFound() {
        Order order = new Order(1, (byte) 1, 1, 5, OrderStatus.UNPAID, 100, 99999999);
        when(orderDao.findByOwnerIdAndType(1L, (byte) 1)).thenReturn(List.of(order));

        when(userClient.getUserById(anyLong())).thenReturn(ResponseUtil.success("获取当前用户信息成功！", new UserDTO()));
        when(serviceClient.getServiceById(99999999L)).thenReturn(ResponseUtil.error("服务不存在！"));

        List<OrderDTO> result = orderService.findByOwnerIdAndType(1L, (byte) 1);

        assertTrue(result.isEmpty());
        verify(orderDao, times(1)).findByOwnerIdAndType(1L, (byte) 1);
        verify(serviceClient, times(1)).getServiceById(99999999L);
        verify(userClient, times(2)).getUserById(anyLong());
    }

    @Test
    @DisplayName("Should return list of OrderDTO when all dependencies found and type is 0")
    public void findByOwnerIdAndTypeAllDependenciesFoundTypeZero() {
        when(orderDao.findByOwnerIdAndType(1L, (byte) 0)).thenReturn(List.of(validTaskOrder));

        when(userClient.getUserById(1L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validOwner));
        when(userClient.getUserById(5L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validAccessor));
        when(taskClient.getTaskById(3L)).thenReturn(ResponseUtil.success("获取任务信息成功！", validTask));

        List<OrderDTO> result = orderService.findByOwnerIdAndType(1L, (byte) 0);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1, result.getFirst().getOrder().getOrderId());
        assertEquals((byte)0, result.getFirst().getOrder().getType());
        assertEquals(1, result.getFirst().getOwner().getId());
        assertEquals(5, result.getFirst().getAccessor().getId());
        assertEquals(OrderStatus.UNPAID, result.getFirst().getOrder().getStatus());
        assertEquals(100, result.getFirst().getOrder().getCost());
        assertEquals(3, result.getFirst().getTask().getId());

        verify(orderDao, times(1)).findByOwnerIdAndType(1L, (byte) 0);
        verify(userClient, times(2)).getUserById(anyLong());
        verify(taskClient, times(1)).getTaskById(3L);
    }

    @Test
    @DisplayName("Should return list of OrderDTO when all dependencies found and type is not 0")
    public void findByOwnerIdAndTypeAllDependenciesFoundTypeNonZero() {
        when(orderDao.findByOwnerIdAndType(1L, (byte) 1)).thenReturn(List.of(validServiceOrder));

        when(userClient.getUserById(1L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validOwner));
        when(userClient.getUserById(5L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validAccessor));
        when(serviceClient.getServiceById(6L)).thenReturn(ResponseUtil.success("获取服务信息成功！", validService));

        List<OrderDTO> result = orderService.findByOwnerIdAndType(1L, (byte) 1);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(2, result.getFirst().getOrder().getOrderId());
        assertEquals((byte)1, result.getFirst().getOrder().getType());
        assertEquals(1, result.getFirst().getOwner().getId());
        assertEquals(5, result.getFirst().getAccessor().getId());
        assertEquals(OrderStatus.UNPAID, result.getFirst().getOrder().getStatus());
        assertEquals(100, result.getFirst().getOrder().getCost());
        assertEquals(6, result.getFirst().getService().getId());

        verify(orderDao, times(1)).findByOwnerIdAndType(1L, (byte) 1);
        verify(userClient, times(2)).getUserById(anyLong());
        verify(serviceClient, times(1)).getServiceById(6L);
    }

    // findByAccessorIdAndType
    @Test
    @DisplayName("Should return empty list when accesser not found")
    public void findByAccessorIdAndTypeAccessorNotFound() {
        when(orderDao.findByAccessorIdAndType(99999999L, (byte) 0)).thenReturn(Collections.emptyList());

        // Mock userClient.getUserById 返回用户不存在的错误
        when(userClient.getUserById(99999999L)).thenReturn(ResponseUtil.error("用户不存在！"));

        // 调用服务方法
        List<OrderDTO> result = orderService.findByAccessorIdAndType(99999999L, (byte) 0);

        // 断言结果为空列表
        assertTrue(result.isEmpty());

        // 验证 orderDao.findByAccessorIdAndType 被调用了一次
        verify(orderDao, times(1)).findByAccessorIdAndType(99999999L, (byte) 0);
        // 验证 userClient.getUserById 被调用了一次
        verify(userClient, times(1)).getUserById(99999999L);
    }

    @Test
    @DisplayName("Should return empty list when owner not found")
    public void findByAccessorIdAndTypeOwnerNotFound() {
        Order order = new Order(1, (byte) 0, 99999999, 5, OrderStatus.UNPAID, 100, 1);
        when(orderDao.findByAccessorIdAndType(5L, (byte) 0)).thenReturn(List.of(order));

        // Mock userClient.getUserById 返回用户不存在的错误
        when(userClient.getUserById(5L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", new UserDTO()));

        // Mock userClient.getUserById 返回用户不存在的错误
        when(userClient.getUserById(99999999L)).thenReturn(ResponseUtil.error("用户不存在！"));

        List<OrderDTO> result = orderService.findByAccessorIdAndType(5L, (byte) 0);

        assertTrue(result.isEmpty());
        verify(orderDao, times(1)).findByAccessorIdAndType(5L, (byte) 0);
        verify(userClient, times(1)).getUserById(5L);
        verify(userClient, times(1)).getUserById(99999999L);
        verify(userClient, times(2)).getUserById(anyLong());
    }

    @Test
    @DisplayName("Should return empty list when task not found and type is 0")
    public void findByAccessorIdAndTypeTaskNotFound() {
        Order order = new Order(1, (byte) 0, 1, 5, OrderStatus.UNPAID, 100, 99999999);
        when(orderDao.findByAccessorIdAndType(5L, (byte) 0)).thenReturn(List.of(order));

        when(userClient.getUserById(anyLong())).thenReturn(ResponseUtil.success("获取当前用户信息成功！", new UserDTO()));
        when(taskClient.getTaskById(99999999L)).thenReturn(ResponseUtil.error("任务不存在！"));

        List<OrderDTO> result = orderService.findByAccessorIdAndType(5L, (byte) 0);

        assertTrue(result.isEmpty());
        verify(orderDao, times(1)).findByAccessorIdAndType(5L, (byte) 0);
        verify(taskClient, times(1)).getTaskById(99999999L);
        verify(userClient, times(2)).getUserById(anyLong());
    }

    @Test
    @DisplayName("Should return empty list when service not found and type is not 0")
    public void findByAccessorIdAndTypeServiceNotFound() {
        Order order = new Order(1, (byte) 1, 1, 5, OrderStatus.UNPAID, 100, 99999999);
        when(orderDao.findByAccessorIdAndType(5L, (byte) 1)).thenReturn(List.of(order));

        when(userClient.getUserById(anyLong())).thenReturn(ResponseUtil.success("获取当前用户信息成功！", new UserDTO()));
        when(serviceClient.getServiceById(99999999L)).thenReturn(ResponseUtil.error("服务不存在！"));

        List<OrderDTO> result = orderService.findByAccessorIdAndType(5L, (byte) 1);

        assertTrue(result.isEmpty());
        verify(orderDao, times(1)).findByAccessorIdAndType(5L, (byte) 1);
        verify(serviceClient, times(1)).getServiceById(99999999L);
        verify(userClient, times(2)).getUserById(anyLong());
    }

    @Test
    @DisplayName("Should return list of OrderDTO when all dependencies found and type is 0")
    public void findByAccessorIdAndTypeAllDependenciesFoundTypeZero() {
        when(orderDao.findByAccessorIdAndType(5L, (byte) 0)).thenReturn(List.of(validTaskOrder));

        when(userClient.getUserById(1L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validOwner));
        when(userClient.getUserById(5L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validAccessor));
        when(taskClient.getTaskById(3L)).thenReturn(ResponseUtil.success("获取任务信息成功！", validTask));

        List<OrderDTO> result = orderService.findByAccessorIdAndType(5L, (byte) 0);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1, result.getFirst().getOrder().getOrderId());
        assertEquals((byte)0, result.getFirst().getOrder().getType());
        assertEquals(1, result.getFirst().getOwner().getId());
        assertEquals(5, result.getFirst().getAccessor().getId());
        assertEquals(OrderStatus.UNPAID, result.getFirst().getOrder().getStatus());
        assertEquals(100, result.getFirst().getOrder().getCost());
        assertEquals(3, result.getFirst().getTask().getId());

        verify(orderDao, times(1)).findByAccessorIdAndType(5L, (byte) 0);
        verify(userClient, times(2)).getUserById(anyLong());
        verify(taskClient, times(1)).getTaskById(3L);
    }

    @Test
    @DisplayName("Should return list of OrderDTO when all dependencies found and type is not 0")
    public void findByAccessorIdAndTypeAllDependenciesFoundTypeNonZero() {
        when(orderDao.findByAccessorIdAndType(5L, (byte) 1)).thenReturn(List.of(validServiceOrder));

        when(userClient.getUserById(1L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validOwner));
        when(userClient.getUserById(5L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validAccessor));
        when(serviceClient.getServiceById(6L)).thenReturn(ResponseUtil.success("获取服务信息成功！", validService));

        List<OrderDTO> result = orderService.findByAccessorIdAndType(5L, (byte) 1);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(2, result.getFirst().getOrder().getOrderId());
        assertEquals((byte)1, result.getFirst().getOrder().getType());
        assertEquals(1, result.getFirst().getOwner().getId());
        assertEquals(5, result.getFirst().getAccessor().getId());
        assertEquals(OrderStatus.UNPAID, result.getFirst().getOrder().getStatus());
        assertEquals(100, result.getFirst().getOrder().getCost());
        assertEquals(6, result.getFirst().getService().getId());

        verify(orderDao, times(1)).findByAccessorIdAndType(5L, (byte) 1);
        verify(userClient, times(2)).getUserById(anyLong());
        verify(serviceClient, times(1)).getServiceById(6L);
    }

    // mapOrderToOrderDTO
    @Test
    @DisplayName("Should return OrderDTO when all dependencies found and type is 0")
    public void mapOrderToOrderDTOAllDependenciesFoundTypeZero() {
        Order order = new Order(1, (byte) 0, 1, 5, OrderStatus.UNPAID, 100, 3);
        UserDTO owner = new UserDTO(1, 3500, "Apple", "apple.png");
        UserDTO accessor = new UserDTO(5, 4900, "Banana", "banana.png");
        TaskDTO task = new TaskDTO(3, "Task 1", "Task 1 Description", "Task 1.png", LocalDateTime.of(2021, 1, 1, 0, 0));

        when(userClient.getUserById(1L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", owner));
        when(userClient.getUserById(5L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", accessor));
        when(taskClient.getTaskById(3L)).thenReturn(ResponseUtil.success("获取任务信息成功！", task));

        OrderDTO result = orderService.mapOrderToOrderDTO(order);

        assertNotNull(result);
        assertEquals(1, result.getOrder().getOrderId());
        assertEquals((byte)0, result.getOrder().getType());
        assertEquals(1, result.getOwner().getId());
        assertEquals(5, result.getAccessor().getId());
        assertEquals(OrderStatus.UNPAID, result.getOrder().getStatus());
        assertEquals(100, result.getOrder().getCost());
        assertEquals(3, result.getTask().getId());

        verify(userClient, times(1)).getUserById(1L);
        verify(userClient, times(1)).getUserById(5L);
        verify(taskClient, times(1)).getTaskById(3L);
    }

    @Test
    @DisplayName("Should return null when owner not found")
    public void mapOrderToOrderDTOOwnerNotFound() {
        Order order = new Order(1, (byte) 0, 1, 5, OrderStatus.UNPAID, 100, 3);

        when(userClient.getUserById(1L)).thenReturn(ResponseUtil.error("用户不存在！"));

        OrderDTO result = orderService.mapOrderToOrderDTO(order);

        assertNull(result);
        verify(userClient, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("Should return null when accessor not found")
    public void mapOrderToOrderDTOAccessorNotFound() {
        Order order = new Order(1, (byte) 0, 1, 5, OrderStatus.UNPAID, 100, 3);
        UserDTO owner = new UserDTO(1, 3500, "Apple", "apple.png");

        when(userClient.getUserById(1L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", owner));
        when(userClient.getUserById(5L)).thenReturn(ResponseUtil.error("用户不存在！"));

        OrderDTO result = orderService.mapOrderToOrderDTO(order);

        assertNull(result);
        verify(userClient, times(1)).getUserById(1L);
        verify(userClient, times(1)).getUserById(5L);
    }

    @Test
    @DisplayName("Should return null when task not found and type is 0")
    public void mapOrderToOrderDTOTaskNotFound() {
        Order order = new Order(1, (byte) 0, 1, 5, OrderStatus.UNPAID, 100, 3);
        UserDTO owner = new UserDTO(1, 3500, "Apple", "apple.png");
        UserDTO accessor = new UserDTO(5, 4900, "Banana", "banana.png");

        when(userClient.getUserById(1L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", owner));
        when(userClient.getUserById(5L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", accessor));
        when(taskClient.getTaskById(3L)).thenReturn(ResponseUtil.error("任务不存在！"));

        OrderDTO result = orderService.mapOrderToOrderDTO(order);

        assertNull(result);
        verify(userClient, times(1)).getUserById(1L);
        verify(userClient, times(1)).getUserById(5L);
        verify(taskClient, times(1)).getTaskById(3L);
    }

    @Test
    @DisplayName("Should return OrderDTO when all dependencies found and type is not 0")
    public void mapOrderToOrderDTOAllDependenciesFoundTypeNonZero() {
        Order order = new Order(1, (byte) 1, 1, 5, OrderStatus.UNPAID, 100, 6);
        UserDTO owner = new UserDTO(1, 3500, "Apple", "apple.png");
        UserDTO accessor = new UserDTO(5, 4900, "Banana", "banana.png");
        ServiceDTO service = new ServiceDTO(6, "Service 1", "Service 1 Description", "Service 1.png", LocalDateTime.of(2021, 1, 1, 0, 0));

        when(userClient.getUserById(1L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", owner));
        when(userClient.getUserById(5L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", accessor));
        when(serviceClient.getServiceById(6L)).thenReturn(ResponseUtil.error("服务不存在！"));

        OrderDTO result = orderService.mapOrderToOrderDTO(order);

        assertNull(result);
        verify(userClient, times(1)).getUserById(1L);
        verify(userClient, times(1)).getUserById(5L);
        verify(serviceClient, times(1)).getServiceById(6L);
    }

    // mapOrdersToOrderDTOs
    @Test
    @DisplayName("Should map all orders to OrderDTOs when all dependencies found")
    public void mapOrdersToOrderDTOsAllDependenciesFound() {
        List<Order> orders = List.of(validTaskOrder, validServiceOrder);
        List<OrderDTO> orderDTOs = new ArrayList<>();

        when(userClient.getUserById(anyLong())).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validOwner));
        when(taskClient.getTaskById(anyLong())).thenReturn(ResponseUtil.success("获取任务信息成功！", validTask));
        when(serviceClient.getServiceById(anyLong())).thenReturn(ResponseUtil.success("获取服务信息成功！", validService));

        orderService.mapOrdersToOrderDTOs(orders, orderDTOs);

        assertEquals(2, orderDTOs.size());
        verify(userClient, times(4)).getUserById(anyLong());
        verify(taskClient, times(1)).getTaskById(anyLong());
        verify(serviceClient, times(1)).getServiceById(anyLong());
    }

    @Test
    @DisplayName("Should map partial orders to OrderDTOs when some dependencies not found")
    public void mapOrdersToOrderDTOsPartialDependenciesFound() {
        List<Order> orders = List.of(validTaskOrder, validServiceOrder);
        List<OrderDTO> orderDTOs = new ArrayList<>();

        when(userClient.getUserById(1L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validOwner));
        when(userClient.getUserById(5L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validAccessor));
        when(taskClient.getTaskById(3L)).thenReturn(ResponseUtil.error("任务不存在！"));
        when(serviceClient.getServiceById(6L)).thenReturn(ResponseUtil.success("获取服务信息成功！", validService));

        orderService.mapOrdersToOrderDTOs(orders, orderDTOs);

        assertEquals(1, orderDTOs.size());
        verify(userClient, times(4)).getUserById(anyLong());
        verify(taskClient, times(1)).getTaskById(anyLong());
        verify(serviceClient, times(1)).getServiceById(anyLong());
    }

    // findByItemIdAndType
    @Test
    @DisplayName("Should return empty list when no orders found for given item id and type")
    public void findByItemIdAndTypeNoOrdersFound() {
        long itemId = 99999999L;
        Byte type = 0;

        when(orderDao.findByItemIdAndType(itemId, type)).thenReturn(Collections.emptyList());

        List<OrderDTO> result = orderService.findByItemIdAndType(itemId, type);

        assertTrue(result.isEmpty());
        verify(orderDao, times(1)).findByItemIdAndType(itemId, type);
    }

    @Test
    @DisplayName("Should return list of OrderDTOs when orders found for given item id and type")
    public void findByItemIdAndTypeOrdersFound() {
        long itemId = 1L;
        Byte type = 0;
        List<Order> orders = List.of(validTaskOrder, validServiceOrder);
        List<OrderDTO> orderDTOs = new ArrayList<>();

        when(orderDao.findByItemIdAndType(itemId, type)).thenReturn(orders);
        when(userClient.getUserById(anyLong())).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validOwner));
        when(taskClient.getTaskById(anyLong())).thenReturn(ResponseUtil.success("获取任务信息成功！", validTask));
        when(serviceClient.getServiceById(anyLong())).thenReturn(ResponseUtil.success("获取服务信息成功！", validService));

        List<OrderDTO> result = orderService.findByItemIdAndType(itemId, type);

        assertEquals(2, result.size());
        verify(orderDao, times(1)).findByItemIdAndType(itemId, type);
        verify(userClient, times(4)).getUserById(anyLong());
        verify(taskClient, times(1)).getTaskById(anyLong());
        verify(serviceClient, times(1)).getServiceById(anyLong());
    }

    // findByStatusAndType
    @Test
    @DisplayName("Should return empty list when no orders found for given status and type")
    public void findByStatusAndTypeNoOrdersFound() {
        OrderStatus status = OrderStatus.UNPAID;
        Byte type = 0;

        when(orderDao.findByStatusAndType(status, type)).thenReturn(Collections.emptyList());

        List<OrderDTO> result = orderService.findByStatusAndType(status, type);

        assertTrue(result.isEmpty());
        verify(orderDao, times(1)).findByStatusAndType(status, type);
    }

    @Test
    @DisplayName("Should return list of OrderDTOs when orders found for given status and type")
    public void findByStatusAndTypeOrdersFound() {
        OrderStatus status = OrderStatus.UNPAID;
        Byte type = 0;
        List<Order> orders = List.of(validTaskOrder, validServiceOrder);
        List<OrderDTO> orderDTOs = new ArrayList<>();

        when(orderDao.findByStatusAndType(status, type)).thenReturn(orders);
        when(userClient.getUserById(anyLong())).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validOwner));
        when(taskClient.getTaskById(anyLong())).thenReturn(ResponseUtil.success("获取任务信息成功！", validTask));
        when(serviceClient.getServiceById(anyLong())).thenReturn(ResponseUtil.success("获取服务信息成功！", validService));

        List<OrderDTO> result = orderService.findByStatusAndType(status, type);

        assertEquals(2, result.size());
        verify(orderDao, times(1)).findByStatusAndType(status, type);
        verify(userClient, times(4)).getUserById(anyLong());
        verify(taskClient, times(1)).getTaskById(anyLong());
        verify(serviceClient, times(1)).getServiceById(anyLong());
    }

    // findById
    @Test
    @DisplayName("Should return null when order not found")
    public void findByIdOrderNotFound() {
        long orderId = 99999999L;

        when(orderDao.findById(orderId)).thenReturn(null);

        OrderDTO result = orderService.findById(orderId);

        assertNull(result);
        verify(orderDao, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Should return OrderDTO when order found and all dependencies found")
    public void findByIdOrderFoundAllDependenciesFound() {
        long orderId = 1L;
        Order order = new Order(1, (byte) 0, 1, 5, OrderStatus.UNPAID, 100, 3);

        when(orderDao.findById(orderId)).thenReturn(order);
        when(userClient.getUserById(1L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validOwner));
        when(userClient.getUserById(5L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validAccessor));
        when(taskClient.getTaskById(anyLong())).thenReturn(ResponseUtil.success("获取任务信息成功！", validTask));

        OrderDTO result = orderService.findById(orderId);

        assertNotNull(result);
        assertEquals(1, result.getOrder().getOrderId());
        assertEquals((byte)0, result.getOrder().getType());
        assertEquals(1, result.getOwner().getId());
        assertEquals(5, result.getAccessor().getId());
        assertEquals(OrderStatus.UNPAID, result.getOrder().getStatus());
        assertEquals(100, result.getOrder().getCost());
        assertEquals(3, result.getTask().getId());

        verify(orderDao, times(1)).findById(orderId);
        verify(userClient, times(2)).getUserById(anyLong());
        verify(taskClient, times(1)).getTaskById(anyLong());
    }

    @Test
    @DisplayName("Should return null when order found but owner not found")
    public void findByIdOrderFoundOwnerNotFound() {
        long orderId = 1L;
        Order order = new Order(1, (byte) 0, 1, 5, OrderStatus.UNPAID, 100, 3);

        when(orderDao.findById(orderId)).thenReturn(order);
        when(userClient.getUserById(1L)).thenReturn(ResponseUtil.error("用户不存在！"));

        OrderDTO result = orderService.findById(orderId);

        assertNull(result);
        verify(orderDao, times(1)).findById(orderId);
        verify(userClient, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("Should return null when order found but accessor not found")
    public void findByIdOrderFoundAccessorNotFound() {
        long orderId = 1L;
        Order order = new Order(1, (byte) 0, 1, 5, OrderStatus.UNPAID, 100, 3);

        when(orderDao.findById(orderId)).thenReturn(order);
        when(userClient.getUserById(1L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validOwner));
        when(userClient.getUserById(5L)).thenReturn(ResponseUtil.error("用户不存在！"));

        OrderDTO result = orderService.findById(orderId);

        assertNull(result);
        verify(orderDao, times(1)).findById(orderId);
        verify(userClient, times(1)).getUserById(1L);
        verify(userClient, times(1)).getUserById(5L);
    }

    @Test
    @DisplayName("Should return null when order found but task not found and type is 0")
    public void findByIdOrderFoundTaskNotFound() {
        long orderId = 1L;
        Order order = new Order(1, (byte) 0, 1, 5, OrderStatus.UNPAID, 100, 3);

        when(orderDao.findById(orderId)).thenReturn(order);
        when(userClient.getUserById(1L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validOwner));
        when(userClient.getUserById(5L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validAccessor));
        when(taskClient.getTaskById(anyLong())).thenReturn(ResponseUtil.error("任务不存在！"));

        OrderDTO result = orderService.findById(orderId);

        assertNull(result);
        verify(orderDao, times(1)).findById(orderId);
        verify(userClient, times(2)).getUserById(anyLong());
        verify(taskClient, times(1)).getTaskById(anyLong());
    }

    // findAllOrders
    @Test
    @DisplayName("Should return empty list when no orders found")
    public void findAllOrdersNoOrdersFound() {
        when(orderDao.findAll()).thenReturn(Collections.emptyList());

        List<OrderDTO> result = orderService.findAllOrders();

        assertTrue(result.isEmpty());
        verify(orderDao, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return list of OrderDTOs when orders found and all dependencies found")
    public void findAllOrdersOrdersFoundAllDependenciesFound() {
        List<Order> orders = List.of(validTaskOrder, validServiceOrder);

        when(orderDao.findAll()).thenReturn(orders);
        when(userClient.getUserById(anyLong())).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validOwner));
        when(taskClient.getTaskById(anyLong())).thenReturn(ResponseUtil.success("获取任务信息成功！", validTask));
        when(serviceClient.getServiceById(anyLong())).thenReturn(ResponseUtil.success("获取服务信息成功！", validService));

        List<OrderDTO> result = orderService.findAllOrders();

        assertEquals(2, result.size());
        verify(orderDao, times(1)).findAll();
        verify(userClient, times(4)).getUserById(anyLong());
        verify(taskClient, times(1)).getTaskById(anyLong());
        verify(serviceClient, times(1)).getServiceById(anyLong());
    }

    @Test
    @DisplayName("Should return list of OrderDTOs when orders found but some dependencies not found")
    public void findAllOrdersOrdersFoundSomeDependenciesNotFound() {
        List<Order> orders = List.of(validTaskOrder, validServiceOrder);

        when(orderDao.findAll()).thenReturn(orders);
        when(userClient.getUserById(1L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validOwner));
        when(userClient.getUserById(5L)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validAccessor));
        when(taskClient.getTaskById(3L)).thenReturn(ResponseUtil.error("任务不存在！"));
        when(serviceClient.getServiceById(6L)).thenReturn(ResponseUtil.success("获取服务信息成功！", validService));

        List<OrderDTO> result = orderService.findAllOrders();

        assertEquals(1, result.size());
        verify(orderDao, times(1)).findAll();
        verify(userClient, times(4)).getUserById(anyLong());
        verify(taskClient, times(1)).getTaskById(anyLong());
        verify(serviceClient, times(1)).getServiceById(anyLong());
    }

    // createOrder
    @Test
    @DisplayName("Should create order successfully when all inputs are valid and type is 0")
    public void createOrderAllValidInputsTypeZero() {
        long taskId = 1L;
        long ownerId = 1L;
        List<Long> accessors = List.of(5L);
        long cost = 100L;
        Byte type = 0;

        when(taskClient.getTaskById(taskId)).thenReturn(ResponseUtil.success("获取任务信息成功！", validTask));
        when(userClient.getUserById(ownerId)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validOwner));
        when(userClient.getUserById(accessors.getFirst())).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validAccessor));

        boolean result = orderService.createOrder(taskId, ownerId, accessors, cost, type);

        assertTrue(result);
        verify(taskClient, times(1)).getTaskById(taskId);
        verify(userClient, times(1)).getUserById(ownerId);
        verify(userClient, times(1)).getUserById(accessors.getFirst());
        verify(orderDao, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Should create order successfully when all inputs are valid and type is not 0")
    public void createOrderAllValidInputsTypeNonZero() {
        long taskId = 1L;
        long ownerId = 1L;
        List<Long> accessors = List.of(5L);
        long cost = 100L;
        Byte type = 1;

        when(serviceClient.getServiceById(taskId)).thenReturn(ResponseUtil.success("获取服务信息成功！", validService));
        when(userClient.getUserById(ownerId)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validOwner));
        when(userClient.getUserById(accessors.getFirst())).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validAccessor));

        boolean result = orderService.createOrder(taskId, ownerId, accessors, cost, type);

        assertTrue(result);
        verify(serviceClient, times(1)).getServiceById(taskId);
        verify(userClient, times(1)).getUserById(ownerId);
        verify(userClient, times(1)).getUserById(accessors.getFirst());
        verify(orderDao, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when task not found and type is 0")
    public void createOrderTaskNotFoundTypeZero() {
        long taskId = 99999999L;
        long ownerId = 1L;
        List<Long> accessors = List.of(5L);
        long cost = 100L;
        Byte type = 0;

        when(taskClient.getTaskById(taskId)).thenReturn(ResponseUtil.error("任务不存在！"));

        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(taskId, ownerId, accessors, cost, type));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when service not found and type is not 0")
    public void createOrderServiceNotFoundTypeNonZero() {
        long taskId = 99999999L;
        long ownerId = 1L;
        List<Long> accessors = List.of(5L);
        long cost = 100L;
        Byte type = 1;

        when(serviceClient.getServiceById(taskId)).thenReturn(ResponseUtil.error("服务不存在！"));

        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(taskId, ownerId, accessors, cost, type));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when owner not found")
    public void createOrderOwnerNotFound() {
        long taskId = 1L;
        long ownerId = 99999999L;
        List<Long> accessors = List.of(5L);
        long cost = 100L;
        Byte type = 0;

        when(taskClient.getTaskById(taskId)).thenReturn(ResponseUtil.success("获取任务信息成功！", validTask));
        when(userClient.getUserById(ownerId)).thenReturn(ResponseUtil.error("用户不存在！"));

        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(taskId, ownerId, accessors, cost, type));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when accessor not found")
    public void createOrderAccessorNotFound() {
        long taskId = 1L;
        long ownerId = 1L;
        List<Long> accessors = List.of(99999999L);
        long cost = 100L;
        Byte type = 0;

        when(taskClient.getTaskById(taskId)).thenReturn(ResponseUtil.success("获取任务信息成功！", validTask));
        when(userClient.getUserById(ownerId)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validOwner));
        when(userClient.getUserById(accessors.getFirst())).thenReturn(ResponseUtil.error("用户不存在！"));

        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(taskId, ownerId, accessors, cost, type));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when cost is not positive")
    public void createOrderCostNotPositive() {
        long taskId = 1L;
        long ownerId = 1L;
        List<Long> accessors = List.of(5L);
        long cost = 0L;
        Byte type = 0;

        when(taskClient.getTaskById(taskId)).thenReturn(ResponseUtil.success("获取任务信息成功！", validTask));
        when(userClient.getUserById(ownerId)).thenReturn(ResponseUtil.success("获取当前用户信息成功！", validOwner));

        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(taskId, ownerId, accessors, cost, type));
    }

    // setOrderStatusForce
    @Test
    @DisplayName("Should return false when order not found")
    public void setOrderStatusForceOrderNotFound() {
        long orderId = 99999999L;
        OrderStatus status = OrderStatus.IN_PROGRESS;

        when(orderDao.findById(orderId)).thenReturn(null);

        boolean result = orderService.setOrderStatusForce(orderId, status);

        assertFalse(result);
        verify(orderDao, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Should return true and update status when order found")
    public void setOrderStatusForceOrderFound() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.IN_PROGRESS;
        Order order = new Order();

        when(orderDao.findById(orderId)).thenReturn(order);
        when(orderDao.save(any(Order.class))).thenReturn(order);

        boolean result = orderService.setOrderStatusForce(orderId, status);

        assertTrue(result);
        assertEquals(status, order.getStatus());
        verify(orderDao, times(1)).findById(orderId);
        verify(orderDao, times(1)).save(order);
    }

    // checkOrderExist
    @Test
    @DisplayName("Should return true when order exists")
    public void checkOrderExistOrderExists() {
        long orderId = 1L;

        when(orderDao.existsById(orderId)).thenReturn(true);

        boolean result = orderService.checkOrderExist(orderId);

        assertTrue(result);
        verify(orderDao, times(1)).existsById(orderId);
    }

    @Test
    @DisplayName("Should return false when order does not exist")
    public void checkOrderExistOrderDoesNotExist() {
        long orderId = 99999999L;

        when(orderDao.existsById(orderId)).thenReturn(false);

        boolean result = orderService.checkOrderExist(orderId);

        assertFalse(result);
        verify(orderDao, times(1)).existsById(orderId);
    }

    // checkOrderStatus
    @Test
    @DisplayName("Should return true when order status matches the provided status")
    public void checkOrderStatusOrderStatusMatches() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.COMPLETED;
        Order order = new Order();
        order.setStatus(status);

        when(orderDao.findById(orderId)).thenReturn(order);

        boolean result = orderService.checkOrderStatus(orderId, status);

        assertTrue(result);
        verify(orderDao, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Should return false when order status does not match the provided status")
    public void checkOrderStatusOrderStatusDoesNotMatch() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.COMPLETED;
        Order order = new Order();
        order.setStatus(OrderStatus.UNPAID);

        when(orderDao.findById(orderId)).thenReturn(order);

        boolean result = orderService.checkOrderStatus(orderId, status);

        assertFalse(result);
        verify(orderDao, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Should return false when order does not exist")
    public void checkOrderStatusOrderDoesNotExist() {
        long orderId = 99999999L;
        OrderStatus status = OrderStatus.COMPLETED;

        when(orderDao.findById(orderId)).thenReturn(null);

        boolean result = orderService.checkOrderStatus(orderId, status);

        assertFalse(result);
        verify(orderDao, times(1)).findById(orderId);
    }

    // markOrderStatus
    @Test
    @DisplayName("Should return order not found when order does not exist")
    public void markOrderStatusOrderNotFound() {
        long orderId = 99999999L;
        OrderStatus status = OrderStatus.IN_PROGRESS;

        when(orderDao.findById(orderId)).thenReturn(null);

        Pair<Boolean, String> result = orderService.markOrderStatus(orderId, 1L, status);

        assertFalse(result.getFirst());
        assertEquals("订单不存在", result.getSecond());
        verify(orderDao, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Should return order payment success when order is unpaid and status is in progress")
    public void markOrderStatusOrderPaymentSuccess() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.IN_PROGRESS;
        Order order = new Order();
        order.setStatus(OrderStatus.UNPAID);
        order.setOwnerId(1L);

        OrderServiceImpl orderServiceSpy = Mockito.spy(orderService);

        when(orderDao.findById(orderId)).thenReturn(order);
        doReturn(true).when(orderServiceSpy).payOrder(order);

        Pair<Boolean, String> result = orderServiceSpy.markOrderStatus(orderId, 1L, status);

        assertTrue(result.getFirst());
        assertEquals("订单支付成功", result.getSecond());
        verify(orderDao, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Should return order completion success when order is in progress and status is completed")
    public void markOrderStatusOrderCompletionSuccess() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.COMPLETED;
        Order order = new Order();
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setAccessorId(1L);

        OrderServiceImpl orderServiceSpy = Mockito.spy(orderService);

        when(orderDao.findById(orderId)).thenReturn(order);
        doReturn(true).when(orderServiceSpy).completeOrder(order);

        Pair<Boolean, String> result = orderServiceSpy.markOrderStatus(orderId, 1L, status);

        assertTrue(result.getFirst());
        assertEquals("订单完成成功", result.getSecond());
        verify(orderDao, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Should return order confirmation success when order is completed and status is confirmed")
    public void markOrderStatusOrderConfirmationSuccess() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.CONFIRMED;
        Order order = new Order();
        order.setStatus(OrderStatus.COMPLETED);
        order.setOwnerId(1L);

        OrderServiceImpl orderServiceSpy = Mockito.spy(orderService);

        when(orderDao.findById(orderId)).thenReturn(order);
        doReturn(true).when(orderServiceSpy).confirmOrder(order);

        Pair<Boolean, String> result = orderServiceSpy.markOrderStatus(orderId, 1L, status);

        assertTrue(result.getFirst());
        assertEquals("订单确认成功", result.getSecond());
        verify(orderDao, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Should return order cancellation success when order is not confirmed or cancelled and status is cancelled")
    public void markOrderStatusOrderCancellationSuccess() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.CANCELLED;
        Order order = new Order();
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setOwnerId(1L);

        OrderServiceImpl orderServiceSpy = Mockito.spy(orderService);

        when(orderDao.findById(orderId)).thenReturn(order);
        doReturn(true).when(orderServiceSpy).cancelOrder(order);

        Pair<Boolean, String> result = orderServiceSpy.markOrderStatus(orderId, 1L, status);

        assertTrue(result.getFirst());
        assertEquals("订单取消成功", result.getSecond());
        verify(orderDao, times(1)).findById(orderId);
    }

    // 支付失败
    @Test
    @DisplayName("Should return order payment failure when order is unpaid and status is in progress")
    public void markOrderStatusOrderPaymentFailure() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.IN_PROGRESS;
        Order order = new Order();
        order.setStatus(OrderStatus.UNPAID);
        order.setOwnerId(1L);

        OrderServiceImpl orderServiceSpy = Mockito.spy(orderService);

        when(orderDao.findById(orderId)).thenReturn(order);
        doReturn(false).when(orderServiceSpy).payOrder(order);

        Pair<Boolean, String> result = orderServiceSpy.markOrderStatus(orderId, 1L, status);

        assertFalse(result.getFirst());
        assertEquals("订单支付失败", result.getSecond());
        verify(orderDao, times(1)).findById(orderId);
    }

    // 确认失败
    @Test
    @DisplayName("Should return order confirmation failure when order is completed and status is confirmed")
    public void markOrderStatusOrderConfirmationFailure() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.CONFIRMED;
        Order order = new Order();
        order.setStatus(OrderStatus.COMPLETED);
        order.setOwnerId(1L);

        OrderServiceImpl orderServiceSpy = Mockito.spy(orderService);

        when(orderDao.findById(orderId)).thenReturn(order);
        doReturn(false).when(orderServiceSpy).confirmOrder(order);

        Pair<Boolean, String> result = orderServiceSpy.markOrderStatus(orderId, 1L, status);

        assertFalse(result.getFirst());
        assertEquals("订单确认失败", result.getSecond());
        verify(orderDao, times(1)).findById(orderId);
    }

    // 完成失败
    @Test
    @DisplayName("Should return order completion failure when order is in progress and status is completed")
    public void markOrderStatusOrderCompletionFailure() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.COMPLETED;
        Order order = new Order();
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setAccessorId(1L);

        OrderServiceImpl orderServiceSpy = Mockito.spy(orderService);

        when(orderDao.findById(orderId)).thenReturn(order);
        doReturn(false).when(orderServiceSpy).completeOrder(order);

        Pair<Boolean, String> result = orderServiceSpy.markOrderStatus(orderId, 1L, status);

        assertFalse(result.getFirst());
        assertEquals("订单完成失败", result.getSecond());
        verify(orderDao, times(1)).findById(orderId);
    }

    // 取消失败
    @Test
    @DisplayName("Should return order cancellation failure when order is not confirmed or cancelled and status is cancelled")
    public void markOrderStatusOrderCancellationFailure() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.CANCELLED;
        Order order = new Order();
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setOwnerId(1L);

        OrderServiceImpl orderServiceSpy = Mockito.spy(orderService);

        when(orderDao.findById(orderId)).thenReturn(order);
        doReturn(false).when(orderServiceSpy).cancelOrder(order);

        Pair<Boolean, String> result = orderServiceSpy.markOrderStatus(orderId, 1L, status);

        assertFalse(result.getFirst());
        assertEquals("订单取消失败", result.getSecond());
        verify(orderDao, times(1)).findById(orderId);
    }

    // illegal status

    @Test
    @DisplayName("Should return order status illegal when order status does not match: UNPAID -> IN_PROGRESS")
    public void markOrderStatusIllegalStatusUnpaidInProgress() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.COMPLETED;
        Order order = new Order();
        order.setStatus(OrderStatus.UNPAID);

        when(orderDao.findById(orderId)).thenReturn(order);

        Pair<Boolean, String> result = orderService.markOrderStatus(orderId, 1L, status);

        assertFalse(result.getFirst());
        assertEquals("订单状态不合法", result.getSecond());
        verify(orderDao, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Should return order status illegal when order status does not match: IN_PROGRESS -> COMPLETED")
    public void markOrderStatusIllegalStatusInProgressCompleted() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.CONFIRMED;
        Order order = new Order();
        order.setStatus(OrderStatus.IN_PROGRESS);

        when(orderDao.findById(orderId)).thenReturn(order);

        Pair<Boolean, String> result = orderService.markOrderStatus(orderId, 1L, status);

        assertFalse(result.getFirst());
        assertEquals("订单状态不合法", result.getSecond());
        verify(orderDao, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Should return order status illegal when order status does not match: COMPLETED -> CONFIRMED")
    public void markOrderStatusIllegalStatusCompletedConfirmed() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.CANCELLED;
        Order order = new Order();
        order.setStatus(OrderStatus.COMPLETED);

        when(orderDao.findById(orderId)).thenReturn(order);

        Pair<Boolean, String> result = orderService.markOrderStatus(orderId, 1L, status);

        assertFalse(result.getFirst());
        assertEquals("订单状态不合法", result.getSecond());
        verify(orderDao, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Should return order status illegal when order status match: CONFIRMED -> CANCELLED")
    public void markOrderStatusIllegalStatusConfirmedCancelled() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.CANCELLED;
        Order order = new Order();
        order.setStatus(OrderStatus.CONFIRMED);

        when(orderDao.findById(orderId)).thenReturn(order);

        Pair<Boolean, String> result = orderService.markOrderStatus(orderId, 1L, status);

        assertFalse(result.getFirst());
        assertEquals("订单状态不合法", result.getSecond());
        verify(orderDao, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Should return order status illegal when order status does not match: CANCELLED -> CANCELLED")
    public void markOrderStatusIllegalStatusCancelledCancelled() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.CANCELLED;
        Order order = new Order();
        order.setStatus(OrderStatus.CANCELLED);

        when(orderDao.findById(orderId)).thenReturn(order);

        Pair<Boolean, String> result = orderService.markOrderStatus(orderId, 1L, status);

        assertFalse(result.getFirst());
        assertEquals("订单状态不合法", result.getSecond());
        verify(orderDao, times(1)).findById(orderId);
    }

    // 由于userId不匹配而失败
    @Test
    @DisplayName("Should return order status illegal when userId does not match order owner")
    public void markOrderStatusUserIdNotMatch() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.IN_PROGRESS;
        Order order = new Order();
        order.setStatus(OrderStatus.UNPAID);
        order.setOwnerId(1L);

        when(orderDao.findById(orderId)).thenReturn(order);

        Pair<Boolean, String> result = orderService.markOrderStatus(orderId, 2L, status);

        assertFalse(result.getFirst());
        assertEquals("订单状态不合法", result.getSecond());
        verify(orderDao, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Should return order status illegal when userId does not match order accessor")
    public void markOrderStatusUserIdNotMatchAccessor() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.COMPLETED;
        Order order = new Order();
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setAccessorId(1L);

        when(orderDao.findById(orderId)).thenReturn(order);

        Pair<Boolean, String> result = orderService.markOrderStatus(orderId, 2L, status);

        assertFalse(result.getFirst());
        assertEquals("订单状态不合法", result.getSecond());
        verify(orderDao, times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Should return order status illegal when userId does not match order owner, COMPLETED -> CONFIRMED")
    public void markOrderStatusUserIdNotMatchCompletedConfirmed() {
        long orderId = 1L;
        OrderStatus status = OrderStatus.CONFIRMED;
        Order order = new Order();
        order.setStatus(OrderStatus.COMPLETED);
        order.setOwnerId(1L);

        when(orderDao.findById(orderId)).thenReturn(order);

        Pair<Boolean, String> result = orderService.markOrderStatus(orderId, 2L, status);

        assertFalse(result.getFirst());
        assertEquals("订单状态不合法", result.getSecond());
        verify(orderDao, times(1)).findById(orderId);
    }

    // payOrder
    @Test
    @DisplayName("Should return true when balance is sufficient and order status is updated")
    public void payOrderBalanceSufficientStatusUpdated() {
        Order order = new Order();
        order.setOwnerId(1L);
        order.setCost(100L);

        OrderServiceImpl orderServiceSpy = Mockito.spy(orderService);

        doReturn(true).when(orderServiceSpy).modifyUserBalance(1L, -100L);
        when(orderDao.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        boolean result = orderServiceSpy.payOrder(order);

        assertTrue(result);
        assertEquals(OrderStatus.IN_PROGRESS, order.getStatus());
        verify(orderServiceSpy, times(1)).modifyUserBalance(1L, -100L);
        verify(orderDao, times(1)).save(order);
    }

    @Test
    @DisplayName("Should return false when balance is insufficient")
    public void payOrderBalanceInsufficient() {
        Order order = new Order();
        order.setOwnerId(1L);
        order.setCost(100L);

        OrderServiceImpl orderServiceSpy = Mockito.spy(orderService);

        doReturn(false).when(orderServiceSpy).modifyUserBalance(1L, -100L);

        boolean result = orderServiceSpy.payOrder(order);

        assertFalse(result);
        verify(orderServiceSpy, times(1)).modifyUserBalance(1L, -100L);
        verify(orderDao, times(0)).save(order);
    }

    // completeOrder, confirmOrder, cancelOrder
    @Test
    @DisplayName("Should complete order successfully")
    public void completeOrderSuccess() {
        Order order = new Order();
        OrderServiceImpl orderServiceSpy = Mockito.spy(orderService);

        when(orderDao.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        boolean result = orderServiceSpy.completeOrder(order);

        assertTrue(result);
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
        verify(orderDao, times(1)).save(order);
    }

    @Test
    @DisplayName("Should confirm order successfully when balance is sufficient")
    public void confirmOrderBalanceSufficient() {
        Order order = new Order();
        order.setAccessorId(1L);
        order.setCost(100L);

        OrderServiceImpl orderServiceSpy = Mockito.spy(orderService);

        doReturn(true).when(orderServiceSpy).modifyUserBalance(1L, 100L);
        when(orderDao.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        boolean result = orderServiceSpy.confirmOrder(order);

        assertTrue(result);
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        verify(orderServiceSpy, times(1)).modifyUserBalance(1L, 100L);
        verify(orderDao, times(1)).save(order);
    }

    @Test
    @DisplayName("Should return false when balance is insufficient to confirm order")
    public void confirmOrderBalanceInsufficient() {
        Order order = new Order();
        order.setAccessorId(1L);
        order.setCost(100L);

        OrderServiceImpl orderServiceSpy = Mockito.spy(orderService);

        doReturn(false).when(orderServiceSpy).modifyUserBalance(1L, 100L);

        boolean result = orderServiceSpy.confirmOrder(order);

        assertFalse(result);
        verify(orderServiceSpy, times(1)).modifyUserBalance(1L, 100L);
        verify(orderDao, times(0)).save(order);
    }

    @Test
    @DisplayName("Should cancel order successfully when balance is sufficient")
    public void cancelOrderBalanceSufficient() {
        Order order = new Order();
        order.setOwnerId(1L);
        order.setCost(100L);

        OrderServiceImpl orderServiceSpy = Mockito.spy(orderService);

        doReturn(true).when(orderServiceSpy).modifyUserBalance(1L, 100L);
        when(orderDao.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        boolean result = orderServiceSpy.cancelOrder(order);

        assertTrue(result);
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(orderServiceSpy, times(1)).modifyUserBalance(1L, 100L);
        verify(orderDao, times(1)).save(order);
    }

    @Test
    @DisplayName("Should return false when balance is insufficient to cancel order")
    public void cancelOrderBalanceInsufficient() {
        Order order = new Order();
        order.setOwnerId(1L);
        order.setCost(100L);

        OrderServiceImpl orderServiceSpy = Mockito.spy(orderService);

        doReturn(false).when(orderServiceSpy).modifyUserBalance(1L, 100L);

        boolean result = orderServiceSpy.cancelOrder(order);

        assertFalse(result);
        verify(orderServiceSpy, times(1)).modifyUserBalance(1L, 100L);
        verify(orderDao, times(0)).save(order);
    }

    // modifyUserBalance
    @Test
    @DisplayName("Should modify user balance successfully when user exists")
    public void modifyUserBalanceUserExists() {
        long userId = 1L;
        long amount = 100L;
        UserDTO user = new UserDTO();
        user.setBalance(500L);

        JSONObject getUserResult = new JSONObject();
        getUserResult.put("ok", true);
        getUserResult.put("data", user);

        JSONObject updateUserResult = new JSONObject();
        updateUserResult.put("ok", true);

        when(userClient.getUserById(userId)).thenReturn(getUserResult);
        when(userClient.updateUser(any(UserDTO.class))).thenReturn(updateUserResult);

        boolean result = orderService.modifyUserBalance(userId, amount);

        assertTrue(result);
        verify(userClient, times(1)).getUserById(userId);
        verify(userClient, times(1)).updateUser(any(UserDTO.class));
    }

    @Test
    @DisplayName("Should not modify user balance when user does not exist")
    public void modifyUserBalanceUserDoesNotExist() {
        long userId = 1L;
        long amount = 100L;

        JSONObject getUserResult = new JSONObject();
        getUserResult.put("ok", false);

        when(userClient.getUserById(userId)).thenReturn(getUserResult);

        boolean result = orderService.modifyUserBalance(userId, amount);

        assertFalse(result);
        verify(userClient, times(1)).getUserById(userId);
        verify(userClient, times(0)).updateUser(any(UserDTO.class));
    }

    @Test
    @DisplayName("Should not modify user balance when updateUser fails")
    public void modifyUserBalanceUpdateUserFails() {
        long userId = 1L;
        long amount = 100L;
        UserDTO user = new UserDTO();
        user.setBalance(500L);

        JSONObject getUserResult = new JSONObject();
        getUserResult.put("ok", true);
        getUserResult.put("data", user);

        JSONObject updateUserResult = new JSONObject();
        updateUserResult.put("ok", false);

        when(userClient.getUserById(userId)).thenReturn(getUserResult);
        when(userClient.updateUser(any(UserDTO.class))).thenReturn(updateUserResult);

        boolean result = orderService.modifyUserBalance(userId, amount);

        assertFalse(result);
        verify(userClient, times(1)).getUserById(userId);
        verify(userClient, times(1)).updateUser(any(UserDTO.class));
    }
}