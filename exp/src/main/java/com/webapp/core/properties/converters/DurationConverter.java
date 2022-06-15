package com.webapp.core.properties.converters;

import com.webapp.common.utils.StrUtil;
import lombok.AllArgsConstructor;
import org.aeonbits.owner.Converter;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.webapp.common.utils.StreamUtils.getFirstInOptional;
import static com.webapp.common.utils.StreamUtils.mapEnumToList;

public class DurationConverter implements Converter<Duration> {
    @Override
    public Duration convert(Method method, String text) {
        int numericPart = StrUtil.getNumberFromStr(text);

        return getFirstInOptional(DurationTags.values(), tag -> text.endsWith(tag.strValue))
                .map(tag -> Duration.of(numericPart, tag.chronoUnitValue))
                .orElseThrow(() -> new IllegalStateException(String.format("Time unit not specified for %s property. Expected time unit values are: %s",
                        text, mapEnumToList(DurationTags.class, tag -> tag.strValue))));
    }

    @AllArgsConstructor
    private enum DurationTags {
        MINUTES(".min", ChronoUnit.MINUTES),
        SECONDS(".sec", ChronoUnit.SECONDS),
        MILLIS(".milli", ChronoUnit.MILLIS),
        HOURS(".hour", ChronoUnit.HOURS);

        private String strValue;
        private ChronoUnit chronoUnitValue;
    }
}
