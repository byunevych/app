package com.webapp.api.services;

import com.webapp.app.enums.StatusCode;
import io.restassured.response.ValidatableResponse;

import static com.webapp.api.services.SwapiApiManager.withResponseStatus;
import static com.webapp.common.utils.StrUtil.EMPTY;
import static io.restassured.RestAssured.given;
import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;

public abstract class AbstractService {
    protected SwapiApiManager apiManager;

    public AbstractService() {
        apiManager = new SwapiApiManager();
    }

    protected abstract String getEndpoint();

    public ValidatableResponse get(String value) {
        return given()
                .spec(apiManager.getSpec())
                .get(getEndpoint(value))
                .then()
                .spec(withResponseStatus(StatusCode._200));
    }

    public ValidatableResponse get() {
        return get(EMPTY);
    }

    private String getEndpoint(String value) {
        return ofNullable(value)
                .filter(not(String::isEmpty))
                .map(v -> getEndpoint() + "/" + v)
                .orElseGet(this::getEndpoint);
    }
}