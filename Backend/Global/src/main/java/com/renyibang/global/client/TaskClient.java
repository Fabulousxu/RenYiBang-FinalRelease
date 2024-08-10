package com.renyibang.global.client;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.dto.TaskDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "TaskApi")
public interface TaskClient {
    // Order 模块的调用
	/*
	public class TaskDTO {
		long id;
		String title;
		String description;
		String images;
		LocalDateTime time;
	}
	*/
    @GetMapping("/api/task/getTask/{taskId}")
    JSONObject getTaskById(@PathVariable Long taskId);
    ///////////////////

    @GetMapping("/api/task/{taskId}/ownerId")
    JSONObject getTaskOwnerId(@PathVariable long taskId);
}
