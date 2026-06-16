package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.checkAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Files;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Exercises every {@link SoftChecks#assertThat} alias (and related {@code assertThat*} helpers)
 * under {@link Reality#checkAll}.
 */
class SoftChecksAliasTest {

    record Email(String address) {}

    record EmailCheck(Email actual, FailureHandler failureHandler) implements Check<EmailCheck, Email> {

        @Override
        public EmailCheck self() {
            return this;
        }

        EmailCheck hasAt() {
            return failureHandler.check(
                    self(), actual.address() != null && actual.address().contains("@"), "expected @");
        }
    }

    sealed interface Shape permits Circle, Square, Triangle {}

    static final class Circle implements Shape {}

    static final class Square implements Shape {}

    static final class Triangle implements Shape {}

    @Test
    void assertThat_string() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat("hello").isNotEmpty()));
    }

    @Test
    void assertThat_int() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(42).isPositive()));
    }

    @Test
    void assertThat_long() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(99L).isPositive()));
    }

    @Test
    void assertThat_double() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(0.5).isPositive()));
    }

    @Test
    void assertThat_boolean() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(true).isTrue()));
    }

    @Test
    void assertThat_path() throws Exception {
        var path = Files.createTempFile("soft-alias", ".txt");
        try {
            assertDoesNotThrow(() -> checkAll(s -> s.assertThat(path).exists()));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void assertThat_file() throws Exception {
        var path = Files.createTempFile("soft-alias-f", ".txt");
        try {
            assertDoesNotThrow(() -> checkAll(s -> s.assertThat(path.toFile()).exists()));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void assertThat_inputStream() {
        assertDoesNotThrow(
                () -> checkAll(s -> s.assertThat(new ByteArrayInputStream(new byte[]{1, 2, 3})).hasSize(3)));
    }

    @Test
    void assertThat_collection() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(List.of(1, 2, 3)).hasSize(3)));
    }

    @Test
    void assertThat_map() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(Map.of("k", 1)).containsKey("k")));
    }

    @Test
    void assertThat_optional() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(Optional.of("x")).hasValue("x")));
    }

    @Test
    void assertThat_completableFuture() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(CompletableFuture.completedFuture(1)).isDone()));
    }

    @Test
    void assertThat_instant() {
        assertDoesNotThrow(
                () -> checkAll(s -> s.assertThat(Instant.now().minusSeconds(60)).isInThePast()));
    }

    @Test
    void assertThat_localDate() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(LocalDate.of(2026, 4, 1)).hasYear(2026)));
    }

    @Test
    void assertThat_localDateTime() {
        assertDoesNotThrow(
                () -> checkAll(s -> s.assertThat(LocalDateTime.of(2026, 4, 1, 12, 0)).hasHour(12)));
    }

    @Test
    void assertThat_duration() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(Duration.ofSeconds(5)).isPositive()));
    }

    @Test
    void assertThat_zonedDateTime() {
        assertDoesNotThrow(() -> checkAll(s ->
                s.assertThat(ZonedDateTime.of(2026, 4, 1, 10, 0, 0, 0, ZoneOffset.UTC)).hasYear(2026)));
    }

    @Test
    void assertThat_offsetDateTime() {
        assertDoesNotThrow(() -> checkAll(s ->
                s.assertThat(OffsetDateTime.of(2026, 4, 1, 10, 0, 0, 0, ZoneOffset.UTC)).hasYear(2026)));
    }

    @Test
    void assertThat_intArray() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(new int[]{1, 2, 3}).hasLength(3)));
    }

    @Test
    void assertThat_longArray() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(new long[]{1L, 2L}).hasLength(2)));
    }

    @Test
    void assertThat_doubleArray() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(new double[]{1.0, 2.0}).hasLength(2)));
    }

    @Test
    void assertThat_byteArray() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(new byte[]{1, 2}).isNotEmpty()));
    }

    @Test
    void assertThat_uri() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(URI.create("https://example.com")).hasScheme("https")));
    }

    @Test
    void assertThat_uuid() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(UUID.randomUUID()).isNotNil()));
    }

    @Test
    void assertThat_bigDecimal() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(new BigDecimal("3.14")).isPositive()));
    }

    @Test
    void assertThat_bigInteger() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(BigInteger.valueOf(7)).isPositive()));
    }

    @Test
    void assertThatStream() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThatStream(Stream.of("a", "b")).hasSize(2)));
    }

    @Test
    void assertThatSealed() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThatSealed(Shape.class).isSealed()));
    }

    @Test
    void assertThatEnum() {
        assertDoesNotThrow(() ->
                checkAll(s -> s.assertThatEnum(TimeUnit.MILLISECONDS).isEqualTo(TimeUnit.MILLISECONDS)));
    }

    @Test
    void assertThatMultiline() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThatMultiline("a\nb\nc").hasLineCount(3)));
    }

    @Test
    void assertThatCode() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThatCode(() -> {}).doesNotThrow()));
    }

    @Test
    void assertThatCsv() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThatCsv("h1,h2\n1,2").hasRowCount(2)));
    }

    @Test
    void assertThatObject() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThatObject("ref").isEqualTo("ref")));
    }

    @Test
    void assertThatIterable() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThatIterable(List.of(1, 2)).hasSize(2)));
    }

    @Test
    void assertThatArray() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThatArray("x", "y").hasLength(2)));
    }

    @Test
    void assertThat_withCheckFactory() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThat(new Email("a@b"), EmailCheck::new).hasAt()));
    }

    @Test
    void assertThatComparable() {
        assertDoesNotThrow(() -> checkAll(s -> s.assertThatComparable(5).isGreaterThan(3)));
    }

    @Test
    void assertThatCsvFile_path(@org.junit.jupiter.api.io.TempDir java.nio.file.Path tmp) throws java.io.IOException {
        java.nio.file.Path csv = tmp.resolve("test.csv");
        java.nio.file.Files.writeString(csv, "a,b\n1,2\n");
        assertDoesNotThrow(() -> checkAll(s -> s.assertThatCsvFile(csv).hasRowCount(2)));
    }

    @Test
    void assertThatCsvFile_file(@org.junit.jupiter.api.io.TempDir java.nio.file.Path tmp) throws java.io.IOException {
        java.nio.file.Path csv = tmp.resolve("test.csv");
        java.nio.file.Files.writeString(csv, "a,b\n1,2\n");
        assertDoesNotThrow(() -> checkAll(s -> s.assertThatCsvFile(csv.toFile()).hasRowCount(2)));
    }
}
