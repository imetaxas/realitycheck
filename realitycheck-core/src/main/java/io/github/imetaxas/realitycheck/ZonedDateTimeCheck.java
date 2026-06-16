package io.github.imetaxas.realitycheck;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Fluent assertions for {@link ZonedDateTime} values.
 *
 * <pre>{@code
 * checkThat(event).hasYear(2026).hasZone(ZoneId.of("UTC"));
 * checkThat(meeting).isInTheFuture().isBefore(deadline);
 * }</pre>
 */
public final class ZonedDateTimeCheck extends AbstractTemporalCheck<ZonedDateTimeCheck, ZonedDateTime> {

    ZonedDateTimeCheck(ZonedDateTime actual, FailureHandler handler) {
        super(actual, handler);
    }

    public ZonedDateTimeCheck isInThePast() {
        return failureHandler().check(self(), actual().isBefore(ZonedDateTime.now()),
                "expected <%s> to be in the past", actual());
    }

    public ZonedDateTimeCheck isInTheFuture() {
        return failureHandler().check(self(), actual().isAfter(ZonedDateTime.now()),
                "expected <%s> to be in the future", actual());
    }

    public ZonedDateTimeCheck isCloseTo(ZonedDateTime expected, Duration tolerance) {
        Duration gap = Duration.between(actual(), expected).abs();
        return failureHandler().check(self(), gap.compareTo(tolerance) <= 0,
                "expected <%s> to be within <%s> of <%s> but gap was <%s>",
                actual(), tolerance, expected, gap);
    }

    public ZonedDateTimeCheck hasYear(int year) {
        return failureHandler().check(self(), actual().getYear() == year,
                "expected year <%d> but was: <%d>", year, actual().getYear());
    }

    public ZonedDateTimeCheck hasMonth(Month month) {
        return failureHandler().check(self(), actual().getMonth() == month,
                "expected month <%s> but was: <%s>", month, actual().getMonth());
    }

    public ZonedDateTimeCheck hasDayOfMonth(int day) {
        return failureHandler().check(self(), actual().getDayOfMonth() == day,
                "expected day of month <%d> but was: <%d>", day, actual().getDayOfMonth());
    }

    public ZonedDateTimeCheck hasHour(int hour) {
        return failureHandler().check(self(), actual().getHour() == hour,
                "expected hour <%d> but was: <%d>", hour, actual().getHour());
    }

    public ZonedDateTimeCheck hasMinute(int minute) {
        return failureHandler().check(self(), actual().getMinute() == minute,
                "expected minute <%d> but was: <%d>", minute, actual().getMinute());
    }

    public ZonedDateTimeCheck hasDayOfWeek(DayOfWeek dayOfWeek) {
        return failureHandler().check(self(), actual().getDayOfWeek() == dayOfWeek,
                "expected day of week <%s> but was: <%s>", dayOfWeek, actual().getDayOfWeek());
    }

    public ZonedDateTimeCheck hasZone(ZoneId zone) {
        return failureHandler().check(self(), actual().getZone().equals(zone),
                "expected zone <%s> but was: <%s>", zone, actual().getZone());
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
