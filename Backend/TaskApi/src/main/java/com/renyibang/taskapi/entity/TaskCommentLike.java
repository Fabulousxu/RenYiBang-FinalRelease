package com.renyibang.taskapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "task_comment_like")
@Getter
@Setter
@NoArgsConstructor
public class TaskCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long taskCommentLikeId; // 评论点赞id

    @ManyToOne
    @JoinColumn(name = "task_comment_id")
    private TaskComment taskComment; // 评论

    @Column(name = "liker_id")
    private long likerId; // 点赞者id
}
