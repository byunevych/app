package com.webapp.widgets;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.webapp.common.widgets.Widget;

public class CookiesPopup extends Widget<CookiesPopup> {

    private SelenideElement modifyButton = getSelf().$x(".//button[contains(.,'MODIFY PREFERENCES')]");
    private SelenideElement acceptButton = getSelf().$x(".//button[contains(.,'ACCEPT ALL')]");

    public CookiesPopup(SelenideElement self) {
        super(self);
    }

    public void clickAcceptButton() {
        acceptButton.shouldBe(Condition.visible).click();
        this.shouldBe(Condition.disappear);
    }

}