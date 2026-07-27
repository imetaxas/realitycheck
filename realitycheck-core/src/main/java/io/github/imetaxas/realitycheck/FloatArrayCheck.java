package io.github.imetaxas.realitycheck;

import java.util.Arrays;
import java.util.function.DoublePredicate;

/**
 * Fluent assertions for {@code float[]} arrays.
 *
 * <pre>{@code
 * checkThat(new float[]{1.0f, 2.0f, 3.0f}).hasLength(3).contains(2.0f).isSorted();
 * checkThat(scores).allMatch(s -> s >= 0, "non-negative");
 * }</pre>
 */
public final class FloatArrayCheck extends AbstractCheck<FloatArrayCheck, float[]> {

    FloatArrayCheck(float[] actual, FailureHandler handler) {
        super(actual, handler);
    }

    public FloatArrayCheck isEmpty() {
        return failureHandler().check(self(), actual().length == 0,
                "expected an empty float[] but had <%d> elements", actual().length);
    }

    public FloatArrayCheck isNotEmpty() {
        return failureHandler().check(self(), actual().length != 0,
                "expected a non-empty float[]");
    }

    public FloatArrayCheck hasLength(int expected) {
        return failureHandler().check(self(), actual().length == expected,
                "expected float[] length <%d> but was: <%d>", expected, actual().length);
    }

    public FloatArrayCheck contains(float element) {
        boolean found = false;
        for (float e : actual()) {
            if (Float.compare(e, element) == 0) { found = true; break; }
        }
        return failureHandler().check(self(), found,
                "expected float[] to contain <%s> but was: %s",
                element, Arrays.toString(actual()));
    }

    public FloatArrayCheck doesNotContain(float element) {
        for (float e : actual()) {
            if (Float.compare(e, element) == 0) {
                failureHandler().fail("expected float[] not to contain <%s>", element);
            }
        }
        return self();
    }

    public FloatArrayCheck containsExactly(float... expected) {
        return failureHandler().check(self(), Arrays.equals(actual(), expected),
                "expected exactly %s but was: %s",
                Arrays.toString(expected), Arrays.toString(actual()));
    }

    public FloatArrayCheck isSorted() {
        for (int i = 0; i < actual().length - 1; i++) {
            if (actual()[i] > actual()[i + 1]) {
                failureHandler().fail(
                        "expected sorted float[] but element at index <%d> (<%s>) > element at index <%d> (<%s>)",
                        i, actual()[i], i + 1, actual()[i + 1]);
            }
        }
        return self();
    }

    /**
     * Asserts that all elements satisfy the given predicate.
     *
     * @param predicate   the condition each element must meet
     * @param description a human-readable label for the predicate (used in the failure message)
     */
    public FloatArrayCheck allMatch(DoublePredicate predicate, String description) {
        for (float e : actual()) {
            if (!predicate.test(e)) {
                failureHandler().fail("expected all elements to match [%s] but <%s> did not",
                        description, e);
            }
        }
        return self();
    }

    /**
     * Asserts that all elements satisfy the given predicate (no-label overload).
     */
    public FloatArrayCheck allMatch(DoublePredicate predicate) {
        return allMatch(predicate, "predicate");
    }

    /**
     * Asserts that at least one element satisfies the given predicate.
     *
     * @param predicate   the condition to test
     * @param description a human-readable label for the predicate (used in the failure message)
     */
    public FloatArrayCheck anyMatch(DoublePredicate predicate, String description) {
        boolean found = false;
        for (float e : actual()) {
            if (predicate.test(e)) { found = true; break; }
        }
        return failureHandler().check(self(), found,
                "expected at least one element matching [%s] but none did", description);
    }

    /**
     * Asserts that at least one element satisfies the given predicate (no-label overload).
     */
    public FloatArrayCheck anyMatch(DoublePredicate predicate) {
        return anyMatch(predicate, "predicate");
    }
}
