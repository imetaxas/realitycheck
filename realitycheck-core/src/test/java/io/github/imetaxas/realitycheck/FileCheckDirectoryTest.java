package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileCheckDirectoryTest {

    @Test
    void isEmptyDirectory_passes(@TempDir Path dir) {
        assertDoesNotThrow(() -> checkThat(dir).isEmptyDirectory());
    }

    @Test
    void isEmptyDirectory_failsOnNonEmpty(@TempDir Path dir) throws Exception {
        Files.writeString(dir.resolve("file.txt"), "data");
        assertThrows(AssertionError.class, () -> checkThat(dir).isEmptyDirectory());
    }

    @Test
    void isEmptyDirectory_failsOnFile(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("file.txt");
        Files.writeString(f, "data");
        assertThrows(AssertionError.class, () -> checkThat(f).isEmptyDirectory());
    }

    @Test
    void isNonEmptyDirectory_passes(@TempDir Path dir) throws Exception {
        Files.writeString(dir.resolve("file.txt"), "data");
        assertDoesNotThrow(() -> checkThat(dir).isNonEmptyDirectory());
    }

    @Test
    void isNonEmptyDirectory_failsOnEmpty(@TempDir Path dir) {
        assertThrows(AssertionError.class, () -> checkThat(dir).isNonEmptyDirectory());
    }

    @Test
    void isDirectoryContaining_predicate_passes(@TempDir Path dir) throws Exception {
        Files.writeString(dir.resolve("report.csv"), "a,b");
        assertDoesNotThrow(() -> checkThat(dir)
                .isDirectoryContaining(p -> p.getFileName().toString().endsWith(".csv")));
    }

    @Test
    void isDirectoryContaining_predicate_fails(@TempDir Path dir) throws Exception {
        Files.writeString(dir.resolve("report.txt"), "data");
        assertThrows(AssertionError.class, () -> checkThat(dir)
                .isDirectoryContaining(p -> p.getFileName().toString().endsWith(".csv")));
    }

    @Test
    void isDirectoryContaining_glob_passes(@TempDir Path dir) throws Exception {
        Files.writeString(dir.resolve("data.json"), "{}");
        assertDoesNotThrow(() -> checkThat(dir).isDirectoryContaining("*.json"));
    }

    @Test
    void isDirectoryContaining_glob_fails(@TempDir Path dir) throws Exception {
        Files.writeString(dir.resolve("data.txt"), "hi");
        assertThrows(AssertionError.class, () -> checkThat(dir).isDirectoryContaining("*.json"));
    }

    @Test
    void isDirectoryNotContaining_predicate_passes(@TempDir Path dir) throws Exception {
        Files.writeString(dir.resolve("data.txt"), "hi");
        assertDoesNotThrow(() -> checkThat(dir)
                .isDirectoryNotContaining(p -> p.getFileName().toString().endsWith(".csv")));
    }

    @Test
    void isDirectoryNotContaining_predicate_fails(@TempDir Path dir) throws Exception {
        Files.writeString(dir.resolve("data.csv"), "a,b");
        assertThrows(AssertionError.class, () -> checkThat(dir)
                .isDirectoryNotContaining(p -> p.getFileName().toString().endsWith(".csv")));
    }

    @Test
    void isDirectoryNotContaining_glob_passes(@TempDir Path dir) throws Exception {
        Files.writeString(dir.resolve("data.txt"), "hi");
        assertDoesNotThrow(() -> checkThat(dir).isDirectoryNotContaining("*.csv"));
    }

    @Test
    void isDirectoryNotContaining_glob_fails(@TempDir Path dir) throws Exception {
        Files.writeString(dir.resolve("data.csv"), "a,b");
        assertThrows(AssertionError.class, () -> checkThat(dir).isDirectoryNotContaining("*.csv"));
    }

    @Test
    void directoryContainsFile_passes(@TempDir Path dir) throws Exception {
        Files.writeString(dir.resolve("target.txt"), "content");
        assertDoesNotThrow(() -> checkThat(dir).directoryContainsFile("target.txt"));
    }

    @Test
    void directoryContainsFile_fails(@TempDir Path dir) {
        assertThrows(AssertionError.class, () -> checkThat(dir).directoryContainsFile("missing.txt"));
    }

    @Test
    void directoryHasEntryCount_passes(@TempDir Path dir) throws Exception {
        Files.writeString(dir.resolve("a.txt"), "a");
        Files.writeString(dir.resolve("b.txt"), "b");
        assertDoesNotThrow(() -> checkThat(dir).directoryHasEntryCount(2));
    }

    @Test
    void directoryHasEntryCount_fails(@TempDir Path dir) throws Exception {
        Files.writeString(dir.resolve("a.txt"), "a");
        assertThrows(AssertionError.class, () -> checkThat(dir).directoryHasEntryCount(5));
    }

    @Test
    void hasDirectoryContentMatching_passes(@TempDir Path dir) throws Exception {
        Files.writeString(dir.resolve("a.json"), "{}");
        Files.writeString(dir.resolve("b.json"), "[]");
        assertDoesNotThrow(() -> checkThat(dir).hasDirectoryContentMatching(
                p -> p.getFileName().toString().endsWith(".json"), "all JSON"));
    }

    @Test
    void hasDirectoryContentMatching_fails(@TempDir Path dir) throws Exception {
        Files.writeString(dir.resolve("a.json"), "{}");
        Files.writeString(dir.resolve("b.txt"), "text");
        assertThrows(AssertionError.class, () -> checkThat(dir).hasDirectoryContentMatching(
                p -> p.getFileName().toString().endsWith(".json"), "all JSON"));
    }

    @Test
    void hasContent_passes(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("test.txt");
        Files.writeString(f, "hello world");
        assertDoesNotThrow(() -> checkThat(f).hasContent("hello world"));
    }

    @Test
    void hasContent_fails(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("test.txt");
        Files.writeString(f, "hello world");
        assertThrows(AssertionError.class, () -> checkThat(f).hasContent("goodbye"));
    }

    @Test
    void hasSameContentAs_passes(@TempDir Path dir) throws Exception {
        Path a = dir.resolve("a.txt");
        Path b = dir.resolve("b.txt");
        Files.writeString(a, "same");
        Files.writeString(b, "same");
        assertDoesNotThrow(() -> checkThat(a).hasSameContentAs(b));
    }

    @Test
    void hasSameContentAs_fails(@TempDir Path dir) throws Exception {
        Path a = dir.resolve("a.txt");
        Path b = dir.resolve("b.txt");
        Files.writeString(a, "aaa");
        Files.writeString(b, "bbb");
        assertThrows(AssertionError.class, () -> checkThat(a).hasSameContentAs(b));
    }

    @Test
    void hasNotSameContentAs_passes(@TempDir Path dir) throws Exception {
        Path a = dir.resolve("a.txt");
        Path b = dir.resolve("b.txt");
        Files.writeString(a, "aaa");
        Files.writeString(b, "bbb");
        assertDoesNotThrow(() -> checkThat(a).hasNotSameContentAs(b));
    }

    @Test
    void hasNotSameContentAs_fails(@TempDir Path dir) throws Exception {
        Path a = dir.resolve("a.txt");
        Path b = dir.resolve("b.txt");
        Files.writeString(a, "same");
        Files.writeString(b, "same");
        assertThrows(AssertionError.class, () -> checkThat(a).hasNotSameContentAs(b));
    }

    @Test
    void hasLineCount_passes(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("lines.txt");
        Files.writeString(f, "line1\nline2\nline3");
        assertDoesNotThrow(() -> checkThat(f).hasLineCount(3));
    }

    @Test
    void hasLineCount_fails(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("lines.txt");
        Files.writeString(f, "line1\nline2");
        assertThrows(AssertionError.class, () -> checkThat(f).hasLineCount(5));
    }

    @Test
    void containsLine_passes(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("lines.txt");
        Files.writeString(f, "alpha\nbeta\ngamma");
        assertDoesNotThrow(() -> checkThat(f).containsLine("beta"));
    }

    @Test
    void containsLine_fails(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("lines.txt");
        Files.writeString(f, "alpha\nbeta");
        assertThrows(AssertionError.class, () -> checkThat(f).containsLine("omega"));
    }

    @Test
    void hasSize_passes(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("sized.txt");
        Files.writeString(f, "12345");
        assertDoesNotThrow(() -> checkThat(f).hasSize(5));
    }

    @Test
    void hasSize_fails(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("sized.txt");
        Files.writeString(f, "12345");
        assertThrows(AssertionError.class, () -> checkThat(f).hasSize(99));
    }

    @Test
    void hasExtension_passes(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("data.csv");
        Files.writeString(f, "a,b");
        assertDoesNotThrow(() -> checkThat(f).hasExtension("csv"));
    }

    @Test
    void hasExtension_fails(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("data.csv");
        Files.writeString(f, "a,b");
        assertThrows(AssertionError.class, () -> checkThat(f).hasExtension("json"));
    }

    @Test
    void hasName_passes(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("myfile.txt");
        Files.writeString(f, "content");
        assertDoesNotThrow(() -> checkThat(f).hasName("myfile.txt"));
    }

    @Test
    void hasName_fails(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("myfile.txt");
        Files.writeString(f, "content");
        assertThrows(AssertionError.class, () -> checkThat(f).hasName("other.txt"));
    }

    @Test
    void isEmpty_passes(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("empty.txt");
        Files.writeString(f, "");
        assertDoesNotThrow(() -> checkThat(f).isEmpty());
    }

    @Test
    void isEmpty_fails(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("notempty.txt");
        Files.writeString(f, "data");
        assertThrows(AssertionError.class, () -> checkThat(f).isEmpty());
    }

    @Test
    void isNotEmpty_passes(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("notempty.txt");
        Files.writeString(f, "data");
        assertDoesNotThrow(() -> checkThat(f).isNotEmpty());
    }

    @Test
    void isReadable_passes(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("readable.txt");
        Files.writeString(f, "data");
        assertDoesNotThrow(() -> checkThat(f).isReadable());
    }

    @Test
    void isWritable_passes(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("writable.txt");
        Files.writeString(f, "data");
        assertDoesNotThrow(() -> checkThat(f).isWritable());
    }

    @Test
    void isNotHidden_passes(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("visible.txt");
        Files.writeString(f, "data");
        assertDoesNotThrow(() -> checkThat(f).isNotHidden());
    }
}
