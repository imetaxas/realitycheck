package com.yanimetaxas.realitycheck;

import java.util.Arrays;
import java.util.function.LongPredicate;

/**
 * Fluent assertions for {@code long[]} arrays.
 *
 * <pre>{@code
 * checkThat(new long[]{1L, 2L, 3L}).hasLength(3).contains(2L).isSorted();
 * }</pre>
 */
public final class LongArrayCheck extends AbstractCheck<LongArrayCheck, long[]> {

    LongArrayCheck(long[] actual, FailureHandler handler) {
        super(actual, handler);
    }

    public LongArrayCheck isEmpty() {
        return failureHandler().check(self(), actual().length == 0,
                "expected an empty long[] but had <%d> elements", actual().length);
    }

    public LongArrayCheck isNotEmpty() {
        return failureHandler().check(self(), actual().length != 0,
                "expected a non-empty long[]");
    }

    public LongArrayCheck hasLength(int expected) {
        return failureHandler().check(self(), actual().length == expected,
                "expected long[] length <%d> but was: <%d>", expected, actual().length);
    }

    public LongArrayCheck contains(long element) {
        boolean found = false;
        for (long e : actual()) {
            if (e == element) { found = true; break; }
        }
        return failureHandler().check(self(), found,
                "expected long[] to contain <%d> but was: %s",
                element, Arrays.toString(actual()));
    }

    public LongArrayCheck doesNotContain(long element) {
        for (long e : actual()) {
            if (e == element) {
                failureHandler().fail("expected long[] not to contain <%d>", element);
            }
        }
        return self();
    }

    public LongArrayCheck containsExactly(long... expected) {
        return failureHandler().check(self(), Arrays.equals(actual(), expected),
                "expected exactly %s but was: %s",
                Arrays.toString(expected), Arrays.toString(actual()));
    }

    public LongArrayCheck containsExactlyInAnyOrder(long... expected) {
        long[] sortedActual = actual().clone();
        long[] sortedExpected = expected.clone();
        Arrays.sort(sortedActual);
        Arrays.sort(sortedExpected);
        return failureHandler().check(self(), Arrays.equals(sortedActual, sortedExpected),
                "expected (in any order) %s but was: %s",
                Arrays.toString(expected), Arrays.toString(actual()));
    }

    public LongArrayCheck isSorted() {
        for (int i = 0; i < actual().length - 1; i++) {
            if (actual()[i] > actual()[i + 1]) {
                failureHandler().fail(
                        "expected sorted long[] but element at index <%d> (<%d>) > element at index <%d> (<%d>)",
                        i, actual()[i], i + 1, actual()[i + 1]);
            }
        }
        return self();
    }

    public LongArrayCheck allMatch(LongPredicate predicate, String description) {
        for (long e : actual()) {
            if (!predicate.test(e)) {
                failureHandler().fail("expected all elements to match [%s] but <%d> did not",
                        description, e);
            }
        }
        return self();
    }
}
