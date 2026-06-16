package com.yanimetaxas.realitycheck;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Fluent assertions for {@link CompletableFuture} values.
 *
 * @param <T> the result type
 */
public final class FutureCheck<T> extends AbstractCheck<FutureCheck<T>, CompletableFuture<T>> {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    FutureCheck(CompletableFuture<T> actual, FailureHandler handler) {
        super(actual, handler);
    }

    public FutureCheck<T> isDone() {
        return failureHandler().check(self(), actual().isDone(),
                "expected future to be done");
    }

    public FutureCheck<T> isNotDone() {
        return failureHandler().check(self(), !actual().isDone(),
                "expected future not to be done");
    }

    public FutureCheck<T> isCancelled() {
        return failureHandler().check(self(), actual().isCancelled(),
                "expected future to be cancelled");
    }

    public FutureCheck<T> isNotCancelled() {
        return failureHandler().check(self(), !actual().isCancelled(),
                "expected future not to be cancelled");
    }

    public FutureCheck<T> isCompletedExceptionally() {
        return failureHandler().check(self(), actual().isCompletedExceptionally(),
                "expected future to be completed exceptionally");
    }

    public FutureCheck<T> isCompletedNormally() {
        return failureHandler().check(self(),
                actual().isDone() && !actual().isCompletedExceptionally() && !actual().isCancelled(),
                "expected future to be completed normally");
    }

    /**
     * Waits for the future to complete within the given timeout.
     * Fails if the future does not complete in time.
     */
    public FutureCheck<T> completesWithin(Duration timeout) {
        try {
            actual().get(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            failureHandler().fail("expected future to complete within %s but it timed out",
                    timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            failureHandler().fail("interrupted while waiting for future");
        } catch (ExecutionException e) {
            // Completed (with exception) — that's fine, it completed within the timeout
        }
        return self();
    }

    /**
     * Blocks until the future completes (up to 10 seconds) and returns a check on the result value.
     */
    public ObjectCheck<T> hasValue() {
        return hasValueWithin(DEFAULT_TIMEOUT);
    }

    /**
     * Blocks until the future completes within the given timeout and returns a check on the result.
     */
    public ObjectCheck<T> hasValueWithin(Duration timeout) {
        try {
            T value = actual().get(timeout.toMillis(), TimeUnit.MILLISECONDS);
            return new ObjectCheck<>(value, failureHandler());
        } catch (TimeoutException e) {
            failureHandler().fail("future did not complete within %s", timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            failureHandler().fail("interrupted while getting future value");
        } catch (ExecutionException e) {
            failureHandler().fail("expected future to complete normally but it failed with: %s",
                    e.getCause());
        }
        return new ObjectCheck<>(null, failureHandler());
    }

    /**
     * Blocks (up to 10 seconds) and asserts the result equals the expected value.
     */
    public FutureCheck<T> hasValue(T expected) {
        try {
            T value = actual().get(DEFAULT_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
            if (!Objects.equals(value, expected)) {
                failureHandler().fail("expected future value <%s> but was: <%s>",
                        expected, value);
            }
        } catch (TimeoutException e) {
            failureHandler().fail("future did not complete within %s while waiting for value <%s>",
                    DEFAULT_TIMEOUT, expected);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            failureHandler().fail("interrupted while getting future value");
        } catch (ExecutionException e) {
            failureHandler().fail("expected future value <%s> but future failed with: %s",
                    expected, e.getCause());
        }
        return self();
    }
}
