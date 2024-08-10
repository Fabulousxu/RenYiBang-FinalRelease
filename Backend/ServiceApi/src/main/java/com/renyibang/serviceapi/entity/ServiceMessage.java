package com.renyibang.serviceapi.entity;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.serviceapi.util.DateTimeUtil;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "service_message")
@Getter
@Setter
@NoArgsConstructor
public class ServiceMessage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long serviceMessageId; // 服务留言id

  @ManyToOne
  @JoinColumn(name = "service_id")
  private Service service; // 服务

  @Column(name = "messager_id")
  private long messagerId; // 留言者

  private String content; // 留言内容

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt; // 留言时间

  @Column(name = "liked_number")
  private long likedNumber = 0;

  public JSONObject toJSON()
  {
    JSONObject result = new JSONObject();
    result.put("serviceMessageId", serviceMessageId);
    result.put("serviceId", service.getServiceId());
    result.put("content", content);
    result.put("createdAt", DateTimeUtil.formatDateTime(createdAt));
    result.put("likedNumber", likedNumber);

    return result;
  }
}