package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class NumberCheckTest {

    @Test
    void isPositive_passes() {
        assertDoesNotThrow(() -> checkThat(42).isPositive());
    }

    @Test
    void isPositive_failsForNegative() {
        var e = assertThrows(AssertionError.class, () -> checkThat(-1).isPositive());
        assertTrue(e.getMessage().contains("-1"));
    }

    @Test
    void isNegative_passes() {
        assertDoesNotThrow(() -> checkThat(-5).isNegative());
    }

    @Test
    void isZero_passes() {
        assertDoesNotThrow(() -> checkThat(0).isZero());
    }

    @Test
    void isNotZero_passes() {
        assertDoesNotThrow(() -> checkThat(1).isNotZero());
    }

    @Test
    void isGreaterThan_passes() {
        assertDoesNotThrow(() -> checkThat(10).isGreaterThan(5));
    }

    @Test
    void isGreaterThan_failsWithBothValues() {
        var e = assertThrows(AssertionError.class, () -> checkThat(3).isGreaterThan(5));
        assertTrue(e.getMessage().contains("5"));
        assertTrue(e.getMessage().contains("3"));
    }

    @Test
    void isLessThan_passes() {
        assertDoesNotThrow(() -> checkThat(3).isLessThan(5));
    }

    @Test
    void isBetween_passes() {
        assertDoesNotThrow(() -> checkThat(5).isBetween(1, 10));
    }

    @Test
    void isBetween_failsOutOfRange() {
        assertThrows(AssertionError.class, () -> checkThat(15).isBetween(1, 10));
    }

    @Test
    void isEqualTo_passes() {
        assertDoesNotThrow(() -> checkThat(42).isEqualTo(42));
    }

    @Test
    void isNotEqualTo_passes() {
        assertDoesNotThrow(() -> checkThat(42).isNotEqualTo(43));
    }

    @Test
    void isCloseTo_passes() {
        assertDoesNotThrow(() -> checkThat(3.14).isCloseTo(3.15, 0.02));
    }

    @Test
    void isCloseTo_fails() {
        assertThrows(AssertionError.class, () -> checkThat(3.14).isCloseTo(4.0, 0.01));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() -> checkThat(42).isNotNull().isPositive().isGreaterThan(0).isBetween(1, 100));
    }

    @Test
    void longValues_work() {
        assertDoesNotThrow(() -> checkThat(100L).isPositive().isGreaterThan(50L));
    }

    @Test
    void doubleValues_work() {
        assertDoesNotThrow(() -> checkThat(3.14).isPositive().isLessThan(4.0));
    }

    @Test
    void isBetween_pass() {
        assertDoesNotThrow(() -> checkThat(5).isBetween(1, 10));
    }

    @Test
    void isBetween_fail_below() {
        assertThrows(AssertionError.class, () -> checkThat(0).isBetween(1, 10));
    }

    @Test
    void isBetween_fail_above() {
        assertThrows(AssertionError.class, () -> checkThat(11).isBetween(1, 10));
    }

    @Test
    void isCloseTo_pass() {
        assertDoesNotThrow(() -> checkThat(10).isCloseTo(11, 2));
    }

    @Test
    void isCloseTo_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(10).isCloseTo(20, 1));
    }

    @Test
    void isAtLeast_pass() {
        assertDoesNotThrow(() -> checkThat(10).isAtLeast(10));
    }

    @Test
    void isAtLeast_fail() {
        assertThrows(AssertionError.class, () -> checkThat(5).isAtLeast(10));
    }

    @Test
    void isAtMost_pass() {
        assertDoesNotThrow(() -> checkThat(5).isAtMost(10));
    }

    @Test
    void isAtMost_fail() {
        assertThrows(AssertionError.class, () -> checkThat(15).isAtMost(10));
    }

    @Test
    void isIn_alias() {
        assertDoesNotThrow(() -> checkThat(5).isIn(1, 10));
    }

    @Test
    void isIn_alias_fail() {
        assertThrows(AssertionError.class, () -> checkThat(15).isIn(1, 10));
    }

    @Test
    void isGreaterThanOrEqualTo_pass() {
        assertDoesNotThrow(() -> checkThat(10).isGreaterThanOrEqualTo(10));
    }

    @Test
    void isGreaterThanOrEqualTo_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(5).isGreaterThanOrEqualTo(10));
    }

    @Test
    void isLessThanOrEqualTo_pass() {
        assertDoesNotThrow(() -> checkThat(10).isLessThanOrEqualTo(10));
    }

    @Test
    void isLessThanOrEqualTo_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(15).isLessThanOrEqualTo(10));
    }

    @Test
    void isNegative_failsOnPositive() {
        assertThrows(AssertionError.class, () -> checkThat(5).isNegative());
    }

    @Test
    void isNegative_failsOnZero() {
        assertThrows(AssertionError.class, () -> checkThat(0).isNegative());
    }

    @Test
    void isZero_failsOnNonZero() {
        assertThrows(AssertionError.class, () -> checkThat(7).isZero());
    }

    @Test
    void isNotZero_failsOnZero() {
        assertThrows(AssertionError.class, () -> checkThat(0).isNotZero());
    }

    @Test
    void isPositive_failsOnZero() {
        assertThrows(AssertionError.class, () -> checkThat(0).isPositive());
    }

    @Test
    void isLessThan_fails() {
        assertThrows(AssertionError.class, () -> checkThat(10).isLessThan(5));
    }

    @Test
    void isLessThan_failsOnEqual() {
        assertThrows(AssertionError.class, () -> checkThat(5).isLessThan(5));
    }

    @Test
    void isGreaterThan_failsOnEqual() {
        assertThrows(AssertionError.class, () -> checkThat(5).isGreaterThan(5));
    }

    @Test
    void isPositive_worksForLargeLong() {
        long beyondDoublePrecision = (1L << 53) + 1;
        assertDoesNotThrow(() -> checkThat(beyondDoublePrecision).isPositive());
    }

    @Test
    void isZero_worksForLong() {
        assertDoesNotThrow(() -> checkThat(0L).isZero());
    }

    @Test
    void isNegative_worksForLong() {
        assertDoesNotThrow(() -> checkThat(-1L).isNegative());
    }

    @Test
    void isPositive_worksForBigDecimal() {
        assertDoesNotThrow(
                () -> checkThat(new BigDecimal("9999999999999999999.999")).isPositive());
    }

    @Test
    void isZero_worksForBigDecimal() {
        assertDoesNotThrow(() -> checkThat(BigDecimal.ZERO).isZero());
    }

    @Test
    void isNegative_worksForBigDecimal() {
        assertDoesNotThrow(
                () -> checkThat(new BigDecimal("-0.001")).isNegative());
    }

    @Test
    void isPositive_worksForBigInteger() {
        BigInteger big = BigInteger.TWO.pow(100);
        assertDoesNotThrow(() -> checkThat(big).isPositive());
    }

    @Test
    void isZero_worksForBigInteger() {
        assertDoesNotThrow(() -> checkThat(BigInteger.ZERO).isZero());
    }

    @Test
    void isNotZero_worksForBigInteger() {
        assertDoesNotThrow(() -> checkThat(BigInteger.ONE).isNotZero());
    }
}
