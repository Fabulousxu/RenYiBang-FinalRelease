package com.renyibang.orderapi.service;
import com.renyibang.orderapi.dto.OrderDTO;
import com.renyibang.orderapi.entity.Order;
import com.renyibang.orderapi.enums.OrderStatus;

import java.util.List;
import org.springframework.data.util.Pair;

public interface OrderService {
	List<OrderDTO> findByOwnerIdAndType(long ownerId, Byte type);

	List<OrderDTO> findByAccessorIdAndType(long accessorId, Byte type);

	List<OrderDTO> findByItemIdAndType(long itemId, Byte type);

	List<OrderDTO> findByStatusAndType(OrderStatus status, Byte type);

	// 根据订单ID查找订单
	OrderDTO findById(long orderId);

	// 返回所有订单
	List<OrderDTO> findAllOrders();

	// 生成初始订单
	boolean createOrder(long taskId, long ownerId, List<Long> accessors, long cost, Byte type);

	OrderDTO mapOrderToOrderDTO(Order order);

	void mapOrdersToOrderDTOs(List<Order> orders, List<OrderDTO> orderDTOs);

	// 标记订单状态
	Pair<Boolean, String> markOrderStatus(long orderId, long userId, OrderStatus status);

	boolean setOrderStatusForce(long orderId, OrderStatus status);

	// 校验订单是否存在
	boolean checkOrderExist(long orderId);

	// 校验订单状态是否为指定状态
	boolean checkOrderStatus(long orderId, OrderStatus status);

	boolean payOrder(Order order);

	boolean completeOrder(Order order);

	boolean confirmOrder(Order order);

	boolean cancelOrder(Order order);

	// 用户转账
	boolean modifyUserBalance(long userId, long amount);
}
	
	
	
	