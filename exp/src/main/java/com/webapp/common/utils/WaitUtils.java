package com.webapp.common.utils;

import com.webapp.core.properties.PropertiesController;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.core.ConditionFactory;
import org.awaitility.core.ConditionTimeoutException;
import org.awaitility.core.ThrowingRunnable;

import java.net.ConnectException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

import static com.webapp.common.utils.DateUtil.convert;
import static com.webapp.common.utils.StrUtil.EMPTY;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Duration.ONE_SECOND;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WaitUtils {
    public static final Duration TIMEOUT = PropertiesController.appTimeoutConfig().utilWaitTimeout();
    public static final Duration TIMEOUT_MEDIUM = PropertiesController.appTimeoutConfig().utilWaitMediumTimeout();
    public static final Duration TIMEOUT_LONG = PropertiesController.appTimeoutConfig().utilWaitLongTimeout();
    private static final Duration INTERVAL = Duration.of(1, ChronoUnit.SECONDS);
    private static final Duration DELAY = Duration.of(3, ChronoUnit.SECONDS);
    private static final String LOG_MESSAGE = "{} (elapsed time {}ms, remaining time {}ms)\n";
    private static final String LOG_MESSAGE_SHORT = "remaining time {}ms";
    private static String logDescription = EMPTY;

    public static ConditionFactory doWait() {
        return doWait(INTERVAL, TIMEOUT);
    }

    public static ConditionFactory doWaitWithDelay() {
        return doWait().pollDelay(convert(DELAY));
    }

    public static ConditionFactory doWait(Duration timeout) {
        return doWait(INTERVAL.multipliedBy(3), timeout);
    }

    public static ConditionFactory doWaitMedium() {
        return doWait(INTERVAL.multipliedBy(3), TIMEOUT_MEDIUM);
    }

    public static ConditionFactory doWaitMediumLong() {
        return doWait(INTERVAL.multipliedBy(5), TIMEOUT_MEDIUM.multipliedBy(3));
    }

    public static ConditionFactory doWaitLong() {
        return doWait(INTERVAL.multipliedBy(10), TIMEOUT_LONG);
    }

    public static boolean waitAssertCondition(ThrowingRunnable condition) {
        return waitAssertCondition(condition, TIMEOUT_MEDIUM);
    }

    public static boolean waitAssertCondition(ThrowingRunnable condition, Duration timeout) {
        var result = true;
        Duration interval = INTERVAL.multipliedBy(timeout.toMinutes() <= 1 ? 3 : timeout.toMinutes() <= 3 ? 5 : 10);
        try {
            doWait(interval, timeout).untilAsserted(condition);
        } catch (ConditionTimeoutException e) {
            result = false;
        }
        return result;
    }

    @SneakyThrows
    public static <T> T repeatAction(Callable<T> action) {
        return repeatAction(action, Objects::nonNull);
    }

    @SneakyThrows
    public static <T> T repeatAction(Callable<T> action, Predicate<T> condition) {
        try {
            return doWaitMedium().ignoreExceptions().until(action, condition::test);
        } catch (ConditionTimeoutException e) {
            log.debug("Unable to reach condition due to '{}', repeat...", e.getMessage());
            return action.call();
        }
    }

    public static ConditionFactory doWait(Duration pollInterval, Duration timeout) {
        return await().ignoreException(ConnectException.class).with().pollInSameThread()
                .conditionEvaluationListener(condition -> {
                    if (!logDescription.equals(condition.getDescription())) {
                        logDescription = condition.getDescription();
                        log.trace(LOG_MESSAGE, condition.getDescription(),
                                condition.getElapsedTimeInMS(), condition.getRemainingTimeInMS());
                    } else {
                        log.trace(LOG_MESSAGE_SHORT, condition.getRemainingTimeInMS());
                    }
                })
                .await()
                .atMost(convert(timeout))
                .with()
                .pollInterval(convert(pollInterval))
                .pollDelay(ONE_SECOND);    // skip waiting at 1st iteration
    }
}