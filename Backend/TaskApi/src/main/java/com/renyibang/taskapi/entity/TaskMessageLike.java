package com.renyibang.taskapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "task_message_like")
@Getter
@Setter
@NoArgsConstructor
public class TaskMessageLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long taskMessageLikeId; // 消息点赞id

    @ManyToOne
    @JoinColumn(name = "task_message_id")
    private TaskMessage taskMessage; // 消息

    @Column(name = "liker_id")
    private long likerId; // 点赞者id
}
