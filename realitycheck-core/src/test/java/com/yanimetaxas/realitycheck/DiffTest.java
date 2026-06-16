package com.yanimetaxas.realitycheck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class DiffTest {

    @Test
    void identicalStrings_noDifferences() {
        var result = Diff.of("hello\nworld", "hello\nworld");
        assertFalse(result.hasDifferences());
    }

    @Test
    void addedLine_showsPlus() {
        var result = Diff.of("a\nb", "a\nb\nc");
        assertTrue(result.hasDifferences());
        String formatted = result.format();
        assertTrue(formatted.contains("+ c"));
    }

    @Test
    void removedLine_showsMinus() {
        var result = Diff.of("a\nb\nc", "a\nc");
        assertTrue(result.hasDifferences());
        String formatted = result.format();
        assertTrue(formatted.contains("- b"));
    }

    @Test
    void changedLine_showsRemoveAndAdd() {
        var result = Diff.of("a\nold\nc", "a\nnew\nc");
        assertTrue(result.hasDifferences());
        String formatted = result.format();
        assertTrue(formatted.contains("- old"));
        assertTrue(formatted.contains("+ new"));
    }

    @Test
    void compactFormat_truncatesEqualLines() {
        var result = Diff.of("1\n2\n3\n4\n5\n6\n7\n8\n9\n10",
                             "1\n2\n3\n4\nCHANGED\n6\n7\n8\n9\n10");
        String compact = result.formatCompact(1);
        assertTrue(compact.contains("..."));
        assertTrue(compact.contains("CHANGED"));
    }

    @Test
    void emptyInputs() {
        var result = Diff.of("", "");
        assertFalse(result.hasDifferences());
    }

    @Test
    void addedToEmpty() {
        var result = Diff.of("", "new line");
        assertTrue(result.hasDifferences());
    }

    @Test
    void format_returns_noDifferences() {
        Diff.Result result = Diff.of("same", "same");
        assertEquals("(no differences)", result.format());
    }

    @Test
    void formatCompact_returns_noDifferences() {
        Diff.Result result = Diff.of("same", "same");
        assertEquals("(no differences)", result.formatCompact(3));
    }

    // ── Equality fast-path in ofLines ────────────────────────────────────

    @Test
    void ofLines_equalLists_returnsAllEqualLines() {
        List<String> lines = List.of("alpha", "beta", "gamma");
        Diff.Result result = Diff.ofLines(lines, lines);
        assertFalse(result.hasDifferences());
        assertEquals(3, result.lines().size());
        for (var line : result.lines()) {
            assertEquals(Diff.Type.EQUAL, line.type());
        }
    }

    @Test
    void ofLines_equalLists_preservesLineNumbers() {
        List<String> lines = List.of("first", "second", "third");
        Diff.Result result = Diff.ofLines(lines, lines);
        assertEquals(1, result.lines().get(0).lineNumber());
        assertEquals(2, result.lines().get(1).lineNumber());
        assertEquals(3, result.lines().get(2).lineNumber());
    }

    @Test
    void ofLines_emptyEqualLists_returnsEmptyResult() {
        Diff.Result result = Diff.ofLines(List.of(), List.of());
        assertFalse(result.hasDifferences());
        assertEquals(0, result.lines().size());
    }

    // ── ofLinesSimple fallback (triggered when m * n > 10_000 * 10_000) ──

    @Test
    void ofLinesSimple_triggeredForLargeInputs_equalContent() {
        int size = 10_001;
        List<String> lines = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            lines.add("line " + i);
        }
        Diff.Result result = Diff.ofLines(lines, lines);
        assertFalse(result.hasDifferences());
    }

    @Test
    void ofLinesSimple_triggeredForLargeInputs_withDifferences() {
        int size = 10_001;
        List<String> expected = new ArrayList<>(size);
        List<String> actual = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            expected.add("line " + i);
            actual.add("line " + i);
        }
        actual.set(5000, "CHANGED");

        Diff.Result result = Diff.ofLines(expected, actual);
        assertTrue(result.hasDifferences());
        String formatted = result.format();
        assertTrue(formatted.contains("- line 5000"));
        assertTrue(formatted.contains("+ CHANGED"));
    }

    @Test
    void ofLinesSimple_triggeredForLargeInputs_differentLengths() {
        int size = 10_001;
        List<String> expected = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            expected.add("line " + i);
        }
        List<String> actual = new ArrayList<>(expected);
        actual.add("extra line");

        Diff.Result result = Diff.ofLines(expected, actual);
        assertTrue(result.hasDifferences());
        String formatted = result.format();
        assertTrue(formatted.contains("+ extra line"));
    }

    @Test
    void ofLinesSimple_handlesExpectedLongerThanActual() {
        int size = 10_001;
        List<String> expected = new ArrayList<>(size + 1);
        for (int i = 0; i < size + 1; i++) {
            expected.add("line " + i);
        }
        List<String> actual = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            actual.add("line " + i);
        }

        Diff.Result result = Diff.ofLines(expected, actual);
        assertTrue(result.hasDifferences());
        String formatted = result.format();
        assertTrue(formatted.contains("- line " + size));
    }
}
