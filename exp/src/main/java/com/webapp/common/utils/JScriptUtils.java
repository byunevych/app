package com.webapp.common.utils;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.webapp.core.properties.PropertiesController;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.webapp.common.utils.WaitUtils.doWait;
import static org.awaitility.Durations.ONE_SECOND;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JScriptUtils {
    private static final Duration WAIT_PAGE_TIMEOUT = PropertiesController.appTimeoutConfig().utilWaitTimeout();

    public static void loadJQuery(String fileWithPath) {
        WebDriverRunner.getWebDriver().manage().timeouts().scriptTimeout(WAIT_PAGE_TIMEOUT);
        executeJavaScript(getScript(fileWithPath));
        waitForJQueryLoaded();
    }

    public static String getScript(String fileWithPath) {
        try {
            return FileUtil.readFile(fileWithPath);
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException("Script file was not found", e);
        }
    }

    private static void waitForJQueryLoaded() {
        doWait().with().pollInterval(ONE_SECOND)
                .until(() -> executeJavaScript("return !!window.jQuery && window.jQuery.active == 0"));
    }

    public static SelenideElement scrollTo(SelenideElement element) {
        executeJavaScript("arguments[0].scrollIntoView();", element);
        return element;
    }
}