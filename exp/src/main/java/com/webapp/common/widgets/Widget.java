package com.webapp.common.widgets;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.webapp.common.browser.BrowserPageActions;
import com.webapp.common.utils.DateUtil;
import com.webapp.common.utils.WaitUtils;
import com.webapp.pages.PageManager;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;

import static com.webapp.common.utils.WaitUtils.doWait;
import static org.assertj.core.api.Assertions.assertThat;

public class Widget<T extends Widget> {
    protected BrowserPageActions browserPageActions = new BrowserPageActions();
    protected PageManager pages = PageManager.getPages();
    private SelenideElement self;

    public Widget(SelenideElement self) {
        this.self = self;
    }

    public SelenideElement getSelf() {
        return self;
    }

    public T shouldBe(Condition condition) {
        self.shouldBe(condition);
        return (T) this;
    }

    public T shouldNotBe(Condition condition) {
        self.shouldNotBe(condition);
        return (T) this;
    }

    public T scrollTo() {
        self.should(Condition.visible).scrollTo();
        return (T) this;
    }

    public boolean isDisplayed() {
        return self.isDisplayed();
    }

    public boolean isEnabled() {
        return self.isEnabled();
    }

    public void ctrlClick() {
        getActions().keyDown(Keys.LEFT_CONTROL).click(getSelf()).keyUp(Keys.LEFT_CONTROL).build().perform();
    }

    public Object click() {
        getSelf().shouldBe(Condition.visible, Condition.enabled).click();
        return this;
    }

    public Object doubleClick() {
        getSelf().shouldBe(Condition.visible, Condition.enabled).doubleClick();
        return this;
    }

    public void clickEsc() {
        getActions().sendKeys(Keys.ESCAPE).build().perform();
    }

    protected void clickEnter(SelenideElement element) {
        getActions().sendKeys(element, Keys.ENTER).build().perform();
    }

    public T hover() {
        getSelf().shouldBe(Condition.visible, Condition.enabled).hover();
        return (T) this;
    }

    private Actions getActions() {
        return new Actions(WebDriverRunner.getWebDriver());
    }

    public T waitToAppear() {
        doWait().untilAsserted(() -> assertThat(isDisplayed()).as("Widget is NOT visible").isTrue());
        return (T) this;
    }

    public void waitToDisappear() {
        waitToDisappear(WaitUtils.TIMEOUT);
    }

    protected void waitToDisappear(Duration timeout) {
        doWait().timeout(DateUtil.convert(timeout)).untilAsserted(() -> assertThat(isDisplayed()).as("Widget IS visible").isFalse());
    }
}
