package com.webapp.core.aspect;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.webapp.common.utils.StrUtil.COMMA;
import static com.webapp.common.utils.StrUtil.SPACE;

@Slf4j
class LoggingHelper {
    public static String arrayToString(final Object... array) {
        return Stream.of(array)
                .filter(Objects::nonNull)
                .map(object -> {
                    AtomicReference<String> result = new AtomicReference<>();

                    if (isArrayOrList(object)) {
                        processArrayOrList(object, result);
                    }

                    return Optional.ofNullable(result.get())
                            .orElseGet(() -> wrapResult(object.getClass(), convertToString(object)));
                })
                .collect(Collectors.joining(SPACE));
    }

    private static String convertToString(final Object object) {
        try {
            if (Objects.nonNull(object) && object.getClass().isArray()) {
                if (object instanceof Object[]) {
                    return Arrays.toString((Object[]) object);
                } else if (object instanceof long[]) {
                    return Arrays.toString((long[]) object);
                } else if (object instanceof short[]) {
                    return Arrays.toString((short[]) object);
                } else if (object instanceof int[]) {
                    return Arrays.toString((int[]) object);
                } else if (object instanceof char[]) {
                    return Arrays.toString((char[]) object);
                } else if (object instanceof double[]) {
                    return Arrays.toString((double[]) object);
                } else if (object instanceof float[]) {
                    return Arrays.toString((float[]) object);
                } else if (object instanceof boolean[]) {
                    return Arrays.toString((boolean[]) object);
                } else if (object instanceof byte[]) {
                    return "<BINARY>";
                }
            } else if (Objects.nonNull(object) && object.getClass().getPackageName().startsWith("taf.onboarding")) {
                return object.getClass().getSimpleName() + COMMA + SPACE;
            }

            return Objects.toString(object);
        } catch (Exception e) {
            log.error("Could not convert object to string", e);
            return "<NPE>";
        }
    }

    private static void processResult(Object object, AtomicReference<String> result, String name) {
        result.set(wrapResult(object.getClass(), name));
    }

    private static void processArrayOrList(Object object, AtomicReference<String> result) {
        List<Object> list = new ArrayList<>();
        if (object instanceof List) {
            list = (List) object;
        } else {
            for (Object o : List.of((Object[]) object)) {
                if (o != null) {
                    list.add(o);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        list.forEach(item -> sb.append(convertToString(item)));
        processResult(object, result, sb.toString());
    }

    private static String wrapResult(Class clazz, String entity) {
        return String.format("%s[%s]", clazz.getSimpleName(), entity);
    }

    private static boolean isArrayOrList(Object object) {
        return object.getClass().isArray() || object instanceof List;
    }
}
