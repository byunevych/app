package com.webapp.tests.ui.common.max_size_window;

import com.webapp.pages.economiccalendar.EconomicCalendarPage;
import com.webapp.pages.home.HomePage;
import com.webapp.tests.common.UITest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("UI")
@Feature("Navigation")
@Link(name = "Economic Calendar", url = "jira ticket-CS-*****")
public class EconomicCalendarPageNavigationTest extends UITest {

    private HomePage homePage;

    @BeforeAll
    void beforeAll() {
        homePage = HomePage.open();
        homePage.getCookiesPopup().clickAcceptButton();
    }

    @Test
    void economicCalendarPageNavTest() {
        EconomicCalendarPage economicCalendarPage = homePage.getMainNavigationMenuBlock()
                .clickResearchEducationButton()
                .clickEconomicCalendarLink();

        assertThat(economicCalendarPage.isOpened())
                .as("EconomicCalendarPage page is not opened").isTrue();
    }
}
