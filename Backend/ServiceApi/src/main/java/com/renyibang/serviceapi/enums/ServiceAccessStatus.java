package com.renyibang.serviceapi.enums;

import lombok.Getter;

@Getter
public enum ServiceAccessStatus {
    ACCESSING(0),
    ACCESS_SUCCESS(1),
    ACCESS_FAIL(2);

    private final int code;

    ServiceAccessStatus(int code) {
        this.code = code;
    }

    public static ServiceAccessStatus fromCode(int code) {
        for (ServiceAccessStatus status : ServiceAccessStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
