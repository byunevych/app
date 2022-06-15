package com.webapp.pages;

import com.codeborne.selenide.Selenide;
import com.webapp.common.annotations.Url;
import com.webapp.common.browser.BrowserPageActions;
import com.webapp.core.UrlController;
import com.webapp.widgets.CookiesPopup;
import com.webapp.widgets.RiskWarningPdfBlock;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.webapp.common.utils.StrUtil.EMPTY;
import static com.webapp.common.utils.WaitUtils.doWaitMedium;
import static com.webapp.common.utils.WaitUtils.repeatAction;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractPage {
    @Getter
    private BrowserPageActions browserPageActions = new BrowserPageActions();
    protected PageManager pages = PageManager.getPages();

    @Getter
    private CookiesPopup cookiesPopup = new CookiesPopup($(".modal-content"));
    @Getter
    private RiskWarningPdfBlock riskWarningPdfBlock = new RiskWarningPdfBlock($x(".//a[contains(text(),'Risk Disclosure')]//ancestor::div[contains(@id,'risk-block')]"));

    protected AbstractPage waitPage() {
        if (getClass().isAnnotationPresent(Url.class)) {
            doWaitMedium().untilAsserted(() -> assertThat(browserPageActions.getCurrentPageUrl())
                    .as("Page in not loaded yet")
                    .matches(getClass().getAnnotation(Url.class).pattern())
            );
        }
        return this;
    }

    public boolean isOpened() {
        return browserPageActions.checkCurrentPageAt(getClass());
    }

    public AbstractPage refreshPage() {
        browserPageActions.reloadCurrentPage();
        return this;
    }

    protected static <T extends AbstractPage> T open(Class<T> pageClass) {
        String url = pageClass.isAnnotationPresent(Url.class) ?
                pageClass.getAnnotation(Url.class).pattern().replaceFirst("\\.\\*", EMPTY) :
                EMPTY;
        return repeatAction(() -> Selenide.open(UrlController.getUiHttpAppHost() + url, pageClass));
    }

}
