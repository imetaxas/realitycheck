package com.yanimetaxas.realitycheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Fluent assertions for {@link Collection} values.
 *
 * @param <T> the element type
 */
public final class CollectionCheck<T> extends AbstractCheck<CollectionCheck<T>, Collection<T>> {

    CollectionCheck(Collection<T> actual, FailureHandler handler) {
        super(actual, handler);
    }

    public CollectionCheck<T> isEmpty() {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), actual().isEmpty(),
                "expected an empty collection but had <%d> elements: %s",
                actual().size(), actual());
    }

    public CollectionCheck<T> isNotEmpty() {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), !actual().isEmpty(),
                "expected a non-empty collection");
    }

    public CollectionCheck<T> hasSize(int expected) {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), actual().size() == expected,
                "expected collection size <%d> but was: <%d>",
                expected, actual().size());
    }

    public CollectionCheck<T> hasSizeGreaterThan(int minExclusive) {
        return failureHandler().check(self(), actual().size() > minExclusive,
                "expected collection size > <%d> but was: <%d>",
                minExclusive, actual().size());
    }

    public CollectionCheck<T> hasSizeLessThan(int maxExclusive) {
        return failureHandler().check(self(), actual().size() < maxExclusive,
                "expected collection size < <%d> but was: <%d>",
                maxExclusive, actual().size());
    }

    public CollectionCheck<T> contains(T element) {
        return failureHandler().check(self(), actual().contains(element),
                "expected collection to contain <%s> but it did not.\n  actual: %s",
                element, actual());
    }

    public CollectionCheck<T> doesNotContain(T element) {
        return failureHandler().check(self(), !actual().contains(element),
                "expected collection not to contain <%s>", element);
    }

    @SafeVarargs
    public final CollectionCheck<T> containsAll(T... elements) {
        Set<T> missing = new HashSet<>();
        for (T e : elements) {
            if (!actual().contains(e)) {
                missing.add(e);
            }
        }
        return failureHandler().check(self(), missing.isEmpty(),
                "expected collection to contain all of %s but missing: %s",
                Arrays.toString(elements), missing);
    }

    @SafeVarargs
    public final CollectionCheck<T> containsExactly(T... elements) {
        List<T> expected = Arrays.asList(elements);
        List<T> actualList = new ArrayList<>(actual());
        return failureHandler().check(self(), actualList.equals(expected),
                "expected exactly %s but was: %s", expected, actualList);
    }

    @SafeVarargs
    public final CollectionCheck<T> containsExactlyInAnyOrder(T... elements) {
        return failureHandler().check(self(),
                frequencyMap(actual()).equals(frequencyMap(Arrays.asList(elements))),
                "expected (in any order) %s but was: %s",
                Arrays.toString(elements), actual());
    }

    private Map<T, Integer> frequencyMap(Collection<T> items) {
        Map<T, Integer> freq = new HashMap<>();
        for (T e : items) {
            freq.merge(e, 1, Integer::sum);
        }
        return freq;
    }

    @SafeVarargs
    public final CollectionCheck<T> containsAnyOf(T... elements) {
        boolean found = false;
        for (T e : elements) {
            if (actual().contains(e)) { found = true; break; }
        }
        return failureHandler().check(self(), found,
                "expected collection to contain any of %s but was: %s",
                Arrays.toString(elements), actual());
    }

    @SafeVarargs
    public final CollectionCheck<T> containsNoneOf(T... elements) {
        Set<T> found = new HashSet<>();
        for (T e : elements) {
            if (actual().contains(e)) {
                found.add(e);
            }
        }
        return failureHandler().check(self(), found.isEmpty(),
                "expected collection to contain none of %s but found: %s",
                Arrays.toString(elements), found);
    }

    public CollectionCheck<T> allMatch(Predicate<T> predicate, String description) {
        for (T e : actual()) {
            if (!predicate.test(e)) {
                failureHandler().fail("expected all elements to match [%s] but <%s> did not",
                        description, e);
            }
        }
        return self();
    }

    public CollectionCheck<T> noneMatch(Predicate<T> predicate, String description) {
        for (T e : actual()) {
            if (predicate.test(e)) {
                failureHandler().fail("expected no elements to match [%s] but <%s> matched",
                        description, e);
            }
        }
        return self();
    }

    public CollectionCheck<T> anyMatch(Predicate<T> predicate, String description) {
        return failureHandler().check(self(), actual().stream().anyMatch(predicate),
                "expected at least one element matching [%s] but none did",
                description);
    }

    public ObjectCheck<T> first() {
        if (!isActualPresent()) return new ObjectCheck<>(null, failureHandler());
        if (actual().isEmpty()) {
            failureHandler().fail("cannot get first element of an empty collection");
            return new ObjectCheck<>(null, failureHandler());
        }
        return new ObjectCheck<>(actual().iterator().next(), failureHandler());
    }
}
