# Migration Guides

## Migrating from AssertJ

### 1. Change the import

```java
// Before (AssertJ)
import static org.assertj.core.api.Assertions.*;

// After (Reality Check)
import static io.github.imetaxas.realitycheck.RealityAssertions.*; // assertThat() — same muscle memory
```

### 2. API mapping

| AssertJ | Reality Check | Notes |
|---|---|---|
| `assertThat(str).isEqualTo("x")` | `assertThat(str).isEqualTo("x")` | Identical with `RealityAssertions` |
| `assertThat(str).contains("x")` | `assertThat(str).contains("x")` | Identical |
| `assertThat(str).startsWith("x")` | `assertThat(str).startsWith("x")` | Identical |
| `assertThat(str).matches("regex")` | `assertThat(str).matches("regex")` | Identical |
| `assertThat(num).isPositive()` | `assertThat(num).isPositive()` | Identical |
| `assertThat(num).isBetween(1, 10)` | `assertThat(num).isBetween(1, 10)` | Identical |
| `assertThat(num).isCloseTo(3.14, within(0.01))` | `assertThat(num).isCloseTo(3.15, 0.01)` | No `within()` wrapper needed |
| `assertThat(list).hasSize(3)` | `assertThat(list).hasSize(3)` | Identical |
| `assertThat(list).contains("a")` | `assertThat(list).contains("a")` | Identical |
| `assertThat(list).containsExactly("a","b")` | `assertThat(list).containsExactly("a","b")` | Identical |
| `assertThat(map).containsKey("k")` | `assertThat(map).containsKey("k")` | Identical |
| `assertThat(map).containsEntry("k","v")` | `assertThat(map).containsEntry("k","v")` | Identical |
| `assertThat(opt).isPresent()` | `assertThat(opt).isPresent()` | Identical |
| `assertThat(opt).hasValue("x")` | `assertThat(opt).hasValue("x")` | Identical |
| `assertThat(bool).isTrue()` | `assertThat(bool).isTrue()` | Identical |
| `assertThat(file).exists()` | `assertThat(path).exists()` | Takes `Path` not `File` (also accepts `File`) |
| `assertThat(instant).isBefore(x)` | `assertThat(instant).isBefore(x)` | Identical |
| `assertThatThrownBy(() -> ...)` | `assertThatThrownBy(() -> ...)` | Identical |
| `.isInstanceOf(X.class)` | `.isInstanceOf(X.class)` | Identical |
| `.hasMessage("msg")` | `.hasMessage("msg")` | Identical |
| `.hasMessageContaining("x")` | `.hasMessageContaining("x")` | Identical |
| `assertThat(x).satisfies(v -> ...)` | `assertThat(x).satisfies(v -> ...)` | Identical — Consumer overload |
| `assertThat(x).matches(pred)` | `assertThat(x).matches(pred)` | Identical |

### 3. AssertJ features with different Reality Check equivalents

| AssertJ | Reality Check | Why |
|---|---|---|
| `usingRecursiveComparison()` | `assertThat(obj, MyCheck::new)` | No reflection — explicit field assertions |
| `SoftAssertions.assertSoftly(s -> ...)` | `Reality.checkAll(s -> ...)` | Same pattern, different name |
| `@ExtendWith(SoftAssertionsExtension.class)` | `@WithSoftChecks` | Annotation name differs |
| `extracting(Foo::getBar)` | `.satisfies(f -> assertThat(f.getBar())...)` | Use `satisfies` with inline checks |
| `AbstractAssert` subclass (~30 lines) | `record MyCheck(...) implements Check` (3 lines) | Records as checks |
| `Condition` | `satisfies(Predicate, String)` | Predicate + description |

### 4. Soft assertions

```java
// Before (AssertJ)
SoftAssertions.assertSoftly(softly -> {
    softly.assertThat(name).isNotEmpty();
    softly.assertThat(age).isPositive();
});

// After (Reality Check)
RealityAssertions.assertAll(softly -> {
    softly.assertThat(name).isNotEmpty();
    softly.assertThat(age).isPositive();
});
```

### 5. Custom assertions

```java
// Before (AssertJ — ~30 lines)
public class MoneyAssert extends AbstractAssert<MoneyAssert, Money> {
    public MoneyAssert(Money actual) {
        super(actual, MoneyAssert.class);
    }
    public static MoneyAssert assertThat(Money actual) {
        return new MoneyAssert(actual);
    }
    public MoneyAssert hasCurrency(String code) {
        isNotNull();
        if (!actual.getCurrency().equals(code)) {
            failWithMessage("Expected currency <%s> but was <%s>", code, actual.getCurrency());
        }
        return this;
    }
}

// After (Reality Check — 3 lines for the check, use via factory)
record MoneyCheck(Money actual, FailureHandler failureHandler) implements Check<MoneyCheck, Money> {
    @Override public MoneyCheck self() { return this; }
    public MoneyCheck hasCurrency(String code) {
        return failureHandler.check(self(), actual.getCurrency().equals(code),
            "expected currency <%s> but was <%s>", code, actual.getCurrency());
    }
}

// Usage:
assertThat(payment, MoneyCheck::new).hasCurrency("USD");
```

---

## Migrating from Google Truth

### 1. Change the import

```java
// Before (Truth)
import static com.google.common.truth.Truth.assertThat;

// After (Reality Check)
import static io.github.imetaxas.realitycheck.RealityAssertions.*;
```

### 2. API mapping

| Truth | Reality Check | Notes |
|---|---|---|
| `assertThat(str).isEqualTo("x")` | `assertThat(str).isEqualTo("x")` | Identical |
| `assertThat(str).contains("x")` | `assertThat(str).contains("x")` | Identical |
| `assertThat(str).startsWith("x")` | `assertThat(str).startsWith("x")` | Identical |
| `assertThat(str).matches("regex")` | `assertThat(str).matches("regex")` | Identical |
| `assertThat(num).isEqualTo(42)` | `assertThat(num).isEqualTo(42)` | Identical |
| `assertThat(num).isGreaterThan(0)` | `assertThat(num).isGreaterThan(0)` | Identical |
| `assertThat(num).isAtLeast(1)` | `assertThat(num).isAtLeast(1)` | Identical (alias provided) |
| `assertThat(num).isAtMost(100)` | `assertThat(num).isAtMost(100)` | Identical (alias provided) |
| `assertThat(list).hasSize(3)` | `assertThat(list).hasSize(3)` | Identical |
| `assertThat(list).contains("a")` | `assertThat(list).contains("a")` | Identical |
| `assertThat(list).containsExactly("a","b")` | `assertThat(list).containsExactly("a","b")` | Identical |
| `assertThat(list).containsExactly(...).inOrder()` | `assertThat(list).containsExactly(...)` | Always ordered in RC |
| `assertThat(map).containsKey("k")` | `assertThat(map).containsKey("k")` | Identical |
| `assertThat(map).containsEntry("k","v")` | `assertThat(map).containsEntry("k","v")` | Identical |
| `assertThat(opt).isPresent()` | `assertThat(opt).isPresent()` | Identical |
| `assertThat(bool).isTrue()` | `assertThat(bool).isTrue()` | Identical |
| `assertThat(x).isNull()` | `assertThat(x).isNull()` | Identical |
| `assertThat(x).isInstanceOf(Foo.class)` | `assertThat(x).isInstanceOf(Foo.class)` | Identical |

### 3. Truth features that work differently

| Truth | Reality Check | Why |
|---|---|---|
| No method chaining (returns `void`) | Full fluent chaining | RC returns `self()` |
| `assertThrows(X.class, () -> ...)` (JUnit) | `assertThatThrownBy(() -> ...).isInstanceOf(X.class)` | Fluent, with chaining |
| Custom `Subject` (~50 lines) | `record MyCheck(...) implements Check` (3 lines) | 16x less boilerplate |
| Guava dependency (3 MB) | Zero dependencies (96 KB) | No Guava needed |
| JUnit 4 soft assertions only | `Reality.checkAll(...)` / `@WithSoftChecks` | Full JUnit 5 |
| No `BigDecimal` subject | `assertThat(bigDecimal).isCloseTo(...)` | Built in |
| No `Stream` subject | `assertThatStream(stream).contains(...)` | Built in |
| No suppressed exceptions | `.hasSuppressed().suppressedException(0)` | Built in |

### 4. Truth's `isAtLeast` / `isAtMost` naming

```java
// Before (Truth) — works unchanged in Reality Check
assertThat(score).isAtLeast(60);
assertThat(score).isAtMost(100);

// Verbose form also available:
assertThat(score).isGreaterThanOrEqualTo(60);
assertThat(score).isLessThanOrEqualTo(100);

// Or combine:
assertThat(score).isBetween(60, 100);
```

### 5. Truth's `containsExactly(...).inOrder()` pattern

```java
// Before (Truth)
assertThat(list).containsExactly("a", "b", "c").inOrder();

// After (Reality Check — containsExactly is always ordered)
assertThat(list).containsExactly("a", "b", "c");

// For unordered comparison:
assertThat(list).containsExactlyInAnyOrder("c", "a", "b");
```

### 6. Removing Guava from test classpath

After migrating, if Truth was the only reason Guava was on the test classpath, you can remove it:

```xml
<!-- Remove these -->
<dependency>
    <groupId>com.google.truth</groupId>
    <artifactId>truth</artifactId>
</dependency>
<!-- Guava is no longer needed for assertions -->
```
