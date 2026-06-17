package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.AclFileAttributeView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileCheckTest {

    @TempDir
    Path tempDir;

    @Test
    void checkThat_nullFile_yieldsCheckWithNullActual() {
        // Covers the CheckFacade.file(null, handler) ternary true-branch
        FileCheck check = CheckFacade.file(null, new SoftFailureHandler());
        assertNull(check.actual());
    }

    @Test
    void exists_passes() throws IOException {
        Path file = Files.createFile(tempDir.resolve("test.txt"));
        assertDoesNotThrow(() -> checkThat(file).exists());
    }

    @Test
    void exists_fails() {
        Path file = tempDir.resolve("nonexistent.txt");
        assertThrows(AssertionError.class, () -> checkThat(file).exists());
    }

    @Test
    void doesNotExist_passes() {
        assertDoesNotThrow(() -> checkThat(tempDir.resolve("nope.txt")).doesNotExist());
    }

    @Test
    void isDirectory_passes() {
        assertDoesNotThrow(() -> checkThat(tempDir).isDirectory());
    }

    @Test
    void isRegularFile_passes() throws IOException {
        Path file = Files.createFile(tempDir.resolve("test.txt"));
        assertDoesNotThrow(() -> checkThat(file).isRegularFile());
    }

    @Test
    void hasExtension_passes() throws IOException {
        Path file = Files.createFile(tempDir.resolve("data.csv"));
        assertDoesNotThrow(() -> checkThat(file).hasExtension("csv"));
    }

    @Test
    void hasExtension_failsWithActualExtension() throws IOException {
        Path file = Files.createFile(tempDir.resolve("data.csv"));
        var e = assertThrows(AssertionError.class, () -> checkThat(file).hasExtension("txt"));
        assertTrue(e.getMessage().contains("csv"));
    }

    @Test
    void hasName_passes() throws IOException {
        Path file = Files.createFile(tempDir.resolve("readme.md"));
        assertDoesNotThrow(() -> checkThat(file).hasName("readme.md"));
    }

    @Test
    void isEmpty_passes() throws IOException {
        Path file = Files.createFile(tempDir.resolve("empty.txt"));
        assertDoesNotThrow(() -> checkThat(file).isEmpty());
    }

    @Test
    void isNotEmpty_passes() throws IOException {
        Path file = Files.writeString(tempDir.resolve("data.txt"), "content");
        assertDoesNotThrow(() -> checkThat(file).isNotEmpty());
    }

    @Test
    void hasLineCount_passes() throws IOException {
        Path file = Files.writeString(tempDir.resolve("lines.txt"), "a\nb\nc\n");
        assertDoesNotThrow(() -> checkThat(file).hasLineCount(3));
    }

    @Test
    void containsLine_passes() throws IOException {
        Path file = Files.writeString(tempDir.resolve("data.txt"), "hello\nworld\n");
        assertDoesNotThrow(() -> checkThat(file).containsLine("world"));
    }

    @Test
    void hasSameContentAs_passes() throws IOException {
        Path a = Files.writeString(tempDir.resolve("a.txt"), "same content");
        Path b = Files.writeString(tempDir.resolve("b.txt"), "same content");
        assertDoesNotThrow(() -> checkThat(a).hasSameContentAs(b));
    }

    @Test
    void hasSameContentAs_failsWithDiff() throws IOException {
        Path a = Files.writeString(tempDir.resolve("a.txt"), "line1\nline2\n");
        Path b = Files.writeString(tempDir.resolve("b.txt"), "line1\nchanged\n");
        var e = assertThrows(AssertionError.class, () -> checkThat(a).hasSameContentAs(b));
        assertTrue(e.getMessage().contains("differ"));
    }

    @Test
    void hasNotSameContentAs_passes() throws IOException {
        Path a = Files.writeString(tempDir.resolve("a.txt"), "aaa");
        Path b = Files.writeString(tempDir.resolve("b.txt"), "bbb");
        assertDoesNotThrow(() -> checkThat(a).hasNotSameContentAs(b));
    }

    @Test
    void chaining_works() throws IOException {
        Path file = Files.writeString(tempDir.resolve("data.csv"), "a,b\n1,2\n");
        assertDoesNotThrow(() -> checkThat(file).exists().isRegularFile().hasExtension("csv").isNotEmpty());
    }

    @Test
    void hasNotSameContentAs_failsWhenIdentical() throws IOException {
        Path a = Files.writeString(tempDir.resolve("a.txt"), "same");
        Path b = Files.writeString(tempDir.resolve("b.txt"), "same");
        var e = assertThrows(AssertionError.class, () -> checkThat(a).hasNotSameContentAs(b));
        assertTrue(e.getMessage().contains("identical"));
    }

    @Test
    void isEmpty_failsWhenFileHasContent() throws IOException {
        Path f = Files.writeString(tempDir.resolve("n.txt"), "x");
        assertThrows(AssertionError.class, () -> checkThat(f).isEmpty());
    }

    @Test
    void isNotEmpty_failsWhenFileIsZeroBytes() throws IOException {
        Path f = Files.createFile(tempDir.resolve("z.txt"));
        assertThrows(AssertionError.class, () -> checkThat(f).isNotEmpty());
    }

    @Test
    void hasName_failsWhenWrongName() throws IOException {
        Path f = Files.createFile(tempDir.resolve("real.txt"));
        var e = assertThrows(AssertionError.class, () -> checkThat(f).hasName("other.txt"));
        assertTrue(e.getMessage().contains("real.txt"));
    }

    @Test
    void hasExtension_failsWhenNoDotInName() throws IOException {
        Path f = Files.createFile(tempDir.resolve("README"));
        assertThrows(AssertionError.class, () -> checkThat(f).hasExtension("md"));
    }

    @Test
    void hasSize_failsOnMismatch() throws IOException {
        Path f = Files.writeString(tempDir.resolve("s.txt"), "abcd");
        assertThrows(AssertionError.class, () -> checkThat(f).hasSize(1L));
    }

    @Test
    void hasSize_passes() throws IOException {
        Path f = Files.writeString(tempDir.resolve("s.txt"), "hi");
        assertDoesNotThrow(() -> checkThat(f).hasSize(2L));
    }

    @Test
    void hasContent_failsOnMismatch() throws IOException {
        Path f = Files.writeString(tempDir.resolve("c.txt"), "actual");
        assertThrows(AssertionError.class, () -> checkThat(f).hasContent("expected"));
    }

    @Test
    void hasContent_passes() throws IOException {
        Path f = Files.writeString(tempDir.resolve("c.txt"), "exact");
        assertDoesNotThrow(() -> checkThat(f).hasContent("exact"));
    }

    @Test
    void isDirectoryContaining_predicate_passes() throws IOException {
        Files.writeString(tempDir.resolve("a.log"), "x");
        assertDoesNotThrow(() -> checkThat(tempDir)
                .isDirectoryContaining(p -> p.getFileName().toString().endsWith(".log")));
    }

    @Test
    void isDirectoryContaining_predicate_fails() throws IOException {
        Files.writeString(tempDir.resolve("only.txt"), "x");
        assertThrows(AssertionError.class,
                () -> checkThat(tempDir).isDirectoryContaining(p -> p.getFileName().toString().endsWith(".log")));
    }

    @Test
    void hasDirectoryContentMatching_failsWhenOneFileDoesNotMatch() throws IOException {
        Files.writeString(tempDir.resolve("good.json"), "{}");
        Files.writeString(tempDir.resolve("bad.txt"), "nope");
        assertThrows(AssertionError.class,
                () -> checkThat(tempDir).hasDirectoryContentMatching(
                        p -> p.getFileName().toString().endsWith(".json"),
                        "json only"));
    }

    @Test
    void hasDirectoryContentMatching_passes() throws IOException {
        Files.writeString(tempDir.resolve("a.json"), "{}");
        Files.writeString(tempDir.resolve("b.json"), "[]");
        assertDoesNotThrow(() -> checkThat(tempDir).hasDirectoryContentMatching(
                p -> p.getFileName().toString().endsWith(".json"),
                "json only"));
    }

    @Test
    void directoryMethods_requireDirectory_notFile() throws IOException {
        Path file = Files.writeString(tempDir.resolve("f.txt"), "data");
        assertThrows(AssertionError.class, () -> checkThat(file).isDirectoryContaining(p -> true));
        assertThrows(AssertionError.class,
                () -> checkThat(file).hasDirectoryContentMatching(p -> true, "desc"));
    }

    @Test
    void containsLine_failsWhenMissing() throws IOException {
        Path f = Files.writeString(tempDir.resolve("lines.txt"), "a\nb\n");
        assertThrows(AssertionError.class, () -> checkThat(f).containsLine("c"));
    }

    @Test
    void hasLineCount_failsWhenWrong() throws IOException {
        Path f = Files.writeString(tempDir.resolve("n.txt"), "one\ntwo\n");
        assertThrows(AssertionError.class, () -> checkThat(f).hasLineCount(5));
    }

    @Test
    void hasSameContentAs_throwsUncheckedIOExceptionWhenExpectedPathIsDirectory() throws IOException {
        Path file = Files.writeString(tempDir.resolve("f.txt"), "x");
        assertThrows(UncheckedIOException.class, () -> checkThat(file).hasSameContentAs(tempDir));
    }

    @Test
    void hasNotSameContentAs_throwsUncheckedIOExceptionWhenExpectedPathIsDirectory() throws IOException {
        Path file = Files.writeString(tempDir.resolve("f.txt"), "x");
        assertThrows(UncheckedIOException.class, () -> checkThat(file).hasNotSameContentAs(tempDir));
    }

    @Test
    void hasContent_throwsUncheckedIOExceptionWhenPathIsDirectory() {
        assertThrows(UncheckedIOException.class, () -> checkThat(tempDir).hasContent("x"));
    }

    @Test
    void isDirectoryContaining_glob_passes() throws IOException {
        Files.writeString(tempDir.resolve("x.txt"), "1");
        assertDoesNotThrow(() -> checkThat(tempDir).isDirectoryContaining("*.txt"));
    }

    @Test
    void isDirectoryContaining_glob_fails() throws IOException {
        Files.writeString(tempDir.resolve("y.pdf"), "1");
        assertThrows(AssertionError.class, () -> checkThat(tempDir).isDirectoryContaining("*.txt"));
    }

    @Test
    void isEmptyDirectory_passes(@TempDir Path emptyDir) {
        assertDoesNotThrow(() -> checkThat(emptyDir).isEmptyDirectory());
    }

    @Test
    void directoryHasEntryCount_passes() throws IOException {
        Files.writeString(tempDir.resolve("a.txt"), "a");
        Files.writeString(tempDir.resolve("b.txt"), "b");
        assertDoesNotThrow(() -> checkThat(tempDir).directoryHasEntryCount(2));
    }

    @Test
    void hasLineCount_throwsUncheckedIOExceptionWhenPathIsDirectory() {
        assertThrows(UncheckedIOException.class, () -> checkThat(tempDir).hasLineCount(1));
    }

    @Test
    void containsLine_throwsUncheckedIOExceptionWhenPathIsDirectory() {
        assertThrows(UncheckedIOException.class, () -> checkThat(tempDir).containsLine("x"));
    }

    @Test
    void isDirectoryNotContaining_glob_passes() throws IOException {
        Files.writeString(tempDir.resolve("a.txt"), "1");
        assertDoesNotThrow(() -> checkThat(tempDir).isDirectoryNotContaining("*.pdf"));
    }

    @Test
    void isDirectoryNotContaining_glob_fails() throws IOException {
        Files.writeString(tempDir.resolve("r.pdf"), "1");
        assertThrows(AssertionError.class, () -> checkThat(tempDir).isDirectoryNotContaining("*.pdf"));
    }

    @Test
    void doesNotExist_failsWhenFileExists() throws IOException {
        Path f = Files.writeString(tempDir.resolve("e.txt"), "1");
        assertThrows(AssertionError.class, () -> checkThat(f).doesNotExist());
    }

    @Test
    void isDirectory_failsWhenRegularFile() throws IOException {
        Path f = Files.writeString(tempDir.resolve("f.txt"), "1");
        assertThrows(AssertionError.class, () -> checkThat(f).isDirectory());
    }

    @Test
    void isRegularFile_failsWhenDirectory() {
        assertThrows(AssertionError.class, () -> checkThat(tempDir).isRegularFile());
    }

    private static Path nonExistent() {
        return Path.of(System.getProperty("java.io.tmpdir"),
                "realitycheck-absent-" + UUID.randomUUID());
    }

    @Nested
    class RootPathEdgeCases {

        @Test
        void hasExtension_failsGracefullyForRootPath() {
            Path root = Path.of("/");
            assertThrows(AssertionError.class, () -> checkThat(root).hasExtension("txt"));
        }

        @Test
        void hasName_failsGracefullyForRootPath() {
            Path root = Path.of("/");
            assertThrows(AssertionError.class, () -> checkThat(root).hasName("file.txt"));
        }
    }

    @Nested
    class FileMethodIOExceptions {

        private static final boolean IS_WINDOWS =
                System.getProperty("os.name").toLowerCase().startsWith("win");

        @Test
        void isEmpty_throwsUncheckedIOException() {
            assertThrows(UncheckedIOException.class, () -> checkThat(nonExistent()).isEmpty());
        }

        @Test
        void isNotEmpty_throwsUncheckedIOException() {
            assertThrows(UncheckedIOException.class, () -> checkThat(nonExistent()).isNotEmpty());
        }

        @Test
        void hasSize_throwsUncheckedIOException() {
            assertThrows(UncheckedIOException.class, () -> checkThat(nonExistent()).hasSize(0));
        }

        @Test
        void hasLineCount_throwsUncheckedIOException() {
            assertThrows(UncheckedIOException.class, () -> checkThat(nonExistent()).hasLineCount(1));
        }

        @Test
        void containsLine_throwsUncheckedIOException() {
            assertThrows(
                    UncheckedIOException.class, () -> checkThat(nonExistent()).containsLine("x"));
        }

        @Test
        void hasSameContentAs_throwsUncheckedIOException() {
            assertThrows(
                    UncheckedIOException.class,
                    () -> checkThat(nonExistent()).hasSameContentAs(nonExistent()));
        }

        @Test
        void hasNotSameContentAs_throwsUncheckedIOException() {
            assertThrows(
                    UncheckedIOException.class,
                    () -> checkThat(nonExistent()).hasNotSameContentAs(nonExistent()));
        }

        @Test
        void hasContent_throwsUncheckedIOException() {
            assertThrows(
                    UncheckedIOException.class,
                    () -> checkThat(nonExistent()).hasContent("anything"));
        }

        @Test
        void isHidden_throwsUncheckedIOException_onWindows() {
            assumeTrue(IS_WINDOWS, "Files.isHidden only throws IOException on Windows");
            assertThrows(UncheckedIOException.class, () -> checkThat(nonExistent()).isHidden());
        }

        @Test
        void isNotHidden_throwsUncheckedIOException_onWindows() {
            assumeTrue(IS_WINDOWS, "Files.isHidden only throws IOException on Windows");
            assertThrows(UncheckedIOException.class, () -> checkThat(nonExistent()).isNotHidden());
        }
    }

    @Nested
    class ReadableWritable {

        private Path resourceFile(String name) throws URISyntaxException {
            return Path.of(Objects.requireNonNull(
                    getClass().getClassLoader().getResource(name)).toURI());
        }

        @Test
        void isReadable_passesForResourceFile() throws URISyntaxException {
            Path file = resourceFile("readable.txt");
            assertDoesNotThrow(() -> checkThat(file).isReadable());
        }

        @Test
        void isReadable_failsForNonExistentFile() {
            assertThrows(AssertionError.class, () -> checkThat(nonExistent()).isReadable());
        }

        @Test
        void isWritable_passesForTempFile() throws IOException {
            Path file = Files.createTempFile("writable-", ".txt");
            try {
                assertDoesNotThrow(() -> checkThat(file).isWritable());
            } finally {
                Files.deleteIfExists(file);
            }
        }

        @Test
        void isWritable_failsForNonExistentFile() {
            assertThrows(AssertionError.class, () -> checkThat(nonExistent()).isWritable());
        }

        @Test
        void isReadable_chainsWithOtherChecks() throws URISyntaxException {
            Path file = resourceFile("readable.txt");
            assertDoesNotThrow(() -> checkThat(file).exists().isReadable().isRegularFile());
        }
    }

    @Nested
    class HiddenFiles {

        private static final boolean IS_WINDOWS =
                System.getProperty("os.name").toLowerCase().startsWith("win");

        private Path createHiddenFile(@TempDir Path dir) throws IOException {
            if (IS_WINDOWS) {
                Path file = Files.createFile(dir.resolve("hidden-win.txt"));
                Files.setAttribute(file, "dos:hidden", true);
                return file;
            } else {
                return Files.createFile(dir.resolve(".hidden-unix"));
            }
        }

        @Test
        void isHidden_passesForHiddenFile(@TempDir Path dir) throws IOException {
            Path hidden = createHiddenFile(dir);
            assertDoesNotThrow(() -> checkThat(hidden).isHidden());
        }

        @Test
        void isHidden_failsForVisibleFile(@TempDir Path dir) throws IOException {
            Path visible = Files.createFile(dir.resolve("visible.txt"));
            assertThrows(AssertionError.class, () -> checkThat(visible).isHidden());
        }

        @Test
        void isNotHidden_passesForVisibleFile(@TempDir Path dir) throws IOException {
            Path visible = Files.createFile(dir.resolve("visible.txt"));
            assertDoesNotThrow(() -> checkThat(visible).isNotHidden());
        }

        @Test
        void isNotHidden_failsForHiddenFile(@TempDir Path dir) throws IOException {
            Path hidden = createHiddenFile(dir);
            assertThrows(AssertionError.class, () -> checkThat(hidden).isNotHidden());
        }
    }

    @Nested
    class DirectoryMethodIOExceptions {

        private static final boolean IS_WINDOWS =
                System.getProperty("os.name").toLowerCase().startsWith("win");

        private List<AclEntry> savedAcl;

        private Path unreadableDir(Path base) throws IOException {
            Path dir = Files.createDirectory(base.resolve("noaccess-" + UUID.randomUUID()));
            if (IS_WINDOWS) {
                AclFileAttributeView aclView =
                        Files.getFileAttributeView(dir, AclFileAttributeView.class);
                assumeTrue(aclView != null, "Platform does not support ACL permissions");
                savedAcl = aclView.getAcl();
                AclEntry deny = AclEntry.newBuilder()
                        .setType(AclEntryType.DENY)
                        .setPrincipal(aclView.getOwner())
                        .setPermissions(AclEntryPermission.LIST_DIRECTORY,
                                AclEntryPermission.READ_DATA)
                        .build();
                List<AclEntry> acl = new ArrayList<>(savedAcl);
                acl.add(0, deny);
                aclView.setAcl(acl);
            } else {
                boolean removed = dir.toFile().setReadable(false);
                assumeTrue(removed, "Platform does not support removing read permission");
            }
            return dir;
        }

        private void restore(Path dir) {
            try {
                if (IS_WINDOWS && savedAcl != null) {
                    AclFileAttributeView aclView =
                            Files.getFileAttributeView(dir, AclFileAttributeView.class);
                    if (aclView != null) aclView.setAcl(savedAcl);
                } else {
                    dir.toFile().setReadable(true);
                }
            } catch (IOException ignored) {
            }
        }

        @Test
        void isEmptyDirectory_throwsUncheckedIOException(@TempDir Path base) throws IOException {
            Path dir = unreadableDir(base);
            try {
                assertThrows(UncheckedIOException.class, () -> checkThat(dir).isEmptyDirectory());
            } finally {
                restore(dir);
            }
        }

        @Test
        void isNonEmptyDirectory_throwsUncheckedIOException(@TempDir Path base) throws IOException {
            Path dir = unreadableDir(base);
            try {
                assertThrows(UncheckedIOException.class, () -> checkThat(dir).isNonEmptyDirectory());
            } finally {
                restore(dir);
            }
        }

        @Test
        void isDirectoryContaining_predicate_throwsUncheckedIOException(@TempDir Path base)
                throws IOException {
            Path dir = unreadableDir(base);
            try {
                assertThrows(
                        UncheckedIOException.class,
                        () -> checkThat(dir).isDirectoryContaining(p -> true));
            } finally {
                restore(dir);
            }
        }

        @Test
        void isDirectoryContaining_glob_throwsUncheckedIOException(@TempDir Path base)
                throws IOException {
            Path dir = unreadableDir(base);
            try {
                assertThrows(
                        UncheckedIOException.class,
                        () -> checkThat(dir).isDirectoryContaining("*"));
            } finally {
                restore(dir);
            }
        }

        @Test
        void isDirectoryNotContaining_predicate_throwsUncheckedIOException(@TempDir Path base)
                throws IOException {
            Path dir = unreadableDir(base);
            try {
                assertThrows(
                        UncheckedIOException.class,
                        () -> checkThat(dir).isDirectoryNotContaining(p -> true));
            } finally {
                restore(dir);
            }
        }

        @Test
        void isDirectoryNotContaining_glob_throwsUncheckedIOException(@TempDir Path base)
                throws IOException {
            Path dir = unreadableDir(base);
            try {
                assertThrows(
                        UncheckedIOException.class,
                        () -> checkThat(dir).isDirectoryNotContaining("*"));
            } finally {
                restore(dir);
            }
        }

        @Test
        void directoryHasEntryCount_throwsUncheckedIOException(@TempDir Path base)
                throws IOException {
            Path dir = unreadableDir(base);
            try {
                assertThrows(
                        UncheckedIOException.class,
                        () -> checkThat(dir).directoryHasEntryCount(0));
            } finally {
                restore(dir);
            }
        }

        @Test
        void hasDirectoryContentMatching_throwsUncheckedIOException(@TempDir Path base)
                throws IOException {
            Path dir = unreadableDir(base);
            try {
                assertThrows(
                        UncheckedIOException.class,
                        () -> checkThat(dir).hasDirectoryContentMatching(p -> true, "any"));
            } finally {
                restore(dir);
            }
        }
    }
}
