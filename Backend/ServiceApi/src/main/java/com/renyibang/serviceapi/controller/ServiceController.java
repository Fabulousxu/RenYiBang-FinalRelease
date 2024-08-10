package com.renyibang.serviceapi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.util.Response;
import com.renyibang.serviceapi.service.ServiceService;
import com.renyibang.serviceapi.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/service")
public class ServiceController {
    @Autowired ServiceService serviceService;

    @GetMapping("/search")
    public JSONObject searchService(String keyword,
                                    int pageSize,
                                    int pageIndex,
                                    String order,
                                    String timeBegin,
                                    String timeEnd,
                                    long priceLow,
                                    long priceHigh,
                                    @RequestHeader long userId)
    {
        Sort sort;

        if(Objects.equals(order, "time") && !keyword.isEmpty())
        {
            sort = Sort.by("created_at").descending();
        } else if(Objects.equals(order, "time"))
        {
            sort = Sort.by("createdAt").descending();
        }

        else if (Objects.equals(order, "rating"))
        {
            sort = Sort.by("rating").descending();
        }

        else
        {
            return ResponseUtil.error("排序类型错误");
        }

        if (pageSize <= 0 || pageIndex < 0) return ResponseUtil.error("分页错误");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        return serviceService.searchServiceByPaging(keyword, pageable, timeBegin, timeEnd, priceLow, priceHigh, userId);
    }

    @GetMapping("/{serviceId}")
    public JSONObject getServiceInfo(@PathVariable long serviceId, @RequestHeader long userId)
    {
        return serviceService.getServiceInfo(serviceId, userId);
    }

    @GetMapping("/{serviceId}/comment")
    public JSONObject getServiceComment(@PathVariable long serviceId, int pageSize, int pageIndex, String order, @RequestHeader long userId)
    {
        Sort sort;

        if(Objects.equals(order, "likes"))
        {
            sort = Sort.by("likedNumber").descending();
        }

        else if(Objects.equals(order, "time"))
        {
            sort = Sort.by("createdAt").descending();
        }

        else
        {
            return ResponseUtil.error("排序类型错误");
        }

        if (pageSize <= 0 || pageIndex < 0) return ResponseUtil.error("分页错误");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        return serviceService.getServiceComments(serviceId, pageable, userId);
    }

    @GetMapping("/{serviceId}/message")
    public JSONObject getServiceMessage(@PathVariable long serviceId, int pageSize, int pageIndex, String order, @RequestHeader long userId)
    {
        Sort sort;

        if(Objects.equals(order, "likes"))
        {
            sort = Sort.by("likedNumber").descending();
        }

        else if(Objects.equals(order, "time"))
        {
            sort = Sort.by("createdAt").descending();
        }

        else
        {
            return ResponseUtil.error("排序类型错误");
        }

        if (pageSize <= 0 || pageIndex < 0) return ResponseUtil.error("分页错误");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        return serviceService.getServiceMessages(serviceId, pageable, userId);
    }

    @PutMapping("/comment/{serviceCommentId}/like")
    public JSONObject likeComment(@PathVariable long serviceCommentId, @RequestHeader long userId)
    {
        return serviceService.likeComment(serviceCommentId, userId);
    }

    @DeleteMapping("/comment/{serviceCommentId}/unlike")
    public JSONObject unlikeComment(@PathVariable long serviceCommentId, @RequestHeader long userId)
    {
        return serviceService.unlikeComment(serviceCommentId, userId);
    }

    @PutMapping("/message/{serviceMessageId}/like")
    public JSONObject likeMessage(@PathVariable long serviceMessageId, @RequestHeader long userId)
    {
        return serviceService.likeMessage(serviceMessageId, userId);
    }

    @DeleteMapping("/message/{serviceMessageId}/unlike")
    public JSONObject unlikeMessage(@PathVariable long serviceMessageId, @RequestHeader long userId)
    {
        return serviceService.unlikeMessage(serviceMessageId, userId);
    }

    @PutMapping("/{serviceId}/collect")
    public JSONObject collectService(@PathVariable long serviceId, @RequestHeader long userId)
    {
        return serviceService.collectService(serviceId, userId);
    }

    @DeleteMapping("/{serviceId}/uncollect")
    public JSONObject uncollectService(@PathVariable long serviceId, @RequestHeader long userId)
    {
        return serviceService.uncollectService(serviceId, userId);
    }

    @PutMapping("/{serviceId}/access")
    public JSONObject accessService(@PathVariable long serviceId, @RequestHeader long userId)
    {
        return serviceService.accessService(serviceId, userId);
    }

    @DeleteMapping("/{serviceId}/unaccess")
    public JSONObject unaccessService(@PathVariable long serviceId, @RequestHeader long userId)
    {
        return serviceService.unaccessService(serviceId, userId);
    }

    @PutMapping("/{serviceId}/message")
    public JSONObject publishMessage(@PathVariable long serviceId, @RequestBody JSONObject body, @RequestHeader long userId)
    {
        return serviceService.publishMessage(serviceId, userId, body);
    }

    @DeleteMapping("/message/{serviceMessageId}")
    public JSONObject deleteMessage(@PathVariable long serviceMessageId, @RequestHeader long userId)
    {
        return serviceService.deleteMessage(serviceMessageId, userId);
    }

    @PutMapping("/{serviceId}/comment")
    public JSONObject publishComment(@PathVariable long serviceId, @RequestBody JSONObject body, @RequestHeader long userId)
    {
        return serviceService.publishComment(serviceId, userId, body);
    }

    @DeleteMapping("/comment/{serviceCommentId}")
    public JSONObject deleteComment(@PathVariable long serviceCommentId, @RequestHeader long userId)
    {
        return serviceService.deleteComment(serviceCommentId, userId);
    }

    @PostMapping("/issue")
    public JSONObject publishService(@RequestBody JSONObject body, @RequestHeader long userId)
    {
        return serviceService.publishService(userId, body);
    }

    // 以下为后端模块API接口

    //order模块调用
    @GetMapping("/getService/{serviceId}")
    public JSONObject getServiceDtoById(@PathVariable long serviceId)
    {
        return serviceService.getServiceDtoById(serviceId);
    }

    //chat模块调用
    @GetMapping("/{serviceId}/ownerId")
    public JSONObject getServiceOwnerId(@PathVariable long serviceId)
    {
        return serviceService.getServiceOwnerId(serviceId);
    }

    @GetMapping("/initiator/self")
    public JSONObject getMyService(int pageSize, int pageIndex, @RequestHeader long userId)
    {
        if (pageSize <= 0 || pageIndex < 0) return ResponseUtil.error("分页错误");
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return serviceService.getMyService(pageable, userId);
    }

    @GetMapping("/recipient/self")
    public JSONObject getMyAccessedService(int pageSize, int pageIndex, @RequestHeader long userId)
    {
        if (pageSize <= 0 || pageIndex < 0) return ResponseUtil.error("分页错误");
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return serviceService.getMyAccessedService(pageable, userId);
    }

    @GetMapping("/{serviceId}/select/info")
    public JSONObject getServiceAccessorInfo(@PathVariable long serviceId, @RequestHeader long userId, int pageSize, int pageIndex)
    {
        return serviceService.getServiceAccessorInfo(serviceId, userId, PageRequest.of(pageIndex, pageSize));
    }

    @GetMapping("/{serviceId}/select/success")
    public JSONObject getServiceAccessorSuccess(@PathVariable long serviceId, @RequestHeader long userId, int pageSize, int pageIndex) {
        return serviceService.getServiceAccessorSuccess(serviceId, userId, PageRequest.of(pageIndex, pageSize));
    }

    @GetMapping("/{serviceId}/select/fail")
    public JSONObject getServiceAccessorFail(@PathVariable long serviceId, @RequestHeader long userId, int pageSize, int pageIndex) {
        return serviceService.getServiceAccessorFail(serviceId, userId, PageRequest.of(pageIndex, pageSize));
    }

    @DeleteMapping("/{serviceId}/cancel")
    public JSONObject cancelService(@PathVariable long serviceId, @RequestHeader long userId) {
        return serviceService.cancelService(serviceId, userId);
    }

    @PutMapping("/{serviceId}/select/confirm")
    public JSONObject confirmAccessors(@PathVariable long serviceId, @RequestHeader long userId, @RequestBody JSONObject body) {
        return serviceService.confirmAccessors(serviceId, userId, body);
    }

    @PutMapping("/{serviceId}/select/deny")
    public JSONObject denyAccessors(@PathVariable long serviceId, @RequestHeader long userId, @RequestBody JSONObject body) {
        return serviceService.denyAccessors(serviceId, userId, body);
    }

    @GetMapping("/mycollect")
    public JSONObject getMyCollect(int pageSize, int pageIndex, @RequestHeader long userId) {
        if (pageSize <= 0 || pageIndex < 0) return ResponseUtil.error("分页错误");
        return serviceService.getMyCollect(PageRequest.of(pageIndex, pageSize), userId);
    }

    @GetMapping("/health")
    public Response health() {
        return Response.success();
    }
}