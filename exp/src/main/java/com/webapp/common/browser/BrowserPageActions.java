package com.webapp.common.browser;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.webapp.common.annotations.Url;
import com.webapp.common.utils.BeanTools;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.core.ConditionTimeoutException;
import org.openqa.selenium.Cookie;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.webapp.common.utils.StrUtil.EMPTY;
import static com.webapp.common.utils.WaitUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class BrowserPageActions {
    private static final String WAIT_PAGE_LOADED_MESSAGE = "Check URL of the current page:%s";

    public void reloadCurrentPage() {
        getWebDriver().navigate().refresh();
        waitAssertCondition(() ->
                assertThat("complete").isEqualTo(executeJavaScript("return document.readyState")));
    }

    public void switchToIframe(int index) {
        getWebDriver().switchTo().frame(index);
        waitAssertCondition(() ->
                assertThat("complete").isEqualTo(executeJavaScript("return document.readyState")));
    }

    public void switchToIframeByWebElement(SelenideElement el) {
        getWebDriver().switchTo().frame(el);
        waitAssertCondition(() ->
                assertThat("complete").isEqualTo(executeJavaScript("return document.readyState")));
    }

    public void switchToNewBrowserTab() {
        doWait().until(this::getBrowserTabNames, tabs -> tabs.size() > 1)
                .stream().reduce((a, b) -> b).ifPresent(h -> switchTo().window(h));
    }

    public void switchToDefaultContent() {
        getWebDriver().switchTo().defaultContent();
        waitAssertCondition(() ->
                assertThat("complete").isEqualTo(executeJavaScript("return document.readyState")));
    }

    public Set<String> getBrowserTabNames() {
        return getWebDriver().getWindowHandles();
    }

    public int getBrowserTabsCount() {
        return getWebDriver().getWindowHandles().size();
    }

    public void clickBrowserBackButton() {
        getWebDriver().navigate().back();
    }

    public void switchToNewBrowserTab(Runnable action) {
        int tabsCnt = getBrowserTabNames().size();
        action.run();
        doWait().until(this::getBrowserTabNames, tabs -> tabs.size() > tabsCnt)
                .stream().reduce((a, b) -> b).ifPresent(h -> switchTo().window(h));
    }

    public void closeCurrentTab() {
        if (getBrowserTabNames().size() > 1) {
            getWebDriver().close();
            List<String> tabsList = new ArrayList<>(getBrowserTabNames());
            getWebDriver().switchTo().window(tabsList.get(tabsList.size() - 1));
        }
    }

    public void navigateToUrl(String url) {
        open(url);
    }

    public void navigateToUrlNewTab(String url) {
        switchToNewBrowserTab(() -> executeJavaScript("window.open()"));
        repeatAction(() -> {
            Selenide.open(url);
            return url;
        });
    }

    public String getCurrentPageUrl() {
        return getWebDriver().getCurrentUrl();
    }

    public <T> T openPageByUrl(Class<T> clazz, String baseUrl) {
        return openPageByUrl(clazz, baseUrl, List.of(EMPTY));
    }

    public <T> T openPageByUrl(Class<T> clazz, String baseUrl, List<String> urlParams) {
        if (clazz.isAnnotationPresent(Url.class)) {
            String replaceToken = "\\.\\*";
            String url = clazz.getAnnotation(Url.class).pattern()
                    .replaceFirst(replaceToken, EMPTY)
                    .replaceAll("(\\\\)|(\\.\\*$)", EMPTY);

            for (String urlParam : urlParams) {
                url = url.replaceFirst(replaceToken, urlParam);
            }
            return Selenide.open(baseUrl + url, clazz);
        } else {
            throw new RuntimeException(String.format("No @Url annotation for class %s", clazz.getCanonicalName()));
        }
    }

    public void addCookie(Cookie cookie) {
        getWebDriver().manage().addCookie(cookie);
    }

    public void deleteCookie(String cookieName) {
        getWebDriver().manage().deleteCookieNamed(cookieName);
    }

    public Cookie getCookie(String cookieName) {
        return getWebDriver().manage().getCookieNamed(cookieName);
    }

    public long getBrowserTimeZoneOffset() {
        return executeJavaScript("var date = new Date(); return date.getTimezoneOffset();");
    }

    public boolean checkCurrentPageAt(Class<?> pageClass) {
        if (pageClass.isAnnotationPresent(Url.class)) {
            doWait().untilAsserted(() -> assertThat(getCurrentPageUrl())
                    .as(WAIT_PAGE_LOADED_MESSAGE, pageClass.getSimpleName())
                    .matches(pageClass.getAnnotation(Url.class).pattern()));
            return true;
        }
        throw new IllegalArgumentException(String.format("'%s' class has no URL annotation", pageClass.getName()));
    }

    public static boolean isPageLoaded(Class<?> pageClass) {
        if (pageClass.isAnnotationPresent(Url.class)) {
            return getWebDriver().getCurrentUrl().matches(pageClass.getAnnotation(Url.class).pattern());
        }
        throw new IllegalArgumentException(String.format("'%s' class has no URL annotation", pageClass.getName()));
    }

    public <T> Optional<T> waitForPageLoaded(Class<T> pageClass) {
        try {
            checkCurrentPageAt(pageClass);
            return Optional.of(BeanTools.createObject(pageClass));
        } catch (ConditionTimeoutException e) {
            log.info(String.format(WAIT_PAGE_LOADED_MESSAGE, pageClass.getSimpleName()), e);
        }
        return Optional.empty();
    }
}