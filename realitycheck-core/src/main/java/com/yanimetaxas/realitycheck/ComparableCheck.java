package com.yanimetaxas.realitycheck;

/**
 * Fluent assertions for {@link Comparable} values.
 *
 * @param <T> the comparable type
 */
public final class ComparableCheck<T extends Comparable<T>>
        extends OrderedCheck<ComparableCheck<T>, T> {

    ComparableCheck(T actual, FailureHandler handler) {
        super(actual, handler);
    }
}
