package com.renyibang.serviceapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "service_message_like")
@Getter
@Setter
@NoArgsConstructor
public class ServiceMessageLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long serviceMessageLikeId; // 消息点赞id

    @ManyToOne
    @JoinColumn(name = "service_message_id")
    private ServiceMessage serviceMessage; // 消息

    @Column(name = "liker_id")
    private long likerId; // 点赞者id
}