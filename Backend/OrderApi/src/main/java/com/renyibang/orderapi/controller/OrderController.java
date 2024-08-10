package com.renyibang.orderapi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.util.Response;
import com.renyibang.orderapi.dto.OrderDTO;
import com.renyibang.orderapi.enums.OrderStatus;
import com.renyibang.orderapi.service.OrderService;
import com.renyibang.orderapi.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
  @Autowired
  private OrderService orderService;

  // 生成订单
  // /task/create
  // Long taskId, Long ownerId, Long accessorId, Long cost
  // token中的userId
  @PutMapping("/task/create")
  public JSONObject createTaskOrder(@RequestBody JSONObject data, @RequestHeader("userId") Long userId) {
    long taskId = data.getLong("taskId");
    long ownerId = data.getLong("ownerId");
    // accessor是一个列表
    // List<Long> accessors
    List<Long> accessors = data.getJSONArray("accessors").toJavaList(Long.class);
    long cost = data.getLong("cost");
    // 校验ownerId是否为当前用户
    if (ownerId != userId) {
      return ResponseUtil.error("ownerId不匹配");
    }

    // 创建订单
    try{
      orderService.createOrder(taskId, ownerId, accessors, cost, (byte) 0);
    } catch (Exception e) {
      return ResponseUtil.error(e.getMessage());
    }
    return ResponseUtil.success("订单创建成功");
  }

  @PutMapping("/service/create")
  public JSONObject createServiceOrder(@RequestBody JSONObject data, @RequestHeader("userId") Long userId) {
    long serviceId = data.getLong("serviceId");
    long ownerId = data.getLong("ownerId");
    List<Long> accessors = data.getJSONArray("accessors").toJavaList(Long.class);
    long cost = data.getLong("cost");
    // 校验ownerId是否为当前用户
    if (ownerId != userId) {
      return ResponseUtil.error("ownerId不匹配");
    }

    // 创建订单
    try{
      orderService.createOrder(serviceId, ownerId, accessors, cost, (byte) 1);
    } catch (Exception e) {
      return ResponseUtil.error(e.getMessage());
    }
    return ResponseUtil.success("订单创建成功");
  }

  // 获取指定id的order信息
  // /api/order/{id}
  @GetMapping("/{id}")
  public JSONObject getOrder(@PathVariable Long id, @RequestHeader("userId") Long userId) {
    // 身份校验
    OrderDTO order = orderService.findById(id);
    if(order == null) {
      return ResponseUtil.error("订单不存在");
    }
    if (order.getOwner().getId() != userId && order.getAccessor().getId() != userId) {
      return ResponseUtil.error("无权查看订单");
    }
    return ResponseUtil.success(order.getDetail());
  }

  @GetMapping("/task/initiator")
  public JSONObject getTaskOrderByOwner(@RequestHeader("userId") Long userId) {
    List<OrderDTO> taskOrders = orderService.findByOwnerIdAndType(userId, (byte) 0);
    return ResponseUtil.success(toJSON(taskOrders));
  }

  @GetMapping("/task/recipient")
  public JSONObject getTaskOrderByAccessor(@RequestHeader("userId") Long userId) {
    List<OrderDTO> taskOrders = orderService.findByAccessorIdAndType(userId, (byte) 0);
    return ResponseUtil.success(toJSON(taskOrders));
  }
  @GetMapping("/service/initiator")
  public JSONObject getServiceOrderByOwner(@RequestHeader("userId") Long userId) {
    List<OrderDTO> serviceOrders = orderService.findByOwnerIdAndType(userId, (byte) 1);
    return ResponseUtil.success(toJSON(serviceOrders));
  }
  @GetMapping("/service/recipient")
  public JSONObject getServiceOrderByAccessor(@RequestHeader("userId") Long userId) {
    List<OrderDTO> serviceOrders = orderService.findByAccessorIdAndType(userId, (byte) 1);
    return ResponseUtil.success(toJSON(serviceOrders));
  }

  // 任务完成
  @PostMapping("/status")
  public JSONObject changeOrderStatus(@RequestParam Long orderId, @RequestParam Long status, @RequestHeader("userId") Long userId) {
    Pair<Boolean, String> result = orderService.markOrderStatus(orderId, userId, OrderStatus.fromCode(Math.toIntExact(status)));

    if (result.getFirst()) {
      return ResponseUtil.success("订单状态修改成功");
    } else {
      return ResponseUtil.error(result.getSecond());
    }
  }

  // 非接口
  // 传入List<Order>，对于每一个Order，调用toJSON()方法，返回List<JSONObject>
  // 使用模板
  private List<JSONObject> toJSON(List<? extends OrderDTO> orders) {
    List<JSONObject> jsonObjects = new ArrayList<>();
    for (OrderDTO order : orders) {
      jsonObjects.add(order.toJSON());
    }
    return jsonObjects;
  }

  @GetMapping("/health")
  public Response health() {
    return Response.success();
  }
}
