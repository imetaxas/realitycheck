# Roadmap: `realitycheck-ai` — LLM/AI Response Assertions

> **Status**: Planned for a future release. This module does not ship with v1.0.

## Motivation

Neither Truth nor AssertJ offer anything for testing non-deterministic LLM outputs. Every team building LLM-powered features writes ad-hoc `assertTrue(output.contains("expected"))` with fragile exact matches. `realitycheck-ai` will be the first assertion module purpose-built for AI-powered Java applications — while existing tools (DeepEval, Promptfoo, Ragas) are Python-only.

## Proposed Dependency

```xml
<dependency>
    <groupId>com.yanimetaxas</groupId>
    <artifactId>realitycheck-ai</artifactId>
    <version><!-- upcoming --></version>
    <scope>test</scope>
</dependency>
```

```java
import static com.yanimetaxas.realitycheck.ai.AiReality.*;
```

## Proposed API

### Content assertions (fuzzy matching)

```java
assertThatResponse(llmOutput)
    .containsAnyOf("Paris", "paris", "PARIS")
    .doesNotContainAnyOf("Berlin", "London", "Madrid")
    .mentionsAll("capital", "France");
```

### Length and token constraints

```java
assertThatResponse(llmOutput)
    .hasLengthBetween(50, 500)
    .hasTokenCountBetween(10, 100)       // pluggable tokenizer
    .hasSentenceCountBetween(2, 5);
```

### Semantic similarity (pluggable embeddings)

```java
assertThatResponse(llmOutput)
    .isSemanticallyCloseTo("The capital of France is Paris", 0.85);

// Threshold-based, with clear failure messages:
// "expected semantic similarity >= 0.85 but was: 0.71"
```

### Structural conformance

```java
assertThatResponse(llmOutput)
    .isValidJson()
    .hasJsonField("answer")
    .hasJsonField("confidence");

assertThatResponse(llmOutput)
    .matchesSchema(jsonSchema);           // JSON Schema validation
```

### Format validation

```java
assertThatResponse(llmOutput)
    .isValidMarkdown()
    .containsCodeBlock()
    .containsCodeBlockWithLanguage("java")
    .hasHeadingCount(3)
    .hasNumberedList();

assertThatResponse(llmOutput)
    .matchesFormat("1. ${any}\n2. ${any}\n3. ${any}");
```

### Safety and hallucination guards

```java
assertThatResponse(llmOutput)
    .doesNotContainPII()                  // regex-based PII detection
    .doesNotContainProfanity()
    .isNotApology();                      // "I'm sorry, I can't..."

assertThatResponse(llmOutput)
    .onlyMentionsEntitiesFrom(allowedEntities)
    .doesNotInventDates()
    .doesNotContainUrl();
```

### Determinism testing (multi-trial)

```java
assertThatPrompt(prompt, llmClient)
    .withTrials(5)
    .allResponsesContain("Paris")
    .allResponsesSemanticallyCloseTo("Paris is the capital", 0.80)
    .responseVarianceBelow(0.15);         // low divergence across runs
```

### LLM-as-Judge

```java
assertThatResponse(llmOutput)
    .judgedBy(judgeClient)
    .meetsRubric("Is factually accurate about French geography")
    .scoresAbove(0.8);
```

### Extractors (chain into core checks)

```java
assertThatResponse(llmOutput)
    .extractJson()                        // -> JsonCheck
    .hasField("answer")
    .fieldEquals("answer", "Paris");

assertThatResponse(llmOutput)
    .extractCodeBlock("java")             // -> StringCheck
    .contains("public class");
```

## Architecture

```
realitycheck-ai (new module)
├── AiReality.java                    // Entry point: assertThatResponse(), assertThatPrompt()
├── ResponseCheck.java                // Core: length, content, format, safety
├── SemanticCheck.java                // Embedding-based similarity
├── DeterminismCheck.java             // Multi-trial consistency
├── JudgeCheck.java                   // LLM-as-judge evaluation
├── SchemaCheck.java                  // JSON Schema conformance
├── FormatCheck.java                  // Markdown/structure validation
├── SafetyCheck.java                  // PII, profanity, hallucination guards
├── spi/
│   ├── EmbeddingProvider.java        // SPI: pluggable embedding model
│   ├── TokenizerProvider.java        // SPI: pluggable tokenizer
│   └── JudgeProvider.java            // SPI: pluggable LLM judge
└── providers/
    ├── OpenAiEmbeddingProvider.java   // Optional: OpenAI embeddings
    └── SimpleTokenizer.java          // Whitespace tokenizer (zero deps)
```

## Design Principles

1. **Zero mandatory AI dependencies** — Core `ResponseCheck` (length, content, format, safety) has no AI deps. Semantic/judge features use SPI so users bring their own provider.
2. **Deterministic first** — Prefer regex, token counting, and string matching over LLM calls. LLM-as-judge is the escape hatch, not the default.
3. **Pluggable everything** — Embedding provider, tokenizer, and judge are all SPIs. Ships a whitespace tokenizer and cosine similarity util; users plug in OpenAI, Vertex AI, or local models.
4. **Composable with core** — `.extractJson()` returns a `JsonCheck`, `.extractCodeBlock()` returns a `StringCheck`. Full chaining.
5. **Threshold-based, not binary** — Semantic similarity uses configurable thresholds with clear error messages.
