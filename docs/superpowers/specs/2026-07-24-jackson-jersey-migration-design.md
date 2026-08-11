# MRKT-503: Jackson 2.21.2 Compatibility & Jersey 2→3 Migration

**Ticket:** [MRKT-503](https://smartling.atlassian.net/browse/MRKT-503)
**Date:** 2026-07-24
**Status:** Approved

## Problem

`marketo-rest-sdk-java` (v3.1.44) is compiled against Jersey 2.35 (`javax.ws.rs`) and Jackson 2.13.0-rc2. The consuming Spring Boot 3.5.14 applications (`marketo-content-provider`, `marketo-segments-content-provider`) must pin both Jackson (to 2.14.x) and Jersey (to 2.35) in their `build.gradle.kts` to avoid runtime failures:

1. **Jackson pin (MRKT-503):** Since Jackson 2.15, `DeserializationFeature.FAIL_ON_TRAILING_TOKENS` defaults to `true`. Jersey 2.x's `JaxRSFeature.READ_FULL_STREAM` forces this on, causing `MismatchedInputException` when deserializing Marketo API responses.
2. **Jersey pin (MRKT-505):** The SDK uses `javax.ws.rs` (Jersey 2.x), but Spring Boot 3's BOM brings Jersey 3.x (`jakarta.ws.rs`). This causes `NoSuchMethodError` on `StreamDataBodyPart.<init>` in the multipart upload path.

## Goal

Migrate the SDK so both version pins can be removed. The SDK should work with the Jackson and Jersey versions managed by Spring Boot 3.5.14's BOM.

## Approach

Full Jakarta migration: upgrade the SDK to Jersey 3.x / `jakarta.ws.rs`, make Jersey and Jackson dependencies `provided` scope, fix the `FAIL_ON_TRAILING_TOKENS` issue, target Java 17.

## Design

### 1. Dependency Changes (pom.xml)

**Jersey 2.x → 3.1.x (provided scope):**
- `org.glassfish.jersey.core:jersey-client:2.35` → `3.1.x`, scope `provided`
- `org.glassfish.jersey.media:jersey-media-json-jackson:2.35` → `3.1.x`, scope `provided`
- `org.glassfish.jersey.media:jersey-media-multipart:2.35` → `3.1.x`, scope `provided`
- `org.glassfish.jersey.inject:jersey-hk2:2.35` → `3.1.x`, scope `provided`

**Jackson → modern 2.x (provided scope):**
- `com.fasterxml.jackson.core:jackson-annotations:2.13.0-rc2` → `2.17.x`, scope `provided`
- `com.fasterxml.jackson.core:jackson-core:2.13.0-rc2` → `2.17.x`, scope `provided`
- `com.fasterxml.jackson.core:jackson-databind:2.13.0-rc2` → `2.17.x`, scope `provided`

**Java version:**
- `<source>1.8</source>` / `<target>1.8</target>` → `<release>17</release>`

**Test dependencies:**
- `com.github.tomakehurst:wiremock:1.53` → `org.wiremock:wiremock:3.x`
- JUnit 4, Mockito 4.11.0, AssertJ 3.19.0: unchanged

**Other:**
- `com.google.guava:guava:18.0`: unchanged (compile scope, internal use)

### 2. Source Code Migration (javax → jakarta)

Mechanical import replacement: `javax.ws.rs` → `jakarta.ws.rs` across all files.

**Main sources (6 files):**
- `JaxRsHttpCommandExecutor.java`
- `BasicTokenProvider.java`
- `ClientConnectionData.java`
- `ObjectMapperProvider.java`
- `JsonClientLoggingFilter.java`
- `MarketoRestClientManagerFactory.java`

**Test sources (4 files):**
- `JaxRsHttpCommandExecutorTest.java`
- `BaseTransportTest.java`
- `CacheableTokenProviderTest.java`
- `JsonClientLoggingFilterTest.java`

No API changes required — Jersey 3.1 exposes the same JAX-RS API under the `jakarta` namespace.

### 3. Jackson FAIL_ON_TRAILING_TOKENS Fix

In `ObjectMapperProvider.java`, explicitly disable the feature:
```java
objectMapper.configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, false);
```

If Jersey 3.x still overrides this via `READ_FULL_STREAM` (to be determined during implementation), register a custom `JacksonJaxbJsonProvider` with `READ_FULL_STREAM` disabled instead of using `JacksonFeature.class`.

**New test:** Deserialize a `MarketoResponse` from JSON containing extra/unknown trailing fields to confirm no `MismatchedInputException` with Jackson 2.17+.

### 4. WireMock Upgrade

- `com.github.tomakehurst:wiremock:1.53` → `org.wiremock:wiremock:3.x`
- WireMock 3.x uses `jakarta.servlet`, eliminating namespace conflicts
- JUnit 4 `@Rule WireMockRule` pattern is still supported
- Minor API adjustments (imports, static method locations) handled during implementation

## Out of Scope

- Changes to `marketo-content-provider` or `marketo-segments-content-provider` (removing the pins is a separate step after SDK release)
- Migrating from JUnit 4 to JUnit 5
- Upgrading Guava
- Any functional changes to the SDK beyond what's needed for compatibility

## Definition of Done

- SDK compiles and tests pass with Jersey 3.1.x and Jackson 2.17+
- All `javax.ws.rs` references replaced with `jakarta.ws.rs`
- Jersey and Jackson dependencies are `provided` scope
- `FAIL_ON_TRAILING_TOKENS` is explicitly handled
- Unit test confirms deserialization works with trailing tokens
- Java 17 target
- WireMock upgraded to 3.x
- New version released
