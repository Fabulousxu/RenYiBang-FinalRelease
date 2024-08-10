package com.renyibang.global.client;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ServiceApi")
public interface ServiceClient {
  // Order 模块的调用
	/*
	public class ServiceDTO {
		long id;
		String title;
		String description;
		String images;
		LocalDateTime time;
	}
	 */
  @GetMapping("/api/service/getService/{serviceId}")
  JSONObject getServiceById(@PathVariable Long serviceId);

  //////////////////////

  @GetMapping("/api/service/{serviceId}/ownerId")
  JSONObject getServiceOwnerId(@PathVariable long serviceId);

}
