package com.renyibang.orderapi.dao;

import com.renyibang.orderapi.dao.daoImpl.OrderDaoImpl;
import com.renyibang.orderapi.entity.Order;
import com.renyibang.orderapi.enums.OrderStatus;
import com.renyibang.orderapi.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderDaoImplTest {

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private OrderDaoImpl orderDao;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("Should find orders by owner id and type")
	public void findByOwnerIdAndType() {
		when(orderRepository.findByOwnerIdAndType(1L, (byte) 0)).thenReturn(Collections.emptyList());

		orderDao.findByOwnerIdAndType(1L, (byte) 0);

		verify(orderRepository, times(1)).findByOwnerIdAndType(1L, (byte) 0);
	}

	@Test
	@DisplayName("Should find orders by accessor id and type")
	public void findByAccessorIdAndType() {
		when(orderRepository.findByAccessorIdAndType(1L, (byte) 0)).thenReturn(Collections.emptyList());

		orderDao.findByAccessorIdAndType(1L, (byte) 0);

		verify(orderRepository, times(1)).findByAccessorIdAndType(1L, (byte) 0);
	}

	@Test
	@DisplayName("Should find orders by item id and type")
	public void findByItemIdAndType() {
		when(orderRepository.findByItemIdAndType(1L, (byte) 0)).thenReturn(Collections.emptyList());

		orderDao.findByItemIdAndType(1L, (byte) 0);

		verify(orderRepository, times(1)).findByItemIdAndType(1L, (byte) 0);
	}

	@Test
	@DisplayName("Should find orders by status and type")
	public void findByStatusAndType() {
		when(orderRepository.findByStatusAndType(OrderStatus.UNPAID, (byte) 0)).thenReturn(Collections.emptyList());

		orderDao.findByStatusAndType(OrderStatus.UNPAID, (byte) 0);

		verify(orderRepository, times(1)).findByStatusAndType(OrderStatus.UNPAID, (byte) 0);
	}

	@Test
	@DisplayName("Should find order by id")
	public void findById() {
		Order order = new Order();
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

		Order result = orderDao.findById(1L);

		assertEquals(order, result);
		verify(orderRepository, times(1)).findById(1L);
	}

	@Test
	@DisplayName("Should return null when order not found by id")
	public void findByIdNotFound() {
		when(orderRepository.findById(1L)).thenReturn(Optional.empty());

		Order result = orderDao.findById(1L);

		assertNull(result);
		verify(orderRepository, times(1)).findById(1L);
	}

	@Test
	@DisplayName("Should find all orders")
	public void findAll() {
		when(orderRepository.findAll()).thenReturn(Collections.emptyList());

		orderDao.findAll();

		verify(orderRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("Should save order")
	public void save() {
		Order order = new Order();
		when(orderRepository.save(order)).thenReturn(order);

		Order result = orderDao.save(order);

		assertEquals(order, result);
		verify(orderRepository, times(1)).save(order);
	}

	@Test
	@DisplayName("Should check if order exists by id")
	public void existsById() {
		when(orderRepository.existsById(1L)).thenReturn(true);

		boolean result = orderDao.existsById(1L);

		assertTrue(result);
		verify(orderRepository, times(1)).existsById(1L);
	}
}