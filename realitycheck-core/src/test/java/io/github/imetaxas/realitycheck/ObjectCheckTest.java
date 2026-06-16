package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ObjectCheckTest {

    @Test
    void hasToString_passes() {
        assertDoesNotThrow(() -> checkThatObject(Integer.valueOf(5)).hasToString("5"));
    }

    @Test
    void hasToString_fails() {
        assertThrows(AssertionError.class, () -> checkThatObject(5).hasToString("six"));
    }

    @Test
    void hasSameHashCodeAs_passes() {
        assertDoesNotThrow(() -> checkThatObject(Integer.valueOf(42)).hasSameHashCodeAs(Integer.valueOf(42)));
    }

    @Test
    void hasSameHashCodeAs_fails() {
        assertThrows(AssertionError.class, () -> checkThatObject(1).hasSameHashCodeAs(2));
    }

    @Test
    void hasSameHashCodeAs_bothNullBehavesLikeZeroHash() {
        assertDoesNotThrow(() -> checkThatObject(null).hasSameHashCodeAs(null));
    }

    @Test
    void isNull_passes() {
        assertDoesNotThrow(() -> checkThatObject(null).isNull());
    }

    @Test
    void isNull_fails() {
        assertThrows(AssertionError.class, () -> checkThatObject("x").isNull());
    }

    @Test
    void isNotNull_passes() {
        assertDoesNotThrow(() -> checkThatObject("x").isNotNull());
    }

    @Test
    void isNotNull_fails() {
        assertThrows(AssertionError.class, () -> checkThatObject(null).isNotNull());
    }

    @Test
    void isEqualTo_passes() {
        assertDoesNotThrow(() -> checkThatObject("a").isEqualTo("a"));
    }

    @Test
    void isEqualTo_fails() {
        assertThrows(AssertionError.class, () -> checkThatObject("a").isEqualTo("b"));
    }

    @Test
    void isNotEqualTo_passes() {
        assertDoesNotThrow(() -> checkThatObject("a").isNotEqualTo("b"));
    }

    @Test
    void isNotEqualTo_fails() {
        assertThrows(AssertionError.class, () -> checkThatObject("same").isNotEqualTo("same"));
    }

    @Test
    void isInstanceOf_passes() {
        assertDoesNotThrow(() -> checkThatObject("hello").isInstanceOf(CharSequence.class));
    }

    @Test
    void isInstanceOf_fails() {
        assertThrows(AssertionError.class, () -> checkThatObject("hello").isInstanceOf(Integer.class));
    }

    @Test
    void hasSameHashCodeAs_failsNullActualVsNonNull() {
        assertThrows(AssertionError.class,
                () -> checkThatObject(null).hasSameHashCodeAs(Integer.valueOf(42)));
    }
}
