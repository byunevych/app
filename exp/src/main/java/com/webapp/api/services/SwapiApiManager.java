package com.webapp.api.services;

import com.webapp.app.enums.StatusCode;
import com.webapp.core.UrlController;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.Getter;

public class SwapiApiManager {
    @Getter private RequestSpecification spec;

    public SwapiApiManager() {
        spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(UrlController.getWsHttpAppHost())
                .addFilter(new ResponseLoggingFilter()) // Log responses to console
                .addFilter(new RequestLoggingFilter()) // Log requests to console
                .build();
    }

    public static ResponseSpecification withResponseStatus(StatusCode statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode.getCode())
                .build();
    }
}