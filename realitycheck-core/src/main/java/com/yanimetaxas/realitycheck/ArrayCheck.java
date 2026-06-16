package com.yanimetaxas.realitycheck;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Fluent assertions for object arrays.
 *
 * <pre>{@code
 * checkThatArray(names).isNotEmpty().hasLength(3).contains("Alice");
 * checkThatArray(results).containsExactly("a", "b", "c");
 * checkThatArray(scores).allMatch(s -> s > 0, "positive");
 * }</pre>
 *
 * @param <T> the element type
 */
public final class ArrayCheck<T> extends AbstractCheck<ArrayCheck<T>, T[]> {

    ArrayCheck(T[] actual, FailureHandler handler) {
        super(actual, handler);
    }

    public ArrayCheck<T> isEmpty() {
        return failureHandler().check(self(), actual().length == 0,
                "expected an empty array but had <%d> elements", actual().length);
    }

    public ArrayCheck<T> isNotEmpty() {
        return failureHandler().check(self(), actual().length != 0,
                "expected a non-empty array");
    }

    public ArrayCheck<T> hasLength(int expected) {
        return failureHandler().check(self(), actual().length == expected,
                "expected array length <%d> but was: <%d>", expected, actual().length);
    }

    public ArrayCheck<T> contains(T element) {
        boolean found = false;
        for (T e : actual()) {
            if (java.util.Objects.equals(e, element)) { found = true; break; }
        }
        return failureHandler().check(self(), found,
                "expected array to contain <%s> but was: %s",
                element, Arrays.toString(actual()));
    }

    public ArrayCheck<T> doesNotContain(T element) {
        for (T e : actual()) {
            if (java.util.Objects.equals(e, element)) {
                failureHandler().fail("expected array not to contain <%s>", element);
            }
        }
        return self();
    }

    @SafeVarargs
    public final ArrayCheck<T> containsExactly(T... expected) {
        return failureHandler().check(self(), Arrays.equals(actual(), expected),
                "expected exactly %s but was: %s",
                Arrays.toString(expected), Arrays.toString(actual()));
    }

    @SafeVarargs
    public final ArrayCheck<T> containsExactlyInAnyOrder(T... expected) {
        return failureHandler().check(self(),
                frequencyMap(actual()).equals(frequencyMap(expected)),
                "expected (in any order) %s but was: %s",
                Arrays.toString(expected), Arrays.toString(actual()));
    }

    private Map<T, Integer> frequencyMap(T[] elements) {
        Map<T, Integer> freq = new HashMap<>();
        for (T e : elements) {
            freq.merge(e, 1, Integer::sum);
        }
        return freq;
    }

    @SafeVarargs
    public final ArrayCheck<T> containsAll(T... elements) {
        Set<T> actualSet = new HashSet<>(Arrays.asList(actual()));
        Set<T> missing = new HashSet<>();
        for (T e : elements) {
            if (!actualSet.contains(e)) missing.add(e);
        }
        return failureHandler().check(self(), missing.isEmpty(),
                "expected array to contain all of %s but missing: %s",
                Arrays.toString(elements), missing);
    }

    public ArrayCheck<T> allMatch(Predicate<T> predicate, String description) {
        for (T e : actual()) {
            if (!predicate.test(e)) {
                failureHandler().fail("expected all elements to match [%s] but <%s> did not",
                        description, e);
            }
        }
        return self();
    }

    public ArrayCheck<T> anyMatch(Predicate<T> predicate, String description) {
        boolean found = false;
        for (T e : actual()) {
            if (predicate.test(e)) { found = true; break; }
        }
        return failureHandler().check(self(), found,
                "expected at least one element matching [%s] but none did",
                description);
    }

    @SuppressWarnings("unchecked")
    public ArrayCheck<T> isSorted() {
        for (int i = 0; i < actual().length - 1; i++) {
            if (actual()[i] instanceof Comparable<?> c) {
                if (((Comparable<T>) actual()[i]).compareTo(actual()[i + 1]) > 0) {
                    failureHandler().fail("expected sorted array but element at index <%d> (<%s>) > element at index <%d> (<%s>)",
                            i, actual()[i], i + 1, actual()[i + 1]);
                }
            } else {
                failureHandler().fail("cannot check sorting — elements do not implement Comparable");
            }
        }
        return self();
    }

    /** Converts to a {@link CollectionCheck} for collection-specific assertions. */
    public CollectionCheck<T> asCollection() {
        return new CollectionCheck<>(Arrays.asList(actual()), failureHandler());
    }
}
