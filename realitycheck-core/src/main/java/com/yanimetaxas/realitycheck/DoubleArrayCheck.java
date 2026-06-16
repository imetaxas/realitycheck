package com.yanimetaxas.realitycheck;

import java.util.Arrays;
import java.util.function.DoublePredicate;

/**
 * Fluent assertions for {@code double[]} arrays.
 *
 * <pre>{@code
 * checkThat(new double[]{1.0, 2.5, 3.7}).hasLength(3).isSorted();
 * checkThat(measurements).containsCloseTo(3.14, 0.01);
 * }</pre>
 */
public final class DoubleArrayCheck extends AbstractCheck<DoubleArrayCheck, double[]> {

    DoubleArrayCheck(double[] actual, FailureHandler handler) {
        super(actual, handler);
    }

    public DoubleArrayCheck isEmpty() {
        return failureHandler().check(self(), actual().length == 0,
                "expected an empty double[] but had <%d> elements", actual().length);
    }

    public DoubleArrayCheck isNotEmpty() {
        return failureHandler().check(self(), actual().length != 0,
                "expected a non-empty double[]");
    }

    public DoubleArrayCheck hasLength(int expected) {
        return failureHandler().check(self(), actual().length == expected,
                "expected double[] length <%d> but was: <%d>", expected, actual().length);
    }

    public DoubleArrayCheck contains(double element) {
        boolean found = false;
        for (double e : actual()) {
            if (Double.compare(e, element) == 0) { found = true; break; }
        }
        return failureHandler().check(self(), found,
                "expected double[] to contain <%s> but was: %s",
                element, Arrays.toString(actual()));
    }

    public DoubleArrayCheck doesNotContain(double element) {
        for (double e : actual()) {
            if (Double.compare(e, element) == 0) {
                failureHandler().fail("expected double[] not to contain <%s>", element);
            }
        }
        return self();
    }

    public DoubleArrayCheck containsCloseTo(double expected, double tolerance) {
        boolean found = false;
        for (double e : actual()) {
            if (Math.abs(e - expected) <= tolerance) { found = true; break; }
        }
        return failureHandler().check(self(), found,
                "expected double[] to contain a value within <%s> of <%s> but was: %s",
                tolerance, expected, Arrays.toString(actual()));
    }

    public DoubleArrayCheck containsExactly(double... expected) {
        return failureHandler().check(self(), Arrays.equals(actual(), expected),
                "expected exactly %s but was: %s",
                Arrays.toString(expected), Arrays.toString(actual()));
    }

    public DoubleArrayCheck containsExactlyInAnyOrder(double... expected) {
        double[] sortedActual = actual().clone();
        double[] sortedExpected = expected.clone();
        Arrays.sort(sortedActual);
        Arrays.sort(sortedExpected);
        return failureHandler().check(self(), Arrays.equals(sortedActual, sortedExpected),
                "expected (in any order) %s but was: %s",
                Arrays.toString(expected), Arrays.toString(actual()));
    }

    public DoubleArrayCheck isSorted() {
        for (int i = 0; i < actual().length - 1; i++) {
            if (actual()[i] > actual()[i + 1]) {
                failureHandler().fail(
                        "expected sorted double[] but element at index <%d> (<%s>) > element at index <%d> (<%s>)",
                        i, actual()[i], i + 1, actual()[i + 1]);
            }
        }
        return self();
    }

    public DoubleArrayCheck allMatch(DoublePredicate predicate, String description) {
        for (double e : actual()) {
            if (!predicate.test(e)) {
                failureHandler().fail("expected all elements to match [%s] but <%s> did not",
                        description, e);
            }
        }
        return self();
    }
}
