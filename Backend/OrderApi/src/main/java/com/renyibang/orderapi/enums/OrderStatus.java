package com.renyibang.orderapi.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
  UNPAID(0),
  IN_PROGRESS(1),
  COMPLETED(2),
  CONFIRMED(3),
  CANCELLED(4);

  private final int code;

  OrderStatus(int code) {
    this.code = code;
  }

  public static OrderStatus fromCode(int code) {
    for (OrderStatus status : OrderStatus.values()) {
      if (status.getCode() == code) {
        return status;
      }
    }
    throw new IllegalArgumentException("Unknown code: " + code);
  }
}
