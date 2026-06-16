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
 * Builder for assertions with a custom failure message.
 * Created via {@code Reality.checkWithMessage("...")}.
 */
public final class StatementBuilder {

    private final FailureHandler handler;

    StatementBuilder(FailureHandler handler) {
        this.handler = handler;
    }

    public StringCheck that(String actual) {
        return CheckFacade.string(actual, handler);
    }

    public BooleanCheck that(boolean actual) {
        return CheckFacade.bool(actual, handler);
    }

    public NumberCheck<Integer> that(int actual) {
        return CheckFacade.intNumber(actual, handler);
    }

    public NumberCheck<Long> that(long actual) {
        return CheckFacade.longNumber(actual, handler);
    }

    public NumberCheck<Double> that(double actual) {
        return CheckFacade.doubleNumber(actual, handler);
    }

    public FileCheck that(Path actual) {
        return CheckFacade.path(actual, handler);
    }

    public FileCheck that(File actual) {
        return CheckFacade.file(actual, handler);
    }

    public InputStreamCheck that(InputStream actual) {
        return CheckFacade.inputStream(actual, handler);
    }

    public <T> CollectionCheck<T> that(Collection<T> actual) {
        return CheckFacade.collection(actual, handler);
    }

    public <K, V> MapCheck<K, V> that(Map<K, V> actual) {
        return CheckFacade.map(actual, handler);
    }

    public <T> OptionalCheck<T> that(Optional<T> actual) {
        return CheckFacade.optional(actual, handler);
    }

    public <T> FutureCheck<T> that(CompletableFuture<T> actual) {
        return CheckFacade.future(actual, handler);
    }

    public <T> ObjectCheck<T> thatObject(T actual) {
        return CheckFacade.object(actual, handler);
    }

    public <T extends Comparable<T>> ComparableCheck<T> thatComparable(T actual) {
        return CheckFacade.comparable(actual, handler);
    }

    public InstantCheck that(Instant actual) {
        return CheckFacade.instant(actual, handler);
    }

    public LocalDateCheck that(LocalDate actual) {
        return CheckFacade.localDate(actual, handler);
    }

    public LocalDateTimeCheck that(LocalDateTime actual) {
        return CheckFacade.localDateTime(actual, handler);
    }

    public DurationCheck that(Duration actual) {
        return CheckFacade.duration(actual, handler);
    }

    public ZonedDateTimeCheck that(ZonedDateTime actual) {
        return CheckFacade.zonedDateTime(actual, handler);
    }

    public OffsetDateTimeCheck that(OffsetDateTime actual) {
        return CheckFacade.offsetDateTime(actual, handler);
    }

    public <T> IterableCheck<T> thatIterable(Iterable<T> actual) {
        return CheckFacade.iterable(actual, handler);
    }

    @SafeVarargs
    public final <T> ArrayCheck<T> thatArray(T... actual) {
        return CheckFacade.array(actual, handler);
    }

    public IntArrayCheck that(int[] actual) {
        return CheckFacade.intArray(actual, handler);
    }

    public LongArrayCheck that(long[] actual) {
        return CheckFacade.longArray(actual, handler);
    }

    public DoubleArrayCheck that(double[] actual) {
        return CheckFacade.doubleArray(actual, handler);
    }

    public ByteArrayCheck that(byte[] actual) {
        return CheckFacade.byteArray(actual, handler);
    }

    public UriCheck that(URI actual) {
        return CheckFacade.uri(actual, handler);
    }

    public UuidCheck that(UUID actual) {
        return CheckFacade.uuid(actual, handler);
    }

    public BigDecimalCheck that(BigDecimal actual) {
        return CheckFacade.bigDecimal(actual, handler);
    }

    public BigIntegerCheck that(BigInteger actual) {
        return CheckFacade.bigInteger(actual, handler);
    }

    public <T> StreamCheck<T> thatStream(Stream<T> actual) {
        return CheckFacade.stream(actual, handler);
    }

    public SealedClassCheck thatSealed(Class<?> actual) {
        return CheckFacade.sealedClass(actual, handler);
    }

    public <E extends Enum<E>> EnumCheck<E> thatEnum(E actual) {
        return CheckFacade.enumValue(actual, handler);
    }

    public MultilineCheck thatMultiline(String actual) {
        return CheckFacade.multiline(actual, handler);
    }

    public ExecutionCheck thatCode(ThrowingCallable callable) {
        return CheckFacade.code(callable, handler);
    }

    public CsvCheck thatCsv(String csvContent) {
        return CheckFacade.csv(csvContent, handler);
    }

    public CsvCheck thatCsvFile(Path path) {
        return CheckFacade.csvFile(path, handler);
    }

    public CsvCheck thatCsvFile(File file) {
        return CheckFacade.csvFile(file, handler);
    }

    public <C extends Check<C, T>, T> C that(T actual, CheckFactory<C, T> factory) {
        return CheckFacade.custom(actual, factory, handler);
    }
}
