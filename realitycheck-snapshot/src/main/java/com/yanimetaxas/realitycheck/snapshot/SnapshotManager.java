package com.yanimetaxas.realitycheck.snapshot;

import com.yanimetaxas.realitycheck.Diff;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Manages snapshot files for golden-file testing. On first run, saves the value;
 * on subsequent runs, compares against the saved snapshot.
 *
 * <p>Snapshot files are stored under {@code src/test/resources/__snapshots__/}
 * in a structure mirroring the test class and method name.
 *
 * <p>Set the system property {@code realitycheck.update-snapshots=true}
 * to regenerate all snapshot files.
 */
public final class SnapshotManager {

    private static final String UPDATE_PROP = "realitycheck.update-snapshots";
    private static final String STRICT_CI_PROP = "realitycheck.strict-ci";

    private final Path snapshotDir;

    public SnapshotManager(Path snapshotDir) {
        this.snapshotDir = snapshotDir;
    }

    /**
     * Creates a SnapshotManager that stores snapshots relative to the project root.
     * Resolves to {@code src/test/resources/__snapshots__/} by default.
     */
    public static SnapshotManager defaultManager() {
        return new SnapshotManager(Path.of("src/test/resources/__snapshots__"));
    }

    /**
     * Matches the given value against its snapshot. If no snapshot exists, creates one.
     *
     * @param testClass  the test class name (used as directory)
     * @param testMethod the test method name (used as filename)
     * @param actual     the serialized value to compare
     * @return a MatchResult indicating whether the snapshot matched, was created, or differs
     */
    public MatchResult match(String testClass, String testMethod, String actual) {
        Path snapshotFile = resolveFile(testClass, testMethod);
        boolean shouldUpdate = Boolean.getBoolean(UPDATE_PROP);
        boolean strictCI = Boolean.getBoolean(STRICT_CI_PROP);

        if (!Files.exists(snapshotFile)) {
            if (strictCI) {
                return new MatchResult(false, null, false,
                        "Snapshot file missing in strict-CI mode (set -D" + UPDATE_PROP + "=true locally): " + snapshotFile);
            }
            write(snapshotFile, actual);
            return new MatchResult(true, null, true, null);
        }

        if (shouldUpdate) {
            write(snapshotFile, actual);
            return new MatchResult(true, null, true, null);
        }

        String expected = read(snapshotFile);
        if (expected.equals(actual)) {
            return new MatchResult(true, null, false, null);
        }

        Diff.Result diff = Diff.of(expected, actual);
        return new MatchResult(false, diff, false, null);
    }

    public Path resolveFile(String testClass, String testMethod) {
        String path = testClass.replace('.', '/');
        return snapshotDir.resolve(path).resolve(testMethod + ".snap");
    }

    private void write(Path file, String content) {
        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, content);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write snapshot: " + file, e);
        }
    }

    private String read(Path file) {
        try {
            return Files.readString(file);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read snapshot: " + file, e);
        }
    }

    public record MatchResult(boolean matches, Diff.Result diff, boolean created, String ciMessage) {
        public String formatFailure() {
            if (matches) return "(no failure)";
            var sb = new StringBuilder("Snapshot mismatch:\n");
            if (ciMessage != null) {
                sb.append(ciMessage).append('\n');
            }
            if (diff != null) {
                sb.append(diff.formatCompact(3));
            }
            sb.append("\nTo update snapshots, run with -D").append(UPDATE_PROP).append("=true");
            return sb.toString();
        }
    }
}
