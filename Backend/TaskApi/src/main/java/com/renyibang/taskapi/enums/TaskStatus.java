package com.renyibang.taskapi.enums;

import lombok.Getter;

@Getter
public enum TaskStatus {
    NORMAL(0),
    REMOVE(1),
    DELETE(2);

    private final int code;

    TaskStatus(int code) {
        this.code = code;
    }


    public static TaskStatus fromCode(int code) {
        for (TaskStatus status : TaskStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
