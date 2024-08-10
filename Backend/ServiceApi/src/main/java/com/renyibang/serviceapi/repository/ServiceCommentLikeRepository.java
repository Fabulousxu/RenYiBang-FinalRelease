package com.renyibang.serviceapi.repository;

import com.renyibang.serviceapi.entity.ServiceComment;
import com.renyibang.serviceapi.entity.ServiceCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCommentLikeRepository extends JpaRepository<ServiceCommentLike, Long> {
    boolean existsByLikerIdAndServiceComment(long likerId, ServiceComment serviceComment);

    ServiceCommentLike findByLikerIdAndServiceComment(long unlikerId, ServiceComment serviceComment);
}