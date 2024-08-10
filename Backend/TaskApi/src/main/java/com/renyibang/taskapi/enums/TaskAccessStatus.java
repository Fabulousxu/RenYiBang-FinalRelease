package com.renyibang.taskapi.enums;

import lombok.Getter;

@Getter
public enum TaskAccessStatus {
    ACCESSING(0),
    ACCESS_SUCCESS(1),
    ACCESS_FAIL(2);

    private final int code;

    TaskAccessStatus(int code) {
        this.code = code;
    }


    public static TaskAccessStatus fromCode(int code) {
        for (TaskAccessStatus status : TaskAccessStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
