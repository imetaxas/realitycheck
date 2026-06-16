package com.yanimetaxas.realitycheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Fluent assertions for {@link Stream} values. The stream is consumed (materialized)
 * on the first assertion that needs the elements.
 *
 * <pre>{@code
 * checkThatStream(Stream.of("a", "b", "c")).hasSize(3).contains("b");
 * checkThatStream(dataStream).allMatch(x -> x > 0, "positive");
 * }</pre>
 *
 * @param <T> the element type
 */
public final class StreamCheck<T> extends AbstractCheck<StreamCheck<T>, Stream<T>> {

    private List<T> materialized;

    StreamCheck(Stream<T> actual, FailureHandler handler) {
        super(actual, handler);
    }

    public StreamCheck<T> isEmpty() {
        return failureHandler().check(self(), materialize().isEmpty(),
                "expected an empty stream but had <%d> elements",
                materialize().size());
    }

    public StreamCheck<T> isNotEmpty() {
        return failureHandler().check(self(), !materialize().isEmpty(),
                "expected a non-empty stream");
    }

    public StreamCheck<T> hasSize(int expected) {
        int size = materialize().size();
        return failureHandler().check(self(), size == expected,
                "expected stream size <%d> but was: <%d>", expected, size);
    }

    public StreamCheck<T> contains(T element) {
        return failureHandler().check(self(), materialize().contains(element),
                "expected stream to contain <%s> but it did not", element);
    }

    public StreamCheck<T> doesNotContain(T element) {
        return failureHandler().check(self(), !materialize().contains(element),
                "expected stream not to contain <%s>", element);
    }

    @SafeVarargs
    public final StreamCheck<T> containsExactly(T... expected) {
        List<T> list = materialize();
        List<T> expectedList = Arrays.asList(expected);
        return failureHandler().check(self(), list.equals(expectedList),
                "expected exactly %s but was: %s", expectedList, list);
    }

    @SafeVarargs
    public final StreamCheck<T> containsExactlyInAnyOrder(T... expected) {
        return failureHandler().check(self(),
                frequencyMap(materialize()).equals(frequencyMap(Arrays.asList(expected))),
                "expected (in any order) %s but was: %s",
                Arrays.toString(expected), materialize());
    }

    private Map<T, Integer> frequencyMap(Collection<T> items) {
        Map<T, Integer> freq = new HashMap<>();
        for (T e : items) {
            freq.merge(e, 1, Integer::sum);
        }
        return freq;
    }

    public StreamCheck<T> allMatch(Predicate<T> predicate, String description) {
        for (T e : materialize()) {
            if (!predicate.test(e)) {
                failureHandler().fail("expected all elements to match [%s] but <%s> did not",
                        description, e);
            }
        }
        return self();
    }

    public StreamCheck<T> anyMatch(Predicate<T> predicate, String description) {
        return failureHandler().check(self(), materialize().stream().anyMatch(predicate),
                "expected at least one element matching [%s] but none did",
                description);
    }

    public StreamCheck<T> noneMatch(Predicate<T> predicate, String description) {
        for (T e : materialize()) {
            if (predicate.test(e)) {
                failureHandler().fail("expected no elements to match [%s] but <%s> matched",
                        description, e);
            }
        }
        return self();
    }

    /** Extracts the first element for further assertions. Fails if the stream is empty. */
    public ObjectCheck<T> first() {
        List<T> list = materialize();
        if (list.isEmpty()) {
            failureHandler().fail("cannot extract first element — stream is empty");
        }
        T value = list.isEmpty() ? null : list.get(0);
        return new ObjectCheck<>(value, failureHandler());
    }

    /** Converts to a {@link CollectionCheck} for collection-specific assertions. */
    public CollectionCheck<T> toList() {
        return new CollectionCheck<>(materialize(), failureHandler());
    }

    private List<T> materialize() {
        if (materialized == null) {
            materialized = new ArrayList<>();
            Objects.requireNonNull(actual(), "stream must not be null")
                    .forEach(materialized::add);
        }
        return materialized;
    }
}
