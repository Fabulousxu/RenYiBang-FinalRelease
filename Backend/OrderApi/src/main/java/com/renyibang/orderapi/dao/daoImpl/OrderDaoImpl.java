package com.renyibang.orderapi.dao.daoImpl;

import com.renyibang.orderapi.dao.OrderDao;
import com.renyibang.orderapi.entity.Order;
import com.renyibang.orderapi.enums.OrderStatus;
import com.renyibang.orderapi.repository.OrderRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDaoImpl implements OrderDao {
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public List<Order> findByOwnerIdAndType(long ownerId, Byte type) {
		return orderRepository.findByOwnerIdAndType(ownerId, type);
	}

	@Override
	public List<Order> findByAccessorIdAndType(long accessorId, Byte type) {
		return orderRepository.findByAccessorIdAndType(accessorId, type);
	}

	@Override
	public List<Order> findByItemIdAndType(long itemId, Byte type) {
		return orderRepository.findByItemIdAndType(itemId, type);
	}

	@Override
	public List<Order> findByStatusAndType(OrderStatus status, Byte type) {
			return orderRepository.findByStatusAndType(status, type);
	}

	@Override
	public Order findById(long orderId) {
			return orderRepository.findById(orderId).orElse(null);
	}

	@Override
	public List<Order> findAll() {
			return orderRepository.findAll();
	}

	@Override
	public Order save(Order order) {
			return orderRepository.save(order);
	}

	@Override
	public boolean existsById(long orderId) {
			return orderRepository.existsById(orderId);
	}
}