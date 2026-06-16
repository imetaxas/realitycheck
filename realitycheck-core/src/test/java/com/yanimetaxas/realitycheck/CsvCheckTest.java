package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CsvCheckTest {

    private static final String TWO_ROW_CSV = "h1,h2\nv1,v2\n";

    @Test
    void isNotEmpty_passes() {
        assertDoesNotThrow(() -> checkThatCsv(TWO_ROW_CSV).isNotEmpty());
    }

    @Test
    void isNotEmpty_fails_when_blank() {
        assertThrows(AssertionError.class, () -> checkThatCsv("   ").isNotEmpty());
    }

    @Test
    void hasSameContentAs_passes() {
        assertDoesNotThrow(() -> checkThatCsv(TWO_ROW_CSV).hasSameContentAs(TWO_ROW_CSV));
    }

    @Test
    void hasSameContentAs_fails_with_diff_message() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatCsv("a\n").hasSameContentAs("b\n"));
        assertTrue(e.getMessage().contains("differs"), () -> e.getMessage());
    }

    @Test
    void hasNotSameContentAs_passes() {
        assertDoesNotThrow(() -> checkThatCsv("a\n").hasNotSameContentAs("b\n"));
    }

    @Test
    void hasNotSameContentAs_fails_when_identical() {
        assertThrows(AssertionError.class,
                () -> checkThatCsv("x\n").hasNotSameContentAs("x\n"));
    }

    @Test
    void hasRowCount_passes() {
        assertDoesNotThrow(() -> checkThatCsv(TWO_ROW_CSV).hasRowCount(2));
    }

    @Test
    void hasRowCount_fails() {
        assertThrows(AssertionError.class, () -> checkThatCsv(TWO_ROW_CSV).hasRowCount(99));
    }

    @Test
    void hasColumnCount_passes() {
        assertDoesNotThrow(() -> checkThatCsv(TWO_ROW_CSV).hasColumnCount(2));
    }

    @Test
    void hasColumnCount_fails() {
        assertThrows(AssertionError.class,
                () -> checkThatCsv("a,b,c\n1,2,3\n").hasColumnCount(2));
    }

    @Test
    void hasColumnCount_fails_when_csv_effectively_empty() {
        assertThrows(AssertionError.class,
                () -> checkThatCsv("\n\n").hasColumnCount(1));
    }

    @Test
    void headerEquals_passes() {
        assertDoesNotThrow(() -> checkThatCsv(TWO_ROW_CSV).headerEquals("h1", "h2"));
    }

    @Test
    void headerEquals_fails() {
        assertThrows(AssertionError.class,
                () -> checkThatCsv(TWO_ROW_CSV).headerEquals("wrong", "h2"));
    }

    @Test
    void headerEquals_fails_when_empty_csv() {
        assertThrows(AssertionError.class,
                () -> checkThatCsv("\n").headerEquals("a"));
    }

    @Test
    void headerContains_passes() {
        assertDoesNotThrow(() -> checkThatCsv(TWO_ROW_CSV).headerContains("h1"));
    }

    @Test
    void headerContains_fails() {
        assertThrows(AssertionError.class,
                () -> checkThatCsv(TWO_ROW_CSV).headerContains("missing"));
    }

    @Test
    void headerHasNoDigits_passes() {
        assertDoesNotThrow(() -> checkThatCsv(TWO_ROW_CSV).headerHasNoDigits());
    }

    @Test
    void headerHasNoDigits_fails_with_numeric_header() {
        assertThrows(AssertionError.class,
                () -> checkThatCsv("1,2,3\na,b,c\n").headerHasNoDigits());
    }

    @Test
    void containsRow_passes() {
        assertDoesNotThrow(() -> checkThatCsv(TWO_ROW_CSV).containsRow("v1", "v2"));
    }

    @Test
    void containsRow_fails() {
        assertThrows(AssertionError.class,
                () -> checkThatCsv(TWO_ROW_CSV).containsRow("nope", "nope"));
    }

    @Test
    void fromFile_nonExistentPath_throwsUncheckedIOException() {
        assertThrows(UncheckedIOException.class,
                () -> checkThatCsvFile(Path.of(System.getProperty("java.io.tmpdir"),
                        "nonexistent-" + System.nanoTime(), "file.csv")));
    }

    @Test
    void isNotEmpty_failsOnNull() {
        assertThrows(AssertionError.class, () -> checkThatCsv(null).isNotEmpty());
    }

    @Test
    void isNotEmpty_failsOnBlank() {
        assertThrows(AssertionError.class, () -> checkThatCsv("   ").isNotEmpty());
    }

    @Test
    void hasColumnCount_failsOnEmptyCsv() {
        assertThrows(AssertionError.class, () -> checkThatCsv("\n\n").hasColumnCount(2));
    }

    @Test
    void headerEquals_failsOnEmptyCsv() {
        assertThrows(AssertionError.class, () -> checkThatCsv("\n").headerEquals("a"));
    }

    @Test
    void headerEquals_failsWithWrongHeaders() {
        assertThrows(AssertionError.class,
                () -> checkThatCsv("a,b\n1,2").headerEquals("x", "y"));
    }

    @Test
    void headerContains_failsWithMissingColumn() {
        assertThrows(AssertionError.class,
                () -> checkThatCsv("a,b\n1,2").headerContains("zzz"));
    }

    @Test
    void headerHasNoDigits_failsWithNumericHeader() {
        assertThrows(AssertionError.class,
                () -> checkThatCsv("123,name\n1,Alice").headerHasNoDigits());
    }

    @Test
    void containsRow_failsWhenRowNotFound() {
        assertThrows(AssertionError.class,
                () -> checkThatCsv("a,b\n1,2").containsRow("9", "9"));
    }

    @Test
    void hasSameContentAs_failsOnDifferentContent() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatCsv("a,b\n1,2").hasSameContentAs("a,b\n3,4"));
        assertTrue(e.getMessage().contains("differs"));
    }

    @Test
    void hasNotSameContentAs_failsOnIdenticalContent() {
        assertThrows(AssertionError.class,
                () -> checkThatCsv("a,b\n1,2").hasNotSameContentAs("a,b\n1,2"));
    }

    @Test
    void hasRowCount_failsWithWrongCount() {
        assertThrows(AssertionError.class,
                () -> checkThatCsv("a,b\n1,2").hasRowCount(99));
    }

    @Test
    void checkThatCsvFile_path(@TempDir Path dir) throws IOException {
        Path csv = Files.writeString(dir.resolve("data.csv"), "a,b\n1,2\n");
        assertDoesNotThrow(() -> checkThatCsvFile(csv).hasRowCount(2));
    }

    @Test
    void checkThatCsvFile_file(@TempDir Path dir) throws IOException {
        File csv = Files.writeString(dir.resolve("data.csv"), "a,b\n1,2\n").toFile();
        assertDoesNotThrow(() -> checkThatCsvFile(csv).hasRowCount(2));
    }

    // ── RFC 4180 parser tests ────────────────────────────────────────────

    @Test
    void quotedField_withComma() {
        String csv = "name,desc\nAlice,\"has , comma\"\n";
        assertDoesNotThrow(() -> checkThatCsv(csv)
                .hasRowCount(2)
                .hasColumnCount(2)
                .containsRow("Alice", "has , comma"));
    }

    @Test
    void quotedField_withEscapedDoubleQuotes() {
        String csv = "name,quote\nBob,\"He said \"\"hello\"\" there\"\n";
        assertDoesNotThrow(() -> checkThatCsv(csv)
                .hasRowCount(2)
                .containsRow("Bob", "He said \"hello\" there"));
    }

    @Test
    void quotedField_withNewline() {
        String csv = "name,bio\nCarol,\"line1\nline2\"\n";
        assertDoesNotThrow(() -> checkThatCsv(csv)
                .hasRowCount(2)
                .hasColumnCount(2)
                .containsRow("Carol", "line1\nline2"));
    }

    @Test
    void mixedQuotedAndUnquotedFields() {
        String csv = "a,b,c\nplain,\"quoted, value\",42\n";
        assertDoesNotThrow(() -> checkThatCsv(csv)
                .hasRowCount(2)
                .hasColumnCount(3)
                .containsRow("plain", "quoted, value", "42"));
    }

    @Test
    void emptyQuotedField() {
        String csv = "a,b\n\"\",value\n";
        assertDoesNotThrow(() -> checkThatCsv(csv)
                .hasRowCount(2)
                .hasColumnCount(2)
                .containsRow("", "value"));
    }

    @Test
    void crlfLineEndings() {
        String csv = "a,b\r\n1,2\r\n";
        assertDoesNotThrow(() -> checkThatCsv(csv).hasRowCount(2).containsRow("1", "2"));
    }

    @Test
    void standaloneCarriageReturn() {
        String csv = "a,b\r1,2\r";
        assertDoesNotThrow(() -> checkThatCsv(csv).hasRowCount(2).containsRow("1", "2"));
    }

    @Test
    void headerContains_failsOnEmptyCsv2() {
        assertThrows(AssertionError.class,
                () -> checkThatCsv("\n").headerContains("x"));
    }

    @Test
    void headerHasNoDigits_failsOnEmptyCsv() {
        assertThrows(AssertionError.class,
                () -> checkThatCsv("\n").headerHasNoDigits());
    }

    // ── hasDataRowCount ──────────────────────────────────────────────────

    @Test
    void hasDataRowCount_passes_withOneDataRow() {
        assertDoesNotThrow(() ->
                checkThatCsv("name,address\nAlice,Main St\n").hasDataRowCount(1));
    }

    @Test
    void hasDataRowCount_passes_withMultipleDataRows() {
        assertDoesNotThrow(() ->
                checkThatCsv("h1,h2\nv1,v2\nv3,v4\n").hasDataRowCount(2));
    }

    @Test
    void hasDataRowCount_passes_headerOnly() {
        assertDoesNotThrow(() ->
                checkThatCsv("h1,h2\n").hasDataRowCount(0));
    }

    @Test
    void hasDataRowCount_fails_withWrongCount() {
        var e = assertThrows(AssertionError.class, () ->
                checkThatCsv("h1,h2\nv1,v2\n").hasDataRowCount(5));
        assertTrue(e.getMessage().contains("header excluded"));
    }

    @Test
    void hasDataRowCount_rfc4180Example_showsSemanticDifference() {
        String rfc4180 = "name,address\n\"Smith, Jr.\",\"123 Main St\nApt 4\"";
        assertDoesNotThrow(() ->
                checkThatCsv(rfc4180).hasRowCount(2).hasDataRowCount(1));
    }

    // ── Unclosed-quote parser error ──────────────────────────────────────

    @Test
    void unclosedQuote_throwsIllegalArgumentException() {
        var ex = assertThrows(IllegalArgumentException.class,
                () -> checkThatCsv("name,desc\nAlice,\"unclosed\n").hasRowCount(1));
        assertTrue(ex.getMessage().contains("unclosed quoted field"),
                () -> "unexpected message: " + ex.getMessage());
    }
}
