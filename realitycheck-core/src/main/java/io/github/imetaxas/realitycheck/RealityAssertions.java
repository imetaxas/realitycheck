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
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Drop-in alias using the {@code assertThat} naming convention familiar
 * to users of AssertJ, Truth, and Hamcrest.
 *
 * <p>Every method delegates directly to the corresponding {@link Reality} method.
 * Use whichever naming you prefer — both are first-class entry points.
 *
 * <pre>{@code
 * import static io.github.imetaxas.realitycheck.RealityAssertions.*;
 *
 * assertThat("hello").isNotEmpty().startsWith("he");
 * assertThat(42).isPositive();
 * assertThatThrownBy(() -> boom()).isInstanceOf(IllegalStateException.class);
 * }</pre>
 */
public final class RealityAssertions {

    private RealityAssertions() {}

    // ── Strings ──────────────────────────────────────────────────────────

    public static StringCheck assertThat(String actual) {
        return Reality.checkThat(actual);
    }

    // ── Numbers ──────────────────────────────────────────────────────────

    public static NumberCheck<Integer> assertThat(int actual) {
        return Reality.checkThat(actual);
    }

    public static NumberCheck<Long> assertThat(long actual) {
        return Reality.checkThat(actual);
    }

    public static NumberCheck<Double> assertThat(double actual) {
        return Reality.checkThat(actual);
    }

    // ── Booleans ─────────────────────────────────────────────────────────

    public static BooleanCheck assertThat(boolean actual) {
        return Reality.checkThat(actual);
    }

    // ── Files ────────────────────────────────────────────────────────────

    public static FileCheck assertThat(Path actual) {
        return Reality.checkThat(actual);
    }

    public static FileCheck assertThat(File actual) {
        return Reality.checkThat(actual);
    }

    // ── InputStreams ─────────────────────────────────────────────────────

    public static InputStreamCheck assertThat(InputStream actual) {
        return Reality.checkThat(actual);
    }

    // ── Collections ──────────────────────────────────────────────────────

    public static <T> CollectionCheck<T> assertThat(Collection<T> actual) {
        return Reality.checkThat(actual);
    }

    // ── Maps ─────────────────────────────────────────────────────────────

    public static <K, V> MapCheck<K, V> assertThat(Map<K, V> actual) {
        return Reality.checkThat(actual);
    }

    // ── Optionals ────────────────────────────────────────────────────────

    public static <T> OptionalCheck<T> assertThat(Optional<T> actual) {
        return Reality.checkThat(actual);
    }

    // ── Futures ──────────────────────────────────────────────────────────

    public static <T> FutureCheck<T> assertThat(CompletableFuture<T> actual) {
        return Reality.checkThat(actual);
    }

    // ── Date/Time ────────────────────────────────────────────────────────

    public static InstantCheck assertThat(Instant actual) {
        return Reality.checkThat(actual);
    }

    public static LocalDateCheck assertThat(LocalDate actual) {
        return Reality.checkThat(actual);
    }

    public static LocalDateTimeCheck assertThat(LocalDateTime actual) {
        return Reality.checkThat(actual);
    }

    public static DurationCheck assertThat(Duration actual) {
        return Reality.checkThat(actual);
    }

    public static ZonedDateTimeCheck assertThat(ZonedDateTime actual) {
        return Reality.checkThat(actual);
    }

    public static OffsetDateTimeCheck assertThat(OffsetDateTime actual) {
        return Reality.checkThat(actual);
    }

    // ── Primitive arrays ─────────────────────────────────────────────────

    public static IntArrayCheck assertThat(int[] actual) {
        return Reality.checkThat(actual);
    }

    public static LongArrayCheck assertThat(long[] actual) {
        return Reality.checkThat(actual);
    }

    public static DoubleArrayCheck assertThat(double[] actual) {
        return Reality.checkThat(actual);
    }

    public static ByteArrayCheck assertThat(byte[] actual) {
        return Reality.checkThat(actual);
    }

    // ── URIs ─────────────────────────────────────────────────────────────

    public static UriCheck assertThat(URI actual) {
        return Reality.checkThat(actual);
    }

    // ── UUID ─────────────────────────────────────────────────────────────

    public static UuidCheck assertThat(UUID actual) {
        return Reality.checkThat(actual);
    }

    // ── BigDecimal / BigInteger ──────────────────────────────────────────

    public static BigDecimalCheck assertThat(BigDecimal actual) {
        return Reality.checkThat(actual);
    }

    public static BigIntegerCheck assertThat(BigInteger actual) {
        return Reality.checkThat(actual);
    }

    // ── Throwables ───────────────────────────────────────────────────────

    public static ThrowableCheck assertThatThrownBy(ThrowingCallable callable) {
        return Reality.checkThatThrownBy(callable);
    }

    // ── Iterables ────────────────────────────────────────────────────────

    public static <T> IterableCheck<T> assertThatIterable(Iterable<T> actual) {
        return Reality.checkThatIterable(actual);
    }

    // ── Arrays ───────────────────────────────────────────────────────────

    @SafeVarargs
    public static <T> ArrayCheck<T> assertThatArray(T... actual) {
        return Reality.checkThatArray(actual);
    }

    // ── Streams ──────────────────────────────────────────────────────────

    public static <T> StreamCheck<T> assertThatStream(Stream<T> actual) {
        return Reality.checkThatStream(actual);
    }

    // ── Enums ────────────────────────────────────────────────────────────

    public static <E extends Enum<E>> EnumCheck<E> assertThatEnum(E actual) {
        return Reality.checkThatEnum(actual);
    }

    // ── Sealed classes ───────────────────────────────────────────────────

    public static SealedClassCheck assertThatSealed(Class<?> actual) {
        return Reality.checkThatSealed(actual);
    }

    // ── Multiline strings ────────────────────────────────────────────────

    public static MultilineCheck assertThatMultiline(String actual) {
        return Reality.checkThatMultiline(actual);
    }

    // ── Execution ────────────────────────────────────────────────────────

    public static ExecutionCheck assertThatCode(ThrowingCallable callable) {
        return Reality.checkThatCode(callable);
    }

    // ── Comparable ────────────────────────────────────────────────────────

    public static <T extends Comparable<T>> ComparableCheck<T> assertThatComparable(T actual) {
        return Reality.checkThatComparable(actual);
    }

    // ── CSV ──────────────────────────────────────────────────────────────

    public static CsvCheck assertThatCsv(String csvContent) {
        return Reality.checkThatCsv(csvContent);
    }

    public static CsvCheck assertThatCsvFile(Path path) {
        return Reality.checkThatCsvFile(path);
    }

    public static CsvCheck assertThatCsvFile(File file) {
        return Reality.checkThatCsvFile(file);
    }

    // ── Generic object ───────────────────────────────────────────────────

    public static <T> ObjectCheck<T> assertThatObject(T actual) {
        return Reality.checkThatObject(actual);
    }

    // ── Custom extension ─────────────────────────────────────────────────

    public static <C extends Check<C, T>, T> C assertThat(T actual, CheckFactory<C, T> factory) {
        return Reality.checkThat(actual, factory);
    }

    // ── Custom messages ──────────────────────────────────────────────────

    public static StatementBuilder assertWithMessage(String message) {
        return Reality.checkWithMessage(message);
    }

    public static StatementBuilder assertWithContext(String context) {
        return Reality.checkWithContext(context);
    }

    // ── Soft checks ──────────────────────────────────────────────────────

    public static void assertAll(Consumer<SoftChecks> block) {
        Reality.checkAll(block);
    }
}
