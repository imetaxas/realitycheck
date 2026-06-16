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
 * Main entry point for Reality Check assertions.
 *
 * <h2>Basic usage</h2>
 * <pre>{@code
 * import static io.github.imetaxas.realitycheck.Reality.*;
 *
 * checkThat("hello").isNotEmpty().startsWith("he").hasLength(5);
 * checkThat(42).isPositive().isBetween(1, 100);
 * checkThat(myFile).exists().hasExtension("csv").hasSameContentAs(expectedFile);
 * }</pre>
 *
 * <h2>Custom messages</h2>
 * <pre>{@code
 * checkWithMessage("User name must not be blank").that(name).isNotEmpty();
 * }</pre>
 *
 * <h2>Soft checks (collect all failures)</h2>
 * <pre>{@code
 * Reality.checkAll(softly -> {
 *     softly.checkThat(name).isNotEmpty();
 *     softly.checkThat(age).isPositive();
 *     softly.checkThat(email).contains("@");
 * });
 * }</pre>
 *
 * <h2>Custom extension (zero boilerplate)</h2>
 * <pre>{@code
 * checkThat(money, MoneyCheck::new).hasCurrency("USD");
 * }</pre>
 */
public final class Reality {

    private Reality() {}

    // ── Strings ──────────────────────────────────────────────────────────

    public static StringCheck checkThat(String actual) {
        return CheckFacade.string(actual, new FailureHandler());
    }

    // ── Numbers (primitive overloads) ────────────────────────────────────

    public static NumberCheck<Integer> checkThat(int actual) {
        return CheckFacade.intNumber(actual, new FailureHandler());
    }

    public static NumberCheck<Long> checkThat(long actual) {
        return CheckFacade.longNumber(actual, new FailureHandler());
    }

    public static NumberCheck<Double> checkThat(double actual) {
        return CheckFacade.doubleNumber(actual, new FailureHandler());
    }

    // ── Booleans ─────────────────────────────────────────────────────────

    public static BooleanCheck checkThat(boolean actual) {
        return CheckFacade.bool(actual, new FailureHandler());
    }

    // ── Files ────────────────────────────────────────────────────────────

    public static FileCheck checkThat(Path actual) {
        return CheckFacade.path(actual, new FailureHandler());
    }

    public static FileCheck checkThat(File actual) {
        return CheckFacade.file(actual, new FailureHandler());
    }

    // ── InputStreams ─────────────────────────────────────────────────────

    public static InputStreamCheck checkThat(InputStream actual) {
        return CheckFacade.inputStream(actual, new FailureHandler());
    }

    // ── Collections ──────────────────────────────────────────────────────

    public static <T> CollectionCheck<T> checkThat(Collection<T> actual) {
        return CheckFacade.collection(actual, new FailureHandler());
    }

    // ── Maps ─────────────────────────────────────────────────────────────

    public static <K, V> MapCheck<K, V> checkThat(Map<K, V> actual) {
        return CheckFacade.map(actual, new FailureHandler());
    }

    // ── Optionals ────────────────────────────────────────────────────────

    public static <T> OptionalCheck<T> checkThat(Optional<T> actual) {
        return CheckFacade.optional(actual, new FailureHandler());
    }

    // ── Futures ──────────────────────────────────────────────────────────

    public static <T> FutureCheck<T> checkThat(CompletableFuture<T> actual) {
        return CheckFacade.future(actual, new FailureHandler());
    }

    // ── Generic object ───────────────────────────────────────────────────

    public static <T> ObjectCheck<T> checkThatObject(T actual) {
        return CheckFacade.object(actual, new FailureHandler());
    }

    // ── Comparable ───────────────────────────────────────────────────────

    public static <T extends Comparable<T>> ComparableCheck<T> checkThatComparable(T actual) {
        return CheckFacade.comparable(actual, new FailureHandler());
    }

    // ── Date/Time ─────────────────────────────────────────────────────────

    public static InstantCheck checkThat(Instant actual) {
        return CheckFacade.instant(actual, new FailureHandler());
    }

    public static LocalDateCheck checkThat(LocalDate actual) {
        return CheckFacade.localDate(actual, new FailureHandler());
    }

    public static LocalDateTimeCheck checkThat(LocalDateTime actual) {
        return CheckFacade.localDateTime(actual, new FailureHandler());
    }

    public static DurationCheck checkThat(Duration actual) {
        return CheckFacade.duration(actual, new FailureHandler());
    }

    public static ZonedDateTimeCheck checkThat(ZonedDateTime actual) {
        return CheckFacade.zonedDateTime(actual, new FailureHandler());
    }

    public static OffsetDateTimeCheck checkThat(OffsetDateTime actual) {
        return CheckFacade.offsetDateTime(actual, new FailureHandler());
    }

    // ── Throwables ───────────────────────────────────────────────────────

    /**
     * Asserts that the given callable throws an exception.
     *
     * <pre>{@code
     * checkThatThrownBy(() -> service.process(null))
     *     .isInstanceOf(IllegalArgumentException.class)
     *     .hasMessageContaining("must not be null");
     * }</pre>
     */
    public static ThrowableCheck checkThatThrownBy(ThrowingCallable callable) {
        try {
            callable.call();
        } catch (Throwable t) {
            return new ThrowableCheck(t, new FailureHandler());
        }
        throw new AssertionError("expected an exception to be thrown but nothing was thrown");
    }

    // ── Iterables ────────────────────────────────────────────────────────

    public static <T> IterableCheck<T> checkThatIterable(Iterable<T> actual) {
        return CheckFacade.iterable(actual, new FailureHandler());
    }

    // ── Arrays ───────────────────────────────────────────────────────────

    @SafeVarargs
    public static <T> ArrayCheck<T> checkThatArray(T... actual) {
        return CheckFacade.array(actual, new FailureHandler());
    }

    // ── Primitive arrays ─────────────────────────────────────────────────

    public static IntArrayCheck checkThat(int[] actual) {
        return CheckFacade.intArray(actual, new FailureHandler());
    }

    public static LongArrayCheck checkThat(long[] actual) {
        return CheckFacade.longArray(actual, new FailureHandler());
    }

    public static DoubleArrayCheck checkThat(double[] actual) {
        return CheckFacade.doubleArray(actual, new FailureHandler());
    }

    // ── Byte arrays ─────────────────────────────────────────────────────

    public static ByteArrayCheck checkThat(byte[] actual) {
        return CheckFacade.byteArray(actual, new FailureHandler());
    }

    // ── URIs ─────────────────────────────────────────────────────────────

    public static UriCheck checkThat(URI actual) {
        return CheckFacade.uri(actual, new FailureHandler());
    }

    // ── Enums ────────────────────────────────────────────────────────────

    public static <E extends Enum<E>> EnumCheck<E> checkThatEnum(E actual) {
        return CheckFacade.enumValue(actual, new FailureHandler());
    }

    // ── UUID ──────────────────────────────────────────────────────────────

    public static UuidCheck checkThat(UUID actual) {
        return CheckFacade.uuid(actual, new FailureHandler());
    }

    // ── BigDecimal / BigInteger ──────────────────────────────────────────

    public static BigDecimalCheck checkThat(BigDecimal actual) {
        return CheckFacade.bigDecimal(actual, new FailureHandler());
    }

    public static BigIntegerCheck checkThat(BigInteger actual) {
        return CheckFacade.bigInteger(actual, new FailureHandler());
    }

    // ── Streams ──────────────────────────────────────────────────────────

    public static <T> StreamCheck<T> checkThatStream(Stream<T> actual) {
        return CheckFacade.stream(actual, new FailureHandler());
    }

    // ── Sealed classes ───────────────────────────────────────────────────

    public static SealedClassCheck checkThatSealed(Class<?> actual) {
        return CheckFacade.sealedClass(actual, new FailureHandler());
    }

    // ── Multiline strings ────────────────────────────────────────────────

    public static MultilineCheck checkThatMultiline(String actual) {
        return CheckFacade.multiline(actual, new FailureHandler());
    }

    // ── Execution (timing and behavior) ──────────────────────────────────

    /**
     * Wraps a code block for timing and exception assertions.
     *
     * <pre>{@code
     * checkThatCode(() -> sort(list)).completesWithin(Duration.ofSeconds(1));
     * checkThatCode(() -> parse(input)).doesNotThrow();
     * }</pre>
     */
    public static ExecutionCheck checkThatCode(ThrowingCallable callable) {
        return CheckFacade.code(callable, new FailureHandler());
    }

    // ── CSV ──────────────────────────────────────────────────────────────

    public static CsvCheck checkThatCsv(String csvContent) {
        return CheckFacade.csv(csvContent, new FailureHandler());
    }

    public static CsvCheck checkThatCsvFile(Path path) {
        return CheckFacade.csvFile(path, new FailureHandler());
    }

    public static CsvCheck checkThatCsvFile(File file) {
        return CheckFacade.csvFile(file, new FailureHandler());
    }

    // ── Custom extension ─────────────────────────────────────────────────

    /**
     * Creates a check using a custom {@link CheckFactory}.
     * Enables zero-boilerplate extension via method reference:
     * {@code Reality.checkThat(money, MoneyCheck::new)}
     */
    public static <C extends Check<C, T>, T> C checkThat(T actual, CheckFactory<C, T> factory) {
        return CheckFacade.custom(actual, factory, new FailureHandler());
    }

    // ── Custom messages ──────────────────────────────────────────────────

    /**
     * Starts a check with a custom failure message.
     * The custom message replaces the default detail on failure.
     */
    public static StatementBuilder checkWithMessage(String message) {
        return new StatementBuilder(new FailureHandler(message));
    }

    // ── Context messages ──────────────────────────────────────────────────

    /**
     * Starts a check with context that is appended to the default failure message.
     * Unlike {@link #checkWithMessage} which replaces the default message,
     * this preserves it and adds context.
     *
     * <pre>{@code
     * // checkWithMessage replaces:  "User name is required\n  detail: expected a non-empty string"
     * // checkWithContext appends:   "expected a non-empty string [context: validating user registration]"
     * checkWithContext("validating user registration").that(name).isNotEmpty();
     * }</pre>
     */
    public static StatementBuilder checkWithContext(String context) {
        return new StatementBuilder(FailureHandler.withContext(context));
    }

    // ── Soft checks ──────────────────────────────────────────────────────

    /**
     * Runs multiple checks collecting all failures, then reports them together.
     *
     * <pre>{@code
     * Reality.checkAll(softly -> {
     *     softly.checkThat("hello").hasLength(5);
     *     softly.checkThat(42).isPositive();
     * });
     * }</pre>
     *
     * @throws AssertionError if any checks within the block failed
     */
    public static void checkAll(Consumer<SoftChecks> block) {
        var soft = new SoftChecks();
        block.accept(soft);
        soft.assertAll();
    }
}
