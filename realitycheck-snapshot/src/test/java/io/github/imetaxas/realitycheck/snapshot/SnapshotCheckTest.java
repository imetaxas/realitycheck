package io.github.imetaxas.realitycheck.snapshot;

import static io.github.imetaxas.realitycheck.snapshot.SnapshotReality.*;
import static org.junit.jupiter.api.Assertions.*;

import io.github.imetaxas.realitycheck.FailureHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.AclFileAttributeView;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SnapshotCheckTest {

    @TempDir
    Path tempDir;

    @Test
    void firstRun_createsSnapshot() {
        var manager = new SnapshotManager(tempDir);
        assertDoesNotThrow(() ->
                SnapshotReality.checkThatSnapshot("hello world", manager)
                        .matchesSnapshot("SnapshotCheckTest", "firstRun_createsSnapshot"));

        Path snapFile = manager.resolveFile("SnapshotCheckTest", "firstRun_createsSnapshot");
        assertTrue(Files.exists(snapFile));
    }

    @Test
    void secondRun_matchesExistingSnapshot() throws IOException {
        var manager = new SnapshotManager(tempDir);
        Path dir = tempDir.resolve("SnapshotCheckTest");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("preexisting.snap"), "expected value");

        assertDoesNotThrow(() ->
                SnapshotReality.checkThatSnapshot("expected value", manager)
                        .matchesSnapshot("SnapshotCheckTest", "preexisting"));
    }

    @Test
    void mismatch_failsWithDiff() throws IOException {
        var manager = new SnapshotManager(tempDir);
        Path dir = tempDir.resolve("SnapshotCheckTest");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("mismatch.snap"), "original");

        var e = assertThrows(AssertionError.class, () ->
                SnapshotReality.checkThatSnapshot("changed", manager)
                        .matchesSnapshot("SnapshotCheckTest", "mismatch"));
        assertTrue(e.getMessage().contains("Snapshot mismatch"));
        assertTrue(e.getMessage().contains("update-snapshots"));
    }

    @Test
    void customSerializer_works() {
        var manager = new SnapshotManager(tempDir);
        assertDoesNotThrow(() ->
                SnapshotReality.checkThatSnapshot(42, manager)
                        .serializedWith(n -> "The answer is " + n)
                        .matchesSnapshot("SnapshotCheckTest", "customSerializer_works"));
    }

    @Test
    void resolveFile_nestedClassWithDollar() {
        var manager = new SnapshotManager(tempDir);
        Path resolved = manager.resolveFile("Outer$Inner", "testMethod");
        assertEquals("Outer$Inner", resolved.getParent().getFileName().toString());
        assertEquals("testMethod.snap", resolved.getFileName().toString());
    }

    @Test
    void resolveFile_packagePrefixedNestedClass() {
        var manager = new SnapshotManager(tempDir);
        Path resolved = manager.resolveFile("com.example.Outer$Inner", "run");
        assertTrue(resolved.toString().contains("com"));
        assertEquals("run.snap", resolved.getFileName().toString());
    }

    @Test
    void resolveFile_deeplyNestedPackage() {
        var manager = new SnapshotManager(tempDir);
        Path resolved = manager.resolveFile("a.b.c.d.e.TestClass", "m");
        assertEquals("TestClass", resolved.getParent().getFileName().toString());
        assertEquals("m.snap", resolved.getFileName().toString());
    }

    @Test
    void updateMode_overwritesThenMatchesNewContent() throws IOException {
        var manager = new SnapshotManager(tempDir);
        Path dir = tempDir.resolve("OverwriteTest");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("method.snap"), "old-content");

        System.setProperty("realitycheck.update-snapshots", "true");
        try {
            assertDoesNotThrow(() ->
                    checkThatSnapshot("new-content", manager)
                            .matchesSnapshot("OverwriteTest", "method"));
            assertEquals("new-content", Files.readString(dir.resolve("method.snap")));
        } finally {
            System.clearProperty("realitycheck.update-snapshots");
        }
    }

    @Test
    void updateMode_createsSnapshotWhenNoneExists() {
        var manager = new SnapshotManager(tempDir);

        System.setProperty("realitycheck.update-snapshots", "true");
        try {
            assertDoesNotThrow(() ->
                    checkThatSnapshot("brand-new", manager)
                            .matchesSnapshot("FreshTest", "create"));
            assertTrue(Files.exists(manager.resolveFile("FreshTest", "create")));
        } finally {
            System.clearProperty("realitycheck.update-snapshots");
        }
    }

    @Test
    void customSerializer_multiLineOutput() throws IOException {
        var manager = new SnapshotManager(tempDir);
        int[] data = {1, 2, 3};

        assertDoesNotThrow(() ->
                checkThatSnapshot(data, manager)
                        .serializedWith(arr -> {
                            var sb = new StringBuilder();
                            for (int v : arr) sb.append(v).append('\n');
                            return sb.toString();
                        })
                        .matchesSnapshot("SnapshotCheckTest", "multiLine"));

        String content = Files.readString(manager.resolveFile("SnapshotCheckTest", "multiLine"));
        assertEquals("1\n2\n3\n", content);
    }

    @Test
    void customSerializer_mismatchShowsDiff() throws IOException {
        var manager = new SnapshotManager(tempDir);
        Path dir = tempDir.resolve("SnapshotCheckTest");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("serMismatch.snap"), "line1\nline2\nline3");

        var e = assertThrows(AssertionError.class, () ->
                checkThatSnapshot("different", manager)
                        .serializedWith(s -> s + "\ncontent\nhere")
                        .matchesSnapshot("SnapshotCheckTest", "serMismatch"));
        assertTrue(e.getMessage().contains("Snapshot mismatch"));
    }

    @Test
    void snapshotReality_defaultManagerEntryPoint() {
        assertDoesNotThrow(() ->
                checkThatSnapshot("test-value")
                        .matchesSnapshot("SnapshotCheckTest", "defaultEntry"));
    }

    @Test
    void snapshotReality_customManagerEntryPoint() {
        var manager = new SnapshotManager(tempDir);
        assertDoesNotThrow(() ->
                checkThatSnapshot("test-value", manager)
                        .matchesSnapshot("SnapshotCheckTest", "customEntry"));
    }

    @Test
    void matchResult_formatFailure_noFailureWhenMatches() {
        var result = new SnapshotManager.MatchResult(true, null, false, null);
        assertEquals("(no failure)", result.formatFailure());
    }

    @Test
    void matchResult_formatFailure_nullDiffStillShowsMessage() {
        var result = new SnapshotManager.MatchResult(false, null, false, null);
        String msg = result.formatFailure();
        assertTrue(msg.contains("Snapshot mismatch"));
        assertTrue(msg.contains("update-snapshots"));
    }

    @Test
    void matchResult_formatFailure_createdIsTrue() {
        var result = new SnapshotManager.MatchResult(true, null, true, null);
        assertTrue(result.created());
        assertEquals("(no failure)", result.formatFailure());
    }

    @Test
    void multipleSnapshots_differentMethodNames() throws IOException {
        var manager = new SnapshotManager(tempDir);

        assertDoesNotThrow(() ->
                checkThatSnapshot("value-a", manager)
                        .matchesSnapshot("MultiTest", "methodA"));
        assertDoesNotThrow(() ->
                checkThatSnapshot("value-b", manager)
                        .matchesSnapshot("MultiTest", "methodB"));

        assertEquals("value-a",
                Files.readString(manager.resolveFile("MultiTest", "methodA")));
        assertEquals("value-b",
                Files.readString(manager.resolveFile("MultiTest", "methodB")));
    }

    @Test
    void firstRunThenSecondRun_matchesSuccessfully() {
        var manager = new SnapshotManager(tempDir);

        assertDoesNotThrow(() ->
                checkThatSnapshot("stable-value", manager)
                        .matchesSnapshot("RoundTrip", "test1"));
        assertDoesNotThrow(() ->
                checkThatSnapshot("stable-value", manager)
                        .matchesSnapshot("RoundTrip", "test1"));
    }

    @Test
    void firstRunThenSecondRun_mismatchFails() {
        var manager = new SnapshotManager(tempDir);

        assertDoesNotThrow(() ->
                checkThatSnapshot("original", manager)
                        .matchesSnapshot("RoundTrip", "test2"));
        assertThrows(AssertionError.class, () ->
                checkThatSnapshot("modified", manager)
                        .matchesSnapshot("RoundTrip", "test2"));
    }

    @Test
    void defaultManager_pathContainsSnapshotsDir() {
        var mgr = SnapshotManager.defaultManager();
        Path resolved = mgr.resolveFile("SomeClass", "someMethod");
        assertTrue(resolved.toString().contains("__snapshots__"));
        assertTrue(resolved.toString().endsWith(".snap"));
    }

    // ── Methods merged from SnapshotCoverageTest ─────────────────────────

    @Test
    void firstRun_createsSnapshotFile() {
        var manager = new SnapshotManager(tempDir);
        SnapshotManager.MatchResult result = manager.match("TestClass", "method1", "content");
        assertTrue(result.matches());
        assertTrue(result.created());
        assertTrue(Files.exists(manager.resolveFile("TestClass", "method1")));
    }

    @Test
    void existingSnapshot_matches() throws IOException {
        var manager = new SnapshotManager(tempDir);
        Path dir = tempDir.resolve("TestClass");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("method2.snap"), "match me");

        SnapshotManager.MatchResult result = manager.match("TestClass", "method2", "match me");
        assertTrue(result.matches());
        assertFalse(result.created());
        assertNull(result.diff());
    }

    @Test
    void existingSnapshot_mismatch() throws IOException {
        var manager = new SnapshotManager(tempDir);
        Path dir = tempDir.resolve("TestClass");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("method3.snap"), "original");

        SnapshotManager.MatchResult result = manager.match("TestClass", "method3", "changed");
        assertFalse(result.matches());
        assertFalse(result.created());
        assertNotNull(result.diff());
        assertTrue(result.diff().hasDifferences());
    }

    @Test
    void updateMode_overwritesExistingSnapshot() throws IOException {
        var manager = new SnapshotManager(tempDir);
        Path dir = tempDir.resolve("TestClass");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("method4.snap"), "old");

        System.setProperty("realitycheck.update-snapshots", "true");
        try {
            SnapshotManager.MatchResult result = manager.match("TestClass", "method4", "new");
            assertTrue(result.matches());
            assertTrue(result.created());
            assertEquals("new", Files.readString(dir.resolve("method4.snap")));
        } finally {
            System.clearProperty("realitycheck.update-snapshots");
        }
    }

    @Test
    void resolveFile_usesFullPackagePath() {
        var manager = new SnapshotManager(tempDir);
        Path resolved = manager.resolveFile("com.example.MyTest", "testMethod");
        // Normalize separators so the check works on both Unix (/) and Windows (\).
        String normalizedPath = resolved.toString().replace('\\', '/');
        assertTrue(normalizedPath.contains("com/example/MyTest"),
                () -> "Expected path to contain 'com/example/MyTest' but was: " + resolved);
        assertEquals("testMethod.snap", resolved.getFileName().toString());
    }

    @Test
    void resolveFile_simpleClassName() {
        var manager = new SnapshotManager(tempDir);
        Path resolved = manager.resolveFile("MyTest", "testMethod");
        assertEquals("MyTest", resolved.getParent().getFileName().toString());
    }

    @Test
    void matchResult_formatFailure_whenMatching() {
        var result = new SnapshotManager.MatchResult(true, null, false, null);
        assertEquals("(no failure)", result.formatFailure());
    }

    @Test
    void matchResult_formatFailure_withDiff() throws IOException {
        var manager = new SnapshotManager(tempDir);
        Path dir = tempDir.resolve("TestClass");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("fmt.snap"), "line1\nline2");

        SnapshotManager.MatchResult result = manager.match("TestClass", "fmt", "line1\nchanged");
        String failure = result.formatFailure();
        assertTrue(failure.contains("Snapshot mismatch"));
        assertTrue(failure.contains("update-snapshots"));
    }

    @Test
    void defaultManager_resolves() {
        SnapshotManager mgr = SnapshotManager.defaultManager();
        assertNotNull(mgr);
        Path resolved = mgr.resolveFile("AClass", "aMethod");
        assertTrue(resolved.toString().contains("__snapshots__"));
    }

    @Test
    void snapshotReality_defaultManager_createsSnapshot() {
        assertDoesNotThrow(() ->
                checkThatSnapshot("hello world")
                        .matchesSnapshot("SnapshotCoverageTest", "snapshotReality_default"));
    }

    @Test
    void snapshotReality_customManager_createsSnapshot() {
        var manager = new SnapshotManager(tempDir);
        assertDoesNotThrow(() ->
                checkThatSnapshot("hello", manager)
                        .matchesSnapshot("SnapshotCoverageTest", "custom"));
    }

    @Test
    void matchesSnapshot_failOnMismatch() throws IOException {
        var manager = new SnapshotManager(tempDir);
        Path dir = tempDir.resolve("SnapshotCoverageTest");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("mismatchTest.snap"), "expected");

        var e = assertThrows(AssertionError.class, () ->
                checkThatSnapshot("actual", manager)
                        .matchesSnapshot("SnapshotCoverageTest", "mismatchTest"));
        assertTrue(e.getMessage().contains("Snapshot mismatch"));
    }

    @Test
    void serializedWith_customSerializer() {
        var manager = new SnapshotManager(tempDir);
        assertDoesNotThrow(() ->
                checkThatSnapshot(42, manager)
                        .serializedWith(n -> "number=" + n)
                        .matchesSnapshot("SnapshotCoverageTest", "customSerializer"));
    }

    @Test
    void write_ioExceptionThrowsUnchecked(@TempDir Path dir) throws IOException {
        Path blocker = Files.createFile(dir.resolve("blocker"));
        Path nonWritableBase = blocker.resolve("__snapshots__");
        var manager = new SnapshotManager(nonWritableBase);
        assertThrows(java.io.UncheckedIOException.class,
                () -> manager.match("TestClass", "method", "content"));
    }

    @Test
    void read_ioExceptionThrowsUnchecked(@TempDir Path dir) throws IOException {
        var manager = new SnapshotManager(dir);
        Path classDir = dir.resolve("ReadFailTest");
        Files.createDirectories(classDir);
        Path snapFile = classDir.resolve("method.snap");
        Files.writeString(snapFile, "some content");
        List<AclEntry> savedAcl = makeUnreadable(snapFile);
        try {
            assertThrows(java.io.UncheckedIOException.class,
                    () -> manager.match("ReadFailTest", "method", "different"));
        } finally {
            restoreReadable(snapFile, savedAcl);
        }
    }

    private static final boolean IS_WINDOWS =
            System.getProperty("os.name").toLowerCase().startsWith("win");

    /**
     * Makes {@code file} unreadable in a cross-platform way.
     * On Windows, uses ACL DENY; on POSIX, uses {@code setReadable(false)}.
     * Returns the saved ACL list (Windows) or {@code null} (POSIX).
     * The test is skipped (via {@link Assumptions#assumeTrue}) if the platform
     * supports neither mechanism.
     */
    private static List<AclEntry> makeUnreadable(Path file) throws IOException {
        if (IS_WINDOWS) {
            AclFileAttributeView aclView =
                    Files.getFileAttributeView(file, AclFileAttributeView.class);
            Assumptions.assumeTrue(aclView != null, "Platform does not support ACL permissions");
            List<AclEntry> savedAcl = aclView.getAcl();
            AclEntry deny = AclEntry.newBuilder()
                    .setType(AclEntryType.DENY)
                    .setPrincipal(aclView.getOwner())
                    .setPermissions(AclEntryPermission.READ_DATA)
                    .build();
            List<AclEntry> acl = new ArrayList<>(savedAcl);
            acl.add(0, deny);
            aclView.setAcl(acl);
            return savedAcl;
        } else {
            boolean removed = file.toFile().setReadable(false);
            Assumptions.assumeTrue(removed, "Platform does not support removing read permission");
            return null;
        }
    }

    private static void restoreReadable(Path file, List<AclEntry> savedAcl) throws IOException {
        if (IS_WINDOWS && savedAcl != null) {
            AclFileAttributeView aclView =
                    Files.getFileAttributeView(file, AclFileAttributeView.class);
            if (aclView != null) {
                aclView.setAcl(savedAcl);
            }
        } else {
            file.toFile().setReadable(true);
        }
    }

    @Test
    void matchResult_formatFailure_containsDiffWhenPresent() throws IOException {
        var manager = new SnapshotManager(tempDir);
        Path classDir = tempDir.resolve("DiffFormatTest");
        Files.createDirectories(classDir);
        Files.writeString(classDir.resolve("test.snap"), "line1\nline2\nline3");

        SnapshotManager.MatchResult result = manager.match("DiffFormatTest", "test", "line1\nchanged\nline3");
        assertFalse(result.matches());
        assertNotNull(result.diff());
        String failure = result.formatFailure();
        assertTrue(failure.contains("Snapshot mismatch"));
        assertTrue(failure.contains("update-snapshots"));
    }

    @Test
    void matchResult_created_accessorWorks() {
        var created = new SnapshotManager.MatchResult(true, null, true, null);
        assertTrue(created.created());
        var notCreated = new SnapshotManager.MatchResult(true, null, false, null);
        assertFalse(notCreated.created());
    }

    static class CollectingHandler extends FailureHandler {
        final List<String> messages = new ArrayList<>();

        @Override
        public void fail(String format, Object... args) {
            messages.add(args.length == 0 ? format : String.format(format, args));
        }
    }

    @Test
    void assertThatSnapshot_alias_works() {
        var manager = new SnapshotManager(tempDir);
        assertDoesNotThrow(() ->
                SnapshotReality.assertThatSnapshot("hello", manager)
                        .matchesSnapshot("AliasTest", "alias"));
    }

    @Test
    void assertThatSnapshot_default_alias_works() {
        assertDoesNotThrow(() ->
                SnapshotReality.assertThatSnapshot("alias-test")
                        .matchesSnapshot("AliasDefaultTest", "alias"));
    }

    @Test
    void strictCI_preventsSnapshotCreation() {
        var manager = new SnapshotManager(tempDir);
        System.setProperty("realitycheck.strict-ci", "true");
        try {
            SnapshotManager.MatchResult result = manager.match("StrictCITest", "method", "content");
            assertFalse(result.matches());
            assertNotNull(result.ciMessage());
            assertTrue(result.ciMessage().contains("strict-CI"));
        } finally {
            System.clearProperty("realitycheck.strict-ci");
        }
    }

    @Test
    void defaultSerializer_withObjectToString_failsFastWithHelpfulMessage() {
        var manager = new SnapshotManager(tempDir);
        var handler = new CollectingHandler();
        var noToString = new Object() {};
        var check = new SnapshotCheck<>(noToString, handler, manager);
        check.matchesSnapshot("DefaultSerializerTest", "noToString");
        assertFalse(handler.messages.isEmpty());
        assertTrue(handler.messages.get(0).contains("does not override toString()"),
                () -> "unexpected message: " + handler.messages.get(0));
        assertTrue(handler.messages.get(0).contains("serializedWith"),
                () -> "unexpected message: " + handler.messages.get(0));
    }

    @Test
    void defaultSerializer_withActualNull_doesNotTriggerToStringCheck() {
        var manager = new SnapshotManager(tempDir);
        var handler = new CollectingHandler();
        var check = new SnapshotCheck<>(null, handler, manager);
        assertDoesNotThrow(() -> check.matchesSnapshot("NullActualTest", "nullValue"));
    }

    @Test
    void defaultSerializer_withOverriddenToString_proceedsNormally() {
        var manager = new SnapshotManager(tempDir);
        var handler = new CollectingHandler();
        var check = new SnapshotCheck<>("a string", handler, manager);
        assertDoesNotThrow(() -> check.matchesSnapshot("OverriddenToStringTest", "stringValue"));
        assertTrue(handler.messages.isEmpty(), "no failure expected for String (overrides toString)");
    }

    @Test
    void softMode_matchesSnapshot_collectsFailureWithoutThrowing() throws IOException {
        var manager = new SnapshotManager(tempDir);
        Path dir = tempDir.resolve("SoftTest");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("method.snap"), "expected");

        var handler = new CollectingHandler();
        var check = new SnapshotCheck<>("actual", handler, manager);
        check.matchesSnapshot("SoftTest", "method");
        assertFalse(handler.messages.isEmpty());
        assertTrue(handler.messages.get(0).contains("Snapshot mismatch"));
    }

    @Test
    void matchResult_formatFailure_includesCiMessage_whenPresent() {
        // Covers the `if (ciMessage != null)` true-branch in formatFailure()
        var result = new SnapshotManager.MatchResult(false, null, false, "Snapshot missing in CI");
        String msg = result.formatFailure();
        assertTrue(msg.contains("Snapshot missing in CI"));
        assertTrue(msg.contains("update snapshots"));
    }
}
