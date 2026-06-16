package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class BigIntegerCheckTest {

    @Test
    void isZero_passes() {
        assertDoesNotThrow(() -> checkThat(BigInteger.ZERO).isZero());
    }

    @Test
    void isPositive_passes() {
        assertDoesNotThrow(() -> checkThat(BigInteger.TEN).isPositive());
    }

    @Test
    void isNegative_passes() {
        assertDoesNotThrow(() -> checkThat(BigInteger.valueOf(-5)).isNegative());
    }

    @Test
    void isGreaterThan_passes() {
        assertDoesNotThrow(() ->
                checkThat(BigInteger.TEN).isGreaterThan(BigInteger.ONE));
    }

    @Test
    void isLessThan_passes() {
        assertDoesNotThrow(() ->
                checkThat(BigInteger.ONE).isLessThan(BigInteger.TEN));
    }

    @Test
    void isBetween_passes() {
        assertDoesNotThrow(() ->
                checkThat(BigInteger.valueOf(5)).isBetween(BigInteger.ONE, BigInteger.TEN));
    }

    @Test
    void isEven_passes() {
        assertDoesNotThrow(() -> checkThat(BigInteger.valueOf(42)).isEven());
    }

    @Test
    void isEven_fails() {
        assertThrows(AssertionError.class, () -> checkThat(BigInteger.valueOf(7)).isEven());
    }

    @Test
    void isOdd_passes() {
        assertDoesNotThrow(() -> checkThat(BigInteger.valueOf(7)).isOdd());
    }

    @Test
    void isOdd_fails() {
        assertThrows(AssertionError.class, () -> checkThat(BigInteger.valueOf(42)).isOdd());
    }

    @Test
    void isProbablePrime_passes() {
        assertDoesNotThrow(() -> checkThat(BigInteger.valueOf(17)).isProbablePrime());
    }

    @Test
    void isProbablePrime_fails() {
        assertThrows(AssertionError.class, () -> checkThat(BigInteger.valueOf(15)).isProbablePrime());
    }

    @Test
    void hasBitLength_passes() {
        assertDoesNotThrow(() -> checkThat(BigInteger.valueOf(255)).hasBitLength(8));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() ->
                checkThat(BigInteger.valueOf(17))
                        .isPositive()
                        .isOdd()
                        .isProbablePrime());
    }

    @Test
    void hasBitLength_pass() {
        assertDoesNotThrow(() ->
                checkThat(BigInteger.valueOf(255)).hasBitLength(8));
    }

    @Test
    void hasBitLength_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(BigInteger.valueOf(255)).hasBitLength(16));
    }

    @Test
    void isOdd_pass() {
        assertDoesNotThrow(() -> checkThat(BigInteger.valueOf(3)).isOdd());
    }

    @Test
    void isOdd_fail_even() {
        assertThrows(AssertionError.class, () ->
                checkThat(BigInteger.valueOf(4)).isOdd());
    }

    @Test
    void isProbablePrime_pass() {
        assertDoesNotThrow(() -> checkThat(BigInteger.valueOf(17)).isProbablePrime());
    }

    @Test
    void isProbablePrime_fail_composite() {
        assertThrows(AssertionError.class, () ->
                checkThat(BigInteger.valueOf(15)).isProbablePrime());
    }

    @Test
    void isEven_pass() {
        assertDoesNotThrow(() -> checkThat(BigInteger.valueOf(4)).isEven());
    }

    @Test
    void isEven_fail_odd() {
        assertThrows(AssertionError.class, () ->
                checkThat(BigInteger.valueOf(3)).isEven());
    }

    @Test
    void isBetween_pass() {
        assertDoesNotThrow(() ->
                checkThat(BigInteger.TEN).isBetween(BigInteger.ONE, BigInteger.valueOf(100)));
    }

    @Test
    void isBetween_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(BigInteger.ZERO).isBetween(BigInteger.ONE, BigInteger.TEN));
    }

    @Test
    void isPositive_fail_zero() {
        assertThrows(AssertionError.class, () ->
                checkThat(BigInteger.ZERO).isPositive());
    }

    @Test
    void isNegative_fail_zero() {
        assertThrows(AssertionError.class, () ->
                checkThat(BigInteger.ZERO).isNegative());
    }

    @Test
    void isZero_failsOnNonZero() {
        assertThrows(AssertionError.class,
                () -> checkThat(BigInteger.ONE).isZero());
    }

    @Test
    void isNotZero_failsOnZero() {
        assertThrows(AssertionError.class,
                () -> checkThat(BigInteger.ZERO).isNotZero());
    }

    @Test
    void isPositive_failsOnZero() {
        assertThrows(AssertionError.class,
                () -> checkThat(BigInteger.ZERO).isPositive());
    }

    @Test
    void isNegative_failsOnPositive() {
        assertThrows(AssertionError.class,
                () -> checkThat(BigInteger.ONE).isNegative());
    }

    @Test
    void isGreaterThan_failsWhenLessOrEqual() {
        assertThrows(AssertionError.class,
                () -> checkThat(BigInteger.ONE).isGreaterThan(BigInteger.TEN));
    }

    @Test
    void isLessThan_failsWhenGreaterOrEqual() {
        assertThrows(AssertionError.class,
                () -> checkThat(BigInteger.TEN).isLessThan(BigInteger.ONE));
    }

    @Test
    void isBetween_failsWhenOutside() {
        assertThrows(AssertionError.class,
                () -> checkThat(BigInteger.valueOf(50)).isBetween(BigInteger.ONE, BigInteger.TEN));
    }

    @Test
    void isEven_failsOnOdd() {
        assertThrows(AssertionError.class,
                () -> checkThat(BigInteger.valueOf(7)).isEven());
    }

    @Test
    void isOdd_failsOnEven() {
        assertThrows(AssertionError.class,
                () -> checkThat(BigInteger.valueOf(42)).isOdd());
    }

    @Test
    void isProbablePrime_failsOnComposite() {
        assertThrows(AssertionError.class,
                () -> checkThat(BigInteger.valueOf(100)).isProbablePrime());
    }

    @Test
    void hasBitLength_failsOnWrongLength() {
        assertThrows(AssertionError.class,
                () -> checkThat(BigInteger.valueOf(255)).hasBitLength(16));
    }
}
