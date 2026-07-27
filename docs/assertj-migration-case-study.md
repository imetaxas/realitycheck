# Assertion Migration Case Study: AssertJ to Reality Check

> Real-world migration of **sqlsage4j** (94 unit tests across 29 test files) from AssertJ to Reality Check.

---

## Migration Summary

| Metric | Value |
|---|---|
| Total test files migrated | 29 |
| Total test methods | 94 |
| Drop-in replacements (no change needed) | ~70% |
| Assertions requiring adaptation | ~30% |
| Compilation errors after initial swap | 19 (all resolved) |
| Lines of test code reduced | Net neutral (slightly cleaner in JSON tests) |

---

## Assertion Migration: Before (AssertJ) vs Now (Reality Check)

### 1. Enum Equality

`assertThat(E)` now routes enum values directly to `EnumCheck<E>` — the compiler picks the more-specific `<E extends Enum<E>>` overload automatically. `assertThatEnum()` remains available as a more explicit form.

```java
// Before (AssertJ)
assertThat(sys.role()).isEqualTo(ChatRole.SYSTEM);
assertThat(sqlSage4j.rerankingAlgorithm()).isEqualTo(RerankingAlgorithmEnum.GPT_RANKING);

// After (Reality Check) — assertThat() routes to EnumCheck automatically
assertThat(sys.role()).isEqualTo(ChatRole.SYSTEM);
assertThat(sqlSage4j.rerankingAlgorithm()).isEqualTo(RerankingAlgorithmEnum.GPT_RANKING);

// Explicit form also works (same result, more readable for enum-specific assertions)
assertThatEnum(sys.role()).hasName("SYSTEM").isOneOf(ChatRole.USER, ChatRole.SYSTEM);
```

**Files affected:** `ChatMessageTest`, `SqlSage4jTest`, `SqlPromptBuilderTest`

---

### 2. Object Not-Null

The generic `assertThat(T)` fallback routes any reference type that has no dedicated overload to `ObjectCheck<T>`. This is a drop-in replacement for AssertJ's generic `assertThat(Object)`.

```java
// Before (AssertJ)
assertThat(sqlSage4j).isNotNull();
assertThat(cached).isNotNull();
assertThat(cached.df()).isNotNull();

// After (Reality Check) — identical, routes to ObjectCheck via generic fallback
assertThat(sqlSage4j).isNotNull();
assertThat(cached).isNotNull();
assertThat(cached.df()).isNotNull();

// assertThatObject() is also available as a more explicit form
assertThatObject(sqlSage4j).isNotNull();
```

**Files affected:** `SqlSage4jEndToEndTest`, `MultiTurnConversationIT`

---

### 3. Custom Object Equality

The generic `assertThat(T)` fallback makes this a drop-in replacement — no change needed.

```java
// Before (AssertJ)
assertThat(a).isEqualTo(b);

// After (Reality Check) — identical
assertThat(a).isEqualTo(b);
```

**Files affected:** `ChatMessageTest`

---

### 4. Custom Object Inequality

```java
// Before (AssertJ)
assertThat(a).isNotEqualTo(b);

// After (Reality Check) — identical
assertThat(a).isNotEqualTo(b);
```

**Files affected:** `ChatMessageTest`

---

### 5. instanceof Check

The generic `assertThat(T)` fallback also provides `.isInstanceOf()` via the `Check` interface — same as AssertJ.

```java
// Before (AssertJ)
assertThat(client).isInstanceOf(OpenAIClient.class);

// After (Reality Check) — identical
assertThat(client).isInstanceOf(OpenAIClient.class);
```

**Files affected:** `LLMClientFactoryTest` (5 assertions)

---

### 6. Array Length

AssertJ has `.hasSize()` for arrays. Reality Check uses the array's `.length` property directly.

```java
// Before (AssertJ)
assertThat(embedding).hasSize(3);

// After (Reality Check)
assertThat(embedding.length).isEqualTo(3);
```

**Files affected:** `OllamaEmbeddingsProviderTest`

---

### 7. Float/Double Closeness

AssertJ requires a `within()` wrapper object for tolerance. Reality Check takes the tolerance as a direct parameter. `float` is now a first-class overload — no cast to `double` needed.

```java
// Before (AssertJ)
assertThat(embedding[0]).isCloseTo(0.1f, within(0.001f));
assertThat(embedding[1]).isCloseTo(0.2f, within(0.001f));

// After (Reality Check) — float overload, no cast, no within() wrapper
assertThat(embedding[0]).isCloseTo(0.1f, 0.001f);
assertThat(embedding[1]).isCloseTo(0.2f, 0.001f);
```

**Files affected:** `OllamaEmbeddingsProviderTest`, `VectorMathTest`

---

### 8. Object Cast for Primitive Equality

AssertJ auto-boxes and compares `Object` values. Reality Check's typed `assertThat()` overloads require explicit casting from `Object`.

```java
// Before (AssertJ)
assertThat(df.rows().get(0).get(0)).isEqualTo(1);
assertThat(df.rows().get(0).get(0)).isEqualTo("Alice");

// After (Reality Check)
assertThat((int) head.rows().get(0).get(0)).isEqualTo(1);
assertThat((String) df.rows().get(0).get(0)).isEqualTo("Alice");
```

**Files affected:** `DataFrameTest`, `SQLiteConnectorTest`, `MySQLConnectorTest`, `JdbcDatabaseConnectorTest`, `DuckDBConnectorTest`

---

### 9. Double from Object

```java
// Before (AssertJ)
assertThat(df.rows().get(0).get(0)).isEqualTo(247500.00);

// After (Reality Check)
assertThat((double) df.rows().get(0).get(0)).isEqualTo(247500.00);
```

**Files affected:** `SaasMetricsDashboardIT`

---

### 10. Long Literal Matching

When the method returns `long`, AssertJ auto-widens `int` literals. Reality Check requires explicit `long` literals.

```java
// Before (AssertJ)
assertThat(result.successCountA()).isEqualTo(3);

// After (Reality Check)
assertThat(result.successCountA()).isEqualTo(3L);
```

**Files affected:** `SqlSage4jEndToEndTest`, `ABTestWithLLMDiffIT`

---

### 11. Collection allSatisfy → allMatch

AssertJ's `allSatisfy` runs a `Consumer` with nested assertions. Reality Check uses `allMatch` with a `Predicate` and a description.

```java
// Before (AssertJ)
assertThat(suggestions).allSatisfy(q -> assertThat(q).isNotBlank());

// After (Reality Check)
assertThat(suggestions).allMatch(q -> !q.isBlank(), "is not blank");
```

**Files affected:** `DataTeamOnboardingIT`

---

### 12. Collection anyMatch with Description

The description parameter is now **optional** in `anyMatch`, `allMatch`, and `noneMatch` — making this a drop-in replacement.

```java
// Before (AssertJ)
assertThat(concepts).anyMatch(c -> c.contains("revenue"));

// After (Reality Check) — identical, description defaults to "predicate"
assertThat(concepts).anyMatch(c -> c.contains("revenue"));

// With an explicit label for a better failure message:
assertThat(concepts).anyMatch(c -> c.contains("revenue"), "contains revenue");
```

**Files affected:** `SaasMetricsDashboardIT`

---

### 13. Descriptive Alias (.as())

Reality Check now fully supports `.as("description")` — the API is **identical** to AssertJ. This is a drop-in replacement.

```java
// Before (AssertJ)
assertThat(response.sql()).as("generated SQL").isNotEmpty();
assertThat(response.df()).as("query result").isNotNull();

// After (Reality Check) — identical
assertThat(response.sql()).as("generated SQL").isNotEmpty();
assertThat(response.df()).as("query result").isNotNull();

// Failure message: [generated SQL] expected a non-empty string
// Failure message: [query result] expected a non-null value but actual was: <null>
```

**Files affected:** `DataTeamOnboardingIT`, `EcommerceAnalyticsIT`, `MultiTurnConversationIT`

---

### 14. JSON Field Assertions (with realitycheck-json)

Manual Gson parsing replaced with fluent `assertThatJson()` from the `realitycheck-json` module.

```java
// Before (AssertJ + Gson)
JsonObject req = GSON.fromJson(requestBody, JsonObject.class);
assertThat(req.get("model").getAsString()).isEqualTo("llama3");
assertThat(req.get("stream").getAsBoolean()).isFalse();
assertThat(req.getAsJsonObject("options").get("temperature").getAsDouble()).isEqualTo(0.1);

// After (Reality Check JSON)
assertThatJson(requestBody)
    .fieldEquals("model", "llama3")
    .fieldEquals("stream", false)
    .hasField("options.temperature");
```

**Files affected:** `OllamaClientTest`

---

### 15. JSON Array Check

```java
// Before (AssertJ + Gson)
JsonObject req = GSON.fromJson(requestBody, JsonObject.class);
assertThat(req.getAsJsonArray("messages").size()).isEqualTo(3);

// After (Reality Check JSON)
assertThatJson(requestBody).fieldIsArray("messages");
```

**Files affected:** `OllamaClientTest`

---

### 16. Collection Size via Method Call

When size comes from a method returning `int` on a non-collection type (e.g., `JsonArray.size()`), use the numeric `assertThat`.

```java
// Before (AssertJ)
assertThat(messages.size()).isGreaterThanOrEqualTo(2);

// After (Reality Check) — identical
assertThat(messages.size()).isGreaterThanOrEqualTo(2);
```

**Files affected:** `SqlPromptBuilderTest`

---

## Key Differences in Philosophy

| Aspect | AssertJ | Reality Check | Rationale |
|---|---|---|---|
| **Type dispatch** | Single generic `assertThat(Object)` resolves everything via overloading | Typed overloads + generic `assertThat(T)` fallback; enum-specific `assertThat(E)` auto-routes to `EnumCheck` | Explicit dispatch, cleaner type inference at compile time |
| **Generic object checks** | `assertThat(obj).isNotNull()` / `.isInstanceOf()` via the same entry point | `assertThat(obj)` routes to `ObjectCheck<T>` via the generic fallback; `assertThatObject()` also available | Drop-in for most cases; explicit form available when preferred |
| **Assertion description** | `.as("description")` available on any assertion | `.as("description")` / `.withDescription()` — **identical API**, available on every check | Same muscle memory, zero migration cost |
| **Tolerance syntax** | `.isCloseTo(3.14, within(0.01))` | `.isCloseTo(3.14, 0.01)` | Simpler API — no `Offset`/`Percentage` wrapper objects |
| **Float assertions** | `assertThat(floatVal)` (via `AbstractFloatAssert`) | `assertThat(floatVal)` → `NumberCheck<Float>` — first-class `float` primitive overload | **Identical** |
| **Collection predicates** | `.allSatisfy(Consumer)` — runs nested assertions inside lambda | `.allMatch(Predicate)` — label optional, returns boolean | No assertion-in-assertion nesting; predicate is pure |
| **Object field comparison** | `usingRecursiveComparison().isEqualTo()` | `hasSameFieldsAs()` — explicit, shallow, opt-in reflection | Same intent, bounded to first-level fields |
| **Custom extensions** | `AbstractAssert` subclass (~30 lines) | `record MyCheck(...) implements Check` (3 lines) | Java records eliminate boilerplate |
| **Soft assertions** | `SoftAssertions.assertSoftly(s -> ...)` | `assertAll(softly -> ...)` or `@WithSoftChecks` | Thread-safe by design |
| **JSON/XML/YAML** | Requires separate libraries (JsonUnit, XMLUnit) | First-class modules: `realitycheck-json`, `realitycheck-xml`, `realitycheck-yaml` | Unified API and failure messages |
| **Dependencies** | Zero (core only) | Zero (core only), Jackson for JSON module | Same lightweight philosophy |
| **Java version** | Java 8+ | Java 17+ | Leverages records, sealed classes, modern APIs |

---

## Migration Effort Summary

**Trivial (drop-in, no changes):**
- String assertions (`isEqualTo`, `contains`, `startsWith`, `isNotEmpty`, `isBlank`)
- Number comparisons (`isGreaterThan`, `isLessThan`, `isBetween`, `isPositive`)
- Collection assertions (`hasSize`, `contains`, `doesNotContain`, `containsExactly`)
- Map assertions (`containsKey`, `containsEntry`)
- Exception assertions (`assertThatThrownBy`, `isInstanceOf`, `hasMessageContaining`)
- **Assertion labels** → `.as("description")` is identical
- **Enum assertions** → `assertThat(enumValue)` routes to `EnumCheck` automatically
- **Object null/type/equality checks** → `assertThat(anyObj)` routes to `ObjectCheck` via generic fallback
- **`anyMatch`/`allMatch`/`noneMatch` without label** → description is now optional

**Minor adaptation required:**
- Float tolerance → remove `within()` wrapper (no cast needed, `float` is now first-class)
- `Object` return types → add explicit casts to typed `assertThat()` overloads when numeric/string assertions are needed
- `allSatisfy(Consumer)` → `allMatch(Predicate)` — predicate instead of nested assertions
- `int` vs `long` literals → match method return type exactly

**Net improvement (cleaner with Reality Check):**
- JSON assertions → `assertThatJson()` replaces manual Gson parsing
- Soft assertions → `@WithSoftChecks` parameter injection with `.as("label")` for clear failure attribution
- Custom checks → 3-line records vs 30-line classes
- Simple POJO equality → `hasSameFieldsAs()` for first-level field comparison
