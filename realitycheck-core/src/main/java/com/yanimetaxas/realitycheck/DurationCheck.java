package com.yanimetaxas.realitycheck;

import java.time.Duration;

/**
 * Fluent assertions for {@link Duration} values.
 *
 * <pre>{@code
 * checkThat(elapsed).isLessThan(Duration.ofSeconds(5));
 * checkThat(timeout).isPositive().isGreaterThan(Duration.ofMillis(100));
 * }</pre>
 */
public final class DurationCheck extends OrderedCheck<DurationCheck, Duration> {

    DurationCheck(Duration actual, FailureHandler handler) {
        super(actual, handler);
    }

    public DurationCheck isZero() {
        return failureHandler().check(self(), actual().isZero(),
                "expected zero duration but was: <%s>", actual());
    }

    public DurationCheck isNotZero() {
        return failureHandler().check(self(), !actual().isZero(),
                "expected non-zero duration");
    }

    public DurationCheck isPositive() {
        return failureHandler().check(self(), !actual().isNegative() && !actual().isZero(),
                "expected a positive duration but was: <%s>", actual());
    }

    public DurationCheck isNegative() {
        return failureHandler().check(self(), actual().isNegative(),
                "expected a negative duration but was: <%s>", actual());
    }

    public DurationCheck hasMillis(long expectedMillis) {
        long actualMillis = actual().toMillis();
        return failureHandler().check(self(), actualMillis == expectedMillis,
                "expected <%d> millis but was: <%d> millis",
                expectedMillis, actualMillis);
    }

    public DurationCheck hasSeconds(long expectedSeconds) {
        long actualSeconds = actual().getSeconds();
        return failureHandler().check(self(), actualSeconds == expectedSeconds,
                "expected <%d> seconds but was: <%d> seconds",
                expectedSeconds, actualSeconds);
    }

    public DurationCheck hasMinutes(long expectedMinutes) {
        long actualMinutes = actual().toMinutes();
        return failureHandler().check(self(), actualMinutes == expectedMinutes,
                "expected <%d> minutes but was: <%d> minutes",
                expectedMinutes, actualMinutes);
    }

    public DurationCheck hasHours(long expectedHours) {
        long actualHours = actual().toHours();
        return failureHandler().check(self(), actualHours == expectedHours,
                "expected <%d> hours but was: <%d> hours",
                expectedHours, actualHours);
    }
}
