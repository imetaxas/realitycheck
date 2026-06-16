package io.github.imetaxas.realitycheck;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fluent assertions for CSV data. Supports both inline CSV strings and CSV files.
 *
 * <p>The parser is RFC 4180-compliant: quoted fields may contain commas, newlines, and
 * escaped double-quotes ({@code ""} inside a quoted field represents a literal {@code "}).
 */
public final class CsvCheck extends AbstractCheck<CsvCheck, String> {

    private List<String[]> cachedRows;

    CsvCheck(String csvContent, FailureHandler handler) {
        super(csvContent, handler);
    }

    static CsvCheck fromFile(Path path, FailureHandler handler) {
        try {
            String content = Files.readString(path);
            return new CsvCheck(content, handler);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public CsvCheck isNotEmpty() {
        if (actual() == null || actual().isBlank()) {
            failureHandler().fail("expected non-empty CSV content");
        }
        return self();
    }

    public CsvCheck hasSameContentAs(String expectedCsv) {
        if (!isActualPresent()) return self();
        if (!actual().equals(expectedCsv)) {
            var diff = Diff.of(expectedCsv, actual());
            failureHandler().fail("CSV content differs:\n%s",
                    diff.formatCompact(3));
        }
        return self();
    }

    public CsvCheck hasNotSameContentAs(String expectedCsv) {
        if (!isActualPresent()) return self();
        if (actual().equals(expectedCsv)) {
            failureHandler().fail("expected different CSV content but they are identical");
        }
        return self();
    }

    /**
     * Asserts the total number of rows (including the header row, if any).
     *
     * <p>For data-only row counting (excluding the header), use {@link #hasDataRowCount(int)}.
     */
    public CsvCheck hasRowCount(int expected) {
        if (!isActualPresent()) return self();
        long count = rows().size();
        if (count != expected) {
            failureHandler().fail("expected <%d> CSV rows (including header) but found: <%d>",
                    expected, count);
        }
        return self();
    }

    /**
     * Asserts the number of data rows, excluding the first (header) row.
     *
     * <p>For a CSV with a header row plus {@code n} data rows, use {@code hasDataRowCount(n)}.
     * Use {@link #hasRowCount(int)} to assert the total row count including the header.
     */
    public CsvCheck hasDataRowCount(int expected) {
        if (!isActualPresent()) return self();
        int dataCount = Math.max(0, rows().size() - 1);
        if (dataCount != expected) {
            failureHandler().fail("expected <%d> data rows (header excluded) but found: <%d>",
                    expected, dataCount);
        }
        return self();
    }

    public CsvCheck hasColumnCount(int expected) {
        if (!isActualPresent()) return self();
        List<String[]> rows = rows();
        if (rows.isEmpty()) {
            failureHandler().fail("CSV is empty, cannot check column count");
        } else {
            int count = rows.get(0).length;
            if (count != expected) {
                failureHandler().fail("expected <%d> CSV columns but found: <%d>",
                        expected, count);
            }
        }
        return self();
    }

    public CsvCheck headerEquals(String... expectedHeaders) {
        List<String[]> rows = rows();
        if (rows.isEmpty()) {
            failureHandler().fail("CSV is empty, cannot check header");
        } else {
            String[] header = rows.get(0);
            String[] trimmedHeader = Arrays.stream(header).map(String::trim).toArray(String[]::new);
            if (!Arrays.equals(trimmedHeader, expectedHeaders)) {
                failureHandler().fail("expected header %s but was: %s",
                        Arrays.toString(expectedHeaders), Arrays.toString(trimmedHeader));
            }
        }
        return self();
    }

    public CsvCheck headerContains(String... columns) {
        List<String[]> rows = rows();
        if (rows.isEmpty()) {
            failureHandler().fail("CSV is empty, cannot check header");
        } else {
            List<String> header = Arrays.stream(rows.get(0)).map(String::trim).toList();
            for (String col : columns) {
                if (!header.contains(col)) {
                    failureHandler().fail("expected header to contain <%s> but header was: %s",
                            col, header);
                }
            }
        }
        return self();
    }

    public CsvCheck headerHasNoDigits() {
        List<String[]> rows = rows();
        if (rows.isEmpty()) {
            failureHandler().fail("CSV is empty, cannot check header");
        } else {
            for (String col : rows.get(0)) {
                if (col.trim().matches("\\d+")) {
                    failureHandler().fail("header column <%s> is purely numeric — headers should not be digits",
                            col.trim());
                }
            }
        }
        return self();
    }

    public CsvCheck containsRow(String... values) {
        List<String[]> rows = rows();
        boolean found = rows.stream().anyMatch(row -> {
            String[] trimmed = Arrays.stream(row).map(String::trim).toArray(String[]::new);
            return Arrays.equals(trimmed, values);
        });
        if (!found) {
            failureHandler().fail("expected CSV to contain row %s",
                    Arrays.toString(values));
        }
        return self();
    }

    private List<String[]> rows() {
        if (cachedRows == null) {
            cachedRows = parseCsv(actual());
        }
        return cachedRows;
    }

    private static List<String[]> parseCsv(String input) {
        List<String[]> rows = new ArrayList<>();
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;
        int len = input.length();

        for (int i = 0; i < len; i++) {
            char c = input.charAt(i);

            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < len && input.charAt(i + 1) == '"') {
                        field.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    field.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == ',') {
                    fields.add(field.toString());
                    field.setLength(0);
                } else if (c == '\r') {
                    if (i + 1 < len && input.charAt(i + 1) == '\n') {
                        i++;
                    }
                    fields.add(field.toString());
                    field.setLength(0);
                    addRowIfNonBlank(rows, fields);
                    fields = new ArrayList<>();
                } else if (c == '\n') {
                    fields.add(field.toString());
                    field.setLength(0);
                    addRowIfNonBlank(rows, fields);
                    fields = new ArrayList<>();
                } else {
                    field.append(c);
                }
            }
        }

        if (inQuotes) {
            throw new IllegalArgumentException("malformed CSV: unclosed quoted field");
        }

        fields.add(field.toString());
        addRowIfNonBlank(rows, fields);

        return rows;
    }

    private static void addRowIfNonBlank(List<String[]> rows, List<String> fields) {
        boolean allBlank = fields.stream().allMatch(String::isEmpty);
        if (!allBlank) {
            rows.add(fields.toArray(String[]::new));
        }
    }
}
