package com.yanimetaxas.realitycheck;

/**
 * Controls how assertion failures are reported. The default implementation throws
 * immediately; {@link SoftFailureHandler} collects failures for batch reporting.
 *
 * <p>Custom messages (via {@code Reality.checkWithMessage(...)}) override the
 * default failure message while preserving the expected/actual detail as context.
 */
public class FailureHandler {

    private final String customMessage;
    private final String contextMessage;

    public FailureHandler() {
        this.customMessage = null;
        this.contextMessage = null;
    }

    public FailureHandler(String customMessage) {
        this.customMessage = customMessage;
        this.contextMessage = null;
    }

    FailureHandler(String customMessage, String contextMessage) {
        this.customMessage = customMessage;
        this.contextMessage = contextMessage;
    }

    static FailureHandler withContext(String context) {
        return new FailureHandler(null, context);
    }

    /**
     * Verifies a condition; if false, reports a failure with the given message.
     *
     * @return {@code self} for fluent chaining
     */
    public <SELF> SELF check(SELF self, boolean condition, String format, Object... args) {
        if (!condition) {
            fail(format, args);
        }
        return self;
    }

    /**
     * Reports a failure. The default implementation throws {@link AssertionError}.
     */
    public void fail(String format, Object... args) {
        throw new AssertionError(formatMessage(format, args));
    }

    protected String formatMessage(String format, Object... args) {
        String base = (args.length == 0) ? format : String.format(format, args);
        if (customMessage != null) {
            return customMessage + "\n  detail: " + base;
        }
        if (contextMessage != null) {
            return base + " [context: " + contextMessage + "]";
        }
        return base;
    }

    public String customMessage() {
        return customMessage;
    }
}
