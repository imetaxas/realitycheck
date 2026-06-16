package io.github.imetaxas.realitycheck.snapshot;

import io.github.imetaxas.realitycheck.FailureHandler;

/**
 * Entry point for snapshot testing.
 *
 * <pre>{@code
 * import static io.github.imetaxas.realitycheck.snapshot.SnapshotReality.*;
 *
 * checkThatSnapshot(myObject)
 *     .serializedWith(obj -> objectMapper.writeValueAsString(obj))
 *     .matchesSnapshot("MyTest", "testMyMethod");
 * }</pre>
 */
public final class SnapshotReality {

    private SnapshotReality() {}

    public static <T> SnapshotCheck<T> checkThatSnapshot(T actual) {
        return new SnapshotCheck<>(actual, new FailureHandler(), SnapshotManager.defaultManager());
    }

    public static <T> SnapshotCheck<T> checkThatSnapshot(T actual, SnapshotManager manager) {
        return new SnapshotCheck<>(actual, new FailureHandler(), manager);
    }

    /** Alias for {@link #checkThatSnapshot(Object)} for migration convenience. */
    public static <T> SnapshotCheck<T> assertThatSnapshot(T actual) {
        return checkThatSnapshot(actual);
    }

    /** Alias for {@link #checkThatSnapshot(Object, SnapshotManager)} for migration convenience. */
    public static <T> SnapshotCheck<T> assertThatSnapshot(T actual, SnapshotManager manager) {
        return checkThatSnapshot(actual, manager);
    }
}
