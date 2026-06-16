package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class OptionalCheckTest {

    @Test
    void isPresent_passes() {
        assertDoesNotThrow(() -> checkThat(Optional.of("hello")).isPresent());
    }

    @Test
    void isPresent_fails() {
        assertThrows(AssertionError.class, () -> checkThat(Optional.empty()).isPresent());
    }

    @Test
    void isEmpty_passes() {
        assertDoesNotThrow(() -> checkThat(Optional.empty()).isEmpty());
    }

    @Test
    void isEmpty_failsWithValue() {
        var e = assertThrows(AssertionError.class, () -> checkThat(Optional.of("hi")).isEmpty());
        assertTrue(e.getMessage().contains("hi"));
    }

    @Test
    void hasValue_passes() {
        assertDoesNotThrow(() -> checkThat(Optional.of(42)).hasValue(42));
    }

    @Test
    void hasValue_fails() {
        assertThrows(AssertionError.class, () -> checkThat(Optional.of(1)).hasValue(2));
    }

    @Test
    void value_extractsForFurtherChecks() {
        assertDoesNotThrow(() -> checkThat(Optional.of("hello")).value().isEqualTo("hello"));
    }

    @Test
    void value_failsOnEmpty() {
        assertThrows(AssertionError.class, () -> checkThat(Optional.empty()).value());
    }

    @Test
    void hasValue_fail_wrongValue() {
        assertThrows(AssertionError.class, () ->
                checkThat(Optional.of("hello")).hasValue("world"));
    }

    @Test
    void hasValue_fail_empty() {
        assertThrows(AssertionError.class, () ->
                checkThat(Optional.empty()).hasValue("hello"));
    }

    @Test
    void isEmpty_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(Optional.of("present")).isEmpty());
    }

    @Test
    void hasValue_handlesNullExpectedWithoutNpe() {
        assertThrows(AssertionError.class, () -> checkThat(Optional.of("hello")).hasValue(null));
    }

    @Test
    void hasValue_nullMatchesNullableOptional() {
        @SuppressWarnings("OptionalAssignedToNull")
        Optional<String> opt = Optional.ofNullable(null);
        assertThrows(AssertionError.class, () -> checkThat(opt).hasValue(null));
    }

    @Test
    void ifPresent_invokesConsumerWhenPresent() {
        AtomicReference<String> captured = new AtomicReference<>();
        assertDoesNotThrow(() ->
                checkThat(Optional.of("hello")).ifPresent(captured::set));
        assertTrue("hello".equals(captured.get()),
                "consumer should have been called with the optional value");
    }

    @Test
    void ifPresent_skipsConsumerWhenEmpty() {
        AtomicReference<String> captured = new AtomicReference<>();
        assertDoesNotThrow(() ->
                checkThat(Optional.<String>empty()).ifPresent(captured::set));
        assertTrue(captured.get() == null,
                "consumer should not have been called for empty optional");
    }
}
