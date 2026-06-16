package io.github.imetaxas.realitycheck;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Fluent assertions for files ({@link Path} and {@link java.io.File}).
 * Includes content comparison with structured diff output on failure.
 */
public final class FileCheck extends AbstractCheck<FileCheck, Path> {

    FileCheck(Path actual, FailureHandler handler) {
        super(actual, handler);
    }

    public FileCheck exists() {
        if (!Files.exists(actual())) {
            failureHandler().fail("expected file to exist: <%s>",
                    actual());
        }
        return self();
    }

    public FileCheck doesNotExist() {
        if (Files.exists(actual())) {
            failureHandler().fail("expected file not to exist: <%s>",
                    actual());
        }
        return self();
    }

    public FileCheck isDirectory() {
        if (!Files.isDirectory(actual())) {
            failureHandler().fail("expected a directory but was a file: <%s>",
                    actual());
        }
        return self();
    }

    public FileCheck isRegularFile() {
        if (!Files.isRegularFile(actual())) {
            failureHandler().fail("expected a regular file: <%s>",
                    actual());
        }
        return self();
    }

    public FileCheck isReadable() {
        if (!Files.isReadable(actual())) {
            failureHandler().fail("expected readable file: <%s>",
                    actual());
        }
        return self();
    }

    public FileCheck isWritable() {
        if (!Files.isWritable(actual())) {
            failureHandler().fail("expected writable file: <%s>",
                    actual());
        }
        return self();
    }

    public FileCheck isHidden() {
        try {
            if (!Files.isHidden(actual())) {
                failureHandler().fail("expected hidden file: <%s>",
                        actual());
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return self();
    }

    public FileCheck isNotHidden() {
        try {
            if (Files.isHidden(actual())) {
                failureHandler().fail("expected non-hidden file: <%s>",
                        actual());
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return self();
    }

    public FileCheck isEmpty() {
        try {
            long size = Files.size(actual());
            if (size != 0) {
                failureHandler().fail("expected empty file but size was: <%d> bytes", size);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return self();
    }

    public FileCheck isNotEmpty() {
        try {
            if (Files.size(actual()) == 0) {
                failureHandler().fail("expected non-empty file: <%s>",
                        actual());
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return self();
    }

    public FileCheck hasSize(long expectedBytes) {
        try {
            long size = Files.size(actual());
            if (size != expectedBytes) {
                failureHandler().fail("expected file size <%d> bytes but was: <%d> bytes",
                        expectedBytes, size);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return self();
    }

    public FileCheck hasExtension(String expected) {
        if (actual().getFileName() == null) {
            failureHandler().fail("expected extension <%s> but path has no file name component: <%s>",
                    expected, actual());
            return self();
        }
        String name = actual().getFileName().toString();
        int dot = name.lastIndexOf('.');
        String ext = (dot > 0) ? name.substring(dot + 1) : "";
        if (!ext.equals(expected)) {
            failureHandler().fail("expected extension <%s> but was: <%s>",
                    expected, ext);
        }
        return self();
    }

    public FileCheck hasName(String expected) {
        if (actual().getFileName() == null) {
            failureHandler().fail("expected file name <%s> but path has no file name component: <%s>",
                    expected, actual());
            return self();
        }
        String name = actual().getFileName().toString();
        if (!name.equals(expected)) {
            failureHandler().fail("expected file name <%s> but was: <%s>",
                    expected, name);
        }
        return self();
    }

    public FileCheck hasLineCount(int expected) {
        try (Stream<String> lines = Files.lines(actual())) {
            long count = lines.count();
            if (count != expected) {
                failureHandler().fail("expected <%d> lines but file has: <%d> lines",
                        expected, count);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return self();
    }

    public FileCheck containsLine(String expectedLine) {
        try (Stream<String> lines = Files.lines(actual())) {
            if (lines.noneMatch(expectedLine::equals)) {
                failureHandler().fail("expected file to contain line: <%s>",
                        expectedLine);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return self();
    }

    public FileCheck hasSameContentAs(Path expected) {
        try {
            String actualContent = Files.readString(actual());
            String expectedContent = Files.readString(expected);
            if (!actualContent.equals(expectedContent)) {
                var diff = Diff.of(expectedContent, actualContent);
                failureHandler().fail("file contents differ:\n%s",
                        diff.formatCompact(3));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return self();
    }

    public FileCheck hasNotSameContentAs(Path expected) {
        try {
            String actualContent = Files.readString(actual());
            String expectedContent = Files.readString(expected);
            if (actualContent.equals(expectedContent)) {
                failureHandler().fail("expected files to have different content but they are identical");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return self();
    }

    // ── Directory assertions ──────────────────────────────────────────────

    /**
     * Asserts the directory is empty (contains no entries).
     */
    public FileCheck isEmptyDirectory() {
        if (!requireDirectory("isEmptyDirectory")) return self();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(actual())) {
            if (stream.iterator().hasNext()) {
                failureHandler().fail("expected an empty directory but <%s> has entries",
                        actual());
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return self();
    }

    /**
     * Asserts the directory is not empty (has at least one entry).
     */
    public FileCheck isNonEmptyDirectory() {
        if (!requireDirectory("isNonEmptyDirectory")) return self();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(actual())) {
            if (!stream.iterator().hasNext()) {
                failureHandler().fail("expected a non-empty directory: <%s>",
                        actual());
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return self();
    }

    /**
     * Asserts that the directory contains at least one entry matching the predicate.
     *
     * <pre>{@code
     * checkThat(dir).isDirectoryContaining(p -> p.getFileName().toString().endsWith(".csv"));
     * }</pre>
     */
    public FileCheck isDirectoryContaining(Predicate<Path> filter) {
        if (!requireDirectory("isDirectoryContaining")) return self();
        try (Stream<Path> entries = Files.list(actual())) {
            if (entries.noneMatch(filter)) {
                List<String> names = listEntryNames();
                failureHandler().fail("expected directory <%s> to contain an entry matching the filter but entries were: %s",
                        actual(), names);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return self();
    }

    /**
     * Asserts that the directory contains at least one entry matching the glob pattern.
     *
     * <pre>{@code
     * checkThat(dir).isDirectoryContaining("*.csv");
     * checkThat(dir).isDirectoryContaining("report-202?-*.pdf");
     * }</pre>
     */
    public FileCheck isDirectoryContaining(String globPattern) {
        if (!requireDirectory("isDirectoryContaining")) return self();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(actual(), globPattern)) {
            if (!stream.iterator().hasNext()) {
                List<String> names = listEntryNames();
                failureHandler().fail("expected directory <%s> to contain entry matching <%s> but entries were: %s",
                        actual(), globPattern, names);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return self();
    }

    /**
     * Asserts that no entries in the directory match the predicate.
     */
    public FileCheck isDirectoryNotContaining(Predicate<Path> filter) {
        if (!requireDirectory("isDirectoryNotContaining")) return self();
        try (Stream<Path> entries = Files.list(actual())) {
            List<String> matching = entries.filter(filter)
                    .map(p -> p.getFileName().toString())
                    .toList();
            if (!matching.isEmpty()) {
                failureHandler().fail("expected directory <%s> to contain no matching entries but found: %s",
                        actual(), matching);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return self();
    }

    /**
     * Asserts that no entries in the directory match the glob pattern.
     */
    public FileCheck isDirectoryNotContaining(String globPattern) {
        if (!requireDirectory("isDirectoryNotContaining")) return self();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(actual(), globPattern)) {
            List<String> matching = new ArrayList<>();
            stream.forEach(p -> matching.add(p.getFileName().toString()));
            if (!matching.isEmpty()) {
                failureHandler().fail("expected directory <%s> to contain no entries matching <%s> but found: %s",
                        actual(), globPattern, matching);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return self();
    }

    /**
     * Asserts that the directory contains a file with exactly this name.
     */
    public FileCheck directoryContainsFile(String fileName) {
        if (!requireDirectory("directoryContainsFile")) return self();
        Path child = actual().resolve(fileName);
        if (!Files.exists(child)) {
            List<String> names = listEntryNames();
            failureHandler().fail("expected directory <%s> to contain <%s> but entries were: %s",
                    actual(), fileName, names);
        }
        return self();
    }

    /**
     * Asserts that the directory contains exactly the given number of entries.
     */
    public FileCheck directoryHasEntryCount(int expected) {
        if (!requireDirectory("directoryHasEntryCount")) return self();
        try (Stream<Path> entries = Files.list(actual())) {
            long count = entries.count();
            if (count != expected) {
                failureHandler().fail("expected directory <%s> to have <%d> entries but had: <%d>",
                        actual(), expected, count);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return self();
    }

    /**
     * Asserts that all entries in the directory match the predicate.
     *
     * <pre>{@code
     * checkThat(dir).hasDirectoryContentMatching(
     *     p -> p.getFileName().toString().endsWith(".json"),
     *     "all files should be JSON");
     * }</pre>
     */
    public FileCheck hasDirectoryContentMatching(Predicate<Path> filter, String description) {
        if (!requireDirectory("hasDirectoryContentMatching")) return self();
        try (Stream<Path> entries = Files.list(actual())) {
            List<String> nonMatching = entries.filter(filter.negate())
                    .map(p -> p.getFileName().toString())
                    .toList();
            if (!nonMatching.isEmpty()) {
                failureHandler().fail("expected all entries in <%s> to match [%s] but these did not: %s",
                        actual(), description, nonMatching);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return self();
    }

    private boolean requireDirectory(String method) {
        if (!Files.isDirectory(actual())) {
            failureHandler().fail("%s requires a directory but <%s> is not a directory",
                    method, actual());
            return false;
        }
        return true;
    }

    private List<String> listEntryNames() {
        try (Stream<Path> entries = Files.list(actual())) {
            return entries.map(p -> p.getFileName().toString()).sorted().toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    // ── File content assertions ──────────────────────────────────────────

    public FileCheck hasContent(String expectedContent) {
        try {
            String content = Files.readString(actual());
            if (!content.equals(expectedContent)) {
                var diff = Diff.of(expectedContent, content);
                failureHandler().fail("file content differs from expected:\n%s",
                        diff.formatCompact(3));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return self();
    }
}
