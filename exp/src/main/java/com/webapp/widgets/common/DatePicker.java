package com.webapp.widgets.common;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.webapp.common.utils.DateUtil;
import com.webapp.common.widgets.Widget;

import java.time.LocalDate;
import java.util.List;

public class DatePicker extends Widget<DatePicker> {

    private SelenideElement dataRange = getSelf().$("#widgetField");

    public DatePicker(SelenideElement self) {
        super(self);
    }

    public List<LocalDate> getDataRange() {
        var range = dataRange.shouldBe(Condition.visible).getText();
        return DateUtil.getDateRange(range);
    }
}