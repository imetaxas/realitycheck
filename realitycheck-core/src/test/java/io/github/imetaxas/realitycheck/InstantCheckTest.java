package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

/** Pass and fail coverage for {@link InstantCheck} comparison helpers. */
class InstantCheckTest {

    private static final Instant T0 = Instant.parse("2020-01-01T00:00:00Z");
    private static final Instant T1 = Instant.parse("2020-06-01T12:00:00Z");
    private static final Instant T2 = Instant.parse("2021-01-01T00:00:00Z");

    private final Instant now = Instant.now();

    @Test
    void isBefore_pass_and_fail() {
        assertDoesNotThrow(() -> checkThat(T0).isBefore(T1));
        assertThrows(AssertionError.class, () -> checkThat(T1).isBefore(T0));
    }

    @Test
    void isAfter_pass_and_fail() {
        assertDoesNotThrow(() -> checkThat(T1).isAfter(T0));
        assertThrows(AssertionError.class, () -> checkThat(T0).isAfter(T1));
    }

    @Test
    void isBetween_pass_and_fail() {
        assertDoesNotThrow(() -> checkThat(T1).isBetween(T0, T2));
        assertThrows(AssertionError.class, () -> checkThat(T0).isBetween(T1, T2));
    }

    @Test
    void isInThePast_pass_and_fail() {
        assertDoesNotThrow(() -> checkThat(Instant.now().minusSeconds(3600)).isInThePast());
        assertThrows(AssertionError.class, () -> checkThat(Instant.now().plusSeconds(3600)).isInThePast());
    }

    @Test
    void isInTheFuture_pass_and_fail() {
        assertDoesNotThrow(() -> checkThat(Instant.now().plusSeconds(3600)).isInTheFuture());
        assertThrows(AssertionError.class, () -> checkThat(Instant.now().minusSeconds(3600)).isInTheFuture());
    }

    @Test
    void isCloseTo_pass_and_fail() {
        Instant base = Instant.parse("2025-06-15T10:00:00Z");
        assertDoesNotThrow(() -> checkThat(base).isCloseTo(base.plusMillis(50), Duration.ofSeconds(1)));
        assertThrows(
                AssertionError.class,
                () -> checkThat(Instant.EPOCH).isCloseTo(base, Duration.ofMillis(1)));
    }

    @Test
    void isBetween_pass() {
        assertDoesNotThrow(() ->
                checkThat(now).isBetween(now.minusSeconds(10), now.plusSeconds(10)));
    }

    @Test
    void isBetween_fail_before() {
        assertThrows(AssertionError.class, () ->
                checkThat(now.minusSeconds(100)).isBetween(now, now.plusSeconds(10)));
    }

    @Test
    void isBetween_fail_after() {
        assertThrows(AssertionError.class, () ->
                checkThat(now.plusSeconds(100)).isBetween(now.minusSeconds(10), now));
    }

    @Test
    void isCloseTo_pass() {
        assertDoesNotThrow(() ->
                checkThat(now).isCloseTo(now.plusSeconds(1), Duration.ofSeconds(5)));
    }

    @Test
    void isCloseTo_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(now).isCloseTo(now.plusSeconds(100), Duration.ofSeconds(1)));
    }

    @Test
    void isAfter_pass() {
        assertDoesNotThrow(() -> checkThat(now).isAfter(now.minusSeconds(10)));
    }

    @Test
    void isAfter_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(now).isAfter(now.plusSeconds(10)));
    }

    @Test
    void isBefore_pass() {
        assertDoesNotThrow(() -> checkThat(now).isBefore(now.plusSeconds(10)));
    }

    @Test
    void isBefore_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(now).isBefore(now.minusSeconds(10)));
    }

    @Test
    void isBeforeOrEqualTo_pass() {
        assertDoesNotThrow(() -> checkThat(now).isBeforeOrEqualTo(now));
    }

    @Test
    void isBeforeOrEqualTo_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(now.plusSeconds(10)).isBeforeOrEqualTo(now));
    }

    @Test
    void isAfterOrEqualTo_pass() {
        assertDoesNotThrow(() -> checkThat(now).isAfterOrEqualTo(now));
    }

    @Test
    void isAfterOrEqualTo_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(now.minusSeconds(10)).isAfterOrEqualTo(now));
    }

    @Test
    void isBeforeOrEqualTo_passesWhenBefore() {
        assertDoesNotThrow(() -> checkThat(T0).isBeforeOrEqualTo(T1));
    }

    @Test
    void isBeforeOrEqualTo_passesWhenEqual() {
        assertDoesNotThrow(() -> checkThat(T0).isBeforeOrEqualTo(T0));
    }

    @Test
    void isBeforeOrEqualTo_failsWhenAfter() {
        assertThrows(AssertionError.class, () -> checkThat(T1).isBeforeOrEqualTo(T0));
    }

    @Test
    void isAfterOrEqualTo_passesWhenAfter() {
        assertDoesNotThrow(() -> checkThat(T1).isAfterOrEqualTo(T0));
    }

    @Test
    void isAfterOrEqualTo_passesWhenEqual() {
        assertDoesNotThrow(() -> checkThat(T0).isAfterOrEqualTo(T0));
    }

    @Test
    void isAfterOrEqualTo_failsWhenBefore() {
        assertThrows(AssertionError.class, () -> checkThat(T0).isAfterOrEqualTo(T1));
    }
}
