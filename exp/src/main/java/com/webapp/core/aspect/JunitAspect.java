package com.webapp.core.aspect;

import com.webapp.common.allure.AllureFileWriterUtils;
import com.webapp.common.allure.AllureResultTemplateProcessor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class JunitAspect {

    private AllureResultTemplateProcessor processor = new AllureResultTemplateProcessor();

    @Pointcut("@annotation(org.junit.jupiter.api.BeforeAll) && within (com.simplikube.cma.tests..*)")
    public void beforeAll() {
        //pointcut body, should be empty
    }

    @AfterThrowing(pointcut = "beforeAll()", throwing = "throwable")
    public void testPreconditionsFailed(JoinPoint joinPoint, Throwable throwable) {
        AllureResultTemplateProcessor.AllureResultTemplate template = processor.generateAllureResultTemplate(joinPoint, throwable);
        AllureFileWriterUtils.createAllureResultFile(template);
    }
}