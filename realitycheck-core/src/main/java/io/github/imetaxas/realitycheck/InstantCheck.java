package io.github.imetaxas.realitycheck;

import java.time.Duration;
import java.time.Instant;

/**
 * Fluent assertions for {@link Instant} values.
 *
 * <pre>{@code
 * checkThat(event.timestamp()).isInThePast().isAfter(startTime);
 * checkThat(expiry).isInTheFuture().isBefore(Instant.now().plus(Duration.ofHours(1)));
 * checkThat(recorded).isCloseTo(Instant.now(), Duration.ofSeconds(5));
 * }</pre>
 */
public final class InstantCheck extends AbstractTemporalCheck<InstantCheck, Instant> {

    InstantCheck(Instant actual, FailureHandler handler) {
        super(actual, handler);
    }

    public InstantCheck isInThePast() {
        return failureHandler().check(self(), actual().isBefore(Instant.now()),
                "expected <%s> to be in the past", actual());
    }

    public InstantCheck isInTheFuture() {
        return failureHandler().check(self(), actual().isAfter(Instant.now()),
                "expected <%s> to be in the future", actual());
    }

    /**
     * Asserts that the instant is within {@code tolerance} of the expected instant.
     * Useful for comparing timestamps that may have minor clock skew.
     */
    public InstantCheck isCloseTo(Instant expected, Duration tolerance) {
        Duration gap = Duration.between(actual(), expected).abs();
        return failureHandler().check(self(), gap.compareTo(tolerance) <= 0,
                "expected <%s> to be within <%s> of <%s> but gap was <%s>",
                actual(), tolerance, expected, gap);
    }
}
