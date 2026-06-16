package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

class ZonedDateTimeCheckTest {

    private static final ZonedDateTime SAMPLE =
            ZonedDateTime.of(2026, 3, 31, 14, 30, 0, 0, ZoneId.of("UTC"));

    private static final ZonedDateTime DT =
            ZonedDateTime.of(2026, 3, 15, 10, 30, 0, 0, ZoneId.of("UTC"));

    private static final ZonedDateTime SAMPLE2 =
            ZonedDateTime.of(2026, 7, 20, 9, 7, 0, 0, ZoneId.of("UTC"));

    @Test
    void hasYear_passes() {
        assertDoesNotThrow(() -> checkThat(SAMPLE).hasYear(2026));
    }

    @Test
    void hasMonth_passes() {
        assertDoesNotThrow(() -> checkThat(SAMPLE).hasMonth(Month.MARCH));
    }

    @Test
    void hasDayOfMonth_passes() {
        assertDoesNotThrow(() -> checkThat(SAMPLE).hasDayOfMonth(31));
    }

    @Test
    void hasHour_passes() {
        assertDoesNotThrow(() -> checkThat(SAMPLE).hasHour(14));
    }

    @Test
    void hasMinute_passes() {
        assertDoesNotThrow(() -> checkThat(SAMPLE).hasMinute(30));
    }

    @Test
    void hasZone_passes() {
        assertDoesNotThrow(() -> checkThat(SAMPLE).hasZone(ZoneId.of("UTC")));
    }

    @Test
    void hasZone_fails() {
        assertThrows(AssertionError.class, () ->
                checkThat(SAMPLE).hasZone(ZoneId.of("US/Eastern")));
    }

    @Test
    void isBefore_passes() {
        assertDoesNotThrow(() -> checkThat(SAMPLE).isBefore(SAMPLE.plusDays(1)));
    }

    @Test
    void isAfter_passes() {
        assertDoesNotThrow(() -> checkThat(SAMPLE).isAfter(SAMPLE.minusDays(1)));
    }

    @Test
    void isBetween_passes() {
        assertDoesNotThrow(() ->
                checkThat(SAMPLE).isBetween(SAMPLE.minusDays(1), SAMPLE.plusDays(1)));
    }

    @Test
    void isInThePast_passes() {
        var past = ZonedDateTime.now().minusDays(1);
        assertDoesNotThrow(() -> checkThat(past).isInThePast());
    }

    @Test
    void isCloseTo_passes() {
        var now = ZonedDateTime.now();
        assertDoesNotThrow(() ->
                checkThat(now.minusSeconds(2)).isCloseTo(now, Duration.ofSeconds(5)));
    }

    @Test
    void date_extractor() {
        assertDoesNotThrow(() ->
                checkThat(SAMPLE).date().hasYear(2026).hasMonth(Month.MARCH));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() ->
                checkThat(SAMPLE).hasYear(2026).hasHour(14).hasZone(ZoneId.of("UTC")));
    }

    @Test
    void hasMinute_fail() {
        assertThrows(AssertionError.class, () -> checkThat(DT).hasMinute(45));
    }

    @Test
    void isCloseTo_fail() {
        ZonedDateTime other = DT.plusHours(5);
        assertThrows(AssertionError.class, () ->
                checkThat(DT).isCloseTo(other, Duration.ofMinutes(1)));
    }

    @Test
    void hasYear_fail() {
        assertThrows(AssertionError.class, () -> checkThat(DT).hasYear(2025));
    }

    @Test
    void hasMonth_fail() {
        assertThrows(AssertionError.class, () -> checkThat(DT).hasMonth(Month.JANUARY));
    }

    @Test
    void hasDayOfMonth_fail() {
        assertThrows(AssertionError.class, () -> checkThat(DT).hasDayOfMonth(1));
    }

    @Test
    void hasHour_fail() {
        assertThrows(AssertionError.class, () -> checkThat(DT).hasHour(0));
    }

    @Test
    void instant_extractor() {
        assertDoesNotThrow(() -> checkThat(DT).instant().isNotNull());
    }

    @Test
    void hasDayOfMonth_fails() {
        assertThrows(AssertionError.class, () -> checkThat(SAMPLE2).hasDayOfMonth(1));
    }

    @Test
    void hasDayOfWeek_passes() {
        assertDoesNotThrow(() -> checkThat(SAMPLE2).hasDayOfWeek(DayOfWeek.MONDAY));
    }

    @Test
    void hasDayOfWeek_fails() {
        assertThrows(AssertionError.class,
                () -> checkThat(SAMPLE2).hasDayOfWeek(DayOfWeek.FRIDAY));
    }

    @Test
    void instant_extractor_passes() {
        Instant expected = SAMPLE2.toInstant();
        assertDoesNotThrow(() ->
                checkThat(SAMPLE2).instant().isAfterOrEqualTo(expected));
    }

    @Test
    void instant_extractor_failsOnWrongAssumption() {
        assertThrows(AssertionError.class, () ->
                checkThat(SAMPLE2).instant().isBefore(Instant.EPOCH));
    }
}
