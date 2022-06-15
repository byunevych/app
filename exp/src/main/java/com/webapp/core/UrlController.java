package com.webapp.core;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UrlController {
    private static final String MAIN_URL_UI = "www.xm.com";
    private static final String MAIN_URL_WS = "swapi.dev/api";

    public static String getUiHttpAppHost() {
        return String.format(UrlTemplate.SECURE.getTemplate(), MAIN_URL_UI);
    }

    public static String getWsHttpAppHost() {
        return String.format(UrlTemplate.SECURE.getTemplate(), MAIN_URL_WS);
    }

    @AllArgsConstructor
    private enum UrlTemplate {
        SECURE("https://%s");

        @Getter
        private String template;
    }
}