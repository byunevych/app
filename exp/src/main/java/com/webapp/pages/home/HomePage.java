package com.webapp.pages.home;

import com.codeborne.selenide.SelenideElement;
import com.webapp.common.annotations.Url;
import com.webapp.pages.AbstractPage;
import com.webapp.widgets.mainmenu.MainNavigationMenuBlock;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.webapp.common.utils.WaitUtils.doWaitMedium;
import static org.assertj.core.api.Assertions.assertThat;

@Url(pattern = ".*/#")
public class HomePage extends AbstractPage {

    private SelenideElement openAccountButton = $x(".//a[contains(text(),'Open an Account')]//parent::li");
    @Getter
    private MainNavigationMenuBlock mainNavigationMenuBlock = new MainNavigationMenuBlock($x(".//div[contains(@class, 'main-nav')]"));

    @Override
    protected HomePage waitPage() {
        doWaitMedium().untilAsserted(() -> assertThat(openAccountButton.isDisplayed())
                .as("Home page was NOT loaded")
                .isTrue()
        );
        return this;
    }

    public static HomePage open() {
        return open(HomePage.class).waitPage();
    }
}