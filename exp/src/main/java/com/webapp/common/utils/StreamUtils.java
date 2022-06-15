package com.webapp.common.utils;

import com.webapp.common.widgets.WidgetOption;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StreamUtils {

    public static <T, R> List<R> map(Stream<T> stream, Function<T, R> mapper) {
        return stream.map(mapper).collect(toList());
    }

    public static <T, R> List<R> map(Collection<T> collection, Function<T, R> mapper) {
        return map(collection.stream(), mapper);
    }

    public static <T, R> List<R> map(T[] array, Function<T, R> mapper) {
        return map(List.of(array), mapper);
    }

    public static <T, R> List<R> mapAndDistinct(Collection<T> collection, Function<T, R> mapper) {
        return distinct(map(collection, mapper));
    }

    public static <T, E extends Enum<E>> List<T> mapEnumToList(Class<E> enumData, Function<E, T> mapper) {
        return Stream.of(enumData.getEnumConstants()).map(mapper).collect(toList());
    }

    public static <E extends Enum<E> & WidgetOption> List<E> mapToEnumList(Class<E> cls, List<String> values) {
        return values.stream().map(v -> getFirstEnum(cls, e -> v.equalsIgnoreCase(e.getValue()), v)).collect(Collectors.toList());
    }

    public static <T> List<T> filter(Stream<T> stream, Predicate<T> predicate) {
        return stream.filter(predicate).collect(toList());
    }

    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        return filter(collection.stream(), predicate);
    }

    public static <T> List<T> filter(T[] array, Predicate<T> predicate) {
        return filter(List.of(array), predicate);
    }

    public static <T> List<T> filterAndDistinct(Collection<T> collection, Predicate<T> predicate) {
        return distinct(filter(collection, predicate));
    }

    public static <T> T getFirst(Stream<T> origStream, Predicate<T> predicate, String elementName) {
        return origStream.filter(predicate).findFirst().orElseThrow(() -> new NoSuchElementException("element " + elementName + " wasn't found"));
    }

    public static <T> T getFirst(List<T> origList, Predicate<T> predicate, String elementName) {
        return getFirst(origList.stream(), predicate, elementName);
    }

    public static <T> T getFirst(Set<T> origSet, Predicate<T> predicate, String elementName) {
        return getFirst(new ArrayList<T>(origSet), predicate, elementName);
    }

    public static <T> T getFirst(T[] origArray, Predicate<T> predicate, String elementName) {
        return getFirst(List.of(origArray), predicate, elementName);
    }

    public static <T> T getFirst(Collection<T> origCollection, Predicate<T> predicate, String elementName) {
        return getFirst(origCollection.stream(), predicate, elementName);
    }

    public static <T> Optional<T> getFirstInOptional(List<T> origList, Predicate<T> predicate) {
        return origList.stream().filter(predicate).findFirst();
    }

    public static <T> Optional<T> getFirstInOptional(T[] array, Predicate<T> predicate) {
        return getFirstInOptional(List.of(array), predicate);
    }

    public static <T extends Enum<T>> T getFirstEnum(Class<T> clazz, Predicate<T> predicate, String value) {
        return Stream.of(clazz.getEnumConstants())
                .filter(predicate)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Enum not found for: " + value));
    }

    public static <T> List<T> distinct(Collection<T> collection) {
        return collection.stream().distinct().collect(toList());
    }

    public static <T> List<T> intersect(Collection<T> initial, Collection<T> forComparison) {
        return initial.stream().filter(forComparison::contains).collect(Collectors.toList());
    }

    public static Stream<Integer> iterate(int lessThan) {
        return Stream.iterate(0, i -> i < lessThan, i -> i + 1);
    }

    public static <T> Stream<T> concat(List<Stream<T>> streamList) {
        return streamList.stream().reduce(Stream::concat).orElseGet(Stream::empty);
    }

    @SafeVarargs
    public static <T> List<T> executeTasksInParallel(Supplier<T>... tasks) {
        return executeTasksInParallel(List.of(tasks));
    }

    public static <T> List<T> executeTasksInParallel(List<Supplier<T>> tasks) {
        return tasks.stream()
                .map(CompletableFuture::supplyAsync)
                .collect(toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public static <T> void executeSameTaskInParallel(Supplier<T> task, int amount) {
        List<Supplier<T>> suppliers = StreamUtils.iterate(amount).map(i -> task).collect(toList());
        executeTasksInParallel(suppliers);
    }

}
