package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DurationCheckTest {

    @Nested
    class IsZero {

        @Test
        void passesWhenZero() {
            assertDoesNotThrow(() -> checkThat(Duration.ZERO).isZero());
        }

        @Test
        void failsWhenNotZero() {
            assertThrows(AssertionError.class, () -> checkThat(Duration.ofSeconds(1)).isZero());
        }
    }

    @Nested
    class IsNotZero {

        @Test
        void passesWhenNonZero() {
            assertDoesNotThrow(() -> checkThat(Duration.ofNanos(1)).isNotZero());
        }

        @Test
        void failsWhenZero() {
            assertThrows(AssertionError.class, () -> checkThat(Duration.ZERO).isNotZero());
        }
    }

    @Nested
    class IsPositive {

        @Test
        void passes() {
            assertDoesNotThrow(() -> checkThat(Duration.ofMillis(1)).isPositive());
        }

        @Test
        void failsForZero() {
            assertThrows(AssertionError.class, () -> checkThat(Duration.ZERO).isPositive());
        }

        @Test
        void failsForNegative() {
            assertThrows(AssertionError.class, () -> checkThat(Duration.ofSeconds(-1)).isPositive());
        }
    }

    @Nested
    class IsNegative {

        @Test
        void passes() {
            assertDoesNotThrow(() -> checkThat(Duration.ofHours(-1)).isNegative());
        }

        @Test
        void failsForPositive() {
            assertThrows(AssertionError.class, () -> checkThat(Duration.ofSeconds(1)).isNegative());
        }

        @Test
        void failsForZero() {
            assertThrows(AssertionError.class, () -> checkThat(Duration.ZERO).isNegative());
        }
    }

    @Nested
    class IsGreaterThan {

        @Test
        void passes() {
            assertDoesNotThrow(() -> checkThat(Duration.ofSeconds(10)).isGreaterThan(Duration.ofSeconds(5)));
        }

        @Test
        void failsWhenEqual() {
            Duration d = Duration.ofSeconds(5);
            assertThrows(AssertionError.class, () -> checkThat(d).isGreaterThan(d));
        }

        @Test
        void failsWhenLess() {
            assertThrows(AssertionError.class,
                    () -> checkThat(Duration.ofSeconds(1)).isGreaterThan(Duration.ofSeconds(2)));
        }
    }

    @Nested
    class IsLessThan {

        @Test
        void passes() {
            assertDoesNotThrow(() -> checkThat(Duration.ofMillis(100)).isLessThan(Duration.ofSeconds(1)));
        }

        @Test
        void failsWhenEqual() {
            Duration d = Duration.ofMinutes(1);
            assertThrows(AssertionError.class, () -> checkThat(d).isLessThan(d));
        }

        @Test
        void failsWhenGreater() {
            assertThrows(AssertionError.class,
                    () -> checkThat(Duration.ofDays(2)).isLessThan(Duration.ofDays(1)));
        }
    }

    @Nested
    class IsGreaterThanOrEqualTo {

        @Test
        void passesWhenGreaterOrEqual() {
            Duration d = Duration.ofSeconds(5);
            assertDoesNotThrow(() -> checkThat(d).isGreaterThanOrEqualTo(Duration.ofSeconds(4)));
            assertDoesNotThrow(() -> checkThat(d).isGreaterThanOrEqualTo(d));
        }

        @Test
        void failsWhenLess() {
            assertThrows(AssertionError.class,
                    () -> checkThat(Duration.ofSeconds(1)).isGreaterThanOrEqualTo(Duration.ofSeconds(2)));
        }
    }

    @Nested
    class IsLessThanOrEqualTo {

        @Test
        void passesWhenLessOrEqual() {
            Duration d = Duration.ofSeconds(5);
            assertDoesNotThrow(() -> checkThat(d).isLessThanOrEqualTo(Duration.ofSeconds(6)));
            assertDoesNotThrow(() -> checkThat(d).isLessThanOrEqualTo(d));
        }

        @Test
        void failsWhenGreater() {
            assertThrows(AssertionError.class,
                    () -> checkThat(Duration.ofSeconds(10)).isLessThanOrEqualTo(Duration.ofSeconds(9)));
        }
    }

    @Nested
    class IsBetween {

        @Test
        void passesInsideInclusive() {
            assertDoesNotThrow(() ->
                    checkThat(Duration.ofSeconds(5)).isBetween(Duration.ofSeconds(1), Duration.ofSeconds(10)));
        }

        @Test
        void passesOnBoundaries() {
            Duration low = Duration.ofSeconds(2);
            Duration high = Duration.ofSeconds(8);
            assertDoesNotThrow(() -> checkThat(low).isBetween(low, high));
            assertDoesNotThrow(() -> checkThat(high).isBetween(low, high));
        }

        @Test
        void failsBelowRange() {
            assertThrows(AssertionError.class,
                    () -> checkThat(Duration.ZERO).isBetween(Duration.ofSeconds(1), Duration.ofSeconds(2)));
        }

        @Test
        void failsAboveRange() {
            assertThrows(AssertionError.class,
                    () -> checkThat(Duration.ofDays(2)).isBetween(Duration.ZERO, Duration.ofDays(1)));
        }
    }

    @Nested
    class HasMillis {

        @Test
        void passes() {
            assertDoesNotThrow(() -> checkThat(Duration.ofMillis(1500)).hasMillis(1500));
        }

        @Test
        void fails() {
            assertThrows(AssertionError.class, () -> checkThat(Duration.ofSeconds(1)).hasMillis(999));
        }
    }

    @Nested
    class HasSeconds {

        @Test
        void passes() {
            assertDoesNotThrow(() -> checkThat(Duration.ofSeconds(90)).hasSeconds(90));
        }

        @Test
        void fails() {
            assertThrows(AssertionError.class, () -> checkThat(Duration.ofMinutes(1)).hasSeconds(30));
        }
    }

    @Nested
    class HasMinutes {

        @Test
        void passes() {
            assertDoesNotThrow(() -> checkThat(Duration.ofMinutes(45)).hasMinutes(45));
        }

        @Test
        void fails() {
            assertThrows(AssertionError.class, () -> checkThat(Duration.ofHours(1)).hasMinutes(30));
        }
    }

    @Nested
    class HasHours {

        @Test
        void passes() {
            assertDoesNotThrow(() -> checkThat(Duration.ofHours(3)).hasHours(3));
        }

        @Test
        void fails() {
            assertThrows(AssertionError.class, () -> checkThat(Duration.ofSeconds(3600)).hasHours(2));
        }
    }

    @Test
    void hasHours_pass() {
        assertDoesNotThrow(() -> checkThat(Duration.ofHours(3)).hasHours(3));
    }

    @Test
    void hasHours_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(Duration.ofHours(3)).hasHours(5));
    }

    @Test
    void isLessThanOrEqualTo_pass_equal() {
        assertDoesNotThrow(() ->
                checkThat(Duration.ofSeconds(5)).isLessThanOrEqualTo(Duration.ofSeconds(5)));
    }

    @Test
    void isLessThanOrEqualTo_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(Duration.ofSeconds(10)).isLessThanOrEqualTo(Duration.ofSeconds(5)));
    }

    @Test
    void isGreaterThanOrEqualTo_pass_equal() {
        assertDoesNotThrow(() ->
                checkThat(Duration.ofSeconds(5)).isGreaterThanOrEqualTo(Duration.ofSeconds(5)));
    }

    @Test
    void isGreaterThanOrEqualTo_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(Duration.ofSeconds(3)).isGreaterThanOrEqualTo(Duration.ofSeconds(5)));
    }

    @Test
    void hasMillis_pass() {
        assertDoesNotThrow(() -> checkThat(Duration.ofMillis(500)).hasMillis(500));
    }

    @Test
    void hasMillis_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(Duration.ofMillis(500)).hasMillis(999));
    }

    @Test
    void hasSeconds_pass() {
        assertDoesNotThrow(() -> checkThat(Duration.ofSeconds(42)).hasSeconds(42));
    }

    @Test
    void hasSeconds_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(Duration.ofSeconds(42)).hasSeconds(99));
    }

    @Test
    void hasMinutes_pass() {
        assertDoesNotThrow(() -> checkThat(Duration.ofMinutes(10)).hasMinutes(10));
    }

    @Test
    void hasMinutes_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(Duration.ofMinutes(10)).hasMinutes(20));
    }

    @Test
    void isBetween_pass() {
        assertDoesNotThrow(() ->
                checkThat(Duration.ofSeconds(5))
                        .isBetween(Duration.ofSeconds(1), Duration.ofSeconds(10)));
    }

    @Test
    void isBetween_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(Duration.ofSeconds(20))
                        .isBetween(Duration.ofSeconds(1), Duration.ofSeconds(10)));
    }
}
