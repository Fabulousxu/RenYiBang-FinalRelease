package com.renyibang.serviceapi.dao.daoImpl;

import com.renyibang.global.client.UserClient;
import com.renyibang.serviceapi.dao.ServiceCommentDao;
import com.renyibang.serviceapi.entity.Service;
import com.renyibang.serviceapi.entity.ServiceComment;
import com.renyibang.serviceapi.entity.ServiceCommentLike;
import com.renyibang.serviceapi.repository.ServiceCommentLikeRepository;
import com.renyibang.serviceapi.repository.ServiceCommentRepository;
import com.renyibang.serviceapi.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class ServiceCommentDaoImpl implements ServiceCommentDao {
    @Autowired
    private ServiceCommentRepository serviceCommentRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ServiceCommentLikeRepository serviceCommentLikeRepository;

    @Autowired
    UserClient userClient;

    @Override
    public Page<ServiceComment> getServiceComments(long serviceId, Pageable pageable) {
        return serviceCommentRepository.findByServiceServiceId(serviceId, pageable);
    }

    @Override
    public String likeCommentByServiceCommentId(long serviceCommentId, long likerId) {
        try {
            ServiceComment serviceComment = serviceCommentRepository.findById(serviceCommentId).orElse(null);
            if (serviceComment == null) {
                return "评论不存在！";
            }

            if (!userClient.getUserExist(likerId)) {
                return "用户不存在！";
            }

            if (serviceCommentLikeRepository.existsByLikerIdAndServiceComment(likerId, serviceComment)) {
                return "用户已点赞过该评论！";
            } else {
                serviceComment.setLikedNumber(serviceComment.getLikedNumber() + 1);
                serviceCommentRepository.save(serviceComment);

                ServiceCommentLike serviceCommentLike = new ServiceCommentLike();
                serviceCommentLike.setLikerId(likerId);
                serviceCommentLike.setServiceComment(serviceComment);
                serviceCommentLikeRepository.save(serviceCommentLike);

                return "点赞成功！";
            }

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String unlikeCommentByServiceCommentId(long serviceCommentId, long likerId) {
        try {
            ServiceComment serviceComment = serviceCommentRepository.findById(serviceCommentId).orElse(null);
            if (serviceComment == null) {
                return "评论不存在！";
            }

            if (!userClient.getUserExist(likerId)) {
                return "用户不存在！";
            }

            ServiceCommentLike serviceCommentLike = serviceCommentLikeRepository.findByLikerIdAndServiceComment(likerId, serviceComment);

            if (serviceCommentLike == null) {
                return "用户未点赞过该评论！";
            } else {
                serviceComment.setLikedNumber(serviceComment.getLikedNumber() - 1);
                serviceCommentRepository.save(serviceComment);

                serviceCommentLikeRepository.delete(serviceCommentLike);

                return "取消点赞成功！";

            }

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String putComment(long serviceId, long userId, String content, byte rating) {
        try {
            if (!userClient.getUserExist(userId)) {
                return "用户不存在！";
            }

            Service service = serviceRepository.findById(serviceId).orElse(null);
            if (service == null) {
                return "服务不存在！";
            }

            if (serviceCommentRepository.existsByServiceAndCommenterId(service, userId)) {
                return "用户已经评论过该服务！";
            }

            ServiceComment serviceComment = new ServiceComment();
            serviceComment.setService(service);
            serviceComment.setCommenterId(userId);
            serviceComment.setContent(content);
            serviceComment.setCreatedAt(LocalDateTime.now());
            serviceComment.setRating(rating);

            serviceCommentRepository.save(serviceComment);

            return "发布评论成功！";
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String deleteComment(long serviceCommentId, long userId) {
        try
        {
            if(!userClient.getUserExist(userId))
            {
                return "用户不存在！";
            }

            ServiceComment serviceComment = serviceCommentRepository.findById(serviceCommentId).orElse(null);
            if (serviceComment == null) {
                return "评论不存在！";
            }

            if (serviceComment.getCommenterId() != userId) {
                return "该评论不是由此用户发布！";
            }

            serviceCommentRepository.deleteById(serviceCommentId);

            return "删除评论成功！";
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public boolean isLiked(long serviceCommentId, long likerId) {
        ServiceComment serviceComment = serviceCommentRepository.findById(serviceCommentId).orElse(null);
        if (serviceComment == null) {
            return false;
        }

        return serviceCommentLikeRepository.existsByLikerIdAndServiceComment(likerId, serviceComment);
    }

}