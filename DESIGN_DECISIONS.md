# Design Decisions

This document explains the key design decisions behind Reality Check and the trade-offs involved.

## 1. No Reflective Object Comparison

Reality Check does not perform reflective object comparison. Libraries that recurse into your objects via `setAccessible(true)` introduce a class of subtle test failures:

- **Coverage tools** inject synthetic fields (`$jacocoData`) that pollute comparisons
- **Java module system** blocks `setAccessible` without `--add-opens` flags
- **JPA/Hibernate proxies** trigger side effects or produce false positives
- **Circular references** cause `StackOverflowError`
- **HashMap/HashSet iteration order** varies between runs, producing non-deterministic diff output
- **Classes implementing Iterable** can break recursive comparison entirely

Reality Check provides two stable alternatives:

**Option A: Custom check (zero boilerplate)**

```java
public record PersonCheck(Person actual, FailureHandler failureHandler)
        implements Check<PersonCheck, Person> {
    @Override public PersonCheck self() { return this; }

    public PersonCheck hasName(String name) {
        return failureHandler.check(self(),
            actual.getName().equals(name),
            "expected name <%s> but was <%s>", name, actual.getName());
    }
}

// Usage:
assertThat(person, PersonCheck::new).hasName("Yani");
```

**Option B: Field extraction with existing checks**

```java
assertThat(person.getName()).isEqualTo("Yani");
assertThat(person.getAge()).isBetween(18, 65);
```

Both approaches produce clear, deterministic failure messages, work across all JVM configurations, and never touch your object's internals via reflection.

## 2. Simple Soft Assertions (No Proxies)

Reality Check's `SoftFailureHandler` is a simple `CopyOnWriteArrayList<AssertionError>` — no proxies, no bytecode generation, no `@InjectSoftAssertions`. This design:

- Works identically in sequential, parallel, and nested contexts
- Has no thread-safety issues (the `CopyOnWriteArrayList` is inherently safe for concurrent writes)
- Avoids the `ErrorCollector.intercept` nesting problem that proxy-based approaches face
- Works in Groovy and Kotlin without ClassCastException or SAM ambiguity issues

## 3. Kotlin-Friendly by Design

Reality Check avoids `Consumer`/`ThrowingConsumer` overloads on the same method. `assertThatThrownBy` takes a dedicated `ThrowingCallable` type, so Kotlin's SAM resolution is unambiguous. No special Kotlin module or extension functions are needed.

## 4. Zero Runtime Dependencies (Core)

The `realitycheck-core` module has zero runtime dependencies. This avoids:

- Transitive dependency conflicts (e.g., Guava version clashes)
- Android compatibility issues from annotation processing
- Unnecessary classpath bloat for a test-only library

Format-specific modules (`realitycheck-json`, `realitycheck-yaml`) depend on their respective parsers (Jackson, SnakeYAML), but these are isolated — you only pull in what you use.

## 5. Java Records for Custom Extensions

Using Java records as the extension mechanism (instead of abstract class inheritance) achieves:

- **Minimal boilerplate**: 3 lines for the record declaration, `self()`, and `implements Check`
- **Immutability**: Records are final and their fields are final
- **No hidden state**: All state is visible in the record components
- **Natural fit for `CheckFactory`**: The record constructor `(ACTUAL, FailureHandler)` matches the factory signature exactly, enabling `assertThat(value, MyCheck::new)`

## FAQ

### Q: Why doesn't Reality Check have `usingRecursiveComparison`?

See section 1 above. The trade-off is explicit: you write 3 lines of check code per type, and in return you get deterministic, reflection-free assertions that work across all JVM configurations.

### Q: Does Reality Check work with Kotlin?

Yes. See section 3 above. No special Kotlin module or workarounds are needed.

### Q: Why not use proxies for soft assertions like AssertJ?

See section 2 above. Proxy-based soft assertions are more convenient to set up (`@InjectSoftAssertions`) but introduce complexity that surfaces as bugs in nesting, parallel execution, and non-Java JVM languages.

### Q: Why Java 17+ and not Java 8?

Reality Check uses sealed classes, records, and pattern matching — features that make the library's API cleaner and its extension model simpler. Supporting Java 8 would require giving up the record-based `Check` interface that makes custom extensions 3 lines instead of 30.
