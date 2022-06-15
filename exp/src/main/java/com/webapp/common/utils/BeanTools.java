package com.webapp.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanTools {
    public static void copyProperties(Object dest, Object orig) {
        try {
            BeanUtils.copyProperties(dest, orig);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("IllegalAccessException when copying object fields", e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("InvocationTargetException when copying object fields", e);
        }
    }

    public static <T, F> T convert(F from, Class<T> to) {
        T t;
        try {
            t = to.getDeclaredConstructor().newInstance();
            BeanTools.copyProperties(t, from);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        return t;
    }

    public static boolean isObjectContainField(Object object, String fieldName) {
        boolean result = false;
        if (object != null) {
            result = Arrays.stream(object.getClass().getDeclaredFields())
                    .anyMatch(f -> f.getName().equals(fieldName));
        }
        return result;
    }

    public static String getFieldValue(Field field, Object object) {
        String result = null;
        field.setAccessible(true); //NOSONAR
        try {
            result = (String) field.get(object);
        } catch (IllegalAccessException e) {
            log.trace("Could not get field value {}\t{}", field, e.getMessage());
        }

        return result;
    }

    public static Object getField(Field field, Object object) {
        field.setAccessible(true); //NOSONAR
        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
//            log.trace("Could not get field {}\t{}", field, e.getMessage());
        }

        return result;
    }

    public static Field getField(String fieldName, Object object) {
        Field field = null;
        try {
            field = object.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
//            log.trace("Could not get field {}\t{}", fieldName, e.getMessage());
        }

        return field;
    }

    public static <T, C> T createObject(Class<T> objectClass, Class<C> constructorClass, C constructorParameter) {
        try {
            return objectClass.getDeclaredConstructor(constructorClass).newInstance(constructorParameter);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Cannot create object of class " + objectClass + "\n" + e.getMessage(), e);
        }
    }

    public static <T> T createObject(Class<T> objectClass) {
        try {
            return objectClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Cannot create object of class " + objectClass, e);
        }
    }

    public static Optional getFieldValueObject(Field field, Object object) {
        Object result = null;
        field.setAccessible(true); //NOSONAR
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            log.trace("Could not get field value {}\t{}", field, e.getMessage());
        }

        return Optional.ofNullable(result);
    }

    public static Optional getInnerObject(Object object, String fieldName) {
        if (isObjectContainField(object, fieldName)) {
            Field field = getField(fieldName, object);
            return Optional.ofNullable(getField(field, object));
        }
        return Optional.empty();
    }

    public static <T> List<T> getStaticConstants(Class<T> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        List<T> results = new ArrayList<>();

        for (var x : declaredFields) {
            if (x.getType() == clazz) {
                T obj = convertFieldToObject(x);
                results.add(obj);
            }
        }

        return results;
    }

    private static <T> T convertFieldToObject(Field x) {
        try {
            return (T) x.get(null);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Can not convert field to object ", e);
        }
    }
}