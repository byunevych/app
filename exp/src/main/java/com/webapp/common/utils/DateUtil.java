package com.webapp.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Duration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtil {

    public static final String DATE_TIME_FORMAT_RANGE = "dd/MM/yyyy";

    public static List<LocalDate> getDateRange(String date) {
        List<String> dates = Arrays.stream(date.split(" - ")).collect(toList());
        return dates.stream().map(d -> LocalDate.parse(d.trim(), DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_RANGE))).collect(toList());
    }

    public static LocalDateTime parseDate(String date, String pattern) {
        try {
            return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    String.format("Error occurs while parsing %s date by %s pattern", date, pattern), e);
        }
    }

    public static String changeFormat(LocalDateTime ldt, String format) {
        return ldt.format(DateTimeFormatter.ofPattern(format));
    }

    public static Duration convert(java.time.Duration duration) {
        return new Duration(duration.getSeconds(), TimeUnit.SECONDS);
    }

}