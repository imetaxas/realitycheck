package com.yanimetaxas.realitycheck;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Soft assertion entry point. Collects all failures within the block
 * and reports them together when {@link #assertAll()} is called.
 *
 * <p>Used via {@code Reality.checkAll(softly -> { ... })} or instantiated directly.
 *
 * @see Reality#checkAll(java.util.function.Consumer)
 */
public final class SoftChecks {

    private final SoftFailureHandler handler = new SoftFailureHandler();

    public StringCheck checkThat(String actual) {
        return CheckFacade.string(actual, handler);
    }

    public NumberCheck<Integer> checkThat(int actual) {
        return CheckFacade.intNumber(actual, handler);
    }

    public NumberCheck<Long> checkThat(long actual) {
        return CheckFacade.longNumber(actual, handler);
    }

    public NumberCheck<Double> checkThat(double actual) {
        return CheckFacade.doubleNumber(actual, handler);
    }

    public BooleanCheck checkThat(boolean actual) {
        return CheckFacade.bool(actual, handler);
    }

    public FileCheck checkThat(Path actual) {
        return CheckFacade.path(actual, handler);
    }

    public FileCheck checkThat(File actual) {
        return CheckFacade.file(actual, handler);
    }

    public InputStreamCheck checkThat(InputStream actual) {
        return CheckFacade.inputStream(actual, handler);
    }

    public <T> CollectionCheck<T> checkThat(Collection<T> actual) {
        return CheckFacade.collection(actual, handler);
    }

    public <K, V> MapCheck<K, V> checkThat(Map<K, V> actual) {
        return CheckFacade.map(actual, handler);
    }

    public <T> OptionalCheck<T> checkThat(Optional<T> actual) {
        return CheckFacade.optional(actual, handler);
    }

    public <T> FutureCheck<T> checkThat(CompletableFuture<T> actual) {
        return CheckFacade.future(actual, handler);
    }

    public <T> ObjectCheck<T> checkThatObject(T actual) {
        return CheckFacade.object(actual, handler);
    }

    public InstantCheck checkThat(Instant actual) {
        return CheckFacade.instant(actual, handler);
    }

    public LocalDateCheck checkThat(LocalDate actual) {
        return CheckFacade.localDate(actual, handler);
    }

    public LocalDateTimeCheck checkThat(LocalDateTime actual) {
        return CheckFacade.localDateTime(actual, handler);
    }

    public DurationCheck checkThat(Duration actual) {
        return CheckFacade.duration(actual, handler);
    }

    public ZonedDateTimeCheck checkThat(ZonedDateTime actual) {
        return CheckFacade.zonedDateTime(actual, handler);
    }

    public OffsetDateTimeCheck checkThat(OffsetDateTime actual) {
        return CheckFacade.offsetDateTime(actual, handler);
    }

    public <T> IterableCheck<T> checkThatIterable(Iterable<T> actual) {
        return CheckFacade.iterable(actual, handler);
    }

    @SafeVarargs
    public final <T> ArrayCheck<T> checkThatArray(T... actual) {
        return CheckFacade.array(actual, handler);
    }

    public IntArrayCheck checkThat(int[] actual) {
        return CheckFacade.intArray(actual, handler);
    }

    public LongArrayCheck checkThat(long[] actual) {
        return CheckFacade.longArray(actual, handler);
    }

    public DoubleArrayCheck checkThat(double[] actual) {
        return CheckFacade.doubleArray(actual, handler);
    }

    public ByteArrayCheck checkThat(byte[] actual) {
        return CheckFacade.byteArray(actual, handler);
    }

    public UriCheck checkThat(URI actual) {
        return CheckFacade.uri(actual, handler);
    }

    public UuidCheck checkThat(UUID actual) {
        return CheckFacade.uuid(actual, handler);
    }

    public BigDecimalCheck checkThat(BigDecimal actual) {
        return CheckFacade.bigDecimal(actual, handler);
    }

    public BigIntegerCheck checkThat(BigInteger actual) {
        return CheckFacade.bigInteger(actual, handler);
    }

    public <T> StreamCheck<T> checkThatStream(Stream<T> actual) {
        return CheckFacade.stream(actual, handler);
    }

    public SealedClassCheck checkThatSealed(Class<?> actual) {
        return CheckFacade.sealedClass(actual, handler);
    }

    public <E extends Enum<E>> EnumCheck<E> checkThatEnum(E actual) {
        return CheckFacade.enumValue(actual, handler);
    }

    public MultilineCheck checkThatMultiline(String actual) {
        return CheckFacade.multiline(actual, handler);
    }

    public ExecutionCheck checkThatCode(ThrowingCallable callable) {
        return CheckFacade.code(callable, handler);
    }

    public ThrowableCheck checkThatThrownBy(ThrowingCallable callable) {
        return CheckFacade.thrownBy(callable, handler);
    }

    public ThrowableCheck assertThatThrownBy(ThrowingCallable callable) { return checkThatThrownBy(callable); }

    public <T extends Comparable<T>> ComparableCheck<T> checkThatComparable(T actual) {
        return CheckFacade.comparable(actual, handler);
    }

    public CsvCheck checkThatCsv(String csvContent) {
        return CheckFacade.csv(csvContent, handler);
    }

    public CsvCheck checkThatCsvFile(Path path) {
        return CheckFacade.csvFile(path, handler);
    }

    public CsvCheck checkThatCsvFile(File file) {
        return CheckFacade.csvFile(file, handler);
    }

    public <C extends Check<C, T>, T> C checkThat(T actual, CheckFactory<C, T> factory) {
        return CheckFacade.custom(actual, factory, handler);
    }

    // ── assertThat aliases (drop-in for AssertJ/Truth migration) ─────────

    public StringCheck assertThat(String actual) { return checkThat(actual); }
    public NumberCheck<Integer> assertThat(int actual) { return checkThat(actual); }
    public NumberCheck<Long> assertThat(long actual) { return checkThat(actual); }
    public NumberCheck<Double> assertThat(double actual) { return checkThat(actual); }
    public BooleanCheck assertThat(boolean actual) { return checkThat(actual); }
    public FileCheck assertThat(Path actual) { return checkThat(actual); }
    public FileCheck assertThat(File actual) { return checkThat(actual); }
    public InputStreamCheck assertThat(InputStream actual) { return checkThat(actual); }
    public <T> CollectionCheck<T> assertThat(Collection<T> actual) { return checkThat(actual); }
    public <K, V> MapCheck<K, V> assertThat(Map<K, V> actual) { return checkThat(actual); }
    public <T> OptionalCheck<T> assertThat(Optional<T> actual) { return checkThat(actual); }
    public <T> FutureCheck<T> assertThat(CompletableFuture<T> actual) { return checkThat(actual); }
    public InstantCheck assertThat(Instant actual) { return checkThat(actual); }
    public LocalDateCheck assertThat(LocalDate actual) { return checkThat(actual); }
    public LocalDateTimeCheck assertThat(LocalDateTime actual) { return checkThat(actual); }
    public DurationCheck assertThat(Duration actual) { return checkThat(actual); }
    public ZonedDateTimeCheck assertThat(ZonedDateTime actual) { return checkThat(actual); }
    public OffsetDateTimeCheck assertThat(OffsetDateTime actual) { return checkThat(actual); }
    public IntArrayCheck assertThat(int[] actual) { return checkThat(actual); }
    public LongArrayCheck assertThat(long[] actual) { return checkThat(actual); }
    public DoubleArrayCheck assertThat(double[] actual) { return checkThat(actual); }
    public ByteArrayCheck assertThat(byte[] actual) { return checkThat(actual); }
    public UriCheck assertThat(URI actual) { return checkThat(actual); }
    public UuidCheck assertThat(UUID actual) { return checkThat(actual); }
    public BigDecimalCheck assertThat(BigDecimal actual) { return checkThat(actual); }
    public BigIntegerCheck assertThat(BigInteger actual) { return checkThat(actual); }
    public <T> StreamCheck<T> assertThatStream(Stream<T> actual) { return checkThatStream(actual); }
    public SealedClassCheck assertThatSealed(Class<?> actual) { return checkThatSealed(actual); }
    public <E extends Enum<E>> EnumCheck<E> assertThatEnum(E actual) { return checkThatEnum(actual); }
    public MultilineCheck assertThatMultiline(String actual) { return checkThatMultiline(actual); }
    public ExecutionCheck assertThatCode(ThrowingCallable callable) { return checkThatCode(callable); }
    public CsvCheck assertThatCsv(String csvContent) { return checkThatCsv(csvContent); }
    public CsvCheck assertThatCsvFile(Path path) { return checkThatCsvFile(path); }
    public CsvCheck assertThatCsvFile(File file) { return checkThatCsvFile(file); }
    public <T extends Comparable<T>> ComparableCheck<T> assertThatComparable(T actual) { return checkThatComparable(actual); }
    public <T> ObjectCheck<T> assertThatObject(T actual) { return checkThatObject(actual); }
    public <T> IterableCheck<T> assertThatIterable(Iterable<T> actual) { return checkThatIterable(actual); }
    @SafeVarargs
    public final <T> ArrayCheck<T> assertThatArray(T... actual) { return checkThatArray(actual); }
    public <C extends Check<C, T>, T> C assertThat(T actual, CheckFactory<C, T> factory) { return checkThat(actual, factory); }

    /**
     * Reports all collected failures. Called automatically by {@code Reality.checkAll()}.
     */
    public void assertAll() {
        handler.assertAll();
    }
}
