# Jackson/Jersey Migration Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Migrate marketo-rest-sdk-java from Jersey 2.x/javax to Jersey 3.x/jakarta with provided-scope dependencies and fix Jackson FAIL_ON_TRAILING_TOKENS compatibility.

**Architecture:** Update pom.xml dependencies (Jersey 3.1.x, Jackson 2.17.x as provided, WireMock 3.x), mechanically replace `javax.ws.rs` → `jakarta.ws.rs` across all source files, add `FAIL_ON_TRAILING_TOKENS=false` to ObjectMapperProvider, and fix any WireMock API breakages.

**Tech Stack:** Java 17, Jersey 3.1.x, Jackson 2.17.x, WireMock 3.x, JUnit 4, Maven

## Global Constraints

- Java 17 (source and target)
- Jersey and Jackson dependencies must be `provided` scope
- WireMock is `test` scope
- JUnit 4, Mockito, AssertJ versions unchanged
- Guava version unchanged
- No functional changes to SDK behavior — only dependency/namespace migration

---

### Task 1: Update pom.xml dependencies and Java version

**Files:**
- Modify: `pom.xml`

**Interfaces:**
- Consumes: nothing
- Produces: Updated dependency coordinates and versions that all subsequent tasks compile against

- [ ] **Step 1: Update properties and compiler configuration**

In `pom.xml`, change the properties block and compiler plugin:

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>

    <jersey.version>3.1.10</jersey.version>
    <jackson.version>2.17.3</jackson.version>

    <integration.tests.location>com/smartling/it/**/*.java</integration.tests.location>
</properties>
```

In the `maven-compiler-plugin` configuration, replace `<source>` and `<target>` with `<release>`:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.2</version>
    <configuration>
        <showDeprecation>true</showDeprecation>
        <showWarnings>true</showWarnings>
        <release>17</release>
    </configuration>
</plugin>
```

- [ ] **Step 2: Update Jersey dependencies to 3.1.x with provided scope**

Replace the Jersey dependencies block in `pom.xml`:

```xml
<dependency>
    <groupId>org.glassfish.jersey.core</groupId>
    <artifactId>jersey-client</artifactId>
    <version>${jersey.version}</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.glassfish.jersey.media</groupId>
    <artifactId>jersey-media-json-jackson</artifactId>
    <version>${jersey.version}</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.glassfish.jersey.media</groupId>
    <artifactId>jersey-media-multipart</artifactId>
    <version>${jersey.version}</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.glassfish.jersey.inject</groupId>
    <artifactId>jersey-hk2</artifactId>
    <version>${jersey.version}</version>
    <scope>provided</scope>
</dependency>
```

- [ ] **Step 3: Update Jackson dependencies to provided scope**

Replace the Jackson dependencies block:

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-annotations</artifactId>
    <version>${jackson.version}</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-core</artifactId>
    <version>${jackson.version}</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>${jackson.version}</version>
    <scope>provided</scope>
</dependency>
```

- [ ] **Step 4: Update WireMock dependency**

Replace the WireMock dependency:

```xml
<dependency>
    <groupId>org.wiremock</groupId>
    <artifactId>wiremock-standalone</artifactId>
    <version>3.13.2</version>
    <scope>test</scope>
</dependency>
```

Note: Use `wiremock-standalone` to avoid transitive dependency conflicts (it shades its dependencies).

- [ ] **Step 5: Verify pom.xml is well-formed**

Run: `cd /Users/christie/IdeaProjects/marketo-rest-sdk-java && mvn help:effective-pom -q > /dev/null 2>&1; echo $?`
Expected: `0` (no XML parsing errors). The build will not compile yet — that's expected until we fix the imports in subsequent tasks.

- [ ] **Step 6: Commit**

```bash
git add pom.xml
git commit -m "MRKT-503 Update dependencies: Jersey 3.1.x, Jackson 2.17.x (provided), WireMock 3.x, Java 17"
```

---

### Task 2: Migrate main sources from javax to jakarta

**Files:**
- Modify: `src/main/java/com/smartling/marketo/sdk/rest/transport/JaxRsHttpCommandExecutor.java`
- Modify: `src/main/java/com/smartling/marketo/sdk/rest/transport/BasicTokenProvider.java`
- Modify: `src/main/java/com/smartling/marketo/sdk/rest/transport/ClientConnectionData.java`
- Modify: `src/main/java/com/smartling/marketo/sdk/rest/transport/ObjectMapperProvider.java`
- Modify: `src/main/java/com/smartling/marketo/sdk/rest/transport/logging/JsonClientLoggingFilter.java`
- Modify: `src/main/java/com/smartling/marketo/sdk/rest/MarketoRestClientManagerFactory.java`

**Interfaces:**
- Consumes: Updated pom.xml from Task 1
- Produces: Compilable main sources using `jakarta.ws.rs` namespace

- [ ] **Step 1: Update JaxRsHttpCommandExecutor.java imports**

Replace lines 18-24:

```java
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
```

- [ ] **Step 2: Update BasicTokenProvider.java imports**

Replace lines 7-9:

```java
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
```

- [ ] **Step 3: Update ClientConnectionData.java import**

Replace line 3:

```java
import jakarta.ws.rs.client.Client;
```

- [ ] **Step 4: Update ObjectMapperProvider.java import**

Replace line 11:

```java
import jakarta.ws.rs.ext.ContextResolver;
```

- [ ] **Step 5: Update JsonClientLoggingFilter.java imports**

Replace lines 5-10:

```java
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
```

- [ ] **Step 6: Update MarketoRestClientManagerFactory.java imports**

Replace lines 15-16:

```java
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
```

- [ ] **Step 7: Verify main sources compile**

Run: `cd /Users/christie/IdeaProjects/marketo-rest-sdk-java && mvn compile -q 2>&1 | tail -5`
Expected: `BUILD SUCCESS`

- [ ] **Step 8: Commit**

```bash
git add src/main/
git commit -m "MRKT-503 Migrate main sources from javax.ws.rs to jakarta.ws.rs"
```

---

### Task 3: Add FAIL_ON_TRAILING_TOKENS fix to ObjectMapperProvider

**Files:**
- Modify: `src/main/java/com/smartling/marketo/sdk/rest/transport/ObjectMapperProvider.java`

**Interfaces:**
- Consumes: Compiled main sources from Task 2
- Produces: ObjectMapperProvider that explicitly disables FAIL_ON_TRAILING_TOKENS

- [ ] **Step 1: Add FAIL_ON_TRAILING_TOKENS=false**

In `ObjectMapperProvider.java`, add this line after the existing `configure` calls (after line 20):

```java
objectMapper.configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, false);
```

The full configure block should now read:

```java
objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
objectMapper.configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, false);
objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
```

- [ ] **Step 2: Verify compile**

Run: `cd /Users/christie/IdeaProjects/marketo-rest-sdk-java && mvn compile -q 2>&1 | tail -5`
Expected: `BUILD SUCCESS`

- [ ] **Step 3: Commit**

```bash
git add src/main/java/com/smartling/marketo/sdk/rest/transport/ObjectMapperProvider.java
git commit -m "MRKT-503 Disable FAIL_ON_TRAILING_TOKENS for Jackson 2.15+ compatibility"
```

---

### Task 4: Migrate test sources and fix WireMock API changes

**Files:**
- Modify: `src/test/java/com/smartling/marketo/sdk/rest/transport/JaxRsHttpCommandExecutorTest.java`
- Modify: `src/test/java/com/smartling/marketo/sdk/rest/transport/BaseTransportTest.java`
- Modify: `src/test/java/com/smartling/marketo/sdk/rest/transport/BasicTokenProviderTest.java`
- Modify: `src/test/java/com/smartling/marketo/sdk/rest/transport/CacheableTokenProviderTest.java`
- Modify: `src/test/java/com/smartling/marketo/sdk/rest/transport/logging/JsonClientLoggingFilterTest.java`

**Interfaces:**
- Consumes: Compiled main sources from Tasks 2-3, updated pom.xml from Task 1
- Produces: All tests compile and pass with Jersey 3.1.x, Jackson 2.17.x, WireMock 3.x

- [ ] **Step 1: Update BaseTransportTest.java**

Replace the imports (lines 3-5, 12-13). In WireMock 3.x, `UrlMatchingStrategy` is replaced by `UrlPattern` and `ValueMatchingStrategy` by `StringValuePattern`:

```java
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
```

Replace the javax import (lines 12-13):

```java
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
```

Update method return types:

```java
static UrlPattern urlStartingWith(String path) {
    return urlMatching(path + ".*");
}

static UrlPattern path(String path) {
    return urlMatching(path + "(\\?.+)?");
}
```

Update `withFormParam` return type:

```java
StringValuePattern withFormParam(String key, String value) {
    return containing(key + "=" + value);
}
```

- [ ] **Step 2: Update JaxRsHttpCommandExecutorTest.java imports**

Replace the javax imports (lines 32-34):

```java
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
```

The WireMock imports (`com.github.tomakehurst.wiremock.*`) stay the same — WireMock 3.x keeps the same package names.

The `addRequestProcessingDelay` static import (line 42) was removed in WireMock 3.x. Update the `shouldSupportSocketTimeoutConfiguration` test (lines 372-378). Replace:

```java
import static com.github.tomakehurst.wiremock.client.WireMock.addRequestProcessingDelay;
```

with nothing (remove the import). Then update the test method:

```java
@Test(timeout = 2 * 1000, expected = ProcessingException.class)
public void shouldSupportSocketTimeoutConfiguration() throws Exception {
    stubFor(get(urlStartingWith("/rest")).willReturn(
            aJsonResponse("{\"success\": true}").withFixedDelay(5000)));

    testedInstance = new JaxRsHttpCommandExecutor(IDENTITY_URL, REST_URL, CLIENT_ID, CLIENT_SECRET, tokenProvider, buildTestClient(1000));
    testedInstance.execute(command);
}
```

This replaces the global `addRequestProcessingDelay` with a per-stub `withFixedDelay` which is the WireMock 3.x way to add response delays.

- [ ] **Step 3: Update BasicTokenProviderTest.java imports**

No WireMock import changes needed (it only uses `WireMockRule` and `WireMock` statics which are unchanged). No javax imports in this file. No changes needed.

- [ ] **Step 4: Update CacheableTokenProviderTest.java import**

Replace line 13:

```java
import jakarta.ws.rs.ProcessingException;
```

- [ ] **Step 5: Update JsonClientLoggingFilterTest.java imports**

Replace lines 10-14:

```java
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
```

- [ ] **Step 6: Compile test sources**

Run: `cd /Users/christie/IdeaProjects/marketo-rest-sdk-java && mvn test-compile -q 2>&1 | tail -10`
Expected: `BUILD SUCCESS`

If there are additional WireMock API compile errors not covered above, fix them. The most common changes in WireMock 3.x are:
- `UrlMatchingStrategy` → `UrlPattern`
- `ValueMatchingStrategy` → `StringValuePattern`
- `addRequestProcessingDelay()` → per-stub `.withFixedDelay(ms)`

- [ ] **Step 7: Run all tests**

Run: `cd /Users/christie/IdeaProjects/marketo-rest-sdk-java && mvn test 2>&1 | tail -20`
Expected: `BUILD SUCCESS` with all tests passing.

If tests fail due to Jersey 3.x `READ_FULL_STREAM` still forcing `FAIL_ON_TRAILING_TOKENS`, apply the fallback fix: in `MarketoRestClientManagerFactory.java`, replace `JacksonFeature.class` registration with a custom `JacksonJaxbJsonProvider` that has `READ_FULL_STREAM` disabled. See "Fallback Fix" section below.

- [ ] **Step 8: Commit**

```bash
git add src/test/
git commit -m "MRKT-503 Migrate test sources to jakarta.ws.rs and WireMock 3.x"
```

---

### Task 5: Add trailing tokens deserialization test

**Files:**
- Modify: `src/test/java/com/smartling/marketo/sdk/rest/transport/JaxRsHttpCommandExecutorTest.java`

**Interfaces:**
- Consumes: Working test suite from Task 4
- Produces: Explicit regression test confirming FAIL_ON_TRAILING_TOKENS doesn't break deserialization

- [ ] **Step 1: Add test for response with extra fields**

Add this test to `JaxRsHttpCommandExecutorTest.java`, after the `shouldIgnoreUnknownFields` test:

```java
@Test
public void shouldHandleResponseWithExtraFieldsWithoutTrailingTokenError() throws Exception {
    given(command.getPath()).willReturn("/some/path");
    givenThat(get(path("/rest/some/path")).willReturn(
            aJsonResponse("{\"success\": true, \"result\": {\"string\": \"test\"}, \"requestId\": \"abc#123\", \"moreInfo\": \"extra\"}")));

    Data response = testedInstance.execute(command);

    assertThat(response).isNotNull();
    assertThat(response.string).isEqualTo("test");
}
```

This JSON response includes `requestId` and `moreInfo` — fields not bound by `MarketoResponse` — which would trigger `MismatchedInputException` if `FAIL_ON_TRAILING_TOKENS` were true.

- [ ] **Step 2: Run the new test**

Run: `cd /Users/christie/IdeaProjects/marketo-rest-sdk-java && mvn test -pl . -Dtest="JaxRsHttpCommandExecutorTest#shouldHandleResponseWithExtraFieldsWithoutTrailingTokenError" 2>&1 | tail -10`
Expected: `BUILD SUCCESS`, 1 test run, 0 failures.

- [ ] **Step 3: Run full test suite**

Run: `cd /Users/christie/IdeaProjects/marketo-rest-sdk-java && mvn test 2>&1 | tail -20`
Expected: `BUILD SUCCESS`, all tests pass.

- [ ] **Step 4: Commit**

```bash
git add src/test/java/com/smartling/marketo/sdk/rest/transport/JaxRsHttpCommandExecutorTest.java
git commit -m "MRKT-503 Add regression test for FAIL_ON_TRAILING_TOKENS compatibility"
```

---

## Fallback Fix: READ_FULL_STREAM Override

If Task 4 Step 7 reveals that Jersey 3.1.x still forces `FAIL_ON_TRAILING_TOKENS` via `READ_FULL_STREAM`, apply this fix in `MarketoRestClientManagerFactory.java`.

In `buildClient()`, replace:

```java
.register(JacksonFeature.class)
```

with:

```java
.register(new JacksonJsonProvider(new ObjectMapperProvider().getContext(null)) {{
    disable(com.fasterxml.jackson.jaxrs.cfg.JaxRSFeature.READ_FULL_STREAM);
}})
```

And add the required import:

```java
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
```

This bypasses Jersey's `JacksonFeature` (which wraps its own provider with `READ_FULL_STREAM=true`) and instead registers a provider we control directly, using the same ObjectMapper from `ObjectMapperProvider`.

If this fallback is needed, also add `jackson-jaxrs-json-provider` as a `provided` dependency in pom.xml:

```xml
<dependency>
    <groupId>com.fasterxml.jackson.jaxrs</groupId>
    <artifactId>jackson-jaxrs-json-provider</artifactId>
    <version>${jackson.version}</version>
    <scope>provided</scope>
</dependency>
```
