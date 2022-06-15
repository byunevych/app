package com.webapp.common.allure;

import com.webapp.common.utils.StrUtil;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
class AllureCategory {

    private String name;
    private String[] matchedStatuses;
    private String traceRegex;

    AllureCategory(String name, Stream<AllureCategoryStatus> statusStream, String message) {
        this(name, statusStream);
        this.traceRegex = String.format(".*%s.*", message).replaceAll("%s", StrUtil.EMPTY);
    }

    AllureCategory(String name, Stream<AllureCategoryStatus> statusStream) {
        this.name = name;
        this.matchedStatuses = statusStream.map(AllureCategoryStatus::getValue).toArray(String[]::new);
    }
}