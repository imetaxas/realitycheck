package io.github.imetaxas.realitycheck;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

/**
 * Fluent assertions for {@link LocalDateTime} values.
 *
 * <pre>{@code
 * checkThat(event).isAfter(startTime).isBefore(endTime);
 * checkThat(meeting).hasDate(LocalDate.of(2026, 3, 31)).hasHour(14);
 * checkThat(recorded).isCloseTo(LocalDateTime.now(), Duration.ofSeconds(5));
 * }</pre>
 */
public final class LocalDateTimeCheck extends AbstractTemporalCheck<LocalDateTimeCheck, LocalDateTime> {

    LocalDateTimeCheck(LocalDateTime actual, FailureHandler handler) {
        super(actual, handler);
    }

    public LocalDateTimeCheck isInThePast() {
        return failureHandler().check(self(), actual().isBefore(LocalDateTime.now()),
                "expected <%s> to be in the past", actual());
    }

    public LocalDateTimeCheck isInTheFuture() {
        return failureHandler().check(self(), actual().isAfter(LocalDateTime.now()),
                "expected <%s> to be in the future", actual());
    }

    public LocalDateTimeCheck isCloseTo(LocalDateTime expected, Duration tolerance) {
        Duration gap = Duration.between(actual(), expected).abs();
        return failureHandler().check(self(), gap.compareTo(tolerance) <= 0,
                "expected <%s> to be within <%s> of <%s> but gap was <%s>",
                actual(), tolerance, expected, gap);
    }

    public LocalDateTimeCheck hasDate(LocalDate date) {
        return failureHandler().check(self(), actual().toLocalDate().equals(date),
                "expected date <%s> but was: <%s>", date, actual().toLocalDate());
    }

    public LocalDateTimeCheck hasTime(LocalTime time) {
        return failureHandler().check(self(), actual().toLocalTime().equals(time),
                "expected time <%s> but was: <%s>", time, actual().toLocalTime());
    }

    public LocalDateTimeCheck hasYear(int year) {
        return failureHandler().check(self(), actual().getYear() == year,
                "expected year <%d> but was: <%d>", year, actual().getYear());
    }

    public LocalDateTimeCheck hasMonth(Month month) {
        return failureHandler().check(self(), actual().getMonth() == month,
                "expected month <%s> but was: <%s>", month, actual().getMonth());
    }

    public LocalDateTimeCheck hasHour(int hour) {
        return failureHandler().check(self(), actual().getHour() == hour,
                "expected hour <%d> but was: <%d>", hour, actual().getHour());
    }

    public LocalDateTimeCheck hasMinute(int minute) {
        return failureHandler().check(self(), actual().getMinute() == minute,
                "expected minute <%d> but was: <%d>", minute, actual().getMinute());
    }

    /** Extracts the date part for further assertions. */
    public LocalDateCheck date() {
        return new LocalDateCheck(actual().toLocalDate(), failureHandler());
    }
}
