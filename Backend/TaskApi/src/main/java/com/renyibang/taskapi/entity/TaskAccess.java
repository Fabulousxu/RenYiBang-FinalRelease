package com.renyibang.taskapi.entity;

import com.renyibang.taskapi.enums.TaskAccessStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_access")
@Getter
@Setter
@NoArgsConstructor
public class TaskAccess {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long taskAccessId; // 任务接取候选id

  @ManyToOne
  @JoinColumn(name = "task_id")
  private Task task; // 任务

  @Column(name = "accessor_id")
  private long accessorId; // 任务接取者

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt; // 任务接取时间

  @Column(name = "status")
  private TaskAccessStatus taskAccessStatus; // 任务接取状态
}
