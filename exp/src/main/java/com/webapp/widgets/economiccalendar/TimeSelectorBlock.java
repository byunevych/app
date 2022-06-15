package com.webapp.widgets.economiccalendar;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.webapp.common.widgets.Widget;
import com.webapp.widgets.common.DatePicker;
import lombok.Getter;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.webapp.common.utils.WaitUtils.doWait;
import static org.assertj.core.api.Assertions.assertThat;

public class TimeSelectorBlock extends Widget<TimeSelectorBlock> {

    private SelenideElement yesterdayButton = getSelf().$("#timeFrame_yesterday");
    private SelenideElement todayButton = getSelf().$("#timeFrame_today");
    private SelenideElement tomorrowButton = getSelf().$("#timeFrame_tomorrow");
    private SelenideElement thisWeekButton = getSelf().$("#timeFrame_thisWeek");
    @Getter
    DatePicker datePicker = new DatePicker($("#flatDatePickerCanvasHol"));

    private SelenideElement loader = $("#economicCalendarLoading");

    public TimeSelectorBlock(SelenideElement self) {
        super(self);
    }

    public TimeSelectorBlock clickYesterdayButton() {
        yesterdayButton.shouldBe(Condition.visible).click();
        waitForLoader();
        return this;
    }

    public TimeSelectorBlock clickTodayButton() {
        todayButton.shouldBe(Condition.visible).click();
        waitForLoader();
        return this;
    }

    public TimeSelectorBlock clickTomorrowButton() {
        tomorrowButton.shouldBe(Condition.visible).click();
        waitForLoader();
        return this;
    }

    public TimeSelectorBlock clickThisWeekButton() {
        thisWeekButton.shouldBe(Condition.visible).click();
        waitForLoader();
        return this;
    }

    private void waitForLoader() {
        doWait().untilAsserted(() -> assertThat(List.of(loader.getAttribute("style")))
                .as("Loader was NOT loaded updated data")
                .containsAnyOf("display: none;", ""));
    }
}