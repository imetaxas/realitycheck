package io.github.imetaxas.realitycheck;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal line-by-line diff engine for assertion failure messages.
 * Produces human-readable output showing added, removed, and unchanged lines.
 */
public final class Diff {

    public enum Type { EQUAL, ADDED, REMOVED }

    public record Line(Type type, int lineNumber, String content) {
        @Override
        public String toString() {
            return switch (type) {
                case EQUAL   -> "  " + content;
                case ADDED   -> "+ " + content;
                case REMOVED -> "- " + content;
            };
        }
    }

    public record Result(List<Line> lines) {
        public boolean hasDifferences() {
            return lines.stream().anyMatch(l -> l.type() != Type.EQUAL);
        }

        public String format() {
            if (!hasDifferences()) {
                return "(no differences)";
            }
            var sb = new StringBuilder();
            for (var line : lines) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        }

        public String formatCompact(int contextLines) {
            if (!hasDifferences()) {
                return "(no differences)";
            }
            var sb = new StringBuilder();
            boolean[] show = new boolean[lines.size()];
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).type() != Type.EQUAL) {
                    for (int j = Math.max(0, i - contextLines); j <= Math.min(lines.size() - 1, i + contextLines); j++) {
                        show[j] = true;
                    }
                }
            }
            boolean skipping = false;
            for (int i = 0; i < lines.size(); i++) {
                if (show[i]) {
                    skipping = false;
                    sb.append(lines.get(i)).append('\n');
                } else if (!skipping) {
                    sb.append("  ...\n");
                    skipping = true;
                }
            }
            return sb.toString();
        }
    }

    private Diff() {}

    /**
     * Computes a line-by-line diff between two strings using a simple LCS algorithm.
     */
    public static Result of(String expected, String actual) {
        return ofLines(
                expected.lines().toList(),
                actual.lines().toList()
        );
    }

    // int[m+1][n+1] * 4 bytes — at this limit: ~(2001)^2 * 4 ≈ 16 MB, safe in typical test-run heaps.
    // Inputs exceeding this limit use the sequential O(n) fallback instead.
    private static final int LCS_LINE_LIMIT = 2_000;

    /**
     * Computes a line-by-line diff between two lists of lines using LCS (longest common subsequence).
     * Falls back to a simpler sequential comparison when either input exceeds
     * {@value #LCS_LINE_LIMIT} lines to avoid excessive memory usage.
     */
    public static Result ofLines(List<String> expected, List<String> actual) {
        if (expected.equals(actual)) {
            var lines = new ArrayList<Line>(expected.size());
            for (int i = 0; i < expected.size(); i++) {
                lines.add(new Line(Type.EQUAL, i + 1, expected.get(i)));
            }
            return new Result(lines);
        }

        int m = expected.size();
        int n = actual.size();

        if (m > LCS_LINE_LIMIT || n > LCS_LINE_LIMIT) {
            return ofLinesSimple(expected, actual);
        }

        int[][] dp = new int[m + 1][n + 1];
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (expected.get(i).equals(actual.get(j))) {
                    dp[i][j] = dp[i + 1][j + 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j + 1]);
                }
            }
        }

        var result = new ArrayList<Line>();
        int i = 0, j = 0;
        int lineNum = 1;
        while (i < m || j < n) {
            if (i < m && j < n && expected.get(i).equals(actual.get(j))) {
                result.add(new Line(Type.EQUAL, lineNum++, expected.get(i)));
                i++;
                j++;
            } else if (j < n && (i >= m || dp[i][j + 1] >= dp[i + 1][j])) {
                result.add(new Line(Type.ADDED, lineNum++, actual.get(j)));
                j++;
            } else if (i < m) {
                result.add(new Line(Type.REMOVED, lineNum++, expected.get(i)));
                i++;
            }
        }
        return new Result(result);
    }

    private static Result ofLinesSimple(List<String> expected, List<String> actual) {
        var result = new ArrayList<Line>();
        int lineNum = 1;
        int max = Math.max(expected.size(), actual.size());
        for (int i = 0; i < max; i++) {
            if (i < expected.size() && i < actual.size()) {
                if (expected.get(i).equals(actual.get(i))) {
                    result.add(new Line(Type.EQUAL, lineNum++, expected.get(i)));
                } else {
                    result.add(new Line(Type.REMOVED, lineNum++, expected.get(i)));
                    result.add(new Line(Type.ADDED, lineNum++, actual.get(i)));
                }
            } else if (i < expected.size()) {
                result.add(new Line(Type.REMOVED, lineNum++, expected.get(i)));
            } else {
                result.add(new Line(Type.ADDED, lineNum++, actual.get(i)));
            }
        }
        return new Result(result);
    }
}
