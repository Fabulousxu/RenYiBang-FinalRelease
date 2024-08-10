package com.renyibang.userapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "auth")
@Data
public class Auth {
  @Id private long userId;
  private String password;
}
