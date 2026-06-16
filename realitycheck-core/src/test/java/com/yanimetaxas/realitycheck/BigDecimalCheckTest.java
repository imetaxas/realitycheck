package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class BigDecimalCheckTest {

    @Test
    void isZero_passes() {
        assertDoesNotThrow(() -> checkThat(BigDecimal.ZERO).isZero());
    }

    @Test
    void isZero_fails() {
        assertThrows(AssertionError.class, () -> checkThat(BigDecimal.ONE).isZero());
    }

    @Test
    void isPositive_passes() {
        assertDoesNotThrow(() -> checkThat(new BigDecimal("3.14")).isPositive());
    }

    @Test
    void isNegative_passes() {
        assertDoesNotThrow(() -> checkThat(new BigDecimal("-5.0")).isNegative());
    }

    @Test
    void isGreaterThan_passes() {
        assertDoesNotThrow(() ->
                checkThat(new BigDecimal("10")).isGreaterThan(new BigDecimal("5")));
    }

    @Test
    void isLessThan_passes() {
        assertDoesNotThrow(() ->
                checkThat(new BigDecimal("3")).isLessThan(new BigDecimal("10")));
    }

    @Test
    void isBetween_passes() {
        assertDoesNotThrow(() ->
                checkThat(new BigDecimal("5")).isBetween(new BigDecimal("1"), new BigDecimal("10")));
    }

    @Test
    void isEqualByComparingTo_ignoresScale() {
        assertDoesNotThrow(() ->
                checkThat(new BigDecimal("1.0")).isEqualByComparingTo(new BigDecimal("1.00")));
    }

    @Test
    void isEqualByComparingTo_fails() {
        assertThrows(AssertionError.class, () ->
                checkThat(new BigDecimal("1.0")).isEqualByComparingTo(new BigDecimal("2.0")));
    }

    @Test
    void isCloseTo_passes() {
        assertDoesNotThrow(() ->
                checkThat(new BigDecimal("3.14")).isCloseTo(
                        new BigDecimal("3.15"), new BigDecimal("0.02")));
    }

    @Test
    void isCloseTo_fails() {
        assertThrows(AssertionError.class, () ->
                checkThat(new BigDecimal("3.14")).isCloseTo(
                        new BigDecimal("5.0"), new BigDecimal("0.01")));
    }

    @Test
    void hasScale_passes() {
        assertDoesNotThrow(() -> checkThat(new BigDecimal("12.34")).hasScale(2));
    }

    @Test
    void hasPrecision_passes() {
        assertDoesNotThrow(() -> checkThat(new BigDecimal("12.34")).hasPrecision(4));
    }

    @Test
    void hasUnscaledValue_passes() {
        assertDoesNotThrow(() ->
                checkThat(new BigDecimal("12.34")).hasUnscaledValue(BigInteger.valueOf(1234)));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() ->
                checkThat(new BigDecimal("99.99"))
                        .isPositive()
                        .hasScale(2)
                        .isLessThan(new BigDecimal("100")));
    }

    @Test
    void isGreaterThanOrEqualTo_pass_equal() {
        assertDoesNotThrow(() ->
                checkThat(new BigDecimal("5")).isGreaterThanOrEqualTo(new BigDecimal("5")));
    }

    @Test
    void isGreaterThanOrEqualTo_pass_greater() {
        assertDoesNotThrow(() ->
                checkThat(new BigDecimal("6")).isGreaterThanOrEqualTo(new BigDecimal("5")));
    }

    @Test
    void isGreaterThanOrEqualTo_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new BigDecimal("4")).isGreaterThanOrEqualTo(new BigDecimal("5")));
    }

    @Test
    void isLessThanOrEqualTo_pass_equal() {
        assertDoesNotThrow(() ->
                checkThat(new BigDecimal("5")).isLessThanOrEqualTo(new BigDecimal("5")));
    }

    @Test
    void isLessThanOrEqualTo_pass_less() {
        assertDoesNotThrow(() ->
                checkThat(new BigDecimal("4")).isLessThanOrEqualTo(new BigDecimal("5")));
    }

    @Test
    void isLessThanOrEqualTo_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new BigDecimal("6")).isLessThanOrEqualTo(new BigDecimal("5")));
    }

    @Test
    void isPositive_failsOnZero() {
        assertThrows(AssertionError.class, () ->
                checkThat(BigDecimal.ZERO).isPositive());
    }

    @Test
    void isPositive_failsOnNegative() {
        assertThrows(AssertionError.class, () ->
                checkThat(new BigDecimal("-1")).isPositive());
    }

    @Test
    void isNegative_failsOnZero() {
        assertThrows(AssertionError.class, () ->
                checkThat(BigDecimal.ZERO).isNegative());
    }

    @Test
    void isNegative_failsOnPositive() {
        assertThrows(AssertionError.class, () ->
                checkThat(new BigDecimal("1")).isNegative());
    }

    @Test
    void isBetween_pass() {
        assertDoesNotThrow(() ->
                checkThat(new BigDecimal("5")).isBetween(new BigDecimal("1"), new BigDecimal("10")));
    }

    @Test
    void isBetween_fail_below() {
        assertThrows(AssertionError.class, () ->
                checkThat(new BigDecimal("0")).isBetween(new BigDecimal("1"), new BigDecimal("10")));
    }

    @Test
    void isBetween_fail_above() {
        assertThrows(AssertionError.class, () ->
                checkThat(new BigDecimal("11")).isBetween(new BigDecimal("1"), new BigDecimal("10")));
    }

    @Test
    void isCloseTo_pass() {
        assertDoesNotThrow(() ->
                checkThat(new BigDecimal("3.14")).isCloseTo(new BigDecimal("3.15"), new BigDecimal("0.02")));
    }

    @Test
    void isCloseTo_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new BigDecimal("3.14")).isCloseTo(new BigDecimal("5.00"), new BigDecimal("0.01")));
    }

    @Test
    void hasScale_pass() {
        assertDoesNotThrow(() -> checkThat(new BigDecimal("1.00")).hasScale(2));
    }

    @Test
    void hasScale_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new BigDecimal("1.0")).hasScale(3));
    }

    @Test
    void hasPrecision_pass() {
        assertDoesNotThrow(() -> checkThat(new BigDecimal("123")).hasPrecision(3));
    }

    @Test
    void hasPrecision_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new BigDecimal("123")).hasPrecision(5));
    }

    @Test
    void hasUnscaledValue_pass() {
        assertDoesNotThrow(() ->
                checkThat(new BigDecimal("1.23")).hasUnscaledValue(BigInteger.valueOf(123)));
    }

    @Test
    void hasUnscaledValue_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new BigDecimal("1.23")).hasUnscaledValue(BigInteger.valueOf(999)));
    }

    @Test
    void isEqualByComparingTo_pass() {
        assertDoesNotThrow(() ->
                checkThat(new BigDecimal("1.0")).isEqualByComparingTo(new BigDecimal("1.00")));
    }

    @Test
    void isEqualByComparingTo_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new BigDecimal("1.0")).isEqualByComparingTo(new BigDecimal("2.0")));
    }

    @Test
    void isGreaterThan_pass() {
        assertDoesNotThrow(() -> checkThat(new BigDecimal("10")).isGreaterThan(new BigDecimal("5")));
    }

    @Test
    void isGreaterThan_fail_equal() {
        assertThrows(AssertionError.class, () ->
                checkThat(new BigDecimal("5")).isGreaterThan(new BigDecimal("5")));
    }

    @Test
    void isLessThan_pass() {
        assertDoesNotThrow(() -> checkThat(new BigDecimal("3")).isLessThan(new BigDecimal("5")));
    }

    @Test
    void isLessThan_fail_equal() {
        assertThrows(AssertionError.class, () ->
                checkThat(new BigDecimal("5")).isLessThan(new BigDecimal("5")));
    }

    @Test
    void isZero_failsOnNonZero() {
        assertThrows(AssertionError.class,
                () -> checkThat(BigDecimal.ONE).isZero());
    }

    @Test
    void isNotZero_failsOnZero() {
        assertThrows(AssertionError.class,
                () -> checkThat(BigDecimal.ZERO).isNotZero());
    }

    @Test
    void isGreaterThan_failsWhenLessOrEqual() {
        assertThrows(AssertionError.class,
                () -> checkThat(new BigDecimal("5")).isGreaterThan(new BigDecimal("10")));
    }

    @Test
    void isLessThan_failsWhenGreaterOrEqual() {
        assertThrows(AssertionError.class,
                () -> checkThat(new BigDecimal("10")).isLessThan(new BigDecimal("5")));
    }

    @Test
    void isGreaterThanOrEqualTo_failsWhenLess() {
        assertThrows(AssertionError.class,
                () -> checkThat(new BigDecimal("3")).isGreaterThanOrEqualTo(new BigDecimal("5")));
    }

    @Test
    void isLessThanOrEqualTo_failsWhenGreater() {
        assertThrows(AssertionError.class,
                () -> checkThat(new BigDecimal("10")).isLessThanOrEqualTo(new BigDecimal("5")));
    }

    @Test
    void isBetween_failsWhenOutside() {
        assertThrows(AssertionError.class,
                () -> checkThat(new BigDecimal("50")).isBetween(new BigDecimal("1"), new BigDecimal("10")));
    }

    @Test
    void isEqualByComparingTo_failsOnDifferent() {
        assertThrows(AssertionError.class,
                () -> checkThat(new BigDecimal("1")).isEqualByComparingTo(new BigDecimal("2")));
    }

    @Test
    void isCloseTo_failsOutsideTolerance() {
        assertThrows(AssertionError.class,
                () -> checkThat(new BigDecimal("1")).isCloseTo(new BigDecimal("100"), new BigDecimal("0.01")));
    }

    @Test
    void hasScale_failsOnWrongScale() {
        assertThrows(AssertionError.class,
                () -> checkThat(new BigDecimal("1.23")).hasScale(0));
    }

    @Test
    void hasPrecision_failsOnWrongPrecision() {
        assertThrows(AssertionError.class,
                () -> checkThat(new BigDecimal("1.23")).hasPrecision(10));
    }

    @Test
    void hasUnscaledValue_failsOnWrongValue() {
        assertThrows(AssertionError.class,
                () -> checkThat(new BigDecimal("1.23")).hasUnscaledValue(BigInteger.valueOf(999)));
    }
}
