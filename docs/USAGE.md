# Usage Guide

> **Quick navigation** · [Strings](#strings) · [Numbers](#numbers) · [Collections](#collections) · [Maps](#maps) · [Optionals](#optionals) · [Futures](#futures) · [Date/Time](#datetime) · [Files](#files) · [URIs](#uris) · [Exceptions](#exceptions) · [Execution Timing](#execution-timing) · [Multiline Strings](#multiline-strings) · [Regex](#regex-match-groups) · [Enum](#enums) · [Arrays](#arrays) · [Streams](#streams) · [Iterables](#iterables) · [Soft Assertions](#soft-assertions) · [Data Formats](#data-format-modules) · [Custom Extensions](#custom-extensions-3-lines)

## BOM Setup

Use the BOM to manage versions across multiple modules in one place:

**Maven:**

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.imetaxas</groupId>
            <artifactId>realitycheck-bom</artifactId>
            <version>1.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
<!-- then declare modules without versions: -->
<dependency>
    <groupId>io.github.imetaxas</groupId>
    <artifactId>realitycheck-core</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.github.imetaxas</groupId>
    <artifactId>realitycheck-json</artifactId>
    <scope>test</scope>
</dependency>
```

**Gradle (Kotlin DSL):**

```kotlin
testImplementation(platform("io.github.imetaxas:realitycheck-bom:1.0.0"))
testImplementation("io.github.imetaxas:realitycheck-core")
testImplementation("io.github.imetaxas:realitycheck-json")
```

---

## Import

```java
import static io.github.imetaxas.realitycheck.RealityAssertions.*;  // assertThat() — recommended
```

> Prefer Reality Check's own naming? `import static io.github.imetaxas.realitycheck.Reality.*` gives you `checkThat()`.

---

## Strings

```java
assertThat("hello").isNotEmpty().startsWith("he").hasLength(5);
assertThat("hello world").contains("world").matches("hello\\s\\w+");
assertThat("  ").isBlank();
assertThat("Hello").isEqualToIgnoringCase("hello");
assertThat("abc").hasLengthBetween(1, 5);
assertThat("hello").containsIgnoringCase("HELL");
assertThat("email@test.com").matchesPattern(Pattern.compile(".+@.+\\..+"));
```

### Regex match and capture

```java
assertThat("Order-42")
    .matchesAndCaptures("Order-(\\d+)")
    .group(1).isEqualTo("42");
```

---

## Numbers

```java
assertThat(42).isPositive().isGreaterThan(0).isBetween(1, 100);
assertThat(score).isAtLeast(60).isAtMost(100);
assertThat(3.14).isCloseTo(3.15, 0.02);
assertThat(-5L).isNegative();
assertThat(BigDecimal.valueOf(100.50)).isGreaterThan(BigDecimal.valueOf(100));
assertThat(BigInteger.TWO.pow(64)).isPositive();
```

---

## Collections

```java
assertThat(List.of(1, 2, 3)).hasSize(3).contains(2).doesNotContain(5);
assertThat(List.of("a", "b")).containsExactly("a", "b");
assertThat(List.of(2, 4, 6)).allMatch(n -> n % 2 == 0, "is even");
assertThat(List.of(1, 2, 3)).anyMatch(n -> n > 2, "greater than 2");
assertThat(List.of("a", "b", "c")).containsExactlyInAnyOrder("c", "a", "b");
```

---

## Maps

```java
assertThat(Map.of("name", "Alice")).containsKey("name").containsEntry("name", "Alice");
assertThat(config).hasSameEntriesAs(expectedConfig);
```

### Dot-path navigation (nested maps)

```java
Map<String, Object> config = Map.of(
    "database", Map.of("connection", Map.of("host", "localhost"))
);

assertThat(config).atPath("database.connection.host").isEqualTo("localhost");
assertThat(config).stringAtPath("database.connection.host").startsWith("local");
assertThat(config).mapAtPath("database.connection").containsKey("host").hasSize(1);
```

---

## Optionals

```java
assertThat(Optional.of("hello")).isPresent().hasValue("hello");
assertThat(Optional.empty()).isEmpty();
```

---

## Futures

```java
assertThat(myFuture).isCompletedNormally();
assertThat(myFuture).completesWithin(Duration.ofSeconds(5)).hasValue("done");
assertThat(myFuture).isCompletedExceptionally();
```

---

## Date/Time

### Instant

```java
assertThat(Instant.now().minusSeconds(60)).isInThePast().isBefore(Instant.now());
assertThat(event.timestamp()).isCloseTo(Instant.now(), Duration.ofSeconds(5));
```

### LocalDate

```java
assertThat(LocalDate.of(2026, 3, 31)).hasYear(2026).hasMonth(Month.MARCH).isWeekday();
assertThat(birthday).isInThePast().isLeapYear();
```

### LocalDateTime

```java
assertThat(meeting).hasHour(14).hasMinute(30).date().hasMonth(Month.MARCH);
```

### ZonedDateTime / OffsetDateTime

```java
assertThat(zonedEvent).hasZone(ZoneId.of("Europe/Stockholm")).isInThePast();
```

### Duration

```java
assertThat(Duration.ofSeconds(30)).isPositive().isGreaterThan(Duration.ofSeconds(10));
```

---

## Files

```java
assertThat(path).exists().isRegularFile().hasExtension("json");
assertThat(path).hasContent("expected content");
assertThat(dir).isDirectory().isNonEmptyDirectory();
assertThat(file).hasSizeGreaterThan(0).hasContentMatchingRegex(".*error.*");
```

### File content diff on failure

When file content doesn't match, the failure message shows a line-by-line diff:

```
file contents differ:
  line1
- old line
+ new line
  line3
```

---

## URIs

```java
assertThat(URI.create("https://api.example.com/v2?page=1"))
    .hasScheme("https")
    .hasHost("api.example.com")
    .hasPath("/v2")
    .hasQueryParam("page")
    .queryParam("page").isEqualTo("1");
```

---

## Exceptions

```java
assertThatThrownBy(() -> Integer.parseInt("oops"))
    .isInstanceOf(NumberFormatException.class)
    .hasMessageContaining("oops");

// Exact type (no subclasses)
assertThatThrownBy(() -> risky()).throwsExactly(IllegalStateException.class);

// Cause chain traversal
assertThatThrownBy(() -> dao.save(entity))
    .hasCause()
    .hasCauseInstanceOf(SQLException.class)
    .rootCause()
    .hasMessageContaining("deadlock");

// Suppressed exceptions
assertThatThrownBy(() -> cleanup())
    .suppressedException(0)
    .isInstanceOf(IOException.class);
```

---

## Execution Timing

```java
assertThatExecution(() -> myService.process())
    .completesWithin(Duration.ofMillis(200))
    .isLongerThan(Duration.ofMillis(10));
```

---

## Multiline Strings

```java
assertThat(output)
    .hasLineCount(10)
    .line(0).startsWith("INFO")
    .line(3).isEqualTo("Server ready");

// On failure, shows a line-by-line diff:
// multiline content differs:
//    INFO  Starting server
// -  DEBUG Old configuration
// +  DEBUG New configuration
//    INFO  Server ready
```

---

## Regex Match Groups

```java
assertThat("2026-07-10")
    .matchesAndCaptures("(\\d{4})-(\\d{2})-(\\d{2})")
    .group(1).isEqualTo("2026")
    .group(2).isEqualTo("07");
```

---

## Enums

```java
assertThat(Status.ACTIVE).isEqualTo(Status.ACTIVE);
assertThat(status).hasName("ACTIVE").hasOrdinal(1);
assertThat(status).isOneOf(Status.ACTIVE, Status.PENDING);
assertThat(status).isNoneOf(Status.DELETED, Status.ARCHIVED);
```

---

## Arrays

```java
assertThat(new int[]{1, 2, 3}).hasSize(3).contains(2);
assertThat(new double[]{1.1, 2.2}).hasSize(2).contains(1.1);
assertThat(new long[]{10L, 20L}).isSorted();

// Byte arrays
assertThat(bytes).hasSize(16).toHex().startsWith("FF");
assertThat(bytes).toBase64().isEqualTo(expectedBase64);
```

---

## Streams

```java
assertThat(stream).hasSize(3).contains("b").doesNotContain("z");
assertThat(stream).containsExactly("a", "b", "c");
assertThat(stream).allMatch(s -> s.length() > 0, "non-empty");
assertThat(stream).first().isEqualTo("a");
assertThat(stream).toList().hasSize(5);
```

---

## Iterables

Assertions on any `Iterable<T>` — works with lazy generators, database cursors, custom iterables:

```java
assertThat(myIterable).hasSize(100).contains("value").doesNotContain("absent");
assertThat(myIterable).allMatch(x -> x > 0, "positive");
assertThat(myIterable).noneMatch(x -> x < 0, "negative");
assertThat(myIterable).containsAll("a", "b", "c");
```

---

## Soft Assertions

Soft assertions collect all failures instead of stopping at the first one. Thread-safe — can be recorded from parallel streams or concurrent threads without data races.

```java
assertAll(softly -> {
    softly.assertThat(name).isNotEmpty();
    softly.assertThat(age).isPositive();
    softly.assertThat(email).contains("@");
});
// Throws one error listing ALL failures
```

### With JUnit 5 (`@WithSoftChecks`)

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

---

## Data Format Modules

### JSON (`realitycheck-json`)

```java
import static io.github.imetaxas.realitycheck.json.JsonReality.*;

assertThatJson(response)
    .isValidJson()
    .hasField("user.name")
    .fieldEquals("user.name", "Alice")
    .fieldIsArray("user.roles")
    .isStructurallyEqualTo(expectedJson);  // path-level diff on failure

// Array-index navigation
assertThatJson(response).fieldEquals("users[0].name", "Alice");
assertThatJson(response).fieldEquals("matrix[1][2]", 42);

// Bracket notation for keys containing dots
assertThatJson(config).fieldEquals("database[\"connection.string\"]", "jdbc:...");

// Structural diff on failure:
// JSON structures differ:
//   $.name: expected "Alice" but was "Bob"
//   $.age: expected 30 but was 25
```

### CSV (`realitycheck-core`)

RFC 4180-compliant parser — handles quoted fields, embedded commas, embedded newlines:

```java
assertThatCsv(csvContent)
    .hasRowCount(5)
    .row(0).hasColumn("name", "Alice")
    .row(1).hasColumn("age", "30");
```

### XML (`realitycheck-xml`)

XXE-safe — blocks DOCTYPE and external entity declarations:

```java
import static io.github.imetaxas.realitycheck.xml.XmlReality.*;

assertThatXml(response)
    .isWellFormed()
    .hasRootElement("users")
    .hasXPath("//user[1]/name")
    .xpathEquals("//user[1]/name", "Alice");
```

### YAML (`realitycheck-yaml`)

```java
import static io.github.imetaxas.realitycheck.yaml.YamlReality.*;

assertThatYaml(config)
    .isValidYaml()
    .pathEquals("server.port", 8080)
    .pathIsList("features")
    .pathListHasSize("features", 3);
```

### Snapshot Testing (`realitycheck-snapshot`)

```java
import static io.github.imetaxas.realitycheck.snapshot.SnapshotReality.*;

assertThatSnapshot(apiResponse)
    .serializedWith(obj -> objectMapper.writeValueAsString(obj))
    .matchesSnapshot("MyTest", "testApiResponse");
// First run: creates the snapshot file
// Subsequent runs: compares against saved snapshot with diff on failure
// Update snapshots: mvn test -Drealitycheck.update-snapshots=true
```

---

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

Compare this to Google Truth (~50 lines for a custom Subject) or AssertJ (~30 lines for a custom `AbstractAssert` subclass).

See [DESIGN_DECISIONS.md](../DESIGN_DECISIONS.md) for the full rationale on why Reality Check avoids reflection.

---

## What Failure Messages Look Like

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
expected query param <offset> but params were: [page, limit]
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
