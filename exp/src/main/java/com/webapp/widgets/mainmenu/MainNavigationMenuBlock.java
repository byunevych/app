package com.webapp.widgets.mainmenu;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.webapp.common.widgets.Widget;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$x;

public class MainNavigationMenuBlock extends Widget<MainNavigationMenuBlock> {

    private SelenideElement homeButton = getSelf().$x(".//a[contains(.,'Home')]");
    private SelenideElement tradingButton = getSelf().$x(".//a[contains(.,'Trading')]");
    private SelenideElement platformsButton = getSelf().$x(".//a[contains(.,'Platforms')]");
    private SelenideElement researchEducationButton = getSelf().$x(".//a[contains(.,'Research & Education')]");
    private SelenideElement promotionsButton = getSelf().$x(".//a[contains(.,'Promotions')]");
    private SelenideElement aboutUsButton = getSelf().$x(".//a[contains(.,'About Us')]");
    private SelenideElement partnershipsButton = getSelf().$x(".//a[contains(.,'Partnerships')]");

    @Getter
    private SubResearchAndEducationMenuBlock subResearchAndEducationMenuBlock = new SubResearchAndEducationMenuBlock($x(".//li[contains(@class,'research selected')]//div[contains(@class,'dropdown')]"));


    public MainNavigationMenuBlock(SelenideElement self) {
        super(self);
    }

    public SubResearchAndEducationMenuBlock clickResearchEducationButton() {
        researchEducationButton.click();
        subResearchAndEducationMenuBlock.getSelf().shouldBe(Condition.visible);
        return subResearchAndEducationMenuBlock;
    }

}