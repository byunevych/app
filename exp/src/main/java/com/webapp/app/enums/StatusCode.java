package com.webapp.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusCode {
    _200(200, "OK"),
    _201(201, "Created"),
    _204(204, "No Content"),
    _400(400, "Bad Request"),
    _404(404, "Not Found"),
    _500(500, "Internal Server Error");

    private int code;
    private String description;
}