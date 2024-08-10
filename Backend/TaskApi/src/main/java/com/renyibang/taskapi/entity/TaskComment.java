package com.renyibang.taskapi.entity;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.taskapi.util.DateTimeUtil;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "task_comment")
@Getter
@Setter
@NoArgsConstructor
public class TaskComment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long taskCommentId; // 评论id

  @ManyToOne
  @JoinColumn(name = "task_id")
  private Task task; // 任务

  @Column(name = "commenter_id")
  private long commenterId; // 评论者

  private String content; // 评论内容
  private byte rating = 50; // 评论评分(存储10倍评分,范围0~100)

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt; // 评论时间

  //评论点赞数
  @Column(name = "liked_number")
  private long likedNumber = 0;

  public JSONObject toJSON()
  {
    JSONObject result = new JSONObject();
    result.put("taskCommentId", taskCommentId);
    result.put("taskId", task.getTaskId());
    result.put("content", content);
    result.put("rating", rating);
    result.put("createdAt", DateTimeUtil.formatDateTime(createdAt));
    result.put("likedNumber", likedNumber);

    return result;
  }
}
