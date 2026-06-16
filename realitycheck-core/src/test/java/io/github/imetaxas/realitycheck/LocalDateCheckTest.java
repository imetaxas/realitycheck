package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.Test;

class LocalDateCheckTest {

    private static final LocalDate LEAP_SAMPLE = LocalDate.of(2024, 3, 15);
    private static final LocalDate PAST_SAMPLE = LocalDate.of(2000, 1, 15);
    private static final LocalDate MONDAY = LocalDate.of(2024, 3, 18);
    private static final LocalDate SATURDAY = LocalDate.of(2024, 3, 16);

    @Test
    void isBefore_passes() {
        assertDoesNotThrow(() -> checkThat(PAST_SAMPLE).isBefore(LocalDate.now()));
    }

    @Test
    void isBefore_fails() {
        assertThrows(AssertionError.class, () -> checkThat(LocalDate.now()).isBefore(PAST_SAMPLE));
    }

    @Test
    void isAfter_passes() {
        assertDoesNotThrow(() -> checkThat(LocalDate.now().plusDays(1)).isAfter(LocalDate.now().minusDays(1)));
    }

    @Test
    void isAfter_fails() {
        assertThrows(AssertionError.class, () -> checkThat(PAST_SAMPLE).isAfter(LocalDate.now()));
    }

    @Test
    void isBetween_passes() {
        assertDoesNotThrow(() -> checkThat(LEAP_SAMPLE).isBetween(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)));
    }

    @Test
    void isBetween_fails() {
        assertThrows(
                AssertionError.class,
                () -> checkThat(LEAP_SAMPLE).isBetween(LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 30)));
    }

    @Test
    void isToday_passes() {
        assertDoesNotThrow(() -> checkThat(LocalDate.now()).isToday());
    }

    @Test
    void isToday_fails() {
        assertThrows(AssertionError.class, () -> checkThat(PAST_SAMPLE).isToday());
    }

    @Test
    void isInThePast_passes() {
        assertDoesNotThrow(() -> checkThat(PAST_SAMPLE).isInThePast());
    }

    @Test
    void isInThePast_fails() {
        assertThrows(AssertionError.class, () -> checkThat(LocalDate.now().plusDays(1)).isInThePast());
    }

    @Test
    void isInTheFuture_passes() {
        assertDoesNotThrow(() -> checkThat(LocalDate.now().plusDays(1)).isInTheFuture());
    }

    @Test
    void isInTheFuture_fails() {
        assertThrows(AssertionError.class, () -> checkThat(PAST_SAMPLE).isInTheFuture());
    }

    @Test
    void hasYear_passesAndFails() {
        assertDoesNotThrow(() -> checkThat(LEAP_SAMPLE).hasYear(2024));
        assertThrows(AssertionError.class, () -> checkThat(LEAP_SAMPLE).hasYear(1999));
    }

    @Test
    void hasMonth_passesAndFails() {
        assertDoesNotThrow(() -> checkThat(LEAP_SAMPLE).hasMonth(Month.MARCH));
        assertThrows(AssertionError.class, () -> checkThat(LEAP_SAMPLE).hasMonth(Month.APRIL));
    }

    @Test
    void hasMonthValue_passesAndFails() {
        assertDoesNotThrow(() -> checkThat(LEAP_SAMPLE).hasMonthValue(3));
        assertThrows(AssertionError.class, () -> checkThat(LEAP_SAMPLE).hasMonthValue(4));
    }

    @Test
    void hasDayOfMonth_passesAndFails() {
        assertDoesNotThrow(() -> checkThat(LEAP_SAMPLE).hasDayOfMonth(15));
        assertThrows(AssertionError.class, () -> checkThat(LEAP_SAMPLE).hasDayOfMonth(14));
    }

    @Test
    void hasDayOfWeek_passesAndFails() {
        assertDoesNotThrow(() -> checkThat(MONDAY).hasDayOfWeek(DayOfWeek.MONDAY));
        assertThrows(AssertionError.class, () -> checkThat(MONDAY).hasDayOfWeek(DayOfWeek.TUESDAY));
    }

    @Test
    void isWeekday_passesAndFails() {
        assertDoesNotThrow(() -> checkThat(MONDAY).isWeekday());
        assertThrows(AssertionError.class, () -> checkThat(SATURDAY).isWeekday());
    }

    @Test
    void isWeekend_passesAndFails() {
        assertDoesNotThrow(() -> checkThat(SATURDAY).isWeekend());
        assertThrows(AssertionError.class, () -> checkThat(MONDAY).isWeekend());
    }

    @Test
    void isLeapYear_passesAndFails() {
        assertDoesNotThrow(() -> checkThat(LEAP_SAMPLE).isLeapYear());
        assertThrows(AssertionError.class, () -> checkThat(LocalDate.of(2023, 3, 15)).isLeapYear());
    }
}
