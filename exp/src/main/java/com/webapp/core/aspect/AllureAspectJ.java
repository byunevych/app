package com.webapp.core.aspect;

import com.codeborne.selenide.WebDriverRunner;
import com.webapp.common.allure.AllureAttachmentHelper;
import com.webapp.common.allure.AllureLifecycleHelper;
import com.webapp.common.utils.StrUtil;
import io.qameta.allure.model.Attachment;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.webapp.common.allure.AllureLifecycleHelper.getLifecycle;
import static io.qameta.allure.model.Status.FAILED;
import static io.qameta.allure.util.ResultsUtils.getStatus;
import static io.qameta.allure.util.ResultsUtils.getStatusDetails;

@Aspect
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AllureAspectJ {
    private static boolean saveScreenshots = true;

    @Pointcut("execution(* com.simplikube.cma.widgets.Widget.*(..))" +
            " || execution(* java.lang.Enum+.*(..))" +
            " || execution(* com.simplikube.cma.pages.PageManager.getPages(..))"
    )
    public void stepMethodExclusions() {
    }

    @Pointcut("(" +
            "execution(public * com.simplikube.cma.pages..*(..))" +
            " || execution(public * com.simplikube.cma.api..*(..))" +
            " || execution(public * com.simplikube.cma.widgets..*(..))" +
            ")" +
            " && !stepMethodExclusions()"
    )
    public void stepMethod() {
    }

    @Pointcut("call (* assertThat(..)) && within (com.simplikube.cma.tests..*)")
    public void anyAssertCreation() {
    }

    @Pointcut("execution (public * org.assertj.core.api.AbstractAssert+.*(..))" +

            // methods that create redundant rows in report
            " && !execution(* org.assertj.core.api.AbstractAssert+.describedAs(..))" +
            " && !execution(* org.assertj.core.api.AssertJProxySetup.*(..))" +

            //these ones cause issue when failed assertion is displayed as green (should be red) on outer level, but all inner levels are red
            " && !execution(* org.assertj.core.api.StringAssert+.*(..))" +
            " && !execution(* org.assertj.core.api.BooleanAssert+.*(..))" +
            " && !execution(* org.assertj.core.api.ProxyableListAssert+.*(..))"
    )
    public void anyAssert() {
    }

    @Before("stepMethod()")
    public void beforeStepMethod(JoinPoint joinPoint) {
        startStep(joinPoint);
    }

    @AfterReturning(pointcut = "stepMethod()", returning = "returnValue")
    public void afterReturningStepMethod(JoinPoint joinPoint, Object returnValue) {
        stopStep(returnValue);
    }

    @AfterThrowing(pointcut = "stepMethod()", throwing = "e")
    public void afterThrowingStepMethod(JoinPoint joinPoint, Throwable e) {
        stopStep(e, Status.BROKEN);

        addAttachmentsIfFailedInBeforeEach(e);
        addAttachmentsIfFailedInTest(e, Status.BROKEN);
    }

    @AfterThrowing(pointcut = "stepMethodExclusions()", throwing = "e")
    public void afterThrowingStepMethodExclusions(JoinPoint joinPoint, final Throwable e) {
        startStep(joinPoint);
        addAttachmentsIfFailedInBeforeEach(e);
        addAttachmentsIfFailedInTest(e, Status.BROKEN);
        stopStep(e, Status.BROKEN);
    }

    @After("anyAssertCreation()")
    public void afterAssertCreation(JoinPoint joinPoint) {
        final String actual = joinPoint.getArgs().length > 0
                ? AllureLifecycleHelper.arrayToString(joinPoint.getArgs()[0])
                : "<?>";
        final String uuid = UUID.randomUUID().toString();
        final String name = String.format("assertThat '%s'", actual);
        final StepResult result = new StepResult()
                .setName(name)
                .setStatus(Status.PASSED);

        getLifecycle().startStep(uuid, result);
        getLifecycle().stopStep(uuid);
    }

    @Before("anyAssert()")
    public void beforeAnyAssert(JoinPoint joinPoint) {
        startStep(joinPoint);
    }

    @AfterReturning(pointcut = "anyAssert()")
    public void afterReturningAnyAssert(JoinPoint joinPoint) {
        getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
        getLifecycle().stopStep();
    }

    @AfterThrowing(pointcut = "anyAssert()", throwing = "e")
    public void afterThrowingAnyAssert(JoinPoint joinPoint, final Throwable e) {
        addAttachmentsIfFailedInTest(e, Status.FAILED);
        stopStep(e, FAILED);
    }

    private void addAttachmentsIfFailedInBeforeEach(Throwable e) {
        // updateTestCase() is more preferable than updateFixture(), but updateTestCase() doesn't work in some cases, when exception is thrown in beforeEach() method and attachments are
        // not added to the report (reproduces only during command line test execution)
        String uuId = getLifecycle().getCurrentTestCaseOrStep().orElseGet(() -> UUID.randomUUID().toString());
        getLifecycle().updateFixture(fixtureResult -> {
            List<Attachment> attachments = new ArrayList<>();
            addBrowserAttachments(e, uuId, attachments);
            fixtureResult.setAttachments(attachments);
        });
    }

    private void addAttachmentsIfFailedInTest(Throwable e, Status status) {
        getLifecycle().updateTestCase(testResult -> {
            testResult.setStatus(getStatus(e).orElse(status))
                    .setStatusDetails(getStatusDetails(e).orElse(null));

            List<Attachment> stepAttachments = new ArrayList<>();
            addBrowserAttachments(e, testResult.getUuid(), stepAttachments);
            testResult.setAttachments(stepAttachments);
        });
    }

    private void addBrowserAttachments(Throwable e, String uuId, List<Attachment> attachments) {
        if (WebDriverRunner.hasWebDriverStarted()) {
            if (saveScreenshots) {
                attachments.add(AllureAttachmentHelper.getScreenshotAttachment(e));
            }
        }
    }

    private String getMethodArguments(JoinPoint joinPoint) {
        Object[] methodArgs = joinPoint.getArgs();
        String result = AllureLifecycleHelper.arrayToString(methodArgs);

        if (result.contains("%s") && methodArgs.length > 1) {
            Object[] stringFormatArguments = (Object[]) methodArgs[1]; //NOSONAR
            for (Object sfa : stringFormatArguments) {
                result = result.replaceFirst("%s", sfa.toString());
            }
        }

        return result;
    }

    private boolean isCalledFromWait() {
        return isInStackTrace("org.awaitility");
    }

    private void startStep(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String methodNameSentence = camelCaseToSentence(methodName);
        String stepName = joinPoint.getArgs().length > 0
                ? String.format("%s '%s'", methodNameSentence, getMethodArguments(joinPoint))
                : methodNameSentence;

        String uuid = UUID.randomUUID().toString();
        StepResult result = new StepResult().setName(stepName);
        getLifecycle().startStep(uuid, result);
    }

    private void stopStep(Object returnValue) {
        String returnPart = AllureLifecycleHelper.arrayToString(returnValue);
        getLifecycle().updateStep(s -> {
            if (isCalledFromWait()) {
                String newName = returnPart + StrUtil.SPACE + s.getName();
                s.setName(newName);
            }
            s.setStatus(Status.PASSED);
        });
        getLifecycle().stopStep();
    }

    private void stopStep(Throwable e, Status status) {
        getLifecycle().updateStep(s -> s
                .setStatus(getStatus(e).orElse(status))
                .setStatusDetails(getStatusDetails(e).orElse(null)));
        getLifecycle().stopStep();
    }

    private boolean isInStackTrace(String text) {
        return Stream.of(Thread.currentThread().getStackTrace())
                .map(StackTraceElement::toString)
                .anyMatch(methodName -> methodName.contains(text));
    }

    private String camelCaseToSentence(String camelCase) {
        String[] words = camelCase.split("(?<=[a-z])(?=[A-Z])");
        String result = Stream.of(words).collect(Collectors.joining(StrUtil.SPACE)).toLowerCase();
        return StringUtils.capitalize(result);
    }
}