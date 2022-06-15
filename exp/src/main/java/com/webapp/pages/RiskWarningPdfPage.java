package com.webapp.pages;

import com.webapp.common.annotations.Url;

@Url(pattern = ".*\\/assets\\/pdf\\/new\\/docs\\/.*")
public class RiskWarningPdfPage extends AbstractPage {

    public static RiskWarningPdfPage open() {
        return open(RiskWarningPdfPage.class);
    }
}