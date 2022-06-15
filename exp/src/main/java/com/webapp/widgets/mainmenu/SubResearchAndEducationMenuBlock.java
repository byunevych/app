package com.webapp.widgets.mainmenu;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.webapp.common.widgets.Widget;
import com.webapp.pages.economiccalendar.EconomicCalendarPage;

public class SubResearchAndEducationMenuBlock extends Widget<SubResearchAndEducationMenuBlock> {

    private SelenideElement economicCalendarLink = getSelf().$x(".//a[contains(.,'Economic Calendar')]");

    public SubResearchAndEducationMenuBlock(SelenideElement self) {
        super(self);
    }

    public EconomicCalendarPage clickEconomicCalendarLink() {
        economicCalendarLink.shouldBe(Condition.visible).click();
        return pages.getEconomicCalendarPage();
    }

}