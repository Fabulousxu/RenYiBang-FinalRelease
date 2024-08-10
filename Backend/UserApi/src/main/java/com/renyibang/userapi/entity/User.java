package com.renyibang.userapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Set;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long userId; // 用户id

  private byte type = 0; // 用户类型(0:普通用户,1:客服,2:管理员)
  private String nickname = ""; // 用户昵称
  private String avatar = ""; // 用户头像
  private String intro = ""; // 用户介绍
  private byte rating = 50; // 用户评分(存储10倍评分,范围0~100)
  private long balance = 0; // 用户余额(存储100倍余额)
  private String phone = ""; // 用户手机号
  private String email = ""; // 用户邮箱
  private int following = 0; // 关注数
  private int follower = 0; // 粉丝数

  @ManyToMany
  @JoinTable(
      name = "follow",
      joinColumns = @JoinColumn(name = "follower_id"),
      inverseJoinColumns = @JoinColumn(name = "followee_id"))
  @JsonIgnore
  private Set<User> followings; // 关注列表

  @ManyToMany
  @JoinTable(
      name = "follow",
      joinColumns = @JoinColumn(name = "followee_id"),
      inverseJoinColumns = @JoinColumn(name = "follower_id"))
  @JsonIgnore
  private Set<User> followers; // 粉丝列表
}
