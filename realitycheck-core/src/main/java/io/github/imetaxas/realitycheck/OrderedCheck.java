package io.github.imetaxas.realitycheck;

/**
 * Base class for checks on {@link Comparable} values. Provides ordering assertions
 * (greater-than, less-than, between, etc.) using {@code compareTo} semantics.
 *
 * @param <SELF> the concrete check type (for fluent chaining)
 * @param <T>    the comparable value type
 */
public abstract class OrderedCheck<SELF extends OrderedCheck<SELF, T>, T extends Comparable<? super T>>
        extends AbstractCheck<SELF, T> {

    protected OrderedCheck(T actual, FailureHandler failureHandler) {
        super(actual, failureHandler);
    }

    public SELF isGreaterThan(T other) {
        return failureHandler().check(self(), actual().compareTo(other) > 0,
                "expected a value greater than <%s> but was: <%s>", other, actual());
    }

    public SELF isLessThan(T other) {
        return failureHandler().check(self(), actual().compareTo(other) < 0,
                "expected a value less than <%s> but was: <%s>", other, actual());
    }

    public SELF isGreaterThanOrEqualTo(T other) {
        return failureHandler().check(self(), actual().compareTo(other) >= 0,
                "expected a value >= <%s> but was: <%s>", other, actual());
    }

    public SELF isLessThanOrEqualTo(T other) {
        return failureHandler().check(self(), actual().compareTo(other) <= 0,
                "expected a value <= <%s> but was: <%s>", other, actual());
    }

    public SELF isBetween(T startInclusive, T endInclusive) {
        return failureHandler().check(self(),
                actual().compareTo(startInclusive) >= 0 && actual().compareTo(endInclusive) <= 0,
                "expected a value between <%s> and <%s> but was: <%s>",
                startInclusive, endInclusive, actual());
    }

    /** Alias for {@link #isGreaterThanOrEqualTo}. */
    public SELF isAtLeast(T other) {
        return isGreaterThanOrEqualTo(other);
    }

    /** Alias for {@link #isLessThanOrEqualTo}. */
    public SELF isAtMost(T other) {
        return isLessThanOrEqualTo(other);
    }

    /** Alias for {@link #isBetween}. */
    public SELF isIn(T startInclusive, T endInclusive) {
        return isBetween(startInclusive, endInclusive);
    }
}
