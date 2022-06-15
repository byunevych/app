package com.webapp.common.allure;

import com.webapp.common.utils.StrUtil;
import com.webapp.common.utils.StreamUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Label;
import io.qameta.allure.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.webapp.common.utils.BeanTools.getField;
import static com.webapp.common.utils.BeanTools.getInnerObject;
import static com.webapp.common.utils.StrUtil.*;

public class AllureLifecycleHelper {
    private static InheritableThreadLocal<AllureLifecycle> lifecycle = new InheritableThreadLocal<>() {
        @Override
        protected AllureLifecycle initialValue() {
            return Allure.getLifecycle();
        }
    };

    /**
     * For tests only.
     *
     * @param allure allure lifecycle to set.
     */
    public static void setLifecycle(final AllureLifecycle allure) {
        lifecycle.set(allure);
    }

    public static AllureLifecycle getLifecycle() {
        return lifecycle.get();
    }

    public static void setSuiteName(String suiteName) {
        getLifecycle().updateTestCase(testResult -> {
            String suiteKey = "suite";
            Label suiteLabel = StreamUtils.getFirst(testResult.getLabels(), label ->
                    label.getName().equals(suiteKey), suiteKey);
            suiteLabel.setValue(suiteName + "[]");
        });
    }

    public static String arrayToString(final Object... array) {
        return Stream.of(array)
                .filter(Objects::nonNull)
                .map(object -> isNotEmptyOptional(object) ? ((Optional) object).orElseThrow() : object)
                .map(object -> {
                    AtomicReference<String> result = new AtomicReference<>();

                    Optional<String> metadataName = getMetadataName(object);
                    metadataName.ifPresent(name -> processResult(object, result, name));

                    if (isContainerForObjectsWithMetadata(object)) {
                        processContainerForObjectsWithMetadata(object, result);
                    } else if (isArrayOrList(object)) {
                        processArrayOrList(object, result);
                    } else if (isObjectWithMetadata(object)) {
                        processObjectWithMetadata(object, result);
                    }

                    return Optional.ofNullable(result.get())
                            .orElseGet(() -> wrapResult(object.getClass(), ObjectUtils.toString(object)));

                })
                .collect(Collectors.joining(SPACE));
    }

    private static void processResult(Object object, AtomicReference<String> result, String name) {
        result.set(wrapResult(object.getClass(), name));
    }

    private static void processContainerForObjectsWithMetadata(Object object, AtomicReference<String> result) {
        Optional<Object> itemsObject = getInnerObject(object, "items");
        if (itemsObject.isPresent() && (itemsObject.get() instanceof Collection)) {
            StringBuilder sb = new StringBuilder();
            ((List) itemsObject.get()).forEach(item -> {
                Optional<String> itemMetadataName = getMetadataName(item);
                itemMetadataName.ifPresent(s -> sb.append(s).append(SEMICOLON + SPACE));
            });

            processResult(object, result, sb.toString());
        }
    }

    private static void processArrayOrList(Object object, AtomicReference<String> result) {
        processResult(object, result, convertArrayOrListToString(object));
    }

    private static String convertArrayOrListToString(Object object) {
        List list = new ArrayList();
        if (object instanceof List) {
            list = (List) object;
        } else {
            for (Object o : (Object[]) object) {
                if (o != null) {
                    list.add(o);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        list.forEach(item -> {
            Optional<String> metadataName = getMetadataName(item);
            metadataName.ifPresentOrElse(sb::append, () -> sb.append(item));
            sb.append(SEMICOLON + SPACE);
        });
        return StrUtil.deleteLastSymbolIfPResent(sb.toString().strip(), SEMICOLON);
    }

    private static void processObjectWithMetadata(Object object, AtomicReference<String> result) {
        Optional<String> metadataName = getMetadataName(object);
        processResult(object, result, metadataName.orElse(EMPTY));
    }

    private static String wrapResult(Class clazz, String text) {
        final int logSizeLimit = 1000;
        if (text.length() > logSizeLimit) {
            text = text.substring(0, logSizeLimit) + "\nto be continued...\n";
        }
        return String.format("%s[%s]", clazz.getSimpleName(), text);
    }

    private static boolean isContainerForObjectsWithMetadata(Object object) {
        Field kindField = getField("kind", object);

        return kindField != null && StringUtils.containsIgnoreCase(kindField.toString(), "list");
    }

    private static boolean isNotEmptyOptional(Object object) {
        return object instanceof Optional && ((Optional) object).isPresent();
    }

    private static boolean isArrayOrList(Object object) {
        return object.getClass().isArray() || object instanceof List;
    }

    private static boolean isObjectWithMetadata(Object object) {
        Optional<Object> metaObject = getInnerObject(object, "metadata");
        return metaObject.isPresent();
    }

    private static Optional<String> getMetadataName(Object object) {
        Optional<Object> metaObject = getInnerObject(object, "metadata");
        if (metaObject.isPresent()) {
            Optional<Object> nameObject = getInnerObject(metaObject.get(), "name");
            if (nameObject.isPresent()) {
                return Optional.of(nameObject.get().toString());
            }
        }
        return Optional.empty();
    }

}