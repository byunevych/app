package com.webapp.common.allure;

import lombok.Getter;

@Getter
enum AllureCategoryStatus {

    FAILED("failed"),
    SKIPPED("skipped"),
    BROKEN("broken"),
    PASSED("passed"),
    UNKNOWN("unknown");

    private String value;

    AllureCategoryStatus(String value) {
        this.value = value;
    }
}
