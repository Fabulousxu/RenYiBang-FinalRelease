package com.renyibang.orderapi.repository;

import com.renyibang.orderapi.entity.Order;
import com.renyibang.orderapi.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByOwnerIdAndType(long ownerId, Byte type);

	List<Order> findByAccessorIdAndType(long accessorId, Byte type);

	List<Order> findByItemIdAndType(long itemId, Byte type);

	List<Order> findByStatusAndType(OrderStatus status, Byte type);
}
