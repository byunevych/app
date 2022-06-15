package com.webapp.pages;

import com.webapp.pages.economiccalendar.EconomicCalendarPage;
import com.webapp.pages.home.HomePage;
import com.webapp.pages.riskwarning.RiskWarningPage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageManager {
    private static PageManager instance;

    private HomePage homePage;
    private EconomicCalendarPage economicCalendarPage;
    private RiskWarningPage riskWarningPage;
    private RiskWarningPdfPage riskWarningPdfPage;

    public static synchronized PageManager getPages() {
        return Optional.ofNullable(instance).orElseGet(() -> instance = new PageManager());
    }

    public HomePage getHomePage() {
        return Optional.ofNullable(homePage).orElseGet(() -> homePage = new HomePage());
    }

    public EconomicCalendarPage getEconomicCalendarPage() {
        return Optional.ofNullable(economicCalendarPage).orElseGet(() -> economicCalendarPage = new EconomicCalendarPage());
    }

    public RiskWarningPage getRiskWarningPage() {
        return Optional.ofNullable(riskWarningPage).orElseGet(() -> riskWarningPage = new RiskWarningPage());
    }

    public RiskWarningPdfPage getRiskWarningPdfPage() {
        return Optional.ofNullable(riskWarningPdfPage).orElseGet(() -> riskWarningPdfPage = new RiskWarningPdfPage());
    }
}
