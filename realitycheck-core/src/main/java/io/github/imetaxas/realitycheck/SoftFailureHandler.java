package io.github.imetaxas.realitycheck;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A failure handler that collects assertion failures instead of throwing immediately.
 * Call {@link #assertAll()} at the end to report all collected failures at once.
 *
 * <p>Thread-safe: failures may be recorded concurrently (e.g. from parallel streams).
 */
final class SoftFailureHandler extends FailureHandler {

    private final List<AssertionError> failures = new CopyOnWriteArrayList<>();

    @Override
    public void fail(String format, Object... args) {
        failures.add(new AssertionError(formatMessage(format, args)));
    }

    List<AssertionError> failures() {
        return Collections.unmodifiableList(failures);
    }

    void assertAll() {
        if (failures.isEmpty()) {
            return;
        }
        var message = new StringBuilder();
        message.append("Multiple failures (").append(failures.size()).append("):\n");
        for (int i = 0; i < failures.size(); i++) {
            message.append("  ").append(i + 1).append(") ").append(failures.get(i).getMessage()).append('\n');
        }
        AssertionError combined = new AssertionError(message.toString());
        for (AssertionError failure : failures) {
            combined.addSuppressed(failure);
        }
        throw combined;
    }
}
