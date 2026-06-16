package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.Instant;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class OffsetDateTimeCheckTest {

    private static final OffsetDateTime SAMPLE =
            OffsetDateTime.of(2026, 3, 31, 10, 15, 0, 0, ZoneOffset.UTC);

    private static final OffsetDateTime DT =
            OffsetDateTime.of(2026, 3, 15, 10, 30, 0, 0, ZoneOffset.UTC);

    private static final OffsetDateTime SAMPLE2 =
            OffsetDateTime.of(2026, 6, 15, 12, 45, 30, 0, ZoneOffset.ofHours(2));

    @Test
    void hasYear_passes() {
        assertDoesNotThrow(() -> checkThat(SAMPLE).hasYear(2026));
    }

    @Test
    void hasMonth_passes() {
        assertDoesNotThrow(() -> checkThat(SAMPLE).hasMonth(Month.MARCH));
    }

    @Test
    void hasHour_passes() {
        assertDoesNotThrow(() -> checkThat(SAMPLE).hasHour(10));
    }

    @Test
    void hasOffset_passes() {
        assertDoesNotThrow(() -> checkThat(SAMPLE).hasOffset(ZoneOffset.UTC));
    }

    @Test
    void hasOffset_fails() {
        assertThrows(AssertionError.class, () ->
                checkThat(SAMPLE).hasOffset(ZoneOffset.ofHours(5)));
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
    void isInThePast_passes() {
        var past = OffsetDateTime.now().minusDays(1);
        assertDoesNotThrow(() -> checkThat(past).isInThePast());
    }

    @Test
    void isCloseTo_passes() {
        var now = OffsetDateTime.now();
        assertDoesNotThrow(() ->
                checkThat(now.minusSeconds(1)).isCloseTo(now, Duration.ofSeconds(5)));
    }

    @Test
    void date_extractor() {
        assertDoesNotThrow(() -> checkThat(SAMPLE).date().hasYear(2026));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() ->
                checkThat(SAMPLE).hasYear(2026).hasHour(10).hasOffset(ZoneOffset.UTC));
    }

    @Test
    void hasMonth_fail() {
        assertThrows(AssertionError.class, () -> checkThat(DT).hasMonth(Month.JANUARY));
    }

    @Test
    void isCloseTo_fail() {
        OffsetDateTime other = DT.plusHours(5);
        assertThrows(AssertionError.class, () ->
                checkThat(DT).isCloseTo(other, Duration.ofMinutes(1)));
    }

    @Test
    void date_extractor_chain() {
        assertDoesNotThrow(() ->
                checkThat(DT).date().hasYear(2026).hasMonth(Month.MARCH));
    }

    @Test
    void instant_extractor() {
        assertDoesNotThrow(() -> checkThat(DT).instant().isNotNull());
    }

    @Test
    void hasYear_fail() {
        assertThrows(AssertionError.class, () -> checkThat(DT).hasYear(2020));
    }

    @Test
    void hasHour_fail() {
        assertThrows(AssertionError.class, () -> checkThat(DT).hasHour(5));
    }

    @Test
    void isBefore_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(DT).isBefore(DT.minusDays(1)));
    }

    @Test
    void isAfter_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(DT).isAfter(DT.plusDays(1)));
    }

    @Test
    void instant_extractor_passes() {
        Instant expected = SAMPLE2.toInstant();
        assertDoesNotThrow(() ->
                checkThat(SAMPLE2).instant().isBeforeOrEqualTo(expected));
    }

    @Test
    void instant_extractor_failsOnWrongAssumption() {
        assertThrows(AssertionError.class, () ->
                checkThat(SAMPLE2).instant().isBefore(Instant.EPOCH));
    }
}
