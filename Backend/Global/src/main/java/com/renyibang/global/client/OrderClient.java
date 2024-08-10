package com.renyibang.global.client;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "OrderApi")
public interface OrderClient {
	@PutMapping("/api/order/task/create")
	JSONObject createTaskOrder(JSONObject data, @RequestHeader("userId") Long userId);

	@PutMapping("/api/order/service/create")
	JSONObject createServiceOrder(JSONObject data, @RequestHeader("userId") Long userId);
}

