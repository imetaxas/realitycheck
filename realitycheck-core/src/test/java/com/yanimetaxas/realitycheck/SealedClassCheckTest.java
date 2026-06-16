package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.checkThatSealed;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SealedClassCheckTest {

    sealed interface Shape permits Circle, Square, Triangle {}
    static final class Circle implements Shape {}
    static final class Square implements Shape {}
    static final class Triangle implements Shape {}

    @Test
    void isSealed_passes() {
        assertDoesNotThrow(() -> checkThatSealed(Shape.class).isSealed());
    }

    @Test
    void isSealed_failsOnNonSealed() {
        assertThrows(AssertionError.class, () ->
                checkThatSealed(String.class).isSealed());
    }

    @Test
    void isNotSealed_passes() {
        assertDoesNotThrow(() -> checkThatSealed(String.class).isNotSealed());
    }

    @Test
    void permittedCount_passes() {
        assertDoesNotThrow(() -> checkThatSealed(Shape.class).permittedCount(3));
    }

    @Test
    void permittedCount_fails() {
        assertThrows(AssertionError.class, () ->
                checkThatSealed(Shape.class).permittedCount(5));
    }

    @Test
    void permits_passes() {
        assertDoesNotThrow(() -> checkThatSealed(Shape.class).permits(Circle.class));
    }

    @Test
    void permits_fails() {
        assertThrows(AssertionError.class, () ->
                checkThatSealed(Shape.class).permits(String.class));
    }

    @Test
    void permitsExactly_passes() {
        assertDoesNotThrow(() ->
                checkThatSealed(Shape.class)
                        .permitsExactly(Circle.class, Square.class, Triangle.class));
    }

    @Test
    void permitsExactly_fails() {
        assertThrows(AssertionError.class, () ->
                checkThatSealed(Shape.class)
                        .permitsExactly(Circle.class, Square.class));
    }

    @Test
    void permittedSubclasses_extractor() {
        assertDoesNotThrow(() ->
                checkThatSealed(Shape.class).permittedSubclasses().hasSize(3));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() ->
                checkThatSealed(Shape.class)
                        .isSealed()
                        .permittedCount(3)
                        .permits(Circle.class));
    }

    @Test
    void permitsExactly_pass() {
        assertDoesNotThrow(() ->
                checkThatSealed(Shape.class).permitsExactly(Circle.class, Square.class, Triangle.class));
    }

    @Test
    void permitsExactly_fail_wrongClasses() {
        assertThrows(AssertionError.class, () ->
                checkThatSealed(Shape.class).permitsExactly(Circle.class));
    }

    @Test
    void permittedCount_pass() {
        assertDoesNotThrow(() -> checkThatSealed(Shape.class).permittedCount(3));
    }

    @Test
    void permittedCount_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatSealed(Shape.class).permittedCount(5));
    }

    @Test
    void isSealed_pass() {
        assertDoesNotThrow(() -> checkThatSealed(Shape.class).isSealed());
    }

    @Test
    void isSealed_fail_onNonSealed() {
        assertThrows(AssertionError.class, () ->
                checkThatSealed(String.class).isSealed());
    }

    @Test
    void isNotSealed_pass() {
        assertDoesNotThrow(() -> checkThatSealed(String.class).isNotSealed());
    }

    @Test
    void isNotSealed_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatSealed(Shape.class).isNotSealed());
    }

    @Test
    void permits_pass() {
        assertDoesNotThrow(() -> checkThatSealed(Shape.class).permits(Circle.class));
    }

    @Test
    void permits_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatSealed(Shape.class).permits(String.class));
    }

    @Test
    void ensureSealed_failsOnNonSealed_permittedCount() {
        assertThrows(AssertionError.class, () ->
                checkThatSealed(String.class).permittedCount(0));
    }

    @Test
    void ensureSealed_failsOnNonSealed_permits() {
        assertThrows(AssertionError.class, () ->
                checkThatSealed(String.class).permits(Object.class));
    }

    @Test
    void isNotSealed_failsOnSealed() {
        assertThrows(AssertionError.class,
                () -> checkThatSealed(Shape.class).isNotSealed());
    }

    @Test
    void permittedCount_failsOnWrongCount() {
        assertThrows(AssertionError.class,
                () -> checkThatSealed(Shape.class).permittedCount(10));
    }

    @Test
    void permits_failsWhenNotPermitted() {
        assertThrows(AssertionError.class,
                () -> checkThatSealed(Shape.class).permits(String.class));
    }

    @Test
    void permitsExactly_failsOnWrongSet() {
        assertThrows(AssertionError.class,
                () -> checkThatSealed(Shape.class).permitsExactly(Circle.class));
    }

    @Test
    void ensureSealed_failsOnNonSealedForPermittedCount() {
        assertThrows(AssertionError.class,
                () -> checkThatSealed(String.class).permittedCount(0));
    }

    @Test
    void ensureSealed_failsOnNonSealedForPermits() {
        assertThrows(AssertionError.class,
                () -> checkThatSealed(String.class).permits(Object.class));
    }

    @Test
    void ensureSealed_failsOnNonSealedForPermitsExactly() {
        assertThrows(AssertionError.class,
                () -> checkThatSealed(String.class).permitsExactly(Object.class));
    }

    @Test
    void ensureSealed_failsOnNonSealedForPermittedSubclasses() {
        assertThrows(AssertionError.class,
                () -> checkThatSealed(String.class).permittedSubclasses());
    }
}
