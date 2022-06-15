package com.webapp.common.allure;

import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.ex.ElementNotFound;
import com.webapp.common.utils.StrUtil;
import io.qameta.allure.model.Attachment;
import io.qameta.allure.model.StatusDetails;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.List;

import static com.codeborne.selenide.Selenide.screenshot;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AllureAttachmentHelper {

    public static Attachment getStacktraceAttachment(String stepUuid, StatusDetails statusDetails) {
        return new Attachment()
                .setName(statusDetails.getMessage())
                .setSource(AllureFileWriterUtils.writeExceptionLog(stepUuid, statusDetails))
                .setType("text/html");
    }

    public static Attachment getBrowserLogs(String stepUuid, WebDriver driver) {
        List<LogEntry> log = driver.manage().logs().get(LogType.BROWSER).getAll();
        return new Attachment()
                .setName("Browser log")
                .setSource(AllureFileWriterUtils.writeBrowserLog(stepUuid, log))
                .setType("text/plain");
    }

    public static Attachment getScreenshotAttachment(Throwable cause) {
        return new Attachment()
                .setName("Page screenshot")
                .setType("image/png")
                .setSource(extractOrTakeScreenshot(cause));
    }

    static String getStackTrace(Throwable throwable) {
        var sw = new StringWriter();
        var pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

    static Attachment getBrowserLogs() {
        List<LogEntry> log = WebDriverRunner.getWebDriver().manage().logs().get(LogType.BROWSER).getAll();
        return new Attachment()
                .setName("Browser log")
                .setSource(log.toString())
                .setType("text/plain");
    }

    private static String extractOrTakeScreenshot(Throwable cause) {
        String PREFIX_FILE = "file:";
        Throwable exception = cause.getCause();
        if (exception instanceof ElementNotFound) {
            try {
                String screenshotPath = ((ElementNotFound) exception).getScreenshot().replace(PREFIX_FILE, StrUtil.EMPTY);
                return new File(screenshotPath).getAbsolutePath();
            } catch (NullPointerException e) {
                return screenshot("screenshot" + Instant.now().toEpochMilli());
            }
        } else {
            return screenshot("screenshot" + Instant.now().toEpochMilli());
        }
    }
}
