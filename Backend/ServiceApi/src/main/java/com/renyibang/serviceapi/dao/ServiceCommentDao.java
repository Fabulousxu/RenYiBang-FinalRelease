package com.renyibang.serviceapi.dao;

import com.renyibang.serviceapi.entity.ServiceComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCommentDao {
    Page<ServiceComment> getServiceComments(long serviceId, Pageable pageable);

    String likeCommentByServiceCommentId(long serviceCommentId, long likerId);

    String unlikeCommentByServiceCommentId(long serviceCommentId, long unlikerId);

    String putComment(long serviceId, long userId, String content, byte rating);

    String deleteComment(long serviceCommentId, long userId);

    boolean isLiked(long serviceCommentId, long likerId);
}