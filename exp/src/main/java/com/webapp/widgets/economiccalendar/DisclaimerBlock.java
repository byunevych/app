package com.webapp.widgets.economiccalendar;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.webapp.common.widgets.Widget;
import com.webapp.pages.riskwarning.RiskWarningPage;

public class DisclaimerBlock extends Widget<DisclaimerBlock> {

    private SelenideElement openRiskWarningPageLink = getSelf().$x(".//a[contains(@href,'risk_warning')]");

    public DisclaimerBlock(SelenideElement self) {
        super(self);
    }

    public RiskWarningPage clickOpenRiskWarningPageLink(){
        this.scrollTo();
        openRiskWarningPageLink.shouldBe(Condition.visible).click();
        return pages.getRiskWarningPage();
    }
}