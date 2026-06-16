package com.yanimetaxas.realitycheck;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

/**
 * Fluent assertions for {@link LocalDate} values.
 *
 * <pre>{@code
 * checkThat(birthday).hasYear(1990).hasMonth(Month.MARCH);
 * checkThat(releaseDate).isInTheFuture().isAfter(LocalDate.now());
 * checkThat(deadline).isWeekday();
 * }</pre>
 */
public final class LocalDateCheck extends AbstractTemporalCheck<LocalDateCheck, LocalDate> {

    LocalDateCheck(LocalDate actual, FailureHandler handler) {
        super(actual, handler);
    }

    public LocalDateCheck isToday() {
        LocalDate today = LocalDate.now();
        return failureHandler().check(self(), actual().equals(today),
                "expected today <%s> but was: <%s>", today, actual());
    }

    public LocalDateCheck isInThePast() {
        return failureHandler().check(self(), actual().isBefore(LocalDate.now()),
                "expected <%s> to be in the past", actual());
    }

    public LocalDateCheck isInTheFuture() {
        return failureHandler().check(self(), actual().isAfter(LocalDate.now()),
                "expected <%s> to be in the future", actual());
    }

    public LocalDateCheck hasYear(int year) {
        return failureHandler().check(self(), actual().getYear() == year,
                "expected year <%d> but was: <%d>", year, actual().getYear());
    }

    public LocalDateCheck hasMonth(Month month) {
        return failureHandler().check(self(), actual().getMonth() == month,
                "expected month <%s> but was: <%s>", month, actual().getMonth());
    }

    public LocalDateCheck hasMonthValue(int month) {
        return failureHandler().check(self(), actual().getMonthValue() == month,
                "expected month <%d> but was: <%d>", month, actual().getMonthValue());
    }

    public LocalDateCheck hasDayOfMonth(int day) {
        return failureHandler().check(self(), actual().getDayOfMonth() == day,
                "expected day of month <%d> but was: <%d>", day, actual().getDayOfMonth());
    }

    public LocalDateCheck hasDayOfWeek(DayOfWeek dayOfWeek) {
        return failureHandler().check(self(), actual().getDayOfWeek() == dayOfWeek,
                "expected day of week <%s> but was: <%s>", dayOfWeek, actual().getDayOfWeek());
    }

    public LocalDateCheck isWeekday() {
        DayOfWeek dow = actual().getDayOfWeek();
        return failureHandler().check(self(),
                dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY,
                "expected a weekday but was: <%s> (%s)", actual(), dow);
    }

    public LocalDateCheck isWeekend() {
        DayOfWeek dow = actual().getDayOfWeek();
        return failureHandler().check(self(),
                dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY,
                "expected a weekend day but was: <%s> (%s)", actual(), dow);
    }

    public LocalDateCheck isLeapYear() {
        return failureHandler().check(self(), actual().isLeapYear(),
                "expected <%s> to be in a leap year", actual());
    }
}
