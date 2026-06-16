package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.*;
import static com.yanimetaxas.realitycheck.RealityAssertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RealityAssertionsTest {

    record Email(String address) {}

    record EmailCheck(Email actual, FailureHandler failureHandler) implements Check<EmailCheck, Email> {

        @Override
        public EmailCheck self() {
            return this;
        }

        EmailCheck hasAt() {
            return failureHandler.check(self(),
                    actual.address() != null && actual.address().contains("@"),
                    "expected @");
        }
    }

    sealed interface Shape permits Circle, Square, Triangle {}

    static final class Circle implements Shape {}

    static final class Square implements Shape {}

    static final class Triangle implements Shape {}

    @Test
    void assertThat_string() {
        assertDoesNotThrow(() -> assertThat("hello").isNotEmpty().hasLength(5));
    }

    @Test
    void assertThat_number() {
        assertDoesNotThrow(() -> assertThat(42).isPositive().isGreaterThan(0));
    }

    @Test
    void assertThat_boolean() {
        assertDoesNotThrow(() -> assertThat(true).isTrue());
    }

    @Test
    void assertThat_collection() {
        assertDoesNotThrow(() -> assertThat(List.of(1, 2, 3)).hasSize(3).contains(2));
    }

    @Test
    void assertThat_map() {
        assertDoesNotThrow(() -> assertThat(Map.of("a", 1)).containsKey("a"));
    }

    @Test
    void assertThat_optional() {
        assertDoesNotThrow(() -> assertThat(Optional.of("x")).isPresent().hasValue("x"));
    }

    @Test
    void assertThat_instant() {
        assertDoesNotThrow(() -> assertThat(Instant.now().minusSeconds(60)).isInThePast());
    }

    @Test
    void assertThat_duration() {
        assertDoesNotThrow(() -> assertThat(Duration.ofSeconds(5)).isPositive());
    }

    @Test
    void assertThat_bigDecimal() {
        assertDoesNotThrow(() -> assertThat(new BigDecimal("3.14")).isPositive());
    }

    @Test
    void assertThat_uuid() {
        assertDoesNotThrow(() -> assertThat(UUID.randomUUID()).isNotNil().isVersion4());
    }

    @Test
    void assertThat_intArray() {
        assertDoesNotThrow(() -> assertThat(new int[]{1, 2, 3}).hasLength(3));
    }

    @Test
    void assertThat_byteArray() {
        assertDoesNotThrow(() -> assertThat(new byte[]{1, 2}).isNotEmpty());
    }

    @Test
    void assertThatThrownBy_works() {
        assertDoesNotThrow(() ->
                assertThatThrownBy(() -> { throw new IllegalArgumentException("bad"); })
                        .isInstanceOf(IllegalArgumentException.class));
    }

    @Test
    void assertThatStream_works() {
        assertDoesNotThrow(() ->
                assertThatStream(java.util.stream.Stream.of("a", "b")).hasSize(2));
    }

    @Test
    void assertAll_collectsFailures() {
        assertThrows(AssertionError.class, () ->
                assertAll(softly -> {
                    softly.checkThat("").isNotEmpty();
                    softly.checkThat(-1).isPositive();
                }));
    }

    @Test
    void assertThat_Path(@TempDir Path dir) throws IOException {
        Path f = Files.createFile(dir.resolve("t.txt"));
        assertDoesNotThrow(() -> RealityAssertions.assertThat(f).exists());
    }

    @Test
    void assertThat_CompletableFuture() {
        assertDoesNotThrow(
                () -> RealityAssertions.assertThat(CompletableFuture.completedFuture("x")).isDone());
    }

    @Test
    void assertThat_ZonedDateTime() {
        assertDoesNotThrow(
                () -> RealityAssertions.assertThat(ZonedDateTime.now()).isNotNull());
    }

    @Test
    void assertThat_OffsetDateTime() {
        assertDoesNotThrow(
                () -> RealityAssertions.assertThat(OffsetDateTime.now()).isNotNull());
    }

    @Test
    void assertAll_delegates() {
        assertThrows(AssertionError.class, () ->
                RealityAssertions.assertAll(soft -> soft.checkThat("a").isEqualTo("b")));
    }

    @Test
    void assertWithMessage_errorContainsMessage() {
        var e = assertThrows(AssertionError.class,
                () -> assertWithMessage("user validation failed").that("hello").isEqualTo("world"));
        assertTrue(e.getMessage().contains("user validation failed"));
        assertTrue(e.getMessage().contains("hello"));
        assertTrue(e.getMessage().contains("world"));
    }

    @Test
    void assertWithMessage_passDoesNotThrow() {
        assertDoesNotThrow(() ->
                assertWithMessage("should pass").that("hello").isEqualTo("hello"));
    }

    @Test
    void assertWithContext_errorContainsContext() {
        var e = assertThrows(AssertionError.class,
                () -> assertWithContext("signup flow").that("hello").isEqualTo("world"));
        assertTrue(e.getMessage().contains("[context: signup flow]"));
        assertTrue(e.getMessage().contains("hello"));
    }

    @Test
    void assertWithContext_passDoesNotThrow() {
        assertDoesNotThrow(() ->
                assertWithContext("signup flow").that("hello").isEqualTo("hello"));
    }

    @Test
    void satisfies_consumer_pass() {
        assertDoesNotThrow(() ->
                checkThat("hello").satisfies(s -> {
                    checkThat(s).isNotEmpty();
                    checkThat(s).startsWith("he");
                }));
    }

    @Test
    void satisfies_consumer_failWhenConsumerThrows() {
        assertThrows(AssertionError.class, () ->
                checkThat("hello").satisfies(s -> {
                    checkThat(s).isEqualTo("goodbye");
                }));
    }

    @Test
    void matches_pass() {
        assertDoesNotThrow(() -> checkThat(42).matches(n -> n > 0));
    }

    @Test
    void matches_failWithClearError() {
        var e = assertThrows(AssertionError.class,
                () -> checkThat(-5).matches(n -> n > 0));
        assertTrue(e.getMessage().contains("match the given predicate"));
        assertTrue(e.getMessage().contains("-5"));
    }

    @Test
    void satisfies_predicate_pass() {
        assertDoesNotThrow(() ->
                checkThat("test").satisfies(s -> s.length() == 4, "must be 4 chars"));
    }

    @Test
    void satisfies_predicate_failWithDescription() {
        var e = assertThrows(AssertionError.class,
                () -> checkThat("hi").satisfies(s -> s.length() > 5, "longer than 5 chars"));
        assertTrue(e.getMessage().contains("longer than 5 chars"));
        assertTrue(e.getMessage().contains("hi"));
    }

    @Test
    void isInstanceOf_pass() {
        assertDoesNotThrow(() -> checkThatObject("hello").isInstanceOf(String.class));
    }

    @Test
    void isInstanceOf_failWithWrongType() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatObject("hello").isInstanceOf(Integer.class));
        assertTrue(e.getMessage().contains("Integer"));
        assertTrue(e.getMessage().contains("String"));
    }

    @Test
    void isInstanceOf_failWithNullActual() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatObject(null).isInstanceOf(String.class));
        assertTrue(e.getMessage().contains("null"));
    }

    @Test
    void isInstanceOf_nullTypeThrowsNPE() {
        assertThrows(NullPointerException.class,
                () -> checkThatObject("hello").isInstanceOf(null));
    }

    @Test
    void isSameAs_pass() {
        Object obj = new Object();
        assertDoesNotThrow(() -> checkThatObject(obj).isSameAs(obj));
    }

    @Test
    void isSameAs_failWithDifferentInstance() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatObject(new String("a")).isSameAs(new String("a")));
        assertTrue(e.getMessage().contains("same instance"));
    }

    @Test
    void isNotSameAs_pass() {
        assertDoesNotThrow(() ->
                checkThatObject(new String("a")).isNotSameAs(new String("a")));
    }

    @Test
    void isNotSameAs_failWhenSameInstance() {
        Object obj = "shared";
        var e = assertThrows(AssertionError.class,
                () -> checkThatObject(obj).isNotSameAs(obj));
        assertTrue(e.getMessage().contains("different instance"));
    }

    @Test
    void isNull_pass() {
        assertDoesNotThrow(() -> checkThatObject(null).isNull());
    }

    @Test
    void isNull_failWhenNotNull() {
        assertThrows(AssertionError.class, () -> checkThatObject("x").isNull());
    }

    @Test
    void isNotNull_pass() {
        assertDoesNotThrow(() -> checkThatObject("x").isNotNull());
    }

    @Test
    void isNotNull_failWhenNull() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatObject(null).isNotNull());
        assertTrue(e.getMessage().contains("non-null"));
    }

    @Test
    void isEqualTo_pass() {
        assertDoesNotThrow(() -> checkThatObject("abc").isEqualTo("abc"));
    }

    @Test
    void isEqualTo_failWithDifferentValues() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatObject("abc").isEqualTo("xyz"));
        assertTrue(e.getMessage().contains("abc"));
        assertTrue(e.getMessage().contains("xyz"));
    }

    @Test
    void isNotEqualTo_pass() {
        assertDoesNotThrow(() -> checkThatObject("abc").isNotEqualTo("xyz"));
    }

    @Test
    void isNotEqualTo_failWhenEqual() {
        assertThrows(AssertionError.class,
                () -> checkThatObject("abc").isNotEqualTo("abc"));
    }

    @Test
    void assertWithMessage_numberCheck() {
        var e = assertThrows(AssertionError.class,
                () -> assertWithMessage("age check").that(0).isPositive());
        assertTrue(e.getMessage().contains("age check"));
    }

    @Test
    void assertWithMessage_booleanCheck() {
        var e = assertThrows(AssertionError.class,
                () -> assertWithMessage("flag").that(false).isTrue());
        assertTrue(e.getMessage().contains("flag"));
    }

    @Test
    void assertWithContext_numberCheck() {
        var e = assertThrows(AssertionError.class,
                () -> assertWithContext("price validation").that(-1).isPositive());
        assertTrue(e.getMessage().contains("[context: price validation]"));
    }

    @Test
    void assertThat_long_passAndFail() {
        assertDoesNotThrow(() -> assertThat(99L).isPositive());
        assertThrows(AssertionError.class, () -> assertThat(-1L).isPositive());
    }

    @Test
    void assertThat_double_passAndFail() {
        assertDoesNotThrow(() -> assertThat(0.1).isPositive());
        assertThrows(AssertionError.class, () -> assertThat(-0.5).isPositive());
    }

    @Test
    void assertThat_file_passAndFail() throws Exception {
        File f = Files.createTempFile("ra-file-", ".txt").toFile();
        try {
            assertDoesNotThrow(() -> assertThat(f).exists());
        } finally {
            Files.deleteIfExists(f.toPath());
        }
        assertThrows(AssertionError.class, () -> assertThat(new File("/nonexistent/path/ra-file")).exists());
    }

    @Test
    void assertThat_inputStream_passAndFail() {
        assertDoesNotThrow(() -> assertThat(new ByteArrayInputStream(new byte[]{7, 8, 9})).hasSize(3));
        assertThrows(AssertionError.class,
                () -> assertThat(new ByteArrayInputStream(new byte[]{1})).hasSize(2));
    }

    @Test
    void assertThat_localDate_passAndFail() {
        assertDoesNotThrow(() -> assertThat(LocalDate.of(2026, 3, 15)).hasYear(2026));
        assertThrows(AssertionError.class, () -> assertThat(LocalDate.of(2026, 1, 1)).hasYear(2025));
    }

    @Test
    void assertThat_localDateTime_passAndFail() {
        assertDoesNotThrow(() -> assertThat(LocalDateTime.of(2026, 4, 1, 16, 0)).hasHour(16));
        assertThrows(AssertionError.class,
                () -> assertThat(LocalDateTime.of(2026, 4, 1, 16, 0)).hasHour(15));
    }

    @Test
    void assertThat_longArray_passAndFail() {
        assertDoesNotThrow(() -> assertThat(new long[]{1L, 2L, 3L}).hasLength(3));
        assertThrows(AssertionError.class, () -> assertThat(new long[]{1L}).hasLength(2));
    }

    @Test
    void assertThat_doubleArray_passAndFail() {
        assertDoesNotThrow(() -> assertThat(new double[]{1.0, 2.0}).hasLength(2));
        assertThrows(AssertionError.class, () -> assertThat(new double[]{1.0}).hasLength(3));
    }

    @Test
    void assertThat_uri_passAndFail() {
        assertDoesNotThrow(() -> assertThat(URI.create("https://x.example")).hasScheme("https"));
        assertThrows(AssertionError.class, () -> assertThat(URI.create("https://x.example")).hasScheme("http"));
    }

    @Test
    void assertThat_bigInteger_passAndFail() {
        assertDoesNotThrow(() -> assertThat(BigInteger.valueOf(100)).isPositive());
        assertThrows(AssertionError.class, () -> assertThat(BigInteger.valueOf(-1)).isPositive());
    }

    @Test
    void assertThatIterable_passAndFail() {
        assertDoesNotThrow(() -> assertThatIterable(List.of(1, 2)).hasSize(2));
        assertThrows(AssertionError.class, () -> assertThatIterable(List.of(1)).hasSize(2));
    }

    @Test
    void assertThatArray_passAndFail() {
        assertDoesNotThrow(() -> assertThatArray("a", "b", "c").hasLength(3));
        assertThrows(AssertionError.class, () -> assertThatArray("a").hasLength(2));
    }

    @Test
    void assertThatEnum_passAndFail() {
        assertDoesNotThrow(() -> assertThatEnum(TimeUnit.MILLISECONDS).isEqualTo(TimeUnit.MILLISECONDS));
        assertThrows(AssertionError.class,
                () -> assertThatEnum(TimeUnit.MILLISECONDS).isEqualTo(TimeUnit.SECONDS));
    }

    @Test
    void assertThatSealed_passAndFail() {
        assertDoesNotThrow(() -> assertThatSealed(Shape.class).isSealed());
        assertThrows(AssertionError.class, () -> assertThatSealed(String.class).isSealed());
    }

    @Test
    void assertThatMultiline_passAndFail() {
        assertDoesNotThrow(() -> assertThatMultiline("one\ntwo\nthree").hasLineCount(3));
        assertThrows(AssertionError.class, () -> assertThatMultiline("a\nb").hasLineCount(5));
    }

    @Test
    void assertThatCode_passAndFail() {
        assertDoesNotThrow(() -> assertThatCode(() -> {}).doesNotThrow());
        assertThrows(AssertionError.class, () -> assertThatCode(() -> {
                    throw new IllegalStateException("x");
                })
                .doesNotThrow());
    }

    @Test
    void assertThatCsv_passAndFail() {
        assertDoesNotThrow(() -> assertThatCsv("a,b\n1,2").hasRowCount(2));
        assertThrows(AssertionError.class, () -> assertThatCsv("a,b\n1,2").hasRowCount(5));
    }

    @Test
    void assertThatObject_passAndFail() {
        assertDoesNotThrow(() -> assertThatObject("ref").isEqualTo("ref"));
        assertThrows(AssertionError.class, () -> assertThatObject("a").isEqualTo("b"));
    }

    @Test
    void assertThat_customCheckFactory_passAndFail() {
        assertDoesNotThrow(() -> assertThat(new Email("z@y"), EmailCheck::new).hasAt());
        assertThrows(AssertionError.class, () -> assertThat(new Email("z"), EmailCheck::new).hasAt());
    }

    @Test
    void assertAll_delegatesToReality() {
        var e = assertThrows(AssertionError.class, () ->
                assertAll(s -> {
                    s.checkThat("").isNotEmpty();
                    s.checkThat(0).isPositive();
                }));
        assertTrue(e.getMessage().contains("Multiple failures (2)"));
    }

    @Test
    void assertThatComparable_pass() {
        assertDoesNotThrow(() -> assertThatComparable(5).isGreaterThan(3));
    }

    @Test
    void assertThatComparable_fail() {
        assertThrows(AssertionError.class, () -> assertThatComparable(2).isGreaterThan(5));
    }

    @Test
    void assertThatCsvFile_path(@org.junit.jupiter.api.io.TempDir java.nio.file.Path tmp) throws java.io.IOException {
        java.nio.file.Path csv = tmp.resolve("test.csv");
        java.nio.file.Files.writeString(csv, "a,b\n1,2\n");
        assertDoesNotThrow(() -> assertThatCsvFile(csv).hasRowCount(2));
    }

    @Test
    void assertThatCsvFile_file(@org.junit.jupiter.api.io.TempDir java.nio.file.Path tmp) throws java.io.IOException {
        java.nio.file.Path csv = tmp.resolve("test.csv");
        java.nio.file.Files.writeString(csv, "a,b\n1,2\n");
        assertDoesNotThrow(() -> assertThatCsvFile(csv.toFile()).hasRowCount(2));
    }
}
