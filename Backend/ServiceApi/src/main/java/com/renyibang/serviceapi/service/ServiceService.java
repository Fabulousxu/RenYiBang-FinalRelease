package com.renyibang.serviceapi.service;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ServiceService {
    JSONObject searchServiceByPaging(String keyword, Pageable pageable, String timeBegin, String timeEnd, long priceLow, long priceHigh, long userId);

    JSONObject getServiceInfo(long serviceId, long userId);

    JSONObject getServiceComments(long serviceId, Pageable pageable, long userId);

    JSONObject getServiceMessages(long serviceId, Pageable pageable, long userId);

    JSONObject likeComment(long serviceCommentId, long likerId);

    JSONObject unlikeComment(long serviceCommentId, long unlikerId);

    JSONObject likeMessage(long serviceMessageId, long likerId);

    JSONObject unlikeMessage(long serviceMessageId, long unlikerId);

    JSONObject collectService(long serviceId, long collectorId);

    JSONObject uncollectService(long serviceId, long uncollectorId);

    JSONObject accessService(long serviceId, long accessorId);

    JSONObject unaccessService(long serviceId, long unaccessorId);

    JSONObject publishMessage(long serviceId, long userId, JSONObject body);

    JSONObject deleteMessage(long serviceMessageId, long userId);

    JSONObject publishComment(long serviceId, long userId, JSONObject body);

    JSONObject deleteComment(long serviceCommentId, long userId);

    JSONObject publishService(long userId, JSONObject body);

    JSONObject getServiceDtoById(Long serviceId);

    JSONObject getServiceOwnerId(long serviceId);

    JSONObject getMyService(Pageable pageable, long userId);

    JSONObject getMyAccessedService(Pageable pageable, long userId);

    JSONObject getServiceAccessorInfo(long serviceId, long userId, Pageable pageable);

    JSONObject cancelService(long serviceId, long userId);

    JSONObject confirmAccessors(long serviceId, long userId, JSONObject body);

    JSONObject getServiceAccessorSuccess(long taskId, long userId, Pageable pageable);

    JSONObject getServiceAccessorFail(long taskId, long userId, Pageable pageable);

    JSONObject denyAccessors(long taskId, long userId, JSONObject body);

    JSONObject getMyCollect(Pageable pageable, long userId);
}