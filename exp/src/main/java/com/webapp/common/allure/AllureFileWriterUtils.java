package com.webapp.common.allure;

import com.codeborne.selenide.WebDriverRunner;
import com.webapp.common.utils.FileUtil;
import com.webapp.common.utils.StrUtil;
import com.webapp.core.properties.PropertiesController;
import io.qameta.allure.model.StatusDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Properties;


@Slf4j
public class AllureFileWriterUtils {
    private static final String HTML_STEP_EXCEPTION_TEMPLATE = "htmltemplate/StepExceptionAllureTemplate.html";
    private static final String ENVIRONMENT_FILE_NAME = "environment.properties";
    private static final String CATEGORIES_FILE_NAME = "categories.json";
    private static File allureResultsFolder;
    private static File allureAttachmentsFolder;
    private static File environmentFile;
    private static File categoriesFile;
    private static Properties properties = new Properties();

    static {
        allureResultsFolder = Paths.get(FilenameUtils.separatorsToSystem(
                PropertiesController.allureConfig().resultsDirectory())).toAbsolutePath().toFile();
        environmentFile = Paths.get(allureResultsFolder.getPath() + File.separator + ENVIRONMENT_FILE_NAME)
                .toFile();
        categoriesFile = Paths.get(allureResultsFolder + File.separator + CATEGORIES_FILE_NAME).toFile();
        allureAttachmentsFolder = new File(allureResultsFolder.getAbsolutePath() + File.separator + "testAttachments");
        if (!allureAttachmentsFolder.mkdirs()) {
            log.warn("Allure attachment folder wasn't created");
        }
    }

    private AllureFileWriterUtils() {
    }

    public static String writeExceptionLog(String stepUuid, StatusDetails statusDetails) {
        File exceptionLog = new File(allureAttachmentsFolder + File.separator + stepUuid + ".html");
        FileUtil.createFile(exceptionLog);
        String exceptionContent = FileUtil.readFileFormatted(HTML_STEP_EXCEPTION_TEMPLATE)
                .replace("message_placeholder", makeHtmlFriendly(statusDetails.getMessage()))
                .replace("stacktrace_placeholder", makeHtmlFriendly(statusDetails.getTrace()));
        FileUtil.writeToFile(exceptionLog, exceptionContent);
        return exceptionLog.getAbsolutePath();
    }

    public static String writeBrowserLog(String stepUuid, List<LogEntry> log) {
        File browserLog = new File(allureAttachmentsFolder + File.separator + stepUuid + "_browserLog.html");
        FileUtil.createFile(browserLog);
        FileUtil.writeToFile(browserLog, log.toString());
        return browserLog.getAbsolutePath();
    }

    private static String makeHtmlFriendly(String text) {
        return text.replaceAll("[<,>]", StrUtil.EMPTY)
                .replaceAll("\r\n", "<br>")
                .replaceAll("\n", "<br>");
    }

    public static void createAllureCategories() {
        FileUtil.createFile(categoriesFile);
        FileUtil.writeToFile(categoriesFile, AllureCategories.getAllureCategoriesJson());
    }

    public static void createEnvironmentProperties(Map<String, String> props) {
        if (environmentFile.exists()) {
            loadProperties();
        } else {
            FileUtil.createFile(environmentFile);
        }
        addProperties(props);
        saveProperties();
    }

    public static void createAllureResultFile(AllureResultTemplateProcessor.AllureResultTemplate template) {
        File resultFile = Paths.get(allureResultsFolder
                + File.separator
                + template.getUuid().concat("-result.json")).toFile();

        FileUtil.createFile(resultFile);
        FileUtil.writeToFile(resultFile, template.toString());
    }

    private static void addProperties(Map<String, String> props) {
        saveParamToProperties(props);

        if (WebDriverRunner.hasWebDriverStarted()) {
            Capabilities caps = ((RemoteWebDriver) WebDriverRunner.getWebDriver()).getCapabilities();
            Map<String, String> chromeCaps = (Map<String, String>) caps.getCapability("chrome");

            saveParamToProperties("Browser", caps.getBrowserName() + StrUtil.SPACE + caps.getVersion());
            saveParamToProperties("Chromedriver", chromeCaps.get("chromedriverVersion").split(StrUtil.SPACE)[0]);
        }
    }

    private static void saveProperties() {
        try (OutputStream out = Files.newOutputStream(environmentFile.toPath())) {
            properties.store(out, "Properties");
        } catch (IOException e) {
            log.error("Error occurred while adding new props to environment.properties file", e);
        }
        log.info("New properties were added to environment.properties file: {}", properties);
    }

    private static void loadProperties() {
        try (InputStream in = Files.newInputStream(environmentFile.toPath())) {
            properties.load(in);
        } catch (IOException e) {
            log.error("Error occurred while loading properties from environment.properties file", e);
        }
        log.info("Properties were read from environment.properties file: {}", properties);
    }

    private static void saveParamToProperties(String param, String value) {
        if (!properties.stringPropertyNames().contains(param)) {
            properties.setProperty(param, value);
        }
    }

    private static void saveParamToProperties(Map<String, String> projectProperties) {
        projectProperties.forEach((key, value) -> properties.setProperty(key, value));
    }
}
