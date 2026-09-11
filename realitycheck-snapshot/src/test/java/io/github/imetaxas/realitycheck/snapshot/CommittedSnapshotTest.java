package io.github.imetaxas.realitycheck.snapshot;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Strict-mode smoke test for golden files committed to this module.
 *
 * <p>CI runs this class separately with {@code -Drealitycheck.strict-ci=true}. Keeping the
 * expected values here makes a deleted or stale golden file fail instead of being silently
 * recreated by the normal first-run workflow.
 */
class CommittedSnapshotTest {

    @Test
    void committedGoldenFilesExistAndMatch() {
        SnapshotManager manager = SnapshotManager.defaultManager();

        assertMatch(manager.match("SnapshotCheckTest", "defaultEntry", "test-value"));
        assertMatch(manager.match(
                "SnapshotCoverageTest", "snapshotReality_default", "hello world"));
        assertMatch(manager.match("AliasDefaultTest", "alias", "alias-test"));
    }

    private static void assertMatch(SnapshotManager.MatchResult result) {
        assertTrue(result.matches(), result::formatFailure);
        assertFalse(result.created(), "Committed snapshot was missing and got recreated");
    }
}
