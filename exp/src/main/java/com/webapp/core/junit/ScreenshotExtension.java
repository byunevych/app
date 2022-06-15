package com.webapp.core.junit;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.webapp.common.utils.FakeUtil;
import com.webapp.core.logger.LoggingUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.File;

public class ScreenshotExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        logScreenshot(context);
    }

    private void logScreenshot(ExtensionContext context) {
        boolean testFail = context.getExecutionException().isPresent();
        if (!testFail) return;
        if (WebDriverRunner.hasWebDriverStarted()) {
            LoggingUtils.log(new File(Selenide.screenshot("file-" + FakeUtil.generateHash())), "screenshot");
        }
    }
}
