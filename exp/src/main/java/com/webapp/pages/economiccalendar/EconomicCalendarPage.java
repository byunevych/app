package com.webapp.pages.economiccalendar;

import com.webapp.common.annotations.Url;
import com.webapp.pages.AbstractPage;
import com.webapp.widgets.CookiesPopup;
import com.webapp.widgets.economiccalendar.DisclaimerBlock;
import com.webapp.widgets.economiccalendar.HorizontalMenuBlock;
import com.webapp.widgets.economiccalendar.TimeSelectorBlock;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.webapp.common.utils.WaitUtils.doWaitMedium;
import static org.assertj.core.api.Assertions.assertThat;

@Url(pattern = ".*/research/economicCalendar")
public class EconomicCalendarPage extends AbstractPage {

    @Getter
    private CookiesPopup cookiesPopup = new CookiesPopup($(".modal-content"));
    @Getter
    private HorizontalMenuBlock horizontalMenuBlock = new HorizontalMenuBlock($(".scrollable-menu__wrapper"));
    @Getter
    private TimeSelectorBlock timeSelectorBlock = new TimeSelectorBlock($("#timeselector"));
    @Getter
    private DisclaimerBlock disclaimerBlock = new DisclaimerBlock($x(".//a[contains(@href,'risk_warning')]//parent::p//parent::div"));


    @Override
    protected EconomicCalendarPage waitPage() {
        doWaitMedium().untilAsserted(() -> assertThat(horizontalMenuBlock.isDisplayed())
                .as("Economic Calendar Page page was NOT loaded")
                .isTrue()
        );
        return this;
    }

    public static EconomicCalendarPage open() {
        return open(EconomicCalendarPage.class).waitPage();
    }
}