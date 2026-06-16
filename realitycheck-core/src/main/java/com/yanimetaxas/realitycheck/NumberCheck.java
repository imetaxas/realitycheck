package com.yanimetaxas.realitycheck;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Fluent assertions for numeric values ({@code int}, {@code long}, {@code double}, etc.).
 *
 * @param <T> the boxed number type
 */
public final class NumberCheck<T extends Number & Comparable<T>>
        extends OrderedCheck<NumberCheck<T>, T> {

    NumberCheck(T actual, FailureHandler handler) {
        super(actual, handler);
    }

    public NumberCheck<T> isZero() {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), signum() == 0,
                "expected zero but was: <%s>", actual());
    }

    public NumberCheck<T> isNotZero() {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), signum() != 0,
                "expected a non-zero value");
    }

    public NumberCheck<T> isPositive() {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), signum() > 0,
                "expected a positive number but was: <%s>", actual());
    }

    public NumberCheck<T> isNegative() {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), signum() < 0,
                "expected a negative number but was: <%s>", actual());
    }

    private int signum() {
        if (actual() instanceof BigDecimal bd) return bd.signum();
        if (actual() instanceof BigInteger bi) return bi.signum();
        if (actual() instanceof Long l) return Long.compare(l, 0L);
        if (actual() instanceof Integer i) return Integer.compare(i, 0);
        return Double.compare(actual().doubleValue(), 0.0);
    }

    public NumberCheck<T> isCloseTo(T expected, T tolerance) {
        if (!isActualPresent()) return self();
        double diff = Math.abs(actual().doubleValue() - expected.doubleValue());
        return failureHandler().check(self(), diff <= tolerance.doubleValue(),
                "expected <%s> within tolerance <%s> but was: <%s> (difference: %s)",
                expected, tolerance, actual(), diff);
    }
}
