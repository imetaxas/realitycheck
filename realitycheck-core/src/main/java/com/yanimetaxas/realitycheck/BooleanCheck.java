package com.yanimetaxas.realitycheck;

/**
 * Fluent assertions for {@link Boolean} values.
 */
public final class BooleanCheck extends AbstractCheck<BooleanCheck, Boolean> {

    BooleanCheck(Boolean actual, FailureHandler handler) {
        super(actual, handler);
    }

    public BooleanCheck isTrue() {
        return failureHandler().check(self(), Boolean.TRUE.equals(actual()),
                "expected true but was: <%s>", actual());
    }

    public BooleanCheck isFalse() {
        return failureHandler().check(self(), Boolean.FALSE.equals(actual()),
                "expected false but was: <%s>", actual());
    }
}
