package com.webapp.common.allure;

import com.webapp.common.utils.JsonParser;
import lombok.Getter;

import java.util.EnumSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.webapp.common.allure.AllureCategoryStatus.*;

public enum AllureCategories {

    ELEMENT_NOT_FOUND("Element not found", Stream.of(FAILED, SKIPPED), "Element not found"),
    IGNORED_TESTS("Ignored tests", Stream.of(SKIPPED)),
    WAIT_PAGE_LOADED("Page load fails", Stream.of(FAILED, BROKEN), "Check URL of the current page:%s"),
    PROJECT_DELETION("Project deletion fails", Stream.of(FAILED, BROKEN), "Waiting for namespace deletion:%s"),
    WAIT_FOR_LOGIN("Login fails", Stream.of(FAILED, BROKEN), "Waiting for login"),
    WS_BAD_REQUEST("Service - 400 Bad Request", Stream.of(FAILED, BROKEN), "Service returns error with code 400"),
    WS_NOT_FOUND("Service - 404 Not Found", Stream.of(FAILED, BROKEN), "Service returns error with code 404"),
    WS_INTERNAL_SERVER_ERROR("Service - 500 Internal Server Error", Stream.of(FAILED, BROKEN), "Service returns error with code 500"),
    WS_CONFLICT("Service - 409 Conflict", Stream.of(FAILED, BROKEN), "Service returns error with code 409"),
    NO_ROUTE_TO_HOST("No route to host (Host unreachable)", Stream.of(FAILED, BROKEN), "NoRouteToHostException");

    private AllureCategory category;
    @Getter
    private String message;

    AllureCategories(String name, Stream<AllureCategoryStatus> statusStream, String message) {
        this.category = new AllureCategory(name, statusStream, message);
        this.message = message;
    }

    AllureCategories(String name, Stream<AllureCategoryStatus> statusStream) {
        this.category = new AllureCategory(name, statusStream);
    }

    public static String getAllureCategoriesJson() {
        return JsonParser.toPrettyPrintingJson(EnumSet.allOf(AllureCategories.class)
                .stream().map(cat -> cat.category).collect(Collectors.toList()));
    }
}