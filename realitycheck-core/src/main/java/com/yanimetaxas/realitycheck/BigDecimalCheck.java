package com.yanimetaxas.realitycheck;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Fluent assertions for {@link BigDecimal} values.
 *
 * <pre>{@code
 * checkThat(price).isPositive().hasScale(2);
 * checkThat(total).isEqualByComparingTo(new BigDecimal("100.00"));
 * checkThat(amount).isCloseTo(new BigDecimal("3.14"), new BigDecimal("0.01"));
 * }</pre>
 */
public final class BigDecimalCheck extends OrderedCheck<BigDecimalCheck, BigDecimal> {

    BigDecimalCheck(BigDecimal actual, FailureHandler handler) {
        super(actual, handler);
    }

    public BigDecimalCheck isZero() {
        return failureHandler().check(self(), actual().signum() == 0,
                "expected zero but was: <%s>", actual());
    }

    public BigDecimalCheck isNotZero() {
        return failureHandler().check(self(), actual().signum() != 0,
                "expected a non-zero value");
    }

    public BigDecimalCheck isPositive() {
        return failureHandler().check(self(), actual().signum() > 0,
                "expected a positive value but was: <%s>", actual());
    }

    public BigDecimalCheck isNegative() {
        return failureHandler().check(self(), actual().signum() < 0,
                "expected a negative value but was: <%s>", actual());
    }

    /** Compares numerically, ignoring scale: {@code 1.0} equals {@code 1.00}. */
    public BigDecimalCheck isEqualByComparingTo(BigDecimal expected) {
        return failureHandler().check(self(), actual().compareTo(expected) == 0,
                "expected (by compareTo): <%s> but was: <%s>", expected, actual());
    }

    public BigDecimalCheck isCloseTo(BigDecimal expected, BigDecimal tolerance) {
        BigDecimal diff = actual().subtract(expected).abs();
        return failureHandler().check(self(), diff.compareTo(tolerance) <= 0,
                "expected <%s> within tolerance <%s> but was: <%s> (difference: %s)",
                expected, tolerance, actual(), diff);
    }

    public BigDecimalCheck hasScale(int expectedScale) {
        return failureHandler().check(self(), actual().scale() == expectedScale,
                "expected scale <%d> but was: <%d>", expectedScale, actual().scale());
    }

    public BigDecimalCheck hasPrecision(int expectedPrecision) {
        return failureHandler().check(self(), actual().precision() == expectedPrecision,
                "expected precision <%d> but was: <%d>", expectedPrecision, actual().precision());
    }

    public BigDecimalCheck hasUnscaledValue(BigInteger expected) {
        return failureHandler().check(self(), actual().unscaledValue().equals(expected),
                "expected unscaled value <%s> but was: <%s>", expected, actual().unscaledValue());
    }
}
