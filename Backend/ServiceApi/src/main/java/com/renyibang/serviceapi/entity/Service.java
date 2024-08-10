package com.renyibang.serviceapi.entity;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.serviceapi.enums.ServiceStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.renyibang.serviceapi.util.DateTimeUtil;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "service")
@Getter
@Setter
@NoArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private long serviceId; // 服务id

    @Column(name = "owner_id")
    private long ownerId; // 服务发布者
    @Column(name = "title")
    private String title; // 服务标题
    @Column(name = "images")
    private String images; // 服务图片
    @Column(name = "description")
    private String description; // 服务描述
    @Column(name = "price")
    private long price = 0; // 服务价格(存储100倍价格)
    @Column(name = "max_access")
    private int maxAccess = 0; // 服务最大接取数
    @Column(name = "rating")
    private byte rating = 50; // 服务评分(存储10倍评分,范围0~100)

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt; // 服务创建时间

    @OneToMany(mappedBy = "service")
    @OrderBy("createdAt DESC")
    @JsonIgnore
    private List<ServiceComment> comments; // 服务评论列表

    @OneToMany(mappedBy = "service")
    @OrderBy("createdAt DESC")
    @JsonIgnore
    private List<ServiceMessage> messages; // 服务留言列表

    @OneToMany(mappedBy = "service")
    @OrderBy("createdAt DESC")
    @JsonIgnore
    private List<ServiceAccess> accesses; // 服务接取列表

    @Column(name = "collected")
    private long collectedNumber = 0;

    @Column(name = "status")
    private ServiceStatus status = ServiceStatus.NORMAL; // 服务状态

    public static List<String> splitImages(String images) {
        return Arrays.asList(images.split(","));
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("serviceId", serviceId);
        jsonObject.put("title", title);
        List<String> imageList = splitImages(images);
        jsonObject.put("images", imageList);
        jsonObject.put("cover", imageList.getFirst());
        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("maxAccess", maxAccess);
        jsonObject.put("rating", rating);
        jsonObject.put("createdAt", DateTimeUtil.formatDateTime(createdAt));
        jsonObject.put("collectedNumber", collectedNumber);
        jsonObject.put("status", status);
        return jsonObject;
    }

    public JSONObject toSelfJson()
    {
        JSONObject result = new JSONObject();
        result.put("serviceId", serviceId);
        result.put("title", title);
        result.put("price", price);
        result.put("maxAccess", maxAccess);
        result.put("rating", rating);
        result.put("createdAt", DateTimeUtil.formatDateTime(createdAt));
        result.put("collectedNumber", collectedNumber);
        result.put("status", status.toString());

        return result;
    }

    public boolean accessNotFull() {
        return accesses.size() < maxAccess;
    }
}