package io.github.imetaxas.realitycheck;

/**
 * Base class for built-in check types. Handles self-type resolution
 * and stores the actual value + failure handler.
 *
 * @param <SELF>   the concrete check type (for fluent chaining)
 * @param <ACTUAL> the type of the value under test
 */
public abstract class AbstractCheck<SELF extends AbstractCheck<SELF, ACTUAL>, ACTUAL>
        implements Check<SELF, ACTUAL> {

    private final ACTUAL actual;
    private final FailureHandler failureHandler;
    private final SELF self;

    @SuppressWarnings("unchecked")
    protected AbstractCheck(ACTUAL actual, FailureHandler failureHandler) {
        this.actual = actual;
        this.failureHandler = failureHandler;
        this.self = (SELF) this;
    }

    @Override
    public final ACTUAL actual() {
        return actual;
    }

    @Override
    public final FailureHandler failureHandler() {
        return failureHandler;
    }

    @Override
    public final SELF self() {
        return self;
    }

    /**
     * Returns {@code true} if actual is non-null. If null, records a structured
     * failure via the failure handler and returns {@code false}. Leaf check methods
     * that dereference {@link #actual()} should guard with:
     * <pre>{@code if (!isActualPresent()) return self(); }</pre>
     */
    protected final boolean isActualPresent() {
        if (actual == null) {
            failureHandler.fail("expected a non-null value but actual was: <null>");
            return false;
        }
        return true;
    }
}
