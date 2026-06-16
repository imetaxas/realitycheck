package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ComparableCheckTest {

    @Test
    void isGreaterThan_passes() {
        assertDoesNotThrow(() -> checkThatComparable("c").isGreaterThan("b"));
    }

    @Test
    void isGreaterThan_fails() {
        assertThrows(AssertionError.class, () -> checkThatComparable("a").isGreaterThan("b"));
    }

    @Test
    void isLessThan_passes() {
        assertDoesNotThrow(() -> checkThatComparable("a").isLessThan("b"));
    }

    @Test
    void isLessThan_fails() {
        assertThrows(AssertionError.class, () -> checkThatComparable("c").isLessThan("b"));
    }

    @Test
    void isGreaterThanOrEqualTo_passes_when_greater() {
        assertDoesNotThrow(() -> checkThatComparable("z").isGreaterThanOrEqualTo("a"));
    }

    @Test
    void isGreaterThanOrEqualTo_passes_when_equal() {
        assertDoesNotThrow(() -> checkThatComparable("same").isGreaterThanOrEqualTo("same"));
    }

    @Test
    void isGreaterThanOrEqualTo_fails() {
        assertThrows(AssertionError.class,
                () -> checkThatComparable("a").isGreaterThanOrEqualTo("z"));
    }

    @Test
    void isLessThanOrEqualTo_passes_when_less() {
        assertDoesNotThrow(() -> checkThatComparable("a").isLessThanOrEqualTo("z"));
    }

    @Test
    void isLessThanOrEqualTo_passes_when_equal() {
        assertDoesNotThrow(() -> checkThatComparable("same").isLessThanOrEqualTo("same"));
    }

    @Test
    void isLessThanOrEqualTo_fails() {
        assertThrows(AssertionError.class,
                () -> checkThatComparable("z").isLessThanOrEqualTo("a"));
    }

    @Test
    void isBetween_passes() {
        assertDoesNotThrow(() -> checkThatComparable("m").isBetween("a", "z"));
    }

    @Test
    void isBetween_fails() {
        assertThrows(AssertionError.class,
                () -> checkThatComparable("zzz").isBetween("a", "m"));
    }

    @Test
    void isAtLeast_passes() {
        assertDoesNotThrow(() -> new ComparableCheck<>("b", new FailureHandler()).isAtLeast("a"));
    }

    @Test
    void isAtMost_passes() {
        assertDoesNotThrow(() -> new ComparableCheck<>("b", new FailureHandler()).isAtMost("z"));
    }

    @Test
    void isIn_passes() {
        assertDoesNotThrow(() -> checkThatComparable("middle").isIn("a", "z"));
    }

    @Test
    void isAtLeast_fails() {
        assertThrows(AssertionError.class,
                () -> checkThatComparable("a").isAtLeast("z"));
    }

    @Test
    void isAtMost_fails() {
        assertThrows(AssertionError.class,
                () -> checkThatComparable("z").isAtMost("a"));
    }

    @Test
    void isIn_fails() {
        assertThrows(AssertionError.class,
                () -> checkThatComparable("zzz").isIn("a", "m"));
    }
}
