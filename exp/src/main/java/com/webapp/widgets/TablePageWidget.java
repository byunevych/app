package com.webapp.widgets;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.webapp.common.widgets.Widget;

import java.util.List;

import static com.webapp.common.utils.WaitUtils.waitAssertCondition;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public abstract class TablePageWidget extends Widget<TablePageWidget> {
    private static final String COLUMN_HEADER = ".//th[contains(.,'%s')]";
    private SelenideElement loadingRow = getSelf().$x(".//span[contains(., 'Loading data')]");
    private SelenideElement noDataMessage = getSelf().$x(".//span[contains(., 'There is no data yet')]");
    private SelenideElement noMatchesWereFoundMessage = getSelf().$x(".//span[contains(., 'No matches were found')]");
    private ElementsCollection tableHeaders = getSelf().$$("th");

    public TablePageWidget(SelenideElement self) {
        super(self);
    }

    public boolean isNoDataMessageDisplayed() {
        waitForLoaderToDisappear();
        return noDataMessage.isDisplayed();
    }

    public boolean isNoMatchesWereFoundMessageDisplayed() {
        waitForLoaderToDisappear();
        return noMatchesWereFoundMessage.shouldBe(Condition.visible).isDisplayed();
    }

    public String getNoDataMessage() {
        waitForLoaderToDisappear();
        return noDataMessage.shouldBe(Condition.visible).getText();
    }

    public List<String> getHeaders() {
        return tableHeaders.texts();
    }

    public TablePageWidget clickColumnHeader(String name) {
        getSelf().parent().$x(String.format(COLUMN_HEADER, name)).click();
        return this;
    }

    public TablePageWidget waitForLoaderToDisappear() {
        waitAssertCondition(() -> assertThat(loadingRow.isDisplayed()).as("loader didn't disappear").isFalse());
        return this;
    }
}