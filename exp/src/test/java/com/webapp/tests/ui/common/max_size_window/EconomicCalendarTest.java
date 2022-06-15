package com.webapp.tests.ui.common.max_size_window;

import com.webapp.pages.economiccalendar.EconomicCalendarPage;
import com.webapp.pages.home.HomePage;
import com.webapp.tests.common.UITest;
import com.webapp.widgets.economiccalendar.TimeSelectorBlock;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.codeborne.selenide.Selenide.$x;

@Epic("UI")
@Feature("Economic Calendar")
@Link(name = "Economic Calendar", url = "jira ticket-CS-*****")
public class EconomicCalendarTest extends UITest {

    private HomePage homePage;
    private EconomicCalendarPage economicCalendarPage;
    private TimeSelectorBlock timeSelectorBlock;

    @BeforeAll
    void beforeAll() {
        homePage = HomePage.open();
        homePage.getCookiesPopup().clickAcceptButton();
        economicCalendarPage = homePage.getMainNavigationMenuBlock()
                .clickResearchEducationButton()
                .clickEconomicCalendarLink();
        timeSelectorBlock = economicCalendarPage.getTimeSelectorBlock();
    }

    @BeforeEach
    void beforeEach() {
        economicCalendarPage.getBrowserPageActions().switchToIframeByWebElement($x(".//iframe[contains(@title,'economicCalendar')]"));
    }

    @AfterEach
    void afterEach() {
        economicCalendarPage.getBrowserPageActions().switchToDefaultContent();
    }

    @Test
    void economicCalendarPageYesterdayTest() {
        var yesterdayDate = LocalDate.now().minusDays(1);
        timeSelectorBlock.clickYesterdayButton();
        List<LocalDate> range = timeSelectorBlock.getDatePicker().getDataRange();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(range.get(0))
                    .as("Start date is incorrect")
                    .isEqualTo(yesterdayDate);
            softly.assertThat(range.get(1))
                    .as("End date is incorrect")
                    .isEqualTo(yesterdayDate);
        });
    }

    @Test
    void economicCalendarPageTodayTest() {
        var todayDate = LocalDate.now();
        timeSelectorBlock.clickTodayButton();
        List<LocalDate> range = timeSelectorBlock.getDatePicker().getDataRange();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(range.get(0))
                    .as("Start date is incorrect")
                    .isEqualTo(todayDate);
            softly.assertThat(range.get(1))
                    .as("End date is incorrect")
                    .isEqualTo(todayDate);
        });
    }

    @Test
    void economicCalendarPageTomorrowDateTest() {
        var tomorrowDate = LocalDate.now().plusDays(1);
        timeSelectorBlock.clickTomorrowButton();
        List<LocalDate> range = timeSelectorBlock.getDatePicker().getDataRange();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(range.get(0))
                    .as("Start date is incorrect")
                    .isEqualTo(tomorrowDate);
            softly.assertThat(range.get(1))
                    .as("End date is incorrect")
                    .isEqualTo(tomorrowDate);
        });
    }

    @Test
    void economicCalendarPageThisWeekDateTest() {
        ZoneId zoneId = ZoneId.of("Europe/Zaporozhye");
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        LocalDate firstDayOfThisWeek = now.toLocalDate().with(DayOfWeek.MONDAY).minusDays(1);
        LocalDate lastDayOfThisWeek = now.toLocalDate().with(DayOfWeek.SATURDAY);
        timeSelectorBlock.clickThisWeekButton();
        List<LocalDate> range = timeSelectorBlock.getDatePicker().getDataRange();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(range.get(0))
                    .as("Start date is incorrect")
                    .isEqualTo(firstDayOfThisWeek);
            softly.assertThat(range.get(1))
                    .as("End date is incorrect")
                    .isEqualTo(lastDayOfThisWeek);
        });
    }
}
