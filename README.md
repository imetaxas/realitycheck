# Reality Check

[![CI](https://github.com/imetaxas/realitycheck/actions/workflows/ci.yml/badge.svg)](https://github.com/imetaxas/realitycheck/actions)
[![Maven Central](https://img.shields.io/maven-central/v/com.yanimetaxas/realitycheck-core)](https://central.sonatype.com/artifact/com.yanimetaxas/realitycheck-core)
[![codecov](https://codecov.io/gh/imetaxas/realitycheck/graph/badge.svg)](https://codecov.io/gh/imetaxas/realitycheck)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

**The data-native assertion library for modern Java.**

Reality Check is a fluent assertion framework built for Java 17+ that goes beyond generic assertions to provide first-class support for **data formats** (JSON, CSV, XML, YAML), **URIs**, **date/time**, **exception chains**, **execution timing**, **multiline strings**, **snapshot testing**, and **zero-boilerplate extensibility**. No reflection. No surprises.

> **v1.0 Highlights** &mdash; 1,800+ tests at 95%+ line coverage · RFC 4180-compliant CSV parser · XXE-safe XML parsing · JSON array-index paths (`users[0].name`) · thread-safe soft assertions · `throwsExactly()` vs `throwsInstanceOf()` · locale-safe string comparison · null-safe assertion parameters · zero NPE risk

## Why Reality Check?

| Feature | JUnit 5 | Google Truth | AssertJ | Reality Check |
|---|:---:|:---:|:---:|:---:|
| Structured error messages with expected/actual | Basic | Yes | Yes | **Yes** |
| JSON structural diff | - | - | - | **Yes** |
| CSV assertions | - | - | - | **Yes** |
| XML assertions (XPath) | - | - | - | **Yes** |
| YAML assertions (dot-path) | - | - | - | **Yes** |
| Snapshot testing (golden files) | - | - | - | **Yes** |
| File content diff in error messages | - | - | - | **Yes** |
| URI/URL component assertions | - | - | Limited | **Yes** |
| Multiline string assertions with diff | - | - | - | **Yes** |
| Map dot-path navigation | - | - | - | **Yes** |
| Execution timing assertions | - | - | - | **Yes** |
| Exception cause chain traversal | - | Limited | Limited | **Yes** |
| Date/Time assertions (java.time) | - | - | Yes | **Yes** |
| Iterable assertions (lazy) | - | - | Yes | **Yes** |
| Array assertions | Basic | Yes | Yes | **Yes** |
| Enum assertions | - | - | Basic | **Yes** (name, ordinal, `isOneOf`, `isNoneOf`) |
| Primitive array assertions (`int[]`, `double[]`, `long[]`) | - | Limited ([#571](https://github.com/google/truth/issues/571)) | Yes | **Yes** |
| BigDecimal / BigInteger assertions | - | Missing ([#540](https://github.com/google/truth/issues/540)) | Yes | **Yes** |
| Stream assertions | - | Missing ([#342](https://github.com/google/truth/issues/342)) | Yes | **Yes** |
| Regex capture group assertions | - | - | - | **Yes** |
| UUID assertions | - | - | - | **Yes** |
| Byte array assertions | - | - | Yes | **Yes** (+ `toHex()`, `toBase64()` extractors) |
| Sealed class assertions | - | - | - | **Yes** |
| ZonedDateTime / OffsetDateTime | - | - | Yes | **Yes** |
| Suppressed exception access | - | Missing ([#717](https://github.com/google/truth/issues/717)) | - | **Yes** |
| Soft assertions | `assertAll()` (no chaining) | JUnit 4 only ([#893](https://github.com/google/truth/issues/893)) | Yes (buggy: [#1353](https://github.com/assertj/assertj/issues/1353), [#2356](https://github.com/assertj/assertj/issues/2356)) | **Yes** |
| Fluent method chaining | No | No ([#884](https://github.com/google/truth/issues/884)) | Yes | **Yes** |
| Zero-boilerplate custom extension | - | ~50 lines ([#208](https://github.com/google/truth/issues/208)) | ~30 lines | **3 lines** |
| RFC 4180-compliant CSV parser | - | - | - | **Yes** (quoted fields, embedded commas/newlines) |
| XXE-safe XML parsing | - | - | - | **Yes** (DOCTYPE/external entities blocked) |
| JSON array-index paths (`users[0].name`) | - | - | - | **Yes** |
| Thread-safe soft assertions | - (stateless) | JUnit 4 only | Not thread-safe ([#2356](https://github.com/assertj/assertj/issues/2356)) | **Yes** (`CopyOnWriteArrayList`) |
| Null-safe expected values in assertions | - | - | Partial | **Yes** (`Objects.equals` throughout) |
| Reflective object comparison | No | No | Yes (flaky) | **No (by design)** |
| LLM/AI response assertions | - | - | - | **Planned** (`realitycheck-ai`) |
| Zero runtime dependencies (core) | Yes | No (Guava: [#1608](https://github.com/google/truth/issues/1608)) | Yes | **Yes** |
| `assertThat()` drop-in alias | - | Yes | Yes | **Yes** (`RealityAssertions`) |
| Modern Java (17+, records, sealed) | Java 8 | Java 8 | Java 8 | **Java 17+** |

## Quick Start

**Maven:**

```xml
<dependency>
    <groupId>com.yanimetaxas</groupId>
    <artifactId>realitycheck-core</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```

**Gradle (Kotlin DSL):**

```kotlin
testImplementation("com.yanimetaxas:realitycheck-core:1.0.0")
```

**Gradle (Groovy DSL):**

```groovy
testImplementation 'com.yanimetaxas:realitycheck-core:1.0.0'
```

Or use the BOM for multiple modules:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.yanimetaxas</groupId>
            <artifactId>realitycheck-bom</artifactId>
            <version>1.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

Gradle BOM:

```kotlin
testImplementation(platform("com.yanimetaxas:realitycheck-bom:1.0.0"))
testImplementation("com.yanimetaxas:realitycheck-core")
testImplementation("com.yanimetaxas:realitycheck-json")
```

### 30-Second Example

Copy, paste, run. This is a complete test class:

```java
import static com.yanimetaxas.realitycheck.RealityAssertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

class QuickStartTest {

    @Test
    void strings() {
        assertThat("hello world").isNotEmpty().startsWith("hello").hasLength(11);
    }

    @Test
    void numbers() {
        assertThat(42).isPositive().isAtLeast(1).isAtMost(100);
    }

    @Test
    void collections() {
        assertThat(List.of("a", "b", "c")).hasSize(3).contains("b");
    }

    @Test
    void exceptions() {
        assertThatThrownBy(() -> Integer.parseInt("not a number"))
            .isInstanceOf(NumberFormatException.class)
            .hasMessageContaining("not a number");
    }

    @Test
    void oneOffPredicate() {
        assertThat("admin@company.com").matches(email -> email.contains("@"));
    }
}
```

> **Coming from AssertJ or Truth?** `assertThat` works out of the box — same muscle memory, zero friction.
> Prefer Reality Check's canonical style? Use `import static com.yanimetaxas.realitycheck.Reality.*` for `checkThat`.

### What Happens When a Test Fails?

Reality Check produces structured error messages with expected vs. actual values:

```
assertThat("hello").isEqualTo("world");
→ AssertionError: expected: <world> but was: <hello>

assertThat(List.of("a", "b")).contains("c");
→ AssertionError: expected collection [a, b] to contain <c>

assertThat(42).isAtLeast(100);
→ AssertionError: expected a value >= <100> but was: <42>

assertThatThrownBy(() -> safe()).isInstanceOf(IOException.class);
→ AssertionError: expected <java.io.IOException> to be thrown but nothing was thrown

assertThatJson(json).hasField("$.user.name");
→ AssertionError: expected JSON to have field at <$.user.name> but field was absent
```

For multiline and JSON diffs, failures include a line-by-line diff:

```
assertThatJson(actualJson).isEqualTo(expectedJson);
→ AssertionError: JSON content differs:
    - "name": "Alice"
    + "name": "Bob"
      "age": 30
```

## Usage

```java
import static com.yanimetaxas.realitycheck.RealityAssertions.*;  // assertThat() — recommended
```

> Prefer Reality Check's own naming? `import static com.yanimetaxas.realitycheck.Reality.*` gives you `checkThat()`.

### Strings

```java
assertThat("hello").isNotEmpty().startsWith("he").hasLength(5);
assertThat("hello world").contains("world").matches("hello\\s\\w+");
```

### Numbers

```java
assertThat(42).isPositive().isGreaterThan(0).isBetween(1, 100);
assertThat(score).isAtLeast(60).isAtMost(100);
assertThat(3.14).isCloseTo(3.15, 0.02);
```

### Collections

```java
assertThat(List.of(1, 2, 3)).hasSize(3).contains(2).doesNotContain(5);
assertThat(List.of("a", "b")).containsExactly("a", "b");
assertThat(List.of(2, 4, 6)).allMatch(n -> n % 2 == 0, "is even");
```

### Maps

```java
assertThat(Map.of("name", "Alice")).containsKey("name").containsEntry("name", "Alice");
```

#### Map path navigation (nested maps)

```java
Map<String, Object> config = Map.of("database", Map.of("connection", Map.of("host", "localhost")));

assertThat(config).atPath("database.connection.host").isEqualTo("localhost");
assertThat(config).stringAtPath("database.connection.host").startsWith("local");
assertThat(config).mapAtPath("database.connection").containsKey("host").hasSize(1);
```

### Optionals

```java
assertThat(Optional.of("hello")).isPresent().hasValue("hello");
assertThat(Optional.empty()).isEmpty();
```

### Futures

```java
assertThat(myFuture).completesWithin(Duration.ofSeconds(5)).hasValue("done");
```

### Date/Time

```java
assertThat(Instant.now().minusSeconds(60)).isInThePast().isBefore(Instant.now());
assertThat(event.timestamp()).isCloseTo(Instant.now(), Duration.ofSeconds(5));

assertThat(LocalDate.of(2026, 3, 31)).hasYear(2026).hasMonth(Month.MARCH).isWeekday();
assertThat(birthday).isInThePast().isLeapYear();

assertThat(meeting).hasHour(14).hasMinute(30).date().hasMonth(Month.MARCH);

assertThat(Duration.ofSeconds(30)).isPositive().isGreaterThan(Duration.ofSeconds(10));

assertThat(zonedEvent).hasZone(ZoneId.of("UTC")).hasYear(2026).hasHour(14);
assertThat(zonedEvent).date().hasMonth(Month.MARCH);  // extract date for further checks

assertThat(apiTimestamp).hasOffset(ZoneOffset.UTC).isInThePast();
```

### Exceptions

```java
assertThatThrownBy(() -> service.process(null))
    .isInstanceOf(IllegalArgumentException.class)
    .hasMessageContaining("must not be null")
    .hasNoCause();

assertThatThrownBy(() -> db.connect())
    .cause().isInstanceOf(IOException.class)
    .rootCause().hasMessageContaining("refused");

assertThatThrownBy(() -> parse(input))
    .message().startsWith("Error").contains("line 42");

assertThatThrownBy(() -> riskyOperation())
    .hasSuppressed().hasSuppressedCount(2)
    .hasSuppressedInstanceOf(IOException.class);

assertThatThrownBy(() -> riskyOperation())
    .suppressedException(0).hasMessageContaining("cleanup failed");
```

### URIs

```java
assertThat(URI.create("https://api.example.com:8443/v2/users?page=1&sort=name#top"))
    .isAbsolute()
    .hasScheme("https")
    .hasHost("api.example.com")
    .hasPort(8443)
    .hasPath("/v2/users")
    .hasQueryParam("page", "1")
    .hasFragment("top");

assertThat(uri).host().contains("example");
assertThat(uri).path().startsWith("/v2");
```

### Multiline Strings

```java
assertThatMultiline(logOutput)
    .hasLineCount(5)
    .firstLine().startsWith("INFO");

assertThatMultiline(output)
    .containsLineMatching(".*ERROR.*\\d{3}")
    .noLineMatches(".*FATAL.*");

assertThatMultiline(actual).isEqualTo(expected);  // Line-by-line diff on failure
```

### Enums

```java
assertThatEnum(status).hasName("ACTIVE").hasOrdinal(0);
assertThatEnum(priority).isOneOf(Priority.HIGH, Priority.CRITICAL);
assertThatEnum(day).isNoneOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
assertThatEnum(status).name().startsWith("ACT");
```

### Execution Timing

```java
assertThatCode(() -> sort(largeList)).completesWithin(Duration.ofSeconds(2));
assertThatCode(() -> parse(input)).doesNotThrow();

// throwsInstanceOf — matches the exact type and any subclass
assertThatCode(() -> badCall()).throwsInstanceOf(RuntimeException.class).hasMessage("broken");

// throwsExactly — matches only the exact type, rejects subclasses
assertThatCode(() -> badCall()).throwsExactly(IllegalStateException.class).hasMessage("broken");

assertThatCode(() -> process(data)).measuredTime()
    .isGreaterThan(Duration.ofMillis(10))
    .isLessThan(Duration.ofSeconds(5));
```

### Iterables (lazy)

```java
assertThatIterable(lazySequence).isNotEmpty().contains("target");
assertThatIterable(generator).hasSize(100).allMatch(x -> x > 0, "positive");
assertThatIterable(stream::iterator).containsAll("a", "b");
```

### Arrays

```java
assertThatArray("a", "b", "c").hasLength(3).contains("b").isSorted();
assertThatArray(1, 2, 3).containsExactly(1, 2, 3);
assertThatArray(results).allMatch(r -> r > 0, "positive");
```

### Primitive Arrays

```java
assertThat(new int[]{1, 2, 3}).hasLength(3).contains(2).isSorted();
assertThat(new int[]{2, 4, 6}).allMatch(n -> n % 2 == 0, "even");

assertThat(new double[]{1.0, 2.5, 3.7}).isSorted();
assertThat(measurements).containsCloseTo(3.14, 0.01);  // tolerance-based

assertThat(new long[]{100L, 200L}).containsExactly(100L, 200L);
```

### BigDecimal / BigInteger

```java
assertThat(new BigDecimal("99.99")).isPositive().hasScale(2).isLessThan(new BigDecimal("100"));
assertThat(new BigDecimal("1.0")).isEqualByComparingTo(new BigDecimal("1.00"));  // ignores scale
assertThat(price).isCloseTo(new BigDecimal("3.14"), new BigDecimal("0.01"));

assertThat(BigInteger.valueOf(17)).isPositive().isOdd().isProbablePrime();
assertThat(count).isEven().hasBitLength(32);
```

### Streams

```java
assertThatStream(Stream.of("a", "b", "c")).hasSize(3).contains("b");
assertThatStream(dataStream).allMatch(x -> x > 0, "positive");
assertThatStream(results).first().isEqualTo("expected");
assertThatStream(items).toList().hasSize(5);  // chain into CollectionCheck
```

### UUIDs

```java
assertThat(UUID.randomUUID()).isNotNil().isVersion4();
assertThat(traceId).hasVersion(4);
assertThat(uuid).asString().hasLength(36).contains("-");
```

### Byte Arrays

```java
assertThat(data).isNotEmpty().hasLength(256);
assertThat(header).startsWith(new byte[]{0x50, 0x4B});  // ZIP magic bytes
assertThat(payload).toBase64().startsWith("eyJ");
assertThat(hash).toHex().contains("0xCA");
```

### Sealed Classes

```java
assertThatSealed(Shape.class).isSealed().permittedCount(3);
assertThatSealed(Shape.class).permits(Circle.class);
assertThatSealed(Shape.class).permitsExactly(Circle.class, Square.class, Triangle.class);
```

### Regex Capture Groups

```java
assertThat(logLine).matchesAndCaptures("(\\w+) (\\d+) (.+)")
    .hasGroupCount(3)
    .group(1).isEqualTo("ERROR");

assertThat(timestamp).matchesAndCaptures("(?<year>\\d{4})-(?<month>\\d{2})")
    .group("year").isEqualTo("2026");
```

### Files

```java
assertThat(path).exists().isRegularFile().hasExtension("csv").isNotEmpty();
assertThat(fileA).hasSameContentAs(fileB);  // Structured diff on failure!
```

### CSV

RFC 4180-compliant: handles quoted fields, embedded commas, newlines, and escaped quotes.

```java
assertThatCsv(csvString).hasRowCount(10).hasColumnCount(3).headerEquals("id", "name", "age");
assertThatCsvFile(path).headerHasNoDigits().containsRow("1", "Alice", "30");

// Quoted fields with embedded commas and newlines work correctly
String rfc4180 = "name,address\n\"Smith, Jr.\",\"123 Main St\nApt 4\"";
// hasRowCount counts all rows including the header (2 total: 1 header + 1 data row)
assertThatCsv(rfc4180).hasRowCount(2).hasColumnCount(2);
// hasDataRowCount counts only data rows (excluding the header)
assertThatCsv(rfc4180).hasDataRowCount(1);

// Escaped quotes within quoted fields
String escaped = "title\n\"She said \"\"hello\"\"\"";
assertThatCsv(escaped).containsRow("She said \"hello\"");
```

### Custom Messages

```java
assertWithMessage("User name must not be blank").that(name).isNotEmpty();
assertWithMessage("Feature flag should be on").that(enabled).isTrue();
```

### Soft Assertions (collect all failures)

Thread-safe — failures can be recorded from parallel streams or concurrent threads without data races.

```java
assertAll(softly -> {
    softly.assertThat(name).isNotEmpty();
    softly.assertThat(age).isPositive();
    softly.assertThat(email).contains("@");
});
// Throws one error listing ALL failures
```

Or with JUnit 5 (automatic `assertAll()`):

```java
@WithSoftChecks
class MyTest {
    @Test
    void allFieldsValid(SoftChecks softly) {
        softly.assertThat(name).isNotEmpty();
        softly.assertThat(age).isPositive();
    }
}
```

## Data Format Modules

### JSON (`realitycheck-json`)

```java
import static com.yanimetaxas.realitycheck.json.JsonReality.*;

assertThatJson(response)
    .isValidJson()
    .hasField("user.name")
    .fieldEquals("user.name", "Alice")
    .fieldIsArray("user.roles")
    .isStructurallyEqualTo(expectedJson);  // Path-level diff on failure!

// Array-index navigation
assertThatJson(response).fieldEquals("users[0].name", "Alice");
assertThatJson(response).fieldEquals("matrix[1][2]", 42);

// Bracket notation for keys containing dots
assertThatJson(config).fieldEquals("database[\"connection.string\"]", "jdbc:...");
```

### XML (`realitycheck-xml`)

```java
import static com.yanimetaxas.realitycheck.xml.XmlReality.*;

assertThatXml(response)
    .isWellFormed()
    .hasRootElement("users")
    .xpathEquals("//user[1]/name", "Alice");
```

### YAML (`realitycheck-yaml`)

```java
import static com.yanimetaxas.realitycheck.yaml.YamlReality.*;

assertThatYaml(config)
    .isValidYaml()
    .pathEquals("server.port", 8080)
    .pathIsList("features")
    .pathListHasSize("features", 3);
```

### Snapshot Testing (`realitycheck-snapshot`)

```java
import static com.yanimetaxas.realitycheck.snapshot.SnapshotReality.*;

assertThatSnapshot(apiResponse)
    .serializedWith(obj -> objectMapper.writeValueAsString(obj))
    .matchesSnapshot("MyTest", "testApiResponse");
// First run: creates the snapshot file
// Subsequent runs: compares against saved snapshot with diff on failure
// Update: mvn test -Drealitycheck.update-snapshots=true
```

## Custom Extensions (3 lines)

Create your own checks with zero boilerplate using Java records:

```java
record MoneyCheck(Money actual, FailureHandler failureHandler)
        implements Check<MoneyCheck, Money> {
    @Override public MoneyCheck self() { return this; }

    public MoneyCheck hasCurrency(String code) {
        return failureHandler.check(self(),
            actual.getCurrency().equals(code),
            "expected currency <%s> but was <%s>", code, actual.getCurrency());
    }
}

// Use it:
assertThat(payment, MoneyCheck::new).hasCurrency("USD").isNotNull();
```

Compare this to Google Truth (~50 lines for a custom Subject) or AssertJ (~30 lines for a custom AbstractAssert subclass).

## Error Messages

Reality Check produces rich, structured failure messages:

```
expected length <12> but was <5> for string: <hello>
```

```
file contents differ:
  line1
- old line
+ new line
  line3
```

```
JSON structures differ:
  $.name: expected "Alice" but was "Bob"
  $.age: expected 30 but was 25
```

```
Multiple failures (3):
  1) expected a non-empty string
  2) expected a positive number but was: <-1>
  3) expected true but was: <false>
```

```
expected scheme <http> but was: <https> in URI <https://api.example.com/v2>
```

```
expected query param <offset> but params were: [page, limit] in URI <https://example.com?page=1&limit=50>
```

```
multiline content differs:
   INFO  Starting server
-  DEBUG Old configuration
+  DEBUG New configuration
   INFO  Server ready
```

```
path <database.connection.timeout> not found — key <timeout> is missing; available keys: [host, port]
```

```
expected completion within <PT1S> but took: <PT2.4S>
```

```
expected root cause of type <java.net.ConnectException> but was: <java.io.IOException>
```

## Philosophy: No Reflection. No Surprises.

Reality Check uses explicit, user-defined field assertions instead of reflective object traversal. You tell it *what to compare* and *how* — and the result is deterministic, portable, and easy to debug:

```java
assertThat(actual, PersonCheck::new).hasName("Yani").hasAge(30);
```

Reflective comparison libraries (`usingRecursiveComparison`, `isEqualToComparingFieldByField`) are convenient for quick prototyping, but they introduce a class of subtle failures in production test suites: false positives from JPA proxies, broken behavior under coverage tools that inject synthetic fields, `--add-opens` requirements with the Java module system, and non-deterministic iteration order in Set/Map diffs.

Reality Check sidesteps all of these by design. Custom checks are 3 lines with Java records (see [Custom Extensions](#custom-extensions-3-lines)), and you get clear, field-specific failure messages instead of opaque object graph diffs.

See [DESIGN_DECISIONS.md](DESIGN_DECISIONS.md) for the full rationale and FAQ.

## Roadmap

### Completed (v1.0)

| Feature | Status | Addresses |
|---|---|---|
| Suppressed exception access on `ThrowableCheck` | **Done** | Truth [#717](https://github.com/google/truth/issues/717) |
| Primitive array checks (`int[]`, `double[]`, `long[]`) | **Done** | Truth [#571](https://github.com/google/truth/issues/571) |
| BigDecimal / BigInteger assertions | **Done** | Truth [#540](https://github.com/google/truth/issues/540) |
| Stream assertions | **Done** | Truth [#342](https://github.com/google/truth/issues/342) |
| Regex match group assertions | **Done** | Unique |
| ZonedDateTime / OffsetDateTime assertions | **Done** | Unique |
| UUID assertions | **Done** | Unique |
| Byte array assertions | **Done** | Unique |
| Sealed class permit assertions | **Done** | Unique (Java 17+) |
| `satisfies(Predicate)` / `matches(Predicate)` / `satisfies(Consumer)` on base Check | **Done** | Escape hatch for one-off assertions |
| `RealityAssertions.assertThat()` alias | **Done** | Migration from Truth/AssertJ |
| Migration guides (MIGRATION.md) | **Done** | Truth & AssertJ migration |
| Java 17+ support (down from 21) | **Done** | Wider adoption |
| JaCoCo coverage enforcement (95% floor + Codecov badge) | **Done** | Quality gates |
| CHANGELOG.md | **Done** | Release hygiene |
| RFC 4180-compliant CSV parser | **Done** | Quoted fields, escaped quotes, unclosed-quote detection |
| XXE-safe XML parsing (cached factories) | **Done** | Blocks DOCTYPE, external entities; hardened `DocumentBuilderFactory` |
| JSON array-index and bracket-key navigation | **Done** | `users[0].name`, `db["connection.string"]` |
| `throwsExactly()` vs `throwsInstanceOf()` | **Done** | Exact-type vs subclass exception matching |
| Thread-safe soft assertions | **Done** | `CopyOnWriteArrayList` in `SoftFailureHandler` |
| Null-safe expected parameters | **Done** | `Objects.equals()` throughout; zero NPE risk |
| Locale-safe `containsIgnoringCase()` | **Done** | Uses `Locale.ROOT` for consistent behavior |
| Frequency-map `containsExactlyInAnyOrder` | **Done** | Accurate duplicate-count comparison for collections/arrays/streams |

### Upcoming: `realitycheck-ai` — LLM/AI Response Assertions

> **Status**: Planned for a future release. This module does not ship with v1.0.

A new module for asserting on non-deterministic LLM/AI outputs — a category that neither Truth nor AssertJ address. Designed with pluggable SPIs (embedding providers, tokenizers, judges) so you bring your own AI provider with zero mandatory dependencies.

```java
assertThatResponse(llmOutput)
    .isSemanticallyCloseTo("The capital of France is Paris", 0.85)
    .doesNotContainPII()
    .hasLengthBetween(50, 500);

assertThatResponse(llmOutput)
    .extractJson()                        // chains into JsonCheck
    .hasField("answer")
    .fieldEquals("answer", "Paris");
```

See [docs/ROADMAP_AI.md](docs/ROADMAP_AI.md) for the full API design, architecture, and design principles.

## Modules

| Module | Artifact | Description | Dependencies |
|---|---|---|---|
| Core | `realitycheck-core` | String, number, boolean, file, collection, map, optional, future, RFC 4180 CSV, date/time, exception, URI, multiline, enum, execution, iterable, array assertions | **None** |
| JSON | `realitycheck-json` | JSON assertions with structural diff, dot-path queries, and array-index navigation | Jackson |
| XML | `realitycheck-xml` | XXE-safe XML assertions with XPath | JDK only |
| YAML | `realitycheck-yaml` | YAML assertions with dot-path queries | SnakeYAML |
| Snapshot | `realitycheck-snapshot` | Golden-file snapshot testing | **None** |
| JUnit 5 | `realitycheck-junit5` | `@WithSoftChecks` extension | JUnit Jupiter API |
| BOM | `realitycheck-bom` | Version alignment | - |

## Migration from Other Libraries

See **[MIGRATION.md](MIGRATION.md)** for step-by-step guides:

- **From AssertJ** — API mapping table, soft assertion migration, custom assertion conversion
- **From Google Truth** — naming differences (`isAtLeast`/`isAtMost` work unchanged), Guava removal

### Automated migration

Run the included script to bulk-replace imports in your test sources:

```bash
./scripts/migrate.sh src/test
```

This rewrites `org.assertj.core.api.Assertions.*` and `com.google.common.truth.Truth.*` imports to `RealityAssertions.*`. Review the diff and search for `TODO: manual migration` markers for patterns that need hand-editing.

## Requirements

- **Java 17+**
- **JUnit 5** (for test scope)
- [API Javadoc](https://imetaxas.github.io/realitycheck/)

## Building

```bash
mvn clean verify
```

## License

[Apache License 2.0](LICENSE)

## Contributing

Contributions are welcome! See **[CONTRIBUTING.md](CONTRIBUTING.md)** for development setup, coding standards, and the pull request process.
