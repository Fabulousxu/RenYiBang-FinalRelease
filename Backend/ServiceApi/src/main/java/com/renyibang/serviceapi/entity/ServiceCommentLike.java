package com.renyibang.serviceapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "service_comment_like")
@Getter
@Setter
@NoArgsConstructor
public class ServiceCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long serviceCommentLikeId; // 评论点赞id

    @ManyToOne
    @JoinColumn(name = "service_comment_id")
    private ServiceComment serviceComment; // 评论

    @Column(name = "liker_id")
    private long likerId; // 点赞者id
}