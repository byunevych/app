package com.webapp.widgets;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.webapp.common.widgets.Widget;
import com.webapp.pages.RiskWarningPdfPage;

public class RiskWarningPdfBlock extends Widget<RiskWarningPdfBlock> {

    private SelenideElement openRiskWarningPdfPageLink = getSelf().$x(".//a[contains(text(),'Risk Disclosure')]");

    public RiskWarningPdfBlock(SelenideElement self) {
        super(self);
    }

    public RiskWarningPdfPage clickOpenRiskWarningPdfPageLink() {
        openRiskWarningPdfPageLink.shouldBe(Condition.visible).click();
        browserPageActions.switchToNewBrowserTab();
        return pages.getRiskWarningPdfPage();
    }
}