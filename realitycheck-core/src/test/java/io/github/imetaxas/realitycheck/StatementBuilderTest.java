package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class StatementBuilderTest {

    private static void assertCustomMessageInFailure(String customMsg, Runnable failingCheck) {
        var e = assertThrows(AssertionError.class, failingCheck::run);
        assertTrue(e.getMessage().contains(customMsg), () -> "got: " + e.getMessage());
    }

    @Test
    void that_string_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "custom msg",
                () -> checkWithMessage("custom msg").that("hello").isEqualTo("world"));
    }

    @Test
    void that_boolean_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "flag check",
                () -> checkWithMessage("flag check").that(false).isTrue());
    }

    @Test
    void that_int_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "int check",
                () -> checkWithMessage("int check").that(-1).isPositive());
    }

    @Test
    void that_long_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "long check",
                () -> checkWithMessage("long check").that(0L).isPositive());
    }

    @Test
    void that_double_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "double check",
                () -> checkWithMessage("double check").that(0.0).isPositive());
    }

    @Test
    void that_path_includesCustomMessageOnFailure(@TempDir Path tempDir) {
        Path missing = tempDir.resolve("nope.txt");
        assertCustomMessageInFailure(
                "path check",
                () -> checkWithMessage("path check").that(missing).exists());
    }

    @Test
    void that_file_includesCustomMessageOnFailure(@TempDir Path tempDir) {
        File missing = tempDir.resolve("missing.bin").toFile();
        assertCustomMessageInFailure(
                "file check",
                () -> checkWithMessage("file check").that(missing).exists());
    }

    @Test
    void that_inputStream_includesCustomMessageOnFailure() {
        var empty = new ByteArrayInputStream(new byte[0]);
        assertCustomMessageInFailure(
                "stream check",
                () -> checkWithMessage("stream check").that(empty).hasSize(5));
    }

    @Test
    void that_collection_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "collection check",
                () -> checkWithMessage("collection check").that(List.of(1)).isEmpty());
    }

    @Test
    void that_map_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "map check",
                () -> checkWithMessage("map check").that(Map.of("a", 1)).isEmpty());
    }

    @Test
    void that_optional_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "optional check",
                () -> checkWithMessage("optional check").that(Optional.empty()).isPresent());
    }

    @Test
    void that_completableFuture_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "future check",
                () -> checkWithMessage("future check")
                        .that(CompletableFuture.completedFuture("x"))
                        .isNotDone());
    }

    @Test
    void thatObject_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "object check",
                () -> checkWithMessage("object check").thatObject("a").isEqualTo("b"));
    }

    @Test
    void thatCsv_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "csv check",
                () -> checkWithMessage("csv check").thatCsv("").isNotEmpty());
    }

    @Test
    void thatCsvFile_path_includesCustomMessageOnFailure(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("data.csv");
        Files.writeString(csv, "");
        assertCustomMessageInFailure(
                "csv file check",
                () -> checkWithMessage("csv file check").thatCsvFile(csv).hasRowCount(5));
    }

    @Test
    void thatCsvFile_fileOverload_includesCustomMessageOnFailure(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("data2.csv");
        Files.writeString(csv, "");
        assertCustomMessageInFailure(
                "csv file check 2",
                () -> checkWithMessage("csv file check 2").thatCsvFile(csv.toFile()).hasRowCount(3));
    }

    @Test
    void overloads_pass_whenAssertionsHold(@TempDir Path tempDir) throws Exception {
        Path existing = tempDir.resolve("ok.txt");
        Files.writeString(existing, "hi", StandardCharsets.UTF_8);
        Path csv = tempDir.resolve("ok.csv");
        Files.writeString(csv, "a,b\n1,2\n", StandardCharsets.UTF_8);

        assertDoesNotThrow(() -> checkWithMessage("ok").that("x").isEqualTo("x"));
        assertDoesNotThrow(() -> checkWithMessage("ok").that(true).isTrue());
        assertDoesNotThrow(() -> checkWithMessage("ok").that(1).isPositive());
        assertDoesNotThrow(() -> checkWithMessage("ok").that(1L).isPositive());
        assertDoesNotThrow(() -> checkWithMessage("ok").that(1.0).isPositive());
        assertDoesNotThrow(() -> checkWithMessage("ok").that(existing).exists());
        assertDoesNotThrow(() -> checkWithMessage("ok").that(existing.toFile()).exists());
        assertDoesNotThrow(() ->
                checkWithMessage("ok")
                        .that(new ByteArrayInputStream(new byte[] {1, 2, 3}))
                        .hasSize(3));
        assertDoesNotThrow(() -> checkWithMessage("ok").that(List.of(1)).isNotEmpty());
        assertDoesNotThrow(() -> checkWithMessage("ok").that(Map.of("k", "v")).containsKey("k"));
        assertDoesNotThrow(() -> checkWithMessage("ok").that(Optional.of(1)).isPresent());
        assertDoesNotThrow(() ->
                checkWithMessage("ok").that(CompletableFuture.completedFuture(1)).isCompletedNormally());
        assertDoesNotThrow(() -> checkWithMessage("ok").thatObject("z").isEqualTo("z"));
        assertDoesNotThrow(() -> checkWithMessage("ok").thatCsv("a,b\n").hasRowCount(1));
        assertDoesNotThrow(() -> checkWithMessage("ok").thatCsvFile(csv).hasRowCount(2));
    }

    @Test
    void checkWithContext_includesContextOnFailure() {
        var e = assertThrows(
                AssertionError.class,
                () -> checkWithContext("signup flow").that("hello").isEqualTo("world"));
        assertTrue(e.getMessage().contains("[context: signup flow]"));
        assertTrue(e.getMessage().contains("hello"));
    }

    @Test
    void checkWithContext_eachOverloadSurface(@TempDir Path tempDir) throws Exception {
        Path existing = tempDir.resolve("ctx.txt");
        Files.writeString(existing, "x");
        Path csv = tempDir.resolve("ctx.csv");
        Files.writeString(csv, "h\nv\n");

        Runnable[] checks = {
            () -> checkWithContext("ctx").that("").isNotEmpty(),
            () -> checkWithContext("ctx").that(false).isTrue(),
            () -> checkWithContext("ctx").that(0).isPositive(),
            () -> checkWithContext("ctx").that(0L).isPositive(),
            () -> checkWithContext("ctx").that(0.0).isPositive(),
            () -> checkWithContext("ctx").that(tempDir.resolve("missing")).exists(),
            () -> checkWithContext("ctx").that(tempDir.resolve("missing").toFile()).exists(),
            () -> checkWithContext("ctx").that(new ByteArrayInputStream(new byte[0])).hasSize(1),
            () -> checkWithContext("ctx").that(List.of()).isNotEmpty(),
            () -> checkWithContext("ctx").that(Map.of()).containsKey("x"),
            () -> checkWithContext("ctx").that(Optional.empty()).isPresent(),
            () -> checkWithContext("ctx").that(new CompletableFuture<String>()).isDone(),
            () -> checkWithContext("ctx").thatObject("a").isEqualTo("b"),
            () -> checkWithContext("ctx").thatCsv("").isNotEmpty(),
            () -> checkWithContext("ctx").thatCsvFile(csv).hasRowCount(99),
        };

        for (Runnable r : checks) {
            var e = assertThrows(AssertionError.class, r::run);
            assertTrue(e.getMessage().contains("[context: ctx]"), () -> "got: " + e.getMessage());
        }

        assertDoesNotThrow(() -> checkWithContext("ctx").that(existing).exists());
        assertDoesNotThrow(() -> checkWithContext("ctx").that(existing.toFile()).exists());
    }

    // ── New overloads: Instant ───────────────────────────────────────────

    @Test
    void that_instant_returnsInstantCheck() {
        Instant now = Instant.now();
        assertDoesNotThrow(() -> checkWithMessage("ok").that(now.minusSeconds(60)).isInThePast());
    }

    @Test
    void that_instant_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "instant check",
                () -> checkWithMessage("instant check").that(Instant.now().minusSeconds(60)).isInTheFuture());
    }

    // ── New overloads: LocalDate ─────────────────────────────────────────

    @Test
    void that_localDate_returnsLocalDateCheck() {
        LocalDate today = LocalDate.now();
        assertDoesNotThrow(() -> checkWithMessage("ok").that(today).isEqualTo(today));
    }

    @Test
    void that_localDate_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "date check",
                () -> checkWithMessage("date check")
                        .that(LocalDate.of(2020, 1, 1))
                        .isEqualTo(LocalDate.of(2025, 6, 1)));
    }

    // ── New overloads: LocalDateTime ─────────────────────────────────────

    @Test
    void that_localDateTime_returnsLocalDateTimeCheck() {
        LocalDateTime now = LocalDateTime.now();
        assertDoesNotThrow(() -> checkWithMessage("ok").that(now).isEqualTo(now));
    }

    @Test
    void that_localDateTime_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "ldt check",
                () -> checkWithMessage("ldt check")
                        .that(LocalDateTime.of(2020, 1, 1, 0, 0))
                        .isEqualTo(LocalDateTime.of(2025, 6, 1, 12, 0)));
    }

    // ── New overloads: Duration ──────────────────────────────────────────

    @Test
    void that_duration_returnsDurationCheck() {
        assertDoesNotThrow(() -> checkWithMessage("ok").that(Duration.ofSeconds(5)).isPositive());
    }

    @Test
    void that_duration_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "duration check",
                () -> checkWithMessage("duration check").that(Duration.ofSeconds(-1)).isPositive());
    }

    // ── New overloads: ZonedDateTime ─────────────────────────────────────

    @Test
    void that_zonedDateTime_returnsZonedDateTimeCheck() {
        ZonedDateTime now = ZonedDateTime.now();
        assertDoesNotThrow(() -> checkWithMessage("ok").that(now).isEqualTo(now));
    }

    @Test
    void that_zonedDateTime_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "zdt check",
                () -> checkWithMessage("zdt check")
                        .that(ZonedDateTime.now())
                        .isEqualTo(ZonedDateTime.now().plusDays(1)));
    }

    // ── New overloads: OffsetDateTime ────────────────────────────────────

    @Test
    void that_offsetDateTime_returnsOffsetDateTimeCheck() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        assertDoesNotThrow(() -> checkWithMessage("ok").that(now).isEqualTo(now));
    }

    @Test
    void that_offsetDateTime_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "odt check",
                () -> checkWithMessage("odt check")
                        .that(OffsetDateTime.now(ZoneOffset.UTC))
                        .isEqualTo(OffsetDateTime.now(ZoneOffset.UTC).plusDays(1)));
    }

    // ── New overloads: int[] ─────────────────────────────────────────────

    @Test
    void that_intArray_returnsIntArrayCheck() {
        assertDoesNotThrow(() -> checkWithMessage("ok").that(new int[] {1, 2, 3}).hasLength(3));
    }

    @Test
    void that_intArray_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "int[] check",
                () -> checkWithMessage("int[] check").that(new int[] {1, 2}).hasLength(5));
    }

    // ── New overloads: long[] ────────────────────────────────────────────

    @Test
    void that_longArray_returnsLongArrayCheck() {
        assertDoesNotThrow(() -> checkWithMessage("ok").that(new long[] {10L, 20L}).isNotEmpty());
    }

    @Test
    void that_longArray_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "long[] check",
                () -> checkWithMessage("long[] check").that(new long[0]).isNotEmpty());
    }

    // ── New overloads: double[] ──────────────────────────────────────────

    @Test
    void that_doubleArray_returnsDoubleArrayCheck() {
        assertDoesNotThrow(
                () -> checkWithMessage("ok").that(new double[] {1.1, 2.2}).isNotEmpty());
    }

    @Test
    void that_doubleArray_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "double[] check",
                () -> checkWithMessage("double[] check").that(new double[0]).isNotEmpty());
    }

    // ── New overloads: byte[] ────────────────────────────────────────────

    @Test
    void that_byteArray_returnsByteArrayCheck() {
        assertDoesNotThrow(
                () -> checkWithMessage("ok").that(new byte[] {0x01, 0x02}).isNotEmpty());
    }

    @Test
    void that_byteArray_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "byte[] check",
                () -> checkWithMessage("byte[] check").that(new byte[0]).isNotEmpty());
    }

    // ── New overloads: URI ───────────────────────────────────────────────

    @Test
    void that_uri_returnsUriCheck() {
        assertDoesNotThrow(
                () -> checkWithMessage("ok").that(URI.create("https://example.com")).hasScheme("https"));
    }

    @Test
    void that_uri_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "uri check",
                () -> checkWithMessage("uri check")
                        .that(URI.create("http://example.com"))
                        .hasScheme("https"));
    }

    // ── New overloads: UUID ──────────────────────────────────────────────

    @Test
    void that_uuid_returnsUuidCheck() {
        assertDoesNotThrow(() -> checkWithMessage("ok").that(UUID.randomUUID()).isNotNil());
    }

    @Test
    void that_uuid_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "uuid check",
                () -> checkWithMessage("uuid check")
                        .that(new UUID(0L, 0L))
                        .isNotNil());
    }

    // ── New overloads: BigDecimal ────────────────────────────────────────

    @Test
    void that_bigDecimal_returnsBigDecimalCheck() {
        assertDoesNotThrow(
                () -> checkWithMessage("ok").that(new BigDecimal("3.14")).isPositive());
    }

    @Test
    void that_bigDecimal_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "bd check",
                () -> checkWithMessage("bd check").that(new BigDecimal("-1")).isPositive());
    }

    // ── New overloads: BigInteger ────────────────────────────────────────

    @Test
    void that_bigInteger_returnsBigIntegerCheck() {
        assertDoesNotThrow(
                () -> checkWithMessage("ok").that(BigInteger.TEN).isPositive());
    }

    @Test
    void that_bigInteger_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "bi check",
                () -> checkWithMessage("bi check").that(BigInteger.valueOf(-5)).isPositive());
    }

    // ── New overloads: Stream ────────────────────────────────────────────

    @Test
    void thatStream_returnsStreamCheck() {
        assertDoesNotThrow(
                () -> checkWithMessage("ok").thatStream(Stream.of("a", "b")).hasSize(2));
    }

    @Test
    void thatStream_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "stream check",
                () -> checkWithMessage("stream check").thatStream(Stream.of(1)).isEmpty());
    }

    // ── New overloads: SealedClass ───────────────────────────────────────

    @Test
    void thatSealed_returnsSealedClassCheck() {
        assertDoesNotThrow(
                () -> checkWithMessage("ok").thatSealed(String.class).isNotSealed());
    }

    @Test
    void thatSealed_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "sealed check",
                () -> checkWithMessage("sealed check").thatSealed(String.class).isSealed());
    }

    // ── New overloads: Enum ──────────────────────────────────────────────

    private enum Color { RED, GREEN, BLUE }

    @Test
    void thatEnum_returnsEnumCheck() {
        assertDoesNotThrow(
                () -> checkWithMessage("ok").thatEnum(Color.RED).hasName("RED"));
    }

    @Test
    void thatEnum_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "enum check",
                () -> checkWithMessage("enum check").thatEnum(Color.RED).hasName("BLUE"));
    }

    // ── New overloads: Multiline ─────────────────────────────────────────

    @Test
    void thatMultiline_returnsMultilineCheck() {
        assertDoesNotThrow(
                () -> checkWithMessage("ok").thatMultiline("line1\nline2").hasLineCount(2));
    }

    @Test
    void thatMultiline_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "multiline check",
                () -> checkWithMessage("multiline check").thatMultiline("a\nb").hasLineCount(5));
    }

    // ── New overloads: ExecutionCheck (thatCode) ─────────────────────────

    @Test
    void thatCode_returnsExecutionCheck() {
        assertDoesNotThrow(
                () -> checkWithMessage("ok").thatCode(() -> {}).doesNotThrow());
    }

    @Test
    void thatCode_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "code check",
                () -> checkWithMessage("code check")
                        .thatCode(() -> { throw new RuntimeException("boom"); })
                        .doesNotThrow());
    }

    // ── New overloads: CheckFactory (custom) ─────────────────────────────

    @Test
    void that_customFactory_returnsCustomCheck() {
        CheckFactory<ObjectCheck<String>, String> factory = ObjectCheck::new;
        assertDoesNotThrow(
                () -> checkWithMessage("ok").that("hello", factory).isEqualTo("hello"));
    }

    @Test
    void that_customFactory_includesCustomMessageOnFailure() {
        CheckFactory<ObjectCheck<String>, String> factory = ObjectCheck::new;
        assertCustomMessageInFailure(
                "factory check",
                () -> checkWithMessage("factory check")
                        .that("hello", factory)
                        .isEqualTo("world"));
    }

    // ── New overloads: Iterable ──────────────────────────────────────────

    @Test
    void thatIterable_returnsIterableCheck() {
        Iterable<String> iterable = List.of("a", "b");
        assertDoesNotThrow(
                () -> checkWithMessage("ok").thatIterable(iterable).isNotEmpty());
    }

    @Test
    void thatIterable_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "iterable check",
                () -> checkWithMessage("iterable check").thatIterable(List.of()).isNotEmpty());
    }

    // ── New overloads: Array ─────────────────────────────────────────────

    @Test
    void thatArray_returnsArrayCheck() {
        assertDoesNotThrow(
                () -> checkWithMessage("ok").thatArray("x", "y").hasLength(2));
    }

    @Test
    void thatArray_includesCustomMessageOnFailure() {
        assertCustomMessageInFailure(
                "array check",
                () -> checkWithMessage("array check").thatArray("a").hasLength(3));
    }

    // ── Comprehensive passing test for all new overloads ─────────────────

    @Test
    void newOverloads_pass_whenAssertionsHold() {
        Instant past = Instant.now().minusSeconds(60);
        LocalDate today = LocalDate.now();
        LocalDateTime ldt = LocalDateTime.now();
        ZonedDateTime zdt = ZonedDateTime.now();
        OffsetDateTime odt = OffsetDateTime.now(ZoneOffset.UTC);

        assertDoesNotThrow(() -> checkWithMessage("ok").that(past).isInThePast());
        assertDoesNotThrow(() -> checkWithMessage("ok").that(today).isEqualTo(today));
        assertDoesNotThrow(() -> checkWithMessage("ok").that(ldt).isEqualTo(ldt));
        assertDoesNotThrow(() -> checkWithMessage("ok").that(Duration.ofMillis(100)).isPositive());
        assertDoesNotThrow(() -> checkWithMessage("ok").that(zdt).isEqualTo(zdt));
        assertDoesNotThrow(() -> checkWithMessage("ok").that(odt).isEqualTo(odt));
        assertDoesNotThrow(() -> checkWithMessage("ok").that(new int[] {1}).contains(1));
        assertDoesNotThrow(() -> checkWithMessage("ok").that(new long[] {1L}).isNotEmpty());
        assertDoesNotThrow(() -> checkWithMessage("ok").that(new double[] {1.0}).isNotEmpty());
        assertDoesNotThrow(() -> checkWithMessage("ok").that(new byte[] {1}).isNotEmpty());
        assertDoesNotThrow(
                () -> checkWithMessage("ok").that(URI.create("https://a.com")).isAbsolute());
        assertDoesNotThrow(() -> checkWithMessage("ok").that(UUID.randomUUID()).isNotNil());
        assertDoesNotThrow(() -> checkWithMessage("ok").that(BigDecimal.ONE).isPositive());
        assertDoesNotThrow(() -> checkWithMessage("ok").that(BigInteger.ONE).isPositive());
        assertDoesNotThrow(() -> checkWithMessage("ok").thatStream(Stream.of(1)).isNotEmpty());
        assertDoesNotThrow(() -> checkWithMessage("ok").thatSealed(String.class).isNotSealed());
        assertDoesNotThrow(() -> checkWithMessage("ok").thatEnum(Color.GREEN).hasName("GREEN"));
        assertDoesNotThrow(() -> checkWithMessage("ok").thatMultiline("a\nb").hasLineCount(2));
        assertDoesNotThrow(() -> checkWithMessage("ok").thatCode(() -> {}).doesNotThrow());
        CheckFactory<ObjectCheck<String>, String> factory = ObjectCheck::new;
        assertDoesNotThrow(
                () -> checkWithMessage("ok").that("v", factory).isEqualTo("v"));
        assertDoesNotThrow(
                () -> checkWithMessage("ok").thatIterable(List.of("x")).isNotEmpty());
        assertDoesNotThrow(() -> checkWithMessage("ok").thatArray("a", "b").hasLength(2));
    }
}
