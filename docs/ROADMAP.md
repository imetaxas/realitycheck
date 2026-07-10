# Roadmap

## v1.0 — Completed

| Feature | Addresses |
|---|---|
| Suppressed exception access on `ThrowableCheck` | Truth [#717](https://github.com/google/truth/issues/717) |
| Primitive array checks (`int[]`, `double[]`, `long[]`) | Truth [#571](https://github.com/google/truth/issues/571) |
| BigDecimal / BigInteger assertions | Truth [#540](https://github.com/google/truth/issues/540) |
| Stream assertions | Truth [#342](https://github.com/google/truth/issues/342) |
| Regex match group assertions | Unique |
| ZonedDateTime / OffsetDateTime assertions | Unique |
| UUID assertions | Unique |
| Byte array assertions (`toHex()`, `toBase64()` extractors) | Unique |
| Sealed class permit assertions | Unique (Java 17+) |
| `satisfies(Predicate)` / `matches(Predicate)` / `satisfies(Consumer)` | Escape hatch for one-off assertions |
| `RealityAssertions.assertThat()` alias | Migration from Truth/AssertJ |
| Migration guides (`MIGRATION.md`) | Truth & AssertJ |
| Java 17+ support | Wider adoption |
| JaCoCo coverage enforcement (95% floor) + Codecov badge | Quality gates |
| `CHANGELOG.md` | Release hygiene |
| RFC 4180-compliant CSV parser | Quoted fields, escaped quotes, unclosed-quote detection |
| XXE-safe XML parsing (cached factories) | Blocks DOCTYPE, external entities |
| JSON array-index and bracket-key navigation | `users[0].name`, `db["connection.string"]` |
| `throwsExactly()` vs `throwsInstanceOf()` | Exact-type vs subclass exception matching |
| Thread-safe soft assertions (`CopyOnWriteArrayList`) | Parallel-safe assertion collection |
| Null-safe expected parameters (`Objects.equals` throughout) | Zero NPE risk |
| Locale-safe `containsIgnoringCase()` | Uses `Locale.ROOT` |
| Frequency-map `containsExactlyInAnyOrder` | Accurate duplicate-count comparison |

---

## Upcoming: `realitycheck-ai` — LLM/AI Response Assertions

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

See [ROADMAP_AI.md](../docs/ROADMAP_AI.md) for the full API design, architecture, and design principles.

---

## Future Ideas

Ideas under consideration for post-v1 releases. No commitments, no timelines.

| Idea | Notes |
|---|---|
| `realitycheck-ai` | LLM/AI response assertions — see Upcoming above |
| gRPC / Protobuf assertions | Field-level assertion on proto messages |
| Database assertions | Row/column assertions via JDBC |
| Async/reactive support | `Mono`, `Flux`, `CompletableFuture` chains |
| IDE plugin | Inline failure previews in IntelliJ |

Have a feature request? [Open an issue](https://github.com/imetaxas/realitycheck/issues).
