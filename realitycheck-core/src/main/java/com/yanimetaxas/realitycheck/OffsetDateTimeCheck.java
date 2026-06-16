package com.yanimetaxas.realitycheck;

import java.time.Duration;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Fluent assertions for {@link OffsetDateTime} values.
 *
 * <pre>{@code
 * checkThat(apiTimestamp).hasOffset(ZoneOffset.UTC).hasYear(2026);
 * checkThat(event).isInThePast().isBefore(deadline);
 * }</pre>
 */
public final class OffsetDateTimeCheck extends AbstractTemporalCheck<OffsetDateTimeCheck, OffsetDateTime> {

    OffsetDateTimeCheck(OffsetDateTime actual, FailureHandler handler) {
        super(actual, handler);
    }

    public OffsetDateTimeCheck isInThePast() {
        return failureHandler().check(self(), actual().isBefore(OffsetDateTime.now()),
                "expected <%s> to be in the past", actual());
    }

    public OffsetDateTimeCheck isInTheFuture() {
        return failureHandler().check(self(), actual().isAfter(OffsetDateTime.now()),
                "expected <%s> to be in the future", actual());
    }

    public OffsetDateTimeCheck isCloseTo(OffsetDateTime expected, Duration tolerance) {
        Duration gap = Duration.between(actual(), expected).abs();
        return failureHandler().check(self(), gap.compareTo(tolerance) <= 0,
                "expected <%s> to be within <%s> of <%s> but gap was <%s>",
                actual(), tolerance, expected, gap);
    }

    public OffsetDateTimeCheck hasYear(int year) {
        return failureHandler().check(self(), actual().getYear() == year,
                "expected year <%d> but was: <%d>", year, actual().getYear());
    }

    public OffsetDateTimeCheck hasMonth(Month month) {
        return failureHandler().check(self(), actual().getMonth() == month,
                "expected month <%s> but was: <%s>", month, actual().getMonth());
    }

    public OffsetDateTimeCheck hasHour(int hour) {
        return failureHandler().check(self(), actual().getHour() == hour,
                "expected hour <%d> but was: <%d>", hour, actual().getHour());
    }

    public OffsetDateTimeCheck hasOffset(ZoneOffset offset) {
        return failureHandler().check(self(), actual().getOffset().equals(offset),
                "expected offset <%s> but was: <%s>", offset, actual().getOffset());
    }

    /** Extracts the date part as a {@link LocalDateCheck}. */
    public LocalDateCheck date() {
        return new LocalDateCheck(actual().toLocalDate(), failureHandler());
    }

    /** Extracts the instant for further assertions. */
    public InstantCheck instant() {
        return new InstantCheck(actual().toInstant(), failureHandler());
    }
}
