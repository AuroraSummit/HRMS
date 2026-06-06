package com.hrms.common.constant;

public enum CommonStatus {

    ENABLE(0),
    DISABLE(1),
    DELETED(2);

    private final int value;

    CommonStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
