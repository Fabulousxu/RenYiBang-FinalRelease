package com.renyibang.taskapi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "task_collect")
@Getter
@Setter
@NoArgsConstructor
public class TaskCollect {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long taskCollectId; // 任务收藏id


  @Column(name = "collector_id")
  private long collectorId; // 收藏者

  @ManyToOne
  @JoinColumn(name = "task_id")
  private Task task; // 收藏任务

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt; // 收藏时间
}
