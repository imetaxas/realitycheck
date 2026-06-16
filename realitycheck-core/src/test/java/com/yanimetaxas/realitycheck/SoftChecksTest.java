package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.IntStream;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class SoftChecksTest {

    record Email(String address) {}

    record EmailCheck(Email actual, FailureHandler failureHandler) implements Check<EmailCheck, Email> {

        @Override
        public EmailCheck self() {
            return this;
        }

        EmailCheck hasAtSign() {
            return failureHandler.check(self(),
                    actual.address() != null && actual.address().contains("@"),
                    "expected @ in <%s>", actual.address());
        }
    }

    sealed interface Shape permits Circle, Square, Triangle {}

    static final class Circle implements Shape {}

    static final class Square implements Shape {}

    static final class Triangle implements Shape {}

    @Test
    void noFailures_doesNotThrow() {
        assertDoesNotThrow(() -> Reality.checkAll(softly -> {
            softly.checkThat("hello").isNotNull();
            softly.checkThat(42).isPositive();
            softly.checkThat(true).isTrue();
        }));
    }

    @Test
    void singleFailure_reportsIt() {
        var e = assertThrows(AssertionError.class, () -> Reality.checkAll(softly -> {
            softly.checkThat("").isNotEmpty();
        }));
        assertTrue(e.getMessage().contains("1)"));
    }

    @Test
    void multipleFailures_reportsAll() {
        var e = assertThrows(AssertionError.class, () -> Reality.checkAll(softly -> {
            softly.checkThat("").isNotEmpty();
            softly.checkThat(-1).isPositive();
            softly.checkThat(false).isTrue();
        }));
        assertTrue(e.getMessage().contains("Multiple failures (3)"));
        assertTrue(e.getMessage().contains("1)"));
        assertTrue(e.getMessage().contains("2)"));
        assertTrue(e.getMessage().contains("3)"));
    }

    @Test
    void mixOfPassAndFail_onlyReportsFailures() {
        var e = assertThrows(AssertionError.class, () -> Reality.checkAll(softly -> {
            softly.checkThat("ok").isNotNull();
            softly.checkThat("").isNotEmpty();
            softly.checkThat(42).isPositive();
            softly.checkThat(false).isTrue();
        }));
        assertTrue(e.getMessage().contains("Multiple failures (2)"));
    }

    @Test
    void softChecks_dontStopAtFirstFailure() {
        var soft = new SoftChecks();
        soft.checkThat("").isNotEmpty();
        soft.checkThat(-1).isPositive();
        // Should reach here without throwing
        var e = assertThrows(AssertionError.class, soft::assertAll);
        assertTrue(e.getMessage().contains("2)"));
    }

    @Test
    void failures_returns_unmodifiableList() {
        assertDoesNotThrow(() -> checkAll(soft -> {
            soft.checkThat("a").isNotNull();
        }));
    }

    @Test
    void failures_returnsCollectedErrors() {
        var handler = new SoftFailureHandler();
        handler.fail("first: %s", "a");
        handler.fail("second: %s", "b");

        var list = handler.failures();

        assertEquals(2, list.size());
        assertTrue(list.get(0).getMessage().contains("first: a"));
        assertTrue(list.get(1).getMessage().contains("second: b"));
    }

    @Test
    void failures_returnsUnmodifiableList() {
        var handler = new SoftFailureHandler();
        handler.fail("oops");

        assertThrows(UnsupportedOperationException.class,
                () -> handler.failures().add(new AssertionError("injected")));
    }

    @Test
    void customMessage_returnsNull_default() {
        FailureHandler handler = new FailureHandler();
        assertNull(handler.customMessage());
    }

    @Test
    void customMessage_returnsValue() {
        FailureHandler handler = new FailureHandler("my message");
        assertEquals("my message", handler.customMessage());
    }

    @Test
    void softChecks_string() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat("x").isNotEmpty()));
    }

    @Test
    void softChecks_int() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(7).isPositive()));
    }

    @Test
    void softChecks_long() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(42L).isPositive()));
    }

    @Test
    void softChecks_double() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(3.14).isPositive()));
    }

    @Test
    void softChecks_boolean() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(true).isTrue()));
    }

    @Test
    void softChecks_path() throws Exception {
        var path = Files.createTempFile("soft-path-", ".txt");
        try {
            assertDoesNotThrow(() -> checkAll(s -> s.checkThat(path).exists()));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void softChecks_collection() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(List.of(1, 2)).hasSize(2)));
    }

    @Test
    void softChecks_map() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(Map.of("k", 1)).containsKey("k")));
    }

    @Test
    void softChecks_optional() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(Optional.of("v")).isPresent()));
    }

    @Test
    void softChecks_instant() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(Instant.now().minusSeconds(5)).isInThePast()));
    }

    @Test
    void softChecks_localDate() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(LocalDate.of(2026, 4, 1)).hasMonth(Month.APRIL)));
    }

    @Test
    void softChecks_localDateTime() {
        assertDoesNotThrow(() ->
                checkAll(s -> s.checkThat(LocalDateTime.of(2026, 4, 1, 14, 0)).hasHour(14)));
    }

    @Test
    void softChecks_duration() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(Duration.ofSeconds(2)).isPositive()));
    }

    @Test
    void softChecks_zonedDateTime() {
        ZonedDateTime zdt = ZonedDateTime.of(2026, 5, 1, 10, 0, 0, 0, ZoneId.of("UTC"));
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(zdt).hasYear(2026)));
    }

    @Test
    void softChecks_offsetDateTime() {
        OffsetDateTime odt = OffsetDateTime.of(2026, 6, 1, 9, 0, 0, 0, ZoneOffset.UTC);
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(odt).hasYear(2026)));
    }

    @Test
    void softChecks_intArray() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(new int[]{1, 2, 3}).hasLength(3)));
    }

    @Test
    void softChecks_longArray() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(new long[]{1L, 2L}).hasLength(2)));
    }

    @Test
    void softChecks_doubleArray() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(new double[]{1.0, 2.0}).hasLength(2)));
    }

    @Test
    void softChecks_byteArray() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(new byte[]{1, 2}).hasLength(2)));
    }

    @Test
    void softChecks_uri() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(URI.create("https://a.example")).hasScheme("https")));
    }

    @Test
    void softChecks_uuid() {
        UUID id = UUID.randomUUID();
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(id).isNotNil()));
    }

    @Test
    void softChecks_bigDecimal() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(new BigDecimal("2.5")).isPositive()));
    }

    @Test
    void softChecks_bigInteger() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThat(BigInteger.valueOf(11)).isPositive()));
    }

    @Test
    void softChecks_iterable() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThatIterable(List.of("a", "b")).isNotEmpty()));
    }

    @Test
    void softChecks_arrayVarargs() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThatArray("x", "y").hasLength(2)));
    }

    @Test
    void softChecks_stream() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThatStream(Stream.of(1, 2)).hasSize(2)));
    }

    @Test
    void softChecks_sealed() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThatSealed(Shape.class).isSealed()));
    }

    @Test
    void softChecks_enum() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThatEnum(TimeUnit.SECONDS).isEqualTo(TimeUnit.SECONDS)));
    }

    @Test
    void softChecks_multiline() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThatMultiline("a\nb").hasLineCount(2)));
    }

    @Test
    void softChecks_code() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThatCode(() -> {}).doesNotThrow()));
    }

    @Test
    void softChecks_csv() {
        assertDoesNotThrow(() -> checkAll(s -> s.checkThatCsv("h1,h2\n1,2").isNotEmpty().hasRowCount(2)));
    }

    @Test
    void softChecks_customFactory() {
        assertDoesNotThrow(() ->
                checkAll(s -> s.checkThat(new Email("a@b"), EmailCheck::new).hasAtSign()));
    }

    @Test
    void softChecks_customFactory_collectsFailure() {
        var e = assertThrows(AssertionError.class, () ->
                checkAll(s -> s.checkThat(new Email("bad"), EmailCheck::new).hasAtSign()));
        assertTrue(e.getMessage().contains("1)"));
    }

    @Test
    void assertAll_reportsEveryFailure() {
        var e = assertThrows(AssertionError.class, () ->
                checkAll(s -> {
                    s.checkThat("").isNotEmpty();
                    s.checkThat(-1).isPositive();
                    s.checkThat(false).isTrue();
                }));
        assertTrue(e.getMessage().contains("Multiple failures (3)"));
        assertTrue(e.getMessage().contains("1)"));
        assertTrue(e.getMessage().contains("2)"));
        assertTrue(e.getMessage().contains("3)"));
    }

    @Test
    void directSoftChecks_instance_assertAll() {
        var soft = new SoftChecks();
        soft.checkThat("ok").isNotNull();
        assertDoesNotThrow(soft::assertAll);
    }

    @Test
    void softChecks_inputStream() {
        assertDoesNotThrow(() ->
                checkAll(s -> s.checkThat(new ByteArrayInputStream(new byte[]{1, 2})).hasSize(2)));
    }

    @Test
    void softChecks_comparable() {
        assertDoesNotThrow(() ->
                checkAll(s -> s.checkThatComparable(5).isGreaterThan(3)));
    }

    @Test
    void softChecks_csvFile(@org.junit.jupiter.api.io.TempDir java.nio.file.Path tmp) throws java.io.IOException {
        java.nio.file.Path csv = tmp.resolve("test.csv");
        java.nio.file.Files.writeString(csv, "a,b\n1,2\n");
        assertDoesNotThrow(() ->
                checkAll(s -> s.checkThatCsvFile(csv).hasRowCount(2)));
    }

    @Test
    void softChecks_csvFileFromFile(@org.junit.jupiter.api.io.TempDir java.nio.file.Path tmp) throws java.io.IOException {
        java.nio.file.Path csv = tmp.resolve("test.csv");
        java.nio.file.Files.writeString(csv, "a,b\n1,2\n");
        assertDoesNotThrow(() ->
                checkAll(s -> s.checkThatCsvFile(csv.toFile()).hasRowCount(2)));
    }

    @Test
    void fail_isSafeForConcurrentUse() {
        var handler = new SoftFailureHandler();
        IntStream.range(0, 1000).parallel().forEach(i -> handler.fail("failure %d", i));
        assertEquals(1000, handler.failures().size());
    }

    @Test
    void softMode_chainingOffFailedExtractor_doesNotNpe() {
        var handler = new SoftFailureHandler();
        new ExecutionCheck(() -> {}, handler)
                .throwsInstanceOf(java.io.IOException.class)
                .hasMessage("x")
                .hasMessageContaining("x")
                .hasNullMessage()
                .hasNoCause();
        assertEquals(1, handler.failures().size(), "only the throwsInstanceOf failure should be recorded");
    }
}
