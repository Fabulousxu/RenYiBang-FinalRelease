package com.renyibang.serviceapi.repository;

import com.renyibang.serviceapi.entity.Service;
import com.renyibang.serviceapi.entity.ServiceComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCommentRepository extends JpaRepository<ServiceComment, Long> {
    Page<ServiceComment> findByServiceServiceId(long serviceId, Pageable pageable);

    boolean existsByServiceAndCommenterId(Service service, long userId);
}