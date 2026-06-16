package com.yanimetaxas.realitycheck;

/**
 * Base class for temporal check types. Extends {@link OrderedCheck} to inherit
 * comparison methods, and adds temporal-specific assertions (before/after) with
 * time-oriented error messages.
 *
 * @param <SELF> the concrete check type (for fluent chaining)
 * @param <T>    the temporal value type (must be {@link Comparable})
 */
public abstract class AbstractTemporalCheck<SELF extends AbstractTemporalCheck<SELF, T>, T extends Comparable<? super T>>
        extends OrderedCheck<SELF, T> {

    protected AbstractTemporalCheck(T actual, FailureHandler failureHandler) {
        super(actual, failureHandler);
    }

    public SELF isBefore(T other) {
        return failureHandler().check(self(), actual().compareTo(other) < 0,
                "expected <%s> to be before <%s>", actual(), other);
    }

    public SELF isAfter(T other) {
        return failureHandler().check(self(), actual().compareTo(other) > 0,
                "expected <%s> to be after <%s>", actual(), other);
    }

    public SELF isBeforeOrEqualTo(T other) {
        return failureHandler().check(self(), actual().compareTo(other) <= 0,
                "expected <%s> to be before or equal to <%s>", actual(), other);
    }

    public SELF isAfterOrEqualTo(T other) {
        return failureHandler().check(self(), actual().compareTo(other) >= 0,
                "expected <%s> to be after or equal to <%s>", actual(), other);
    }

    @Override
    public SELF isBetween(T startInclusive, T endInclusive) {
        return failureHandler().check(self(),
                actual().compareTo(startInclusive) >= 0 && actual().compareTo(endInclusive) <= 0,
                "expected <%s> to be between <%s> and <%s>",
                actual(), startInclusive, endInclusive);
    }
}
