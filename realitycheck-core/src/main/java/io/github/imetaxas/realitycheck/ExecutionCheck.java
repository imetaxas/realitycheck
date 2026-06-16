package io.github.imetaxas.realitycheck;

import java.time.Duration;

/**
 * Fluent assertions for code execution — timing and exception behavior.
 *
 * <p>The callable is executed exactly once, on the first terminal assertion call.
 * Subsequent chained calls read from the cached outcome, so the callable's
 * side effects occur exactly once regardless of how many assertions are chained.
 *
 * <pre>{@code
 * checkThatCode(() -> sort(largeList)).completesWithin(Duration.ofSeconds(2));
 * checkThatCode(() -> parse(input)).doesNotThrow();
 * checkThatCode(() -> db.insert(row)).doesNotThrow().completesWithin(Duration.ofMillis(500));
 * }</pre>
 */
public final class ExecutionCheck extends AbstractCheck<ExecutionCheck, ThrowingCallable> {

    private record Outcome(Throwable thrown, Duration elapsed) {}

    private Outcome outcome;

    ExecutionCheck(ThrowingCallable actual, FailureHandler handler) {
        super(actual, handler);
    }

    /**
     * Executes the callable exactly once and caches the result.
     * All terminal assertion methods call this to get the shared outcome.
     */
    private Outcome outcome() {
        if (outcome == null) {
            long startNanos = System.nanoTime();
            Throwable thrown = null;
            try {
                actual().call();
            } catch (Throwable t) {
                thrown = t;
            }
            outcome = new Outcome(thrown, Duration.ofNanos(System.nanoTime() - startNanos));
        }
        return outcome;
    }

    /**
     * Asserts that the code block completed within the given duration.
     * Measured with {@link System#nanoTime()} for monotonic accuracy.
     * Does not preempt execution — timing is measured after completion.
     */
    public ExecutionCheck completesWithin(Duration timeout) {
        Outcome o = outcome();
        if (o.thrown() != null) {
            failureHandler().fail("expected code to complete within <%s> but it threw: %s: %s",
                    timeout, o.thrown().getClass().getName(), o.thrown().getMessage());
        } else if (o.elapsed().compareTo(timeout) > 0) {
            failureHandler().fail("expected completion within <%s> but took: <%s>",
                    timeout, o.elapsed());
        }
        return self();
    }

    /**
     * Asserts that the code block completes without throwing any exception.
     */
    public ExecutionCheck doesNotThrow() {
        Outcome o = outcome();
        if (o.thrown() != null) {
            failureHandler().fail("expected no exception but <%s> was thrown: %s",
                    o.thrown().getClass().getName(), o.thrown().getMessage());
        }
        return self();
    }

    /**
     * Asserts that the code block throws an exception that is an instance of the given type
     * (including subclasses). Returns a {@link ThrowableCheck} for further assertions.
     */
    public ThrowableCheck throwsInstanceOf(Class<? extends Throwable> expectedType) {
        Throwable caught = outcome().thrown();
        if (caught == null) {
            failureHandler().fail("expected <%s> to be thrown but nothing was thrown",
                    expectedType.getName());
            return new ThrowableCheck(null, failureHandler());
        }
        if (!expectedType.isInstance(caught)) {
            failureHandler().fail("expected <%s> to be thrown but was: <%s>",
                    expectedType.getName(), caught.getClass().getName());
        }
        return new ThrowableCheck(caught, failureHandler());
    }

    /**
     * Asserts that the code block throws an exception whose class is exactly the given type
     * (not a subclass). Returns a {@link ThrowableCheck} for further assertions.
     */
    public ThrowableCheck throwsExactly(Class<? extends Throwable> expectedType) {
        Throwable caught = outcome().thrown();
        if (caught == null) {
            failureHandler().fail("expected exactly <%s> to be thrown but nothing was thrown",
                    expectedType.getName());
            return new ThrowableCheck(null, failureHandler());
        }
        if (!caught.getClass().equals(expectedType)) {
            failureHandler().fail("expected exactly <%s> to be thrown but was: <%s>",
                    expectedType.getName(), caught.getClass().getName());
        }
        return new ThrowableCheck(caught, failureHandler());
    }

    /** @deprecated Use {@link #throwsInstanceOf(Class)} instead. */
    @Deprecated(forRemoval = true)
    public ThrowableCheck throwsA(Class<? extends Throwable> expectedType) {
        return throwsInstanceOf(expectedType);
    }

    /**
     * Runs the code and returns the elapsed duration as a {@link DurationCheck}
     * for further assertions.
     */
    public DurationCheck measuredTime() {
        Outcome o = outcome();
        if (o.thrown() != null) {
            failureHandler().fail("code threw during timing measurement: %s: %s",
                    o.thrown().getClass().getName(), o.thrown().getMessage());
        }
        return new DurationCheck(o.elapsed(), failureHandler());
    }
}
