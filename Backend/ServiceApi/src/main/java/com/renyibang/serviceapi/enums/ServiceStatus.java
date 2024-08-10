package com.renyibang.serviceapi.enums;

import lombok.Getter;

@Getter
public enum ServiceStatus {
    NORMAL(0),
    REMOVE(1),
    DELETE(2);

    private final int code;

    ServiceStatus(int code) {
        this.code = code;
    }

    public static ServiceStatus fromCode(int code) {
        for (ServiceStatus status : ServiceStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}