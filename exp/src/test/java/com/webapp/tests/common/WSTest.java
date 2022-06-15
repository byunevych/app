package com.webapp.tests.common;


import com.webapp.app.ApplicationManager;
import com.webapp.core.junit.ScreenshotExtension;
import com.webapp.pages.PageManager;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({ScreenshotExtension.class})
@DisplayNameGeneration(DisplayNameGenerator.Standard.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WSTest {
    protected ApplicationManager app = ApplicationManager.getInstance();
}