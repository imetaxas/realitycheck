package io.github.imetaxas.realitycheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Fluent assertions for {@link Iterable} values. Unlike {@link CollectionCheck}, this
 * works with lazy iterables (custom iterators, generator-backed sequences) without
 * requiring a {@code Collection}.
 *
 * <pre>{@code
 * checkThatIterable(lazySequence).isNotEmpty().contains("target");
 * checkThatIterable(generator).hasSize(100).allMatch(x -> x > 0, "positive");
 * }</pre>
 *
 * @param <T> the element type
 */
public final class IterableCheck<T> extends AbstractCheck<IterableCheck<T>, Iterable<T>> {

    private List<T> materialized;

    IterableCheck(Iterable<T> actual, FailureHandler handler) {
        super(actual, handler);
    }

    public IterableCheck<T> isEmpty() {
        return failureHandler().check(self(), materialize().isEmpty(),
                "expected an empty iterable but had <%d> elements",
                materialize().size());
    }

    public IterableCheck<T> isNotEmpty() {
        return failureHandler().check(self(), !materialize().isEmpty(),
                "expected a non-empty iterable");
    }

    public IterableCheck<T> hasSize(int expected) {
        int size = materialize().size();
        return failureHandler().check(self(), size == expected,
                "expected iterable size <%d> but was: <%d>", expected, size);
    }

    public IterableCheck<T> contains(T element) {
        return failureHandler().check(self(), materialize().contains(element),
                "expected iterable to contain <%s> but it did not", element);
    }

    public IterableCheck<T> doesNotContain(T element) {
        return failureHandler().check(self(), !materialize().contains(element),
                "expected iterable not to contain <%s>", element);
    }

    @SafeVarargs
    public final IterableCheck<T> containsAll(T... elements) {
        List<T> list = materialize();
        Set<T> missing = new HashSet<>();
        for (T e : elements) {
            if (!list.contains(e)) {
                missing.add(e);
            }
        }
        return failureHandler().check(self(), missing.isEmpty(),
                "expected iterable to contain all of %s but missing: %s",
                Arrays.toString(elements), missing);
    }

    @SafeVarargs
    public final IterableCheck<T> containsAnyOf(T... elements) {
        List<T> list = materialize();
        boolean found = false;
        for (T e : elements) {
            if (list.contains(e)) { found = true; break; }
        }
        return failureHandler().check(self(), found,
                "expected iterable to contain any of %s but found none",
                (Object) Arrays.toString(elements));
    }

    @SafeVarargs
    public final IterableCheck<T> containsNoneOf(T... elements) {
        List<T> list = materialize();
        Set<T> found = new HashSet<>();
        for (T e : elements) {
            if (list.contains(e)) found.add(e);
        }
        return failureHandler().check(self(), found.isEmpty(),
                "expected iterable to contain none of %s but found: %s",
                Arrays.toString(elements), found);
    }

    public IterableCheck<T> allMatch(Predicate<T> predicate, String description) {
        for (T e : materialize()) {
            if (!predicate.test(e)) {
                failureHandler().fail("expected all elements to match [%s] but <%s> did not",
                        description, e);
            }
        }
        return self();
    }

    public IterableCheck<T> anyMatch(Predicate<T> predicate, String description) {
        return failureHandler().check(self(), materialize().stream().anyMatch(predicate),
                "expected at least one element matching [%s] but none did",
                description);
    }

    public IterableCheck<T> noneMatch(Predicate<T> predicate, String description) {
        for (T e : materialize()) {
            if (predicate.test(e)) {
                failureHandler().fail("expected no elements to match [%s] but <%s> matched",
                        description, e);
            }
        }
        return self();
    }

    /** Converts to a {@link CollectionCheck} for collection-specific assertions. */
    public CollectionCheck<T> asCollection() {
        return new CollectionCheck<>(materialize(), failureHandler());
    }

    private List<T> materialize() {
        if (materialized == null) {
            materialized = new ArrayList<>();
            for (T element : actual()) {
                materialized.add(element);
            }
        }
        return materialized;
    }
}
