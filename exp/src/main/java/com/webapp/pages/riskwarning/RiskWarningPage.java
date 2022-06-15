package com.webapp.pages.riskwarning;

import com.webapp.common.annotations.Url;
import com.webapp.pages.AbstractPage;

import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

@Url(pattern = ".*\\/research\\/risk_warning")
public class RiskWarningPage extends AbstractPage {

    public static RiskWarningPage open() {
        return open(RiskWarningPage.class);
    }
}