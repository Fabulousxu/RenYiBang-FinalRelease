package com.renyibang.serviceapi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "service_collect")
@Getter
@Setter
@NoArgsConstructor
public class ServiceCollect {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long serviceCollectId; // 服务收藏id

  @Column(name = "collector_id")
  private long collectorId; // 收藏者

  @ManyToOne
  @JoinColumn(name = "service_id")
  private Service service; // 收藏服务

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt; // 收藏时间
}