package com.renyibang.serviceapi.dao.daoImpl;

import com.renyibang.global.client.UserClient;
import com.renyibang.serviceapi.dao.ServiceMessageDao;
import com.renyibang.serviceapi.entity.Service;
import com.renyibang.serviceapi.entity.ServiceMessage;
import com.renyibang.serviceapi.entity.ServiceMessageLike;
import com.renyibang.serviceapi.repository.ServiceMessageLikeRepository;
import com.renyibang.serviceapi.repository.ServiceMessageRepository;
import com.renyibang.serviceapi.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class ServiceMessageDaoImpl implements ServiceMessageDao {
    @Autowired
    private ServiceMessageRepository serviceMessageRepository;

    @Autowired ServiceRepository serviceRepository;

    @Autowired ServiceMessageLikeRepository serviceMessageLikeRepository;

    @Autowired UserClient userClient;

    @Override
    public Page<ServiceMessage> getServiceMessages(long serviceId, Pageable pageable)
    {
        return serviceMessageRepository.findByServiceServiceId(serviceId, pageable);
    }

    @Override
    public String likeMessageByServiceMessageId(long serviceMessageId, long likerId)
    {
        try
        {
            ServiceMessage serviceMessage = serviceMessageRepository.findById(serviceMessageId).orElse(null);
            if(serviceMessage == null)
            {
                return "留言不存在！";
            }

            if(!userClient.getUserExist(likerId))
            {
                return "用户不存在！";
            }

            if(serviceMessageLikeRepository.existsByLikerIdAndServiceMessage(likerId, serviceMessage))
            {
                return "用户已点赞过该留言！";
            }

            else
            {
                serviceMessage.setLikedNumber(serviceMessage.getLikedNumber() + 1);
                serviceMessageRepository.save(serviceMessage);

                ServiceMessageLike serviceMessageLike = new ServiceMessageLike();
                serviceMessageLike.setLikerId(likerId);
                serviceMessageLike.setServiceMessage(serviceMessage);
                serviceMessageLikeRepository.save(serviceMessageLike);

                return "点赞成功！";
            }
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    @Override
    public String unlikeMessageByServiceMessageId(long serviceMessageId, long unlikerId)
    {
        try
        {
            ServiceMessage serviceMessage = serviceMessageRepository.findById(serviceMessageId).orElse(null);
            if(serviceMessage == null)
            {
                return "留言不存在！";
            }

            if(!userClient.getUserExist(unlikerId))
            {
                return "用户不存在！";
            }

            ServiceMessageLike serviceMessageLike = serviceMessageLikeRepository.findByLikerIdAndServiceMessage(unlikerId, serviceMessage);

            if(serviceMessageLike == null)
            {
                return "用户未点赞过该留言！";
            }

            else
            {
                serviceMessage.setLikedNumber(serviceMessage.getLikedNumber() - 1);
                serviceMessageRepository.save(serviceMessage);

                serviceMessageLikeRepository.delete(serviceMessageLike);
                return "取消点赞成功！";
            }
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    @Override
    public String putMessage(long serviceId, long userId, String content) {
        try {
            if (!userClient.getUserExist(userId)) {
                return "用户不存在！";
            }

            Service service = serviceRepository.findById(serviceId).orElse(null);
            if (service == null) {
                return "服务不存在！";
            }

            ServiceMessage serviceMessage = new ServiceMessage();
            serviceMessage.setService(service);
            serviceMessage.setMessagerId(userId);
            serviceMessage.setContent(content);
            serviceMessage.setCreatedAt(LocalDateTime.now());
            serviceMessageRepository.save(serviceMessage);

            return "发布留言成功！";
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String deleteMessage(long serviceMessageId, long userId)
    {
        try
        {
            if(!userClient.getUserExist(userId))
            {
                return "用户不存在！";
            }

            ServiceMessage serviceMessage = serviceMessageRepository.findById(serviceMessageId).orElse(null);
            if (serviceMessage == null) {
                return "留言不存在！";
            }

            if (serviceMessage.getMessagerId() != userId) {
                return "该留言不是由此用户发布！";
            }

            serviceMessageRepository.deleteById(serviceMessageId);
            return "删除留言成功！";
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public boolean isLiked(long serviceMessageId, long likerId)
    {
        ServiceMessage serviceMessage = serviceMessageRepository.findById(serviceMessageId).orElse(null);
        if(serviceMessage == null)
        {
            return false;
        }

        return serviceMessageLikeRepository.existsByLikerIdAndServiceMessage(likerId, serviceMessage);
    }
}