package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LocalDateTimeCheckTest {

    private static final LocalDateTime T_2000 = LocalDateTime.of(2000, 6, 15, 12, 30, 0);
    private static final LocalDateTime T_2001 = LocalDateTime.of(2001, 6, 15, 12, 30, 0);
    private static final LocalDateTime T_2099 = LocalDateTime.of(2099, 12, 31, 23, 59, 59);

    @Nested
    class IsBefore {

        @Test
        void passesWhenStrictlyBefore() {
            assertDoesNotThrow(() -> checkThat(T_2000).isBefore(T_2001));
        }

        @Test
        void failsWhenEqualOrAfter() {
            assertThrows(AssertionError.class, () -> checkThat(T_2001).isBefore(T_2000));
            assertThrows(AssertionError.class, () -> checkThat(T_2000).isBefore(T_2000));
        }
    }

    @Nested
    class IsAfter {

        @Test
        void passesWhenStrictlyAfter() {
            assertDoesNotThrow(() -> checkThat(T_2001).isAfter(T_2000));
        }

        @Test
        void failsWhenEqualOrBefore() {
            assertThrows(AssertionError.class, () -> checkThat(T_2000).isAfter(T_2001));
            assertThrows(AssertionError.class, () -> checkThat(T_2000).isAfter(T_2000));
        }
    }

    @Nested
    class IsBetween {

        @Test
        void passesWhenInsideInclusiveRange() {
            LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
            LocalDateTime end = LocalDateTime.of(2026, 12, 31, 23, 59);
            LocalDateTime mid = LocalDateTime.of(2026, 6, 15, 12, 0);
            assertDoesNotThrow(() -> checkThat(mid).isBetween(start, end));
        }

        @Test
        void passesWhenEqualToStartOrEnd() {
            LocalDateTime start = LocalDateTime.of(2026, 3, 1, 10, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 31, 18, 0);
            assertDoesNotThrow(() -> checkThat(start).isBetween(start, end));
            assertDoesNotThrow(() -> checkThat(end).isBetween(start, end));
        }

        @Test
        void failsWhenBeforeStartOrAfterEnd() {
            LocalDateTime start = LocalDateTime.of(2026, 3, 10, 0, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 20, 0, 0);
            assertThrows(AssertionError.class, () -> checkThat(start.minusDays(1)).isBetween(start, end));
            assertThrows(AssertionError.class, () -> checkThat(end.plusDays(1)).isBetween(start, end));
        }
    }

    @Nested
    class IsInThePast {

        @Test
        void passesForFixedPastInstant() {
            assertDoesNotThrow(() -> checkThat(T_2000).isInThePast());
        }

        @Test
        void failsForFuture() {
            assertThrows(AssertionError.class, () -> checkThat(T_2099).isInThePast());
        }
    }

    @Nested
    class IsInTheFuture {

        @Test
        void passesForFixedFutureInstant() {
            assertDoesNotThrow(() -> checkThat(T_2099).isInTheFuture());
        }

        @Test
        void failsForPast() {
            assertThrows(AssertionError.class, () -> checkThat(T_2000).isInTheFuture());
        }
    }

    @Nested
    class IsCloseTo {

        @Test
        void passesWithinTolerance() {
            LocalDateTime base = LocalDateTime.of(2026, 4, 1, 12, 0, 0);
            LocalDateTime nearby = base.plusSeconds(3);
            assertDoesNotThrow(() -> checkThat(base).isCloseTo(nearby, Duration.ofSeconds(5)));
        }

        @Test
        void failsWhenGapExceedsTolerance() {
            LocalDateTime base = LocalDateTime.of(2026, 4, 1, 12, 0, 0);
            LocalDateTime far = base.plusMinutes(10);
            var e = assertThrows(AssertionError.class,
                    () -> checkThat(base).isCloseTo(far, Duration.ofSeconds(5)));
            assertTrue(e.getMessage().contains("gap"));
        }
    }

    @Nested
    class HasDateAndTimeParts {

        @Test
        void hasDate_passAndFail() {
            LocalDateTime dt = LocalDateTime.of(2026, 7, 4, 15, 45, 30);
            assertDoesNotThrow(() -> checkThat(dt).hasDate(LocalDate.of(2026, 7, 4)));
            assertThrows(AssertionError.class, () -> checkThat(dt).hasDate(LocalDate.of(2025, 7, 4)));
        }

        @Test
        void hasTime_passAndFail() {
            LocalDateTime dt = LocalDateTime.of(2026, 1, 1, 14, 30, 0);
            assertDoesNotThrow(() -> checkThat(dt).hasTime(LocalTime.of(14, 30)));
            assertThrows(AssertionError.class, () -> checkThat(dt).hasTime(LocalTime.of(14, 31)));
        }

        @Test
        void hasYear_passAndFail() {
            LocalDateTime dt = LocalDateTime.of(2026, 2, 2, 0, 0);
            assertDoesNotThrow(() -> checkThat(dt).hasYear(2026));
            assertThrows(AssertionError.class, () -> checkThat(dt).hasYear(2025));
        }

        @Test
        void hasMonth_passAndFail() {
            LocalDateTime dt = LocalDateTime.of(2026, Month.APRIL.getValue(), 1, 0, 0);
            assertDoesNotThrow(() -> checkThat(dt).hasMonth(Month.APRIL));
            assertThrows(AssertionError.class, () -> checkThat(dt).hasMonth(Month.MAY));
        }

        @Test
        void hasHour_passAndFail() {
            LocalDateTime dt = LocalDateTime.of(2026, 1, 1, 9, 0);
            assertDoesNotThrow(() -> checkThat(dt).hasHour(9));
            assertThrows(AssertionError.class, () -> checkThat(dt).hasHour(10));
        }

        @Test
        void hasMinute_passAndFail() {
            LocalDateTime dt = LocalDateTime.of(2026, 1, 1, 0, 42);
            assertDoesNotThrow(() -> checkThat(dt).hasMinute(42));
            assertThrows(AssertionError.class, () -> checkThat(dt).hasMinute(41));
        }
    }

    @Nested
    class DateExtractor {

        @Test
        void delegatesToLocalDateCheck() {
            LocalDateTime dt = LocalDateTime.of(2026, 8, 20, 22, 15);
            assertDoesNotThrow(() -> checkThat(dt).date().hasYear(2026).hasMonth(Month.AUGUST).hasDayOfMonth(20));
        }

        @Test
        void dateExtractor_failsWhenDateMismatch() {
            LocalDateTime dt = LocalDateTime.of(2026, 1, 1, 0, 0);
            assertThrows(AssertionError.class, () -> checkThat(dt).date().hasYear(2025));
        }
    }
}
