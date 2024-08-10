package com.renyibang.serviceapi.dao;

import com.renyibang.serviceapi.entity.Service;

import com.renyibang.serviceapi.entity.ServiceAccess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ServiceDao {
  Service findById(long serviceId);

  Page<Service> searchServiceByPaging(String keyword, Pageable pageable, LocalDateTime beginDateTime, LocalDateTime endDateTime, long priceLow, long priceHigh);

  String collectServiceByServiceId(long serviceId, long collectorId);

  String uncollectServiceByServiceId(long serviceId, long uncollectorId);

  String accessServiceByServiceId(long serviceId, long accessorId);

  String unaccessServiceByServiceId(long serviceId, long unaccessorId);

  String publishService(long userId, String title, String description, long price, int maxAccess, List<String> requestImages);

  boolean isCollected(long serviceId, long collectorId);

	boolean isAccessed(long serviceId, long ownerId);

  Page<Service> getMyService(long userId, Pageable pageable);

  Object getAccessingNumber(Service service);

  Object getSucceedNumber(Service service);

  Object getFailedNumber(Service service);

  Page<Service> getMyAccessedService(long userId, Pageable pageable);

  Page<ServiceAccess> getServiceAccessByService(Service service, Pageable pageable);

  Page<ServiceAccess> getServiceAccessSuccessByService(Service service, Pageable pageable);

  Page<ServiceAccess> getServiceAccessFailByService(Service service, Pageable pageable);
  
  String cancelService(long serviceId, long userId);

  String confirmAccessors(long serviceId, long userId, List<Long> accessors);

  String denyAccessors(long serviceId, long userId, List<Long> accessors);

  Page<Service> getMyCollect(long userId, Pageable pageable);
}