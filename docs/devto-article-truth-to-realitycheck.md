---
title: Replacing Google Truth Expect with Reality Check soft assertions on JUnit 6
published: false
tags: java, testing, junit, tutorial
cover_image: https://raw.githubusercontent.com/imetaxas/realitycheck/master/docs/banner.png
description: Google Truth's Expect soft assertions are broken in JUnit 5 and unsupported in JUnit 6. This guide shows a clean, zero-boilerplate migration path to Reality Check — a modern Java 17+ assertion library with built-in soft assertions that work today.
---

If you use Google Truth's `Expect` class to collect soft assertions across a test method, you've hit a wall. `Expect` is a JUnit 4 `@Rule`, and JUnit 4 rules don't exist in JUnit 5 or JUnit 6. Google's own issue tracker ([#893](https://github.com/google/truth/issues/893)) has had this open since 2018 — currently tagged P3 with no timeline. JUnit 6 shipped in September 2025, making JUnit 4 a legacy path you probably don't want your tests tied to.

This post shows a concrete migration path using [Reality Check](https://github.com/imetaxas/realitycheck), a Java 17+ assertion library with thread-safe soft assertions that work on JUnit 5 and JUnit 6 today.

## What soft assertions are and why you used Expect

A regular assertion throws immediately on the first failure:

```java
assertThat(user.name()).isEqualTo("Alice");   // throws here
assertThat(user.email()).contains("@");        // never reached
```

You fix the name, run again, and now discover the email was also broken. Soft assertions collect all failures and report them together at the end of the test:

```
Test failed with 2 assertion errors:
  1) expected: "Alice" but was: "Bob"
  2) expected email to contain <@> but was: <notanemail>
```

Truth's answer was `Expect`:

```java
// JUnit 4 — this no longer works in JUnit 5/6
@Rule
public final Expect expect = Expect.create();

@Test
public void userIsValid() {
    expect.that(user.name()).isEqualTo("Alice");
    expect.that(user.email()).contains("@");
}
```

This worked great — until JUnit 4 `@Rule` became unsupported.

## The problem in concrete terms

In JUnit 5 you cannot use `@Rule` at all. The annotation is simply not recognized. If you try, the test silently ignores the `Expect` extension and your "soft" assertions become hard assertions — the first failure throws and the rest never run.

In JUnit 6 (September 2025), running JUnit 4 tests requires pulling in a separate vintage engine module as a deliberate legacy choice. For teams on a clean JUnit 6 baseline, `@Rule` simply isn't there.

As confirmed on the Truth issue tracker, the `Expect` class itself is `@GwtIncompatible` and tied to the `JUnit4` runner — there is no plan to replace it.

## The migration: three patterns

### Pattern 1 — The lambda block (closest to Expect behavior)

This is the direct equivalent of how most teams used `Expect`. Pass a lambda, get a collected failure report at the end.

**Before (Truth, JUnit 4):**

```java
import static com.google.common.truth.Truth.assertThat;
import com.google.common.truth.Expect;
import org.junit.Rule;
import org.junit.Test;

public class UserValidationTest {

    @Rule
    public final Expect expect = Expect.create();

    @Test
    public void userIsValid() {
        expect.that(user.name()).isEqualTo("Alice");
        expect.that(user.email()).contains("@");
        expect.that(user.age()).isGreaterThan(0);
    }
}
```

**After (Reality Check, JUnit 6):**

```java
import static io.github.imetaxas.realitycheck.RealityAssertions.*;
import io.github.imetaxas.realitycheck.Reality;
import org.junit.jupiter.api.Test;

class UserValidationTest {

    @Test
    void userIsValid() {
        Reality.checkAll(softly -> {
            softly.assertThat(user.name()).isEqualTo("Alice");
            softly.assertThat(user.email()).contains("@");
            softly.assertThat(user.age()).isPositive();
        });
    }
}
```

If two of the three assertions fail, you see:

```
Multiple failures (2):
  1) expected: <Alice> but was: <Bob>
  2) expected string to contain <@> but was: <notanemail>
```

The `assertThat` alias matches Truth's method name exactly — no muscle-memory rewiring.

---

### Pattern 2 — The `@WithSoftChecks` annotation (zero boilerplate)

If you have many tests that all need soft assertions, annotate the class once and let JUnit's extension mechanism inject a `SoftChecks` parameter. The framework calls `assertAll()` automatically after each test.

```java
import io.github.imetaxas.realitycheck.SoftChecks;
import io.github.imetaxas.realitycheck.junit5.WithSoftChecks;
import org.junit.jupiter.api.Test;

@WithSoftChecks
class UserValidationTest {

    @Test
    void userIsValid(SoftChecks softly) {
        softly.assertThat(user.name()).isEqualTo("Alice");
        softly.assertThat(user.email()).contains("@");
        softly.assertThat(user.age()).isPositive();
    }

    @Test
    void addressIsComplete(SoftChecks softly) {
        softly.assertThat(user.address().street()).isNotEmpty();
        softly.assertThat(user.address().city()).isNotEmpty();
        softly.assertThat(user.address().zip()).matches("\\d{5}");
    }
}
```

`@WithSoftChecks` is a composed annotation — it's just shorthand for `@ExtendWith(SoftChecksExtension.class)`. The extension stores one `SoftChecks` instance per test in JUnit's `ExtensionContext` store, then calls `assertAll()` in `afterEach`.

---

### Pattern 3 — Direct instantiation (for non-JUnit contexts)

If you run tests without JUnit (TestNG, custom runners, integration harnesses), use `SoftChecks` directly:

```java
SoftChecks softly = new SoftChecks();

softly.assertThat(response.statusCode()).isEqualTo(200);
softly.assertThat(response.body()).contains("ok");
softly.assertThat(response.header("Content-Type")).startsWith("application/json");

softly.assertAll(); // throws if any assertion above failed
```

---

## Dependency setup

Add one dependency. The `realitycheck-core` module has zero runtime dependencies.

> **Check the latest version** at [Maven Central](https://central.sonatype.com/artifact/io.github.imetaxas/realitycheck-core) and replace `VERSION` below with it.

**Maven:**

```xml
<dependency>
    <groupId>io.github.imetaxas</groupId>
    <artifactId>realitycheck-core</artifactId>
    <version>VERSION</version>
    <scope>test</scope>
</dependency>
```

If you use the `@WithSoftChecks` annotation (Pattern 2), add the JUnit 5 module as well:

```xml
<dependency>
    <groupId>io.github.imetaxas</groupId>
    <artifactId>realitycheck-junit5</artifactId>
    <version>VERSION</version>
    <scope>test</scope>
</dependency>
```

**Gradle (Kotlin DSL):**

```kotlin
testImplementation("io.github.imetaxas:realitycheck-core:VERSION")
testImplementation("io.github.imetaxas:realitycheck-junit5:VERSION") // for @WithSoftChecks
```

---

## Side-by-side API comparison

| Truth (JUnit 4 only) | Reality Check (JUnit 5/6) |
|---|---|
| `expect.that(str).isEqualTo("x")` | `softly.assertThat(str).isEqualTo("x")` |
| `expect.that(n).isGreaterThan(0)` | `softly.assertThat(n).isGreaterThan(0)` |
| `expect.that(list).hasSize(3)` | `softly.assertThat(list).hasSize(3)` |
| `expect.that(map).containsKey("k")` | `softly.assertThat(map).containsKey("k")` |
| `expect.that(opt).isPresent()` | `softly.assertThat(opt).isPresent()` |
| No equivalent | `softly.assertThat(uri).hasScheme("https")` |
| No equivalent | `softly.assertThat(instant).isInThePast()` |
| No equivalent | `softly.assertThatThrownBy(() -> ...).isInstanceOf(...)` |

The `assertThat` overloads in `SoftChecks` dispatch to the right check type automatically — pass a `String`, you get `StringCheck`; pass a `URI`, you get `UriCheck`. No import gymnastics.

---

## What you get that Truth never had

Migrating gives you features that weren't available in Truth at all, as soft-assertable first-class checks:

**Exception soft assertions:**

```java
softly.assertThatThrownBy(() -> service.call(null))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining("null");
```

**URI assertions:**

```java
softly.assertThat(URI.create("https://api.example.com/v2/users"))
      .hasScheme("https")
      .hasHost("api.example.com")
      .pathStartsWith("/v2");
```

**Temporal assertions:**

```java
softly.assertThat(event.createdAt()).isInThePast();
softly.assertThat(event.expiresAt()).isAfter(event.createdAt());
```

**Thread safety — all failures collected correctly from parallel streams:**

```java
softly.assertThat(items).hasSize(100);
items.parallelStream().forEach(item -> {
    // each assertion records to the same thread-safe SoftFailureHandler
    softly.assertThat(item.id()).isNotNull();
});
softly.assertAll();
```

`SoftFailureHandler` uses `CopyOnWriteArrayList` internally, so concurrent writes from parallel streams are safe. Truth's `Expect` is not documented as thread-safe.

---

## How failures are reported

Reality Check wraps all collected failures as suppressed exceptions on a combined `AssertionError`. The `SoftChecksExtension` (Pattern 2) additionally wraps them in `MultipleFailuresError` from `opentest4j`, which means IntelliJ, Eclipse, and the JUnit Platform console renderer display each failure in its own diff pane rather than as a single opaque exception:

```
Multiple assertion failures (2)
  1) expected: <Alice> but was: <Bob>
  2) expected string to contain <@> but was: <notanemail>
```

You see both failures at once, without re-running the test.

---

## Migration checklist

1. Remove the Truth dependency if soft assertions are your only usage:
   ```xml
   <!-- remove -->
   <dependency>
       <groupId>com.google.truth</groupId>
       <artifactId>truth</artifactId>
   </dependency>
   ```

2. Add `realitycheck-core` (and optionally `realitycheck-junit5`) as shown in the Dependency Setup section above. Get the latest version from [Maven Central](https://central.sonatype.com/artifact/io.github.imetaxas/realitycheck-core).

3. Replace `@Rule public final Expect expect = Expect.create();` → delete the field.

4. Replace `expect.that(x).isEqualTo(y)` with `softly.assertThat(x).isEqualTo(y)` inside a `Reality.checkAll()` lambda, or inject `SoftChecks softly` via `@WithSoftChecks`.

5. Remove the JUnit 4 runner annotation from your test class if you were using it only for `Expect` support:
   ```java
   // remove if present
   @RunWith(JUnit4.class)
   ```

6. Run your tests. Most Truth assertions have a direct `assertThat` equivalent in Reality Check with the same method names.

---

## The bottom line

Google Truth's `Expect` was a clean API for soft assertions — but it's tied to JUnit 4 with no roadmap for JUnit 5 or 6. If you're moving to a modern JUnit stack, you need a replacement.

Reality Check gives you the same `assertThat` entry point, a thread-safe soft assertion model, and a `@WithSoftChecks` annotation that handles the `assertAll()` call automatically — with no new dependencies in the core module and a migration that is mostly a search-and-replace.

The full source and more examples are at [github.com/imetaxas/realitycheck](https://github.com/imetaxas/realitycheck).
