package com.webapp.tests.ui.common.max_size_window;

import com.webapp.pages.economiccalendar.EconomicCalendarPage;
import com.webapp.pages.home.HomePage;
import com.webapp.pages.riskwarning.RiskWarningPage;
import com.webapp.tests.common.UITest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("UI")
@Feature("Disclaimer block")
@Link(name = "Disclaimer block navigation", url = "jira ticket-CS-*****")
public class DisclaimerNavigationTest extends UITest {

    private HomePage homePage;
    private EconomicCalendarPage economicCalendarPage;

    @BeforeAll
    void beforeAll() {
        homePage = HomePage.open();
        homePage.getCookiesPopup().clickAcceptButton();
        economicCalendarPage = homePage.getMainNavigationMenuBlock()
                .clickResearchEducationButton()
                .clickEconomicCalendarLink();
    }

    @Test
    void riskWarningNavigationTest() {
        RiskWarningPage riskWarningPage = economicCalendarPage.getDisclaimerBlock().clickOpenRiskWarningPageLink();

        assertThat(riskWarningPage.isOpened())
                .as("Risk Warning page is not opened").isTrue();
    }
}
