package io.github.imetaxas.realitycheck;

import java.util.Arrays;
import java.util.function.IntPredicate;

/**
 * Fluent assertions for {@code int[]} arrays.
 *
 * <pre>{@code
 * checkThat(new int[]{1, 2, 3}).hasLength(3).contains(2).isSorted();
 * checkThat(scores).allMatch(s -> s >= 0, "non-negative");
 * }</pre>
 */
public final class IntArrayCheck extends AbstractCheck<IntArrayCheck, int[]> {

    IntArrayCheck(int[] actual, FailureHandler handler) {
        super(actual, handler);
    }

    public IntArrayCheck isEmpty() {
        return failureHandler().check(self(), actual().length == 0,
                "expected an empty int[] but had <%d> elements", actual().length);
    }

    public IntArrayCheck isNotEmpty() {
        return failureHandler().check(self(), actual().length != 0,
                "expected a non-empty int[]");
    }

    public IntArrayCheck hasLength(int expected) {
        return failureHandler().check(self(), actual().length == expected,
                "expected int[] length <%d> but was: <%d>", expected, actual().length);
    }

    public IntArrayCheck contains(int element) {
        boolean found = false;
        for (int e : actual()) {
            if (e == element) { found = true; break; }
        }
        return failureHandler().check(self(), found,
                "expected int[] to contain <%d> but was: %s",
                element, Arrays.toString(actual()));
    }

    public IntArrayCheck doesNotContain(int element) {
        for (int e : actual()) {
            if (e == element) {
                failureHandler().fail("expected int[] not to contain <%d>", element);
            }
        }
        return self();
    }

    public IntArrayCheck containsExactly(int... expected) {
        return failureHandler().check(self(), Arrays.equals(actual(), expected),
                "expected exactly %s but was: %s",
                Arrays.toString(expected), Arrays.toString(actual()));
    }

    public IntArrayCheck containsExactlyInAnyOrder(int... expected) {
        int[] sortedActual = actual().clone();
        int[] sortedExpected = expected.clone();
        Arrays.sort(sortedActual);
        Arrays.sort(sortedExpected);
        return failureHandler().check(self(), Arrays.equals(sortedActual, sortedExpected),
                "expected (in any order) %s but was: %s",
                Arrays.toString(expected), Arrays.toString(actual()));
    }

    public IntArrayCheck isSorted() {
        for (int i = 0; i < actual().length - 1; i++) {
            if (actual()[i] > actual()[i + 1]) {
                failureHandler().fail(
                        "expected sorted int[] but element at index <%d> (<%d>) > element at index <%d> (<%d>)",
                        i, actual()[i], i + 1, actual()[i + 1]);
            }
        }
        return self();
    }

    public IntArrayCheck allMatch(IntPredicate predicate, String description) {
        for (int e : actual()) {
            if (!predicate.test(e)) {
                failureHandler().fail("expected all elements to match [%s] but <%d> did not",
                        description, e);
            }
        }
        return self();
    }

    public IntArrayCheck anyMatch(IntPredicate predicate, String description) {
        boolean found = false;
        for (int e : actual()) {
            if (predicate.test(e)) { found = true; break; }
        }
        return failureHandler().check(self(), found,
                "expected at least one element matching [%s] but none did",
                description);
    }
}
