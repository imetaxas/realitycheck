package io.github.imetaxas.realitycheck;

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
 * Centralised factory for all check instances. Each method accepts the value under test
 * and a {@link FailureHandler} (or {@link SoftFailureHandler}), returning the appropriate
 * check object. Entry-point classes ({@link Reality}, {@link SoftChecks},
 * {@link StatementBuilder}) delegate here to avoid duplicating construction logic.
 */
final class CheckFacade {

    private CheckFacade() {}

    static StringCheck string(String actual, FailureHandler handler) {
        return new StringCheck(actual, handler);
    }

    static NumberCheck<Integer> intNumber(int actual, FailureHandler handler) {
        return new NumberCheck<>(actual, handler);
    }

    static NumberCheck<Long> longNumber(long actual, FailureHandler handler) {
        return new NumberCheck<>(actual, handler);
    }

    static NumberCheck<Double> doubleNumber(double actual, FailureHandler handler) {
        return new NumberCheck<>(actual, handler);
    }

    static BooleanCheck bool(boolean actual, FailureHandler handler) {
        return new BooleanCheck(actual, handler);
    }

    static FileCheck path(Path actual, FailureHandler handler) {
        return new FileCheck(actual, handler);
    }

    static FileCheck file(File actual, FailureHandler handler) {
        return new FileCheck(actual == null ? null : actual.toPath(), handler);
    }

    static InputStreamCheck inputStream(InputStream actual, FailureHandler handler) {
        return new InputStreamCheck(actual, handler);
    }

    static <T> CollectionCheck<T> collection(Collection<T> actual, FailureHandler handler) {
        return new CollectionCheck<>(actual, handler);
    }

    static <K, V> MapCheck<K, V> map(Map<K, V> actual, FailureHandler handler) {
        return new MapCheck<>(actual, handler);
    }

    static <T> OptionalCheck<T> optional(Optional<T> actual, FailureHandler handler) {
        return new OptionalCheck<>(actual, handler);
    }

    static <T> FutureCheck<T> future(CompletableFuture<T> actual, FailureHandler handler) {
        return new FutureCheck<>(actual, handler);
    }

    static <T> ObjectCheck<T> object(T actual, FailureHandler handler) {
        return new ObjectCheck<>(actual, handler);
    }

    static <T extends Comparable<T>> ComparableCheck<T> comparable(T actual, FailureHandler handler) {
        return new ComparableCheck<>(actual, handler);
    }

    static InstantCheck instant(Instant actual, FailureHandler handler) {
        return new InstantCheck(actual, handler);
    }

    static LocalDateCheck localDate(LocalDate actual, FailureHandler handler) {
        return new LocalDateCheck(actual, handler);
    }

    static LocalDateTimeCheck localDateTime(LocalDateTime actual, FailureHandler handler) {
        return new LocalDateTimeCheck(actual, handler);
    }

    static DurationCheck duration(Duration actual, FailureHandler handler) {
        return new DurationCheck(actual, handler);
    }

    static ZonedDateTimeCheck zonedDateTime(ZonedDateTime actual, FailureHandler handler) {
        return new ZonedDateTimeCheck(actual, handler);
    }

    static OffsetDateTimeCheck offsetDateTime(OffsetDateTime actual, FailureHandler handler) {
        return new OffsetDateTimeCheck(actual, handler);
    }

    static <T> IterableCheck<T> iterable(Iterable<T> actual, FailureHandler handler) {
        return new IterableCheck<>(actual, handler);
    }

    static <T> ArrayCheck<T> array(T[] actual, FailureHandler handler) {
        return new ArrayCheck<>(actual, handler);
    }

    static IntArrayCheck intArray(int[] actual, FailureHandler handler) {
        return new IntArrayCheck(actual, handler);
    }

    static LongArrayCheck longArray(long[] actual, FailureHandler handler) {
        return new LongArrayCheck(actual, handler);
    }

    static DoubleArrayCheck doubleArray(double[] actual, FailureHandler handler) {
        return new DoubleArrayCheck(actual, handler);
    }

    static ByteArrayCheck byteArray(byte[] actual, FailureHandler handler) {
        return new ByteArrayCheck(actual, handler);
    }

    static UriCheck uri(URI actual, FailureHandler handler) {
        return new UriCheck(actual, handler);
    }

    static <E extends Enum<E>> EnumCheck<E> enumValue(E actual, FailureHandler handler) {
        return new EnumCheck<>(actual, handler);
    }

    static UuidCheck uuid(UUID actual, FailureHandler handler) {
        return new UuidCheck(actual, handler);
    }

    static BigDecimalCheck bigDecimal(BigDecimal actual, FailureHandler handler) {
        return new BigDecimalCheck(actual, handler);
    }

    static BigIntegerCheck bigInteger(BigInteger actual, FailureHandler handler) {
        return new BigIntegerCheck(actual, handler);
    }

    static <T> StreamCheck<T> stream(Stream<T> actual, FailureHandler handler) {
        return new StreamCheck<>(actual, handler);
    }

    static SealedClassCheck sealedClass(Class<?> actual, FailureHandler handler) {
        return new SealedClassCheck(actual, handler);
    }

    static MultilineCheck multiline(String actual, FailureHandler handler) {
        return new MultilineCheck(actual, handler);
    }

    static ExecutionCheck code(ThrowingCallable callable, FailureHandler handler) {
        return new ExecutionCheck(callable, handler);
    }

    static ThrowableCheck thrownBy(ThrowingCallable callable, FailureHandler handler) {
        try {
            callable.call();
        } catch (Throwable t) {
            return new ThrowableCheck(t, handler);
        }
        handler.fail("expected an exception to be thrown but nothing was thrown");
        return new ThrowableCheck(null, handler);
    }

    static CsvCheck csv(String csvContent, FailureHandler handler) {
        return new CsvCheck(csvContent, handler);
    }

    static CsvCheck csvFile(Path path, FailureHandler handler) {
        return CsvCheck.fromFile(path, handler);
    }

    static CsvCheck csvFile(File file, FailureHandler handler) {
        return CsvCheck.fromFile(file.toPath(), handler);
    }

    static <C extends Check<C, T>, T> C custom(T actual, CheckFactory<C, T> factory, FailureHandler handler) {
        return factory.create(actual, handler);
    }
}
