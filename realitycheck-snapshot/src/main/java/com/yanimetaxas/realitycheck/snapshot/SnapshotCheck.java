package com.yanimetaxas.realitycheck.snapshot;

import com.yanimetaxas.realitycheck.AbstractCheck;
import com.yanimetaxas.realitycheck.FailureHandler;

import java.util.Arrays;
import java.util.function.Function;

/**
 * Fluent assertions that compare a value against a stored snapshot (golden file).
 *
 * <pre>{@code
 * import static com.yanimetaxas.realitycheck.snapshot.SnapshotReality.*;
 *
 * checkThatSnapshot(apiResponse)
 *     .serializedWith(Object::toString)
 *     .matchesSnapshot("MyTest", "testApiResponse");
 * }</pre>
 *
 * <p><strong>Important:</strong> always call {@link #serializedWith(Function)} to provide a
 * deterministic serializer. If no serializer is set and the value's class does not override
 * {@code toString()}, {@link #matchesSnapshot} will fail fast with a diagnostic message,
 * because the default {@code Object.toString()} produces a non-deterministic identity hash
 * (e.g., {@code "com.example.Foo@1a2b3c"}) that makes every snapshot run mismatch.
 *
 * @param <T> the type of the value under test
 */
public final class SnapshotCheck<T> extends AbstractCheck<SnapshotCheck<T>, T> {

    private final SnapshotManager manager;
    private Function<T, String> serializer = Object::toString;
    private boolean usingDefaultSerializer = true;

    SnapshotCheck(T actual, FailureHandler handler, SnapshotManager manager) {
        super(actual, handler);
        this.manager = manager;
    }

    /**
     * Sets a custom serializer for converting the value to a string for snapshot comparison.
     */
    public SnapshotCheck<T> serializedWith(Function<T, String> serializer) {
        this.serializer = serializer;
        this.usingDefaultSerializer = false;
        return self();
    }

    /**
     * Compares the serialized value against the stored snapshot.
     * Creates the snapshot on first run; fails with a diff on subsequent mismatches.
     *
     * <p>If no custom serializer has been configured and the actual value's class does not
     * override {@code toString()}, this method fails immediately with an instructive error
     * rather than creating a garbage snapshot containing an identity hash.
     */
    public SnapshotCheck<T> matchesSnapshot(String testClass, String testMethod) {
        if (usingDefaultSerializer && actual() != null && usesObjectToString(actual())) {
            failureHandler().fail(
                    "SnapshotCheck: <%s> does not override toString() — the default serializer " +
                    "produces a non-deterministic identity hash. Call .serializedWith(...) first.",
                    actual().getClass().getName());
            return self();
        }
        String serialized = actual() == null ? "null" : serializer.apply(actual());
        SnapshotManager.MatchResult result = manager.match(testClass, testMethod, serialized);
        if (!result.matches()) {
            failureHandler().fail(result.formatFailure());
        }
        return self();
    }

    private static boolean usesObjectToString(Object value) {
        return Arrays.stream(value.getClass().getMethods())
                .filter(m -> m.getName().equals("toString") && m.getParameterCount() == 0)
                .findFirst()
                .map(m -> m.getDeclaringClass() == Object.class)
                .orElse(false);
    }
}
