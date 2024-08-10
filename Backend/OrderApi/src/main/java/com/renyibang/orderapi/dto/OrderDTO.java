package com.renyibang.orderapi.dto;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.dto.ServiceDTO;
import com.renyibang.global.dto.TaskDTO;
import com.renyibang.global.dto.UserDTO;
import com.renyibang.orderapi.entity.Order;
import java.util.Collections;
import lombok.Data;

@Data
public class OrderDTO {

	Order order;
	TaskDTO task;
	ServiceDTO service;
	UserDTO owner;
	UserDTO accessor;

	public OrderDTO() {

	}

	public OrderDTO(Order order) {
		this.order = order;
	}

	// Getters and setters

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", order.getOrderId());
		json.put("type", order.getType());
		json.put("initiator", owner.getNickname());
		json.put("recipient", accessor.getNickname());
		json.put("status", order.getStatus().getCode());
		json.put("cost", order.getCost());
		json.put("time", order.getType() == 0 ? task.getTime() : service.getTime());
		json.put("name", order.getType() == 0 ? task.getTitle() : service.getTitle());
		return json;
	}

	public JSONObject getDetail() {
		JSONObject json = new JSONObject();
		json.put("name", order.getType() == 0 ? task.getTitle() : service.getTitle());
		json.put("initiator_name", owner.getNickname());
		json.put("initiator_img", owner.getAvatar());
		json.put("recipient_name", accessor.getNickname());
		json.put("recipient_img", accessor.getAvatar());
		json.put("status", order.getStatus().getCode());
		json.put("time", order.getType() == 0 ? task.getTime() : service.getTime());
		json.put("order_img", order.getType() == 0 ? Collections.singletonList(task.getImages()) : Collections.singletonList(service.getImages()));
		json.put("description", order.getType() == 0 ? task.getDescription() : service.getDescription());
		return json;
	}
}
