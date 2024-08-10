package com.renyibang.serviceapi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import com.renyibang.serviceapi.enums.ServiceAccessStatus;

@Entity
@Table(name = "service_access")
@Getter
@Setter
@NoArgsConstructor
public class ServiceAccess {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long serviceAccessId; // 服务接取候选id

  @ManyToOne
  @JoinColumn(name = "service_id")
  private Service service; // 服务

  @Column(name = "accessor_id")
  private long accessorId; // 服务接取者

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt; // 服务接取时间

  @Column(name = "status")
  private ServiceAccessStatus serviceAccessStatus; // 服务接取状态
}