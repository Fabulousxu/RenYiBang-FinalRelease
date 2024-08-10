package com.renyibang.userapi.dto;

import lombok.Data;

@Data
public class Register {
  private String password;
  private String nickname;
  private String phone;
  private String email;
  private String intro;
}
