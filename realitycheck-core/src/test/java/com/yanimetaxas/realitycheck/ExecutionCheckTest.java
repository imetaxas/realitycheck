package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.checkThatCode;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class ExecutionCheckTest {

    @Test
    void completesWithin_passes() {
        assertDoesNotThrow(() ->
                checkThatCode(() -> { /* instant */ }).completesWithin(Duration.ofSeconds(1)));
    }

    @Test
    void completesWithin_failsOnSlow() {
        var e = assertThrows(AssertionError.class, () ->
                checkThatCode(() -> Thread.sleep(200)).completesWithin(Duration.ofMillis(10)));
        assertTrue(e.getMessage().contains("completion within"));
    }

    @Test
    void completesWithin_failsOnException() {
        var e = assertThrows(AssertionError.class, () ->
                checkThatCode(() -> { throw new RuntimeException("boom"); })
                        .completesWithin(Duration.ofSeconds(1)));
        assertTrue(e.getMessage().contains("threw"));
    }

    @Test
    void doesNotThrow_passes() {
        assertDoesNotThrow(() ->
                checkThatCode(() -> { int x = 1 + 1; }).doesNotThrow());
    }

    @Test
    void doesNotThrow_fails() {
        var e = assertThrows(AssertionError.class, () ->
                checkThatCode(() -> { throw new IllegalStateException("broken"); }).doesNotThrow());
        assertTrue(e.getMessage().contains("IllegalStateException"));
    }

    @SuppressWarnings("removal")
    @Test
    void throwsA_passes() {
        assertDoesNotThrow(() ->
                checkThatCode(() -> { throw new IllegalArgumentException("bad"); })
                        .throwsA(IllegalArgumentException.class)
                        .hasMessage("bad"));
    }

    @SuppressWarnings("removal")
    @Test
    void throwsA_wrongType() {
        assertThrows(AssertionError.class, () ->
                checkThatCode(() -> { throw new RuntimeException("r"); })
                        .throwsA(IllegalArgumentException.class));
    }

    @SuppressWarnings("removal")
    @Test
    void throwsA_nothingThrown() {
        assertThrows(AssertionError.class, () ->
                checkThatCode(() -> {}).throwsA(RuntimeException.class));
    }

    @Test
    void measuredTime_returnsCheck() {
        assertDoesNotThrow(() ->
                checkThatCode(() -> { /* fast */ })
                        .measuredTime()
                        .isLessThan(Duration.ofSeconds(1)));
    }

    @Test
    void measuredTime_chainable() {
        assertDoesNotThrow(() ->
                checkThatCode(() -> Thread.sleep(50))
                        .measuredTime()
                        .isGreaterThan(Duration.ofMillis(20))
                        .isLessThan(Duration.ofSeconds(5)));
    }

    @Test
    void callable_isExecutedExactlyOnce_whenAssertionsAreChained() {
        var counter = new AtomicInteger();
        checkThatCode(counter::incrementAndGet)
                .doesNotThrow()
                .completesWithin(Duration.ofSeconds(1));
        assertEquals(1, counter.get(), "callable must run exactly once across chained assertions");
    }

    @Test
    void throwsInstanceOf_passes() {
        assertDoesNotThrow(() ->
                checkThatCode(() -> { throw new IllegalArgumentException("bad"); })
                        .throwsInstanceOf(IllegalArgumentException.class)
                        .hasMessage("bad"));
    }

    @Test
    void throwsInstanceOf_matchesSubclass() {
        assertDoesNotThrow(() ->
                checkThatCode(() -> { throw new IllegalArgumentException("bad"); })
                        .throwsInstanceOf(RuntimeException.class));
    }

    @Test
    void throwsInstanceOf_wrongType() {
        assertThrows(AssertionError.class, () ->
                checkThatCode(() -> { throw new RuntimeException("r"); })
                        .throwsInstanceOf(IllegalArgumentException.class));
    }

    @Test
    void throwsInstanceOf_nothingThrown() {
        assertThrows(AssertionError.class, () ->
                checkThatCode(() -> {}).throwsInstanceOf(RuntimeException.class));
    }

    @Test
    void throwsExactly_passes() {
        assertDoesNotThrow(() ->
                checkThatCode(() -> { throw new IllegalArgumentException("bad"); })
                        .throwsExactly(IllegalArgumentException.class)
                        .hasMessage("bad"));
    }

    @Test
    void throwsExactly_rejectsSubclass() {
        assertThrows(AssertionError.class, () ->
                checkThatCode(() -> { throw new IllegalArgumentException("bad"); })
                        .throwsExactly(RuntimeException.class));
    }

    @Test
    void throwsExactly_nothingThrown() {
        assertThrows(AssertionError.class, () ->
                checkThatCode(() -> {}).throwsExactly(RuntimeException.class));
    }
}
