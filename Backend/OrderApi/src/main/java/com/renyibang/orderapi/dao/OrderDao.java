package com.renyibang.orderapi.dao;

import com.renyibang.orderapi.entity.Order;
import com.renyibang.orderapi.enums.OrderStatus;
import java.util.List;

public interface OrderDao {
	List<Order> findByOwnerIdAndType(long ownerId, Byte type);

	List<Order> findByAccessorIdAndType(long accessorId, Byte type);

	List<Order> findByItemIdAndType(long itemId, Byte type);

	List<Order> findByStatusAndType(OrderStatus status, Byte type);

	Order findById(long orderId);

	List<Order> findAll();

	Order save(Order order);

	boolean existsById(long orderId);
}
