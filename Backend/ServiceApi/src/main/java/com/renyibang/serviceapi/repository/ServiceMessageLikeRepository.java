package com.renyibang.serviceapi.repository;

import com.renyibang.serviceapi.entity.ServiceMessage;
import com.renyibang.serviceapi.entity.ServiceMessageLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceMessageLikeRepository extends JpaRepository<ServiceMessageLike, Long> {

    boolean existsByLikerIdAndServiceMessage(long likerId, ServiceMessage serviceMessage);

    ServiceMessageLike findByLikerIdAndServiceMessage(long unlikerId, ServiceMessage serviceMessage);
}