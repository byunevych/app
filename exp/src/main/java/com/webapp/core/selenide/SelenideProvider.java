package com.webapp.core.selenide;

import com.codeborne.selenide.Browsers;
import com.codeborne.selenide.Configuration;
import com.webapp.common.utils.StrUtil;
import com.webapp.core.UrlController;
import com.webapp.core.properties.PropertiesController;

import static com.webapp.core.properties.PropertiesController.systemProperties;

public final class SelenideProvider {

    public static void init() {
        Configuration.browser = Browsers.CHROME;
        Configuration.baseUrl = UrlController.getUiHttpAppHost();
        Configuration.browserSize = getSize();
        Configuration.timeout = PropertiesController.appTimeoutConfig().webdriverWaitTimeout().toMillis();
        Configuration.reportsFolder = "target/selenide-screenshots";
        if (systemProperties().runSize().isEmpty()) {
            Configuration.startMaximized = true;
        }
    }

    private static String getSize() {
        var size = systemProperties().runSize();
        if (size.equalsIgnoreCase("med")) {
            return "1024x768";
        } else if (size.equalsIgnoreCase("min")) {
            return "800x600";
        }
        return StrUtil.EMPTY;
    }
}
