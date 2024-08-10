package com.renyibang.serviceapi.dao.daoImpl;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.client.UserClient;
import com.renyibang.global.client.OrderClient;
import com.renyibang.serviceapi.dao.ServiceDao;
import com.renyibang.serviceapi.entity.Service;
import com.renyibang.serviceapi.entity.ServiceAccess;
import com.renyibang.serviceapi.entity.ServiceCollect;
import com.renyibang.serviceapi.enums.ServiceStatus;
import com.renyibang.serviceapi.enums.ServiceAccessStatus;
import com.renyibang.serviceapi.repository.ServiceAccessRepository;
import com.renyibang.serviceapi.repository.ServiceCollectRepository;
import com.renyibang.serviceapi.repository.ServiceRepository;
import com.renyibang.serviceapi.util.ImageUtil;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

@Repository
public class ServiceDaoImpl implements ServiceDao {
  @Autowired
  ServiceRepository serviceRepository;

  @Autowired
  ServiceCollectRepository serviceCollectRepository;

  @Autowired
  ServiceAccessRepository serviceAccessRepository;

  @Autowired
  UserClient userClient;

  @Autowired
  OrderClient orderClient;

  @Override
  public Service findById(long serviceId) {
    return serviceRepository.findById(serviceId).orElse(null);
  }

  @Override
  public Page<Service> searchServiceByPaging(
      String keyword,
      Pageable pageable,
      LocalDateTime beginDateTime,
      LocalDateTime endDateTime,
      long priceLow,
      long priceHigh) {
    if (!keyword.isEmpty()) {
      return serviceRepository.searchServices(
              keyword, priceLow, priceHigh, beginDateTime, endDateTime, ServiceStatus.DELETE, pageable);
    } else {
      return serviceRepository.findByPriceBetweenAndCreatedAtBetweenAndStatusNot(
              priceLow, priceHigh, beginDateTime, endDateTime, ServiceStatus.DELETE, pageable);
    }
  }

  @Override
  public String collectServiceByServiceId(long serviceId, long collectorId) {
    try {
      if (!userClient.getUserExist(collectorId)) {
        return "用户不存在！";
      }

      Service service = serviceRepository.findById(serviceId).orElse(null);
      if (service == null) {
        return "服务不存在！";
      }

      if (service.getStatus() == ServiceStatus.DELETE) {
        return "该服务已被删除！";
      }

      if (serviceCollectRepository.existsByCollectorIdAndAndService(collectorId, service)) {
        return "用户已收藏该服务！";
      }

      service.setCollectedNumber(service.getCollectedNumber() + 1);
      ServiceCollect serviceCollect = new ServiceCollect();
      serviceCollect.setCollectorId(collectorId);
      serviceCollect.setService(service);
      serviceCollect.setCreatedAt(LocalDateTime.now());

      serviceCollectRepository.save(serviceCollect);

      return "收藏成功！";
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public String uncollectServiceByServiceId(long serviceId, long uncollectorId) {
    try {
      if (!userClient.getUserExist(uncollectorId)) {
        return "用户不存在！";
      }

      Service service = serviceRepository.findById(serviceId).orElse(null);
      if (service == null) {
        return "服务不存在！";
      }

//      if (service.getStatus() == ServiceStatus.DELETE) {
//        return "该服务已被删除！";
//      }

      ServiceCollect serviceCollect =
              serviceCollectRepository.findByServiceAndCollectorId(service, uncollectorId);
      if (serviceCollect == null) {
        return "用户未收藏该服务！";
      }

      service.setCollectedNumber(service.getCollectedNumber() - 1);
      serviceCollectRepository.delete(serviceCollect);

      return "取消收藏成功！";
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public String accessServiceByServiceId(long serviceId, long accessorId) {
    try {
      Service service = serviceRepository.findById(serviceId).orElse(null);
      if (service == null) {
        return "服务不存在！";
      }

      if (service.getStatus() == ServiceStatus.DELETE) {
        return "该服务已被删除！";
      } else if (service.getStatus() == ServiceStatus.REMOVE) {
        return "该服务已被下架！";
      }

      if (service.getOwnerId() == accessorId) {
        return "不能接取自己发布的服务！";
      }

      if (!userClient.getUserExist(accessorId)) {
        return "用户不存在！";
      }

      if (serviceAccessRepository.existsByAccessorIdAndService(accessorId, service)) {
        return "用户已经接取该服务！";
      }

      if (!service.accessNotFull()) {
        return "该服务接取已达上限！";
      }

      if (service.getStatus() == ServiceStatus.DELETE) {
        return "该服务已被删除！";
      } else if (service.getStatus() == ServiceStatus.REMOVE) {
        return "该服务已被下架！";
      }

      ServiceAccess serviceAccess = new ServiceAccess();
      serviceAccess.setAccessorId(accessorId);
      serviceAccess.setService(service);
      serviceAccess.setCreatedAt(LocalDateTime.now());
      serviceAccess.setServiceAccessStatus(ServiceAccessStatus.ACCESSING);

      serviceAccessRepository.save(serviceAccess);
      return "接取服务成功！";
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public String unaccessServiceByServiceId(long serviceId, long unaccessorId) {
    try {
      Service service = serviceRepository.findById(serviceId).orElse(null);
      if (service == null) {
        return "服务不存在！";
      }

//      if (service.getStatus() == ServiceStatus.DELETE) {
//        return "该服务已被删除！";
//      }

      if (!userClient.getUserExist(unaccessorId)) {
        return "用户不存在！";
      }

      ServiceAccess serviceAccess =
              serviceAccessRepository.findByServiceAndAccessorId(service, unaccessorId);
      if (serviceAccess == null) {
        return "用户未接取该服务！";
      }

      serviceAccessRepository.delete(serviceAccess);
      return "取消接取服务成功！";
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public String publishService(
          long userId, String title, String description, long price, int maxAccess, List<String> requestImages) {
    try {
      if (!userClient.getUserExist(userId)) {
        return "用户不存在！";
      }

      String imagesURL = ImageUtil.mergeImages(requestImages);

      Service service = new Service();
      service.setOwnerId(userId);
      service.setTitle(title);
      service.setDescription(description);
      service.setPrice(price);
      service.setImages(imagesURL);
      service.setCreatedAt(LocalDateTime.now());
      service.setMaxAccess(maxAccess);

      serviceRepository.save(service);
      return "服务发布成功！";
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public boolean isCollected(long serviceId, long collectorId) {
    return serviceCollectRepository.existsByCollectorIdAndAndService(
            collectorId, serviceRepository.findById(serviceId).orElse(null));
  }

  @Override
  public boolean isAccessed(long serviceId, long ownerId) {
    return serviceAccessRepository.existsByAccessorIdAndService(
            ownerId, serviceRepository.findById(serviceId).orElse(null));
  }

  @Override
  public Page<Service> getMyService(long userId, Pageable pageable) {
    Page<Service> page = serviceRepository.findByOwnerId(userId, pageable);
    List<Service> filteredServices = page.stream()
        .filter(Service -> Service.getStatus() == ServiceStatus.NORMAL)
        .collect(Collectors.toList());
    return new PageImpl<>(filteredServices, pageable, filteredServices.size());
  }

  @Override
  public Object getAccessingNumber(Service service) {
    return serviceAccessRepository.countByServiceAndServiceAccessStatus(service, ServiceAccessStatus.ACCESSING);
  }

  @Override
  public Object getSucceedNumber(Service service) {
    return serviceAccessRepository.countByServiceAndServiceAccessStatus(service, ServiceAccessStatus.ACCESS_SUCCESS);
  }

  @Override
  public Object getFailedNumber(Service service) {
    return serviceAccessRepository.countByServiceAndServiceAccessStatus(service, ServiceAccessStatus.ACCESS_FAIL);
  }

  @Override
  public Page<Service> getMyAccessedService(long userId, Pageable pageable) {
    return serviceRepository.findByAccessorId(userId, pageable);
  }

  @Override
  public Page<ServiceAccess> getServiceAccessByService(Service service, Pageable pageable) {
    return serviceAccessRepository.findByServiceAndServiceAccessStatus(service, pageable, ServiceAccessStatus.ACCESSING);
  }

  @Override
  public Page<ServiceAccess> getServiceAccessSuccessByService(Service service, Pageable pageable) {
    return serviceAccessRepository.findByServiceAndServiceAccessStatus(service, pageable, ServiceAccessStatus.ACCESS_SUCCESS);
  }

  @Override
  public Page<ServiceAccess> getServiceAccessFailByService(Service service, Pageable pageable) {
    return serviceAccessRepository.findByServiceAndServiceAccessStatus(service, pageable, ServiceAccessStatus.ACCESS_FAIL);
  }

  @Override
  public String cancelService(long serviceId, long userId) {
    Service service = serviceRepository.findById(serviceId).orElse(null);
    if (service == null) {
      return "服务不存在！";
    }

    if (service.getOwnerId() != userId) {
      return "只有服务发布者才能取消该服务！";
    }

    if (service.getStatus() == ServiceStatus.DELETE) {
      return "该服务已被删除！";
    }

    service.setStatus(ServiceStatus.DELETE);
    serviceRepository.save(service);

    return "取消服务成功！";
  }

  @Override
  public String confirmAccessors(long serviceId, long userId, List<Long> accessors) {
    Service service = serviceRepository.findById(serviceId).orElse(null);
    if (service == null) {
      return "服务不存在！";
    }

    if (service.getOwnerId() != userId) {
      return "只有服务发布者才能确认接取者！";
    }

    if (service.getStatus() == ServiceStatus.DELETE) {
      return "该服务已被删除！";
    }

    if (service.getStatus() == ServiceStatus.REMOVE) {
      return "该服务已被下架！";
    }

    if (service.getMaxAccess() < accessors.size()) {
      return "接取者数量超过最大接取数！";
    }

    Set<ServiceAccess> confirmAccessors = new HashSet<>();

    for (long accessorId : accessors) {
      ServiceAccess serviceAccess = serviceAccessRepository.findByServiceAndAccessorId(service, accessorId);
      if (serviceAccess == null) {
        return "接取者" + accessorId + "不存在！";
      }

      else if (serviceAccess.getServiceAccessStatus() != ServiceAccessStatus.ACCESSING) {
        return "接取者状态异常！";
      }

      confirmAccessors.add(serviceAccess);
    }

    JSONObject orderRequest = new JSONObject();
    orderRequest.put("serviceId", serviceId);
    orderRequest.put("ownerId", userId);
    orderRequest.put("accessors", accessors);
    orderRequest.put("cost", service.getPrice());

    JSONObject result = orderClient.createServiceOrder(orderRequest, userId);
    if(Objects.equals(false, result.get("ok"))) {
      return "创建订单失败！";
    }

    for (ServiceAccess serviceAccess : confirmAccessors) {
      serviceAccess.setServiceAccessStatus(ServiceAccessStatus.ACCESS_SUCCESS);
      serviceAccessRepository.save(serviceAccess);
    }

    return "确认接取者成功！";
  }

  @Override
  public String denyAccessors(long serviceId, long userId, List<Long> accessors){
    Service service = serviceRepository.findById(serviceId).orElse(null);
    if (service == null) {
      return "任务不存在！";
    }

    if (service.getOwnerId() != userId) {
      return "只有任务发布者才能拒绝接取者！";
    }

    if (service.getStatus() == ServiceStatus.DELETE) {
      return "该任务已被删除！";
    }

    if (service.getStatus() == ServiceStatus.REMOVE) {
      return "该任务已被下架！";
    }

    for (long accessorId : accessors) {
      ServiceAccess serviceAccess = serviceAccessRepository.findByServiceAndAccessorId(service, accessorId);
      if (serviceAccess == null) {
        return "接取者" + accessorId +"不存在！";
      }

      else if (serviceAccess.getServiceAccessStatus() != ServiceAccessStatus.ACCESSING) {
        return "接取者状态异常！";
      }

      serviceAccess.setServiceAccessStatus(ServiceAccessStatus.ACCESS_FAIL);
      serviceAccessRepository.save(serviceAccess);
    }

    return "拒绝接取者成功！";
  }

  @Override
  public Page<Service> getMyCollect(long userId, Pageable pageable) {
    Page<ServiceCollect> serviceCollects = serviceCollectRepository.findByCollectorId(userId, pageable);
    List<Service> services = serviceCollects.stream().map(ServiceCollect::getService).collect(Collectors.toList());
    return new PageImpl<>(services, pageable, services.size());
  }
}
