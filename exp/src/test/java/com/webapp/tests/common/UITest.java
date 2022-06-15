package com.webapp.tests.common;

import com.webapp.core.selenide.SelenideProvider;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class UITest extends WSTest {

    @BeforeAll
    public void tearUp() {
        SelenideProvider.init();
    }

    @AfterAll
    void tearDown() {
        WebDriverRunner.closeWebDriver();
    }
}
