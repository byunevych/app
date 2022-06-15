package com.webapp.core.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.webapp.common.utils.StrUtil.MINUS;


@Slf4j
@Aspect
public class LoggingAspect {

    @Pointcut("(" +
            "execution(* com.simplikube.cma.api..*(..)) " +
            "|| execution(* com.simplikube.cma.pages..*(..)) " +
            ") " +
            "&& !execution(* com.simplikube.cma.pages.PageManager.getPages(..))"
    )
    public void anyMethod() {
        //pointcut body, should be empty
    }

    @Before("anyMethod()")
    public void beforeAnyMethod(JoinPoint joinPoint) {
        printToConsole("{}> {} {}.{}({})", null, joinPoint);
    }

    @AfterReturning(pointcut = "anyMethod()", returning = "returnValue")
    public void afterAnyMethod(JoinPoint joinPoint, Object returnValue) {
        printToConsole("{}< {} {}.{}({})", returnValue, joinPoint);
    }

    private void printToConsole(String printPattern, Object returnValue, JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String dashes = getNestingLevelInDashes();

        if (!methodName.contains("lambda")) {
            String mainPart = LoggingHelper.arrayToString(joinPoint.getArgs());
            String returnPart = LoggingHelper.arrayToString(returnValue);
            log.trace(printPattern, dashes, returnPart, className, methodName, mainPart);
        }
    }

    private String getNestingLevelInDashes() {
        String result = "";
        int spaces = (int) Stream.of(Thread.currentThread().getStackTrace())
                .filter(stackTraceElement -> {
                    String className = stackTraceElement.getClassName();
                    return (className.startsWith("api") || className.startsWith("pages") || className.startsWith("steps"))
                            && !className.equals(this.getClass().getName());
                })
                .count();

        if (spaces > 0) {
            int spacesToPrint = spaces - 1;
            result = Stream.iterate(0, i -> i < spacesToPrint, i -> i + 1).map(i -> MINUS).collect(Collectors.joining());
        }

        return result;
    }
}
