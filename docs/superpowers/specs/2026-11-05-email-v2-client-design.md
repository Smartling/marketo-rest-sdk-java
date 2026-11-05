# Marketo Asset API v2 email client (New Email Designer) — SDK design

Scope: `marketo-rest-sdk-java` only. The provider (`marketo-content-provider`) gets its own spec and plan.

## Intent

The SDK reads emails only through the v1 Asset API. For emails built in Marketo's New Email Designer, v1 returns
`709 Cannot access content for Acrite Email asset` even on plain metadata (MRKT-506, Findings #1, #2). The v2 Asset API
(`/rest/asset/v2/email`) returns the full email for the same asset. MRKT-507 needs the provider to list and read such
emails, so the SDK needs a v2 email client with **list (filter)** and **get by id**.

Update, clone and approve are **out of scope** here (they belong to the delivery ticket). So are fragments and email
templates.

## Goals

- A v2 email client exposed from `MarketoClientManager`, with two operations: `filterEmails` and `loadEmailById`.
- `filterEmails` supports every filter the Adobe swagger declares for `GET /rest/asset/v2/email/filter` (below).
- No behavior change for any existing v1 command or client.

## Non-goals / anti-goals

- No change to v1 `Email` or the v1 clients.
- No client-side validation or clamping of values the API decides (`sortKey`, `pageSize`, `scriptEngine`, date range).
  A wrong value surfaces as the API's error. The only checks are structural, on the SDK's own inputs: the filter builder
  requires a non-blank `workspaceId`, rejects a null `status` element and copies the `status` list, and
  `loadEmailById` rejects an id that is not a plain number (it becomes a URL path segment).
- No provider code, no UI, no Smartling-side concepts.
- Anti-goal: a v2 call that silently omits `x-app-type: marketo` (the API answers `704`).

## Verified facts (sandbox `749-IOM-373` and Adobe `swagger-asset.json`, 2026-11-05)

Source of truth for parameter names: `https://raw.githubusercontent.com/AdobeDocs/marketo-apis/main/static/swagger-asset.json`,
path `/rest/asset/v2/email/filter`. Behaviors below were probed with read-only GETs on the sandbox (11 emails).

**Filter parameters (swagger):** `workspaceId`, `folderId`, `folderType` (`Folder`|`Program`), `status` (array),
`pageIndex` (int32), `pageSize` (int32, "Maximum value is 50"), `createdAtStart`, `createdAtEnd`, `modifiedAtStart`,
`modifiedAtEnd` (strings, no format given), `name`, `sortKey` (string, no enum), `sortOrder` (`ASC`|`DESC`),
`isCreatedByMe`, `isModifiedByMe` (boolean), `templateId`, `scriptEngine`, `isValueNonNullable` (string),
`includeArchived` (boolean). Adobe's docs page also lists `folderIds`, `createdBy`, `modifiedBy`; they are **not in the
swagger**, so they are not in this design.

**Observed on the sandbox:**

| Behavior | Observation |
|---|---|
| `workspaceId` | Omitting it returns `603 Access denied`, so the SDK requires it. |
| Get by id | `GET /email/6873` with only the `x-app-type` header (no `workspaceId`, no other query parameter) returns `success:true`, so `loadEmailById(String id)` needs only the id. The swagger's get-by-id parameters are exactly `access_token`, `x-app-type` and `id`: there is no `status` or other query parameter (unlike v1's `LoadEmailById`), so the SDK cannot choose draft vs published. A get returns the version the id identifies: email `6886` has `status: published_with_draft`; a get on `6886` returns `state: published` and a get on `6888` (its draft) returns `state: draft`, and both list the two ids in `associatedStates`. A filter call returned `6886` and not `6888`. |
| `x-app-type: marketo` | Omitting it returns `704`. Required on every v2 call. |
| Response envelope (filter) | `result` is an object: `items`, `totalItems`, `pageSize`, `currentPage`. |
| Response envelope (get by id) | `result` is an **array** with one element, as in v1. |
| Pagination | `pageIndex` is zero-based. `pageSize` above 50 (tried 51, 100) is accepted and the response reports `pageSize: 50`. Paging the 11 sandbox emails 3 at a time returned each email exactly once, with and without a `sortKey`, in two runs. |
| `sortKey` | Accepted: `name`, `metadata.createdAt`, `metadata.modifiedAt`, `metadata.createdBy`, `metadata.modifiedBy` (`ASC` and `DESC` reverse the order, checked on `name` and `metadata.modifiedAt`). Rejected with `611`: `id`, `description`, `status`, `state`, `templateId`, `createdBy`, `modifiedBy`, `createdAt`, `modifiedAt`, `updatedAt`, `headers.subject`, `appData.folderId`, `scriptEngine`, `virtualId`, `externalId` and a made-up key. A lowercase `sortOrder` (`asc`) is rejected with `1003`. |
| `sortOrder` alone | Without `sortKey` returns `611` (HTTP 400 downstream). |
| Dates | `modifiedAtStart/End` and `createdAtStart/End` accept a **zone-less date-time `yyyy-MM-dd'T'HH:mm:ss`** (`2026-10-28T08:27:30`), a date only (`2026-10-28`), and fractional seconds (`...30.000`, `...30.5`). A zone suffix or a space separator is rejected with `611` (HTTP 500 downstream): `2026-10-29T00:00:00Z`, `...00.000Z`, `...00+0000`, `...00Z+0000`, `2026-10-29 00:00:00`. The value is read as UTC: email `6881` was modified at `2026-10-28T12:09:33Z`, `modifiedAtStart=2026-10-28T12:09:33` returned it and `...12:09:34` did not. A `*Start` bound is inclusive and a `*End` bound is exclusive: `modifiedAtEnd=2026-10-28T12:09:33` did not return `6881` and `...12:09:34` did (`createdAtStart`/`createdAtEnd` on email `6875`, created at `08:27:30Z`, behave the same). The SDK sends whole seconds. |
| `status` repeated | `status=draft&status=published` returns the union (the swagger declares `status` an array with `collectionFormat: multi`, i.e. repeated parameters). Observed status values: `draft`, `published`, `published_with_draft`. |
| `folderId` | Filters. `folderType` is accepted but did not change results for a plain folder. Folder `2622` is a child of folder `2373` (v1 folder API), and `folderId=2373` also returned the emails whose own `appData.folderId` is `2622`: a folder filter includes sub-folders. |
| `templateId`, `isCreatedByMe`, `isModifiedByMe` | Each narrows the result. |
| `scriptEngine` | Accepted: `VELOCITY` (matched all 11 emails) and `AJO` (matched none). Rejected with `611`: `velocity`, `Velocity`, `HANDLEBARS`, `handlebars`, `HANDLEBAR`, `MUSTACHE`, `AMPSCRIPT`, `JAVASCRIPT`, `SMARTY`, `MARKETO`; the match is case-sensitive. |
| `includeArchived`, `isValueNonNullable` | Accepted; no visible effect on this data (no archived emails to observe). |
| HTTP status of errors | Every error probed (`603`, `611`, `704`, bad `sortKey`, bad date, bad `scriptEngine`, missing id, bad token) came back as **HTTP 200** with `success:false`. The "HTTP 400/500" inside the `611` message text refers to a downstream call, not this response. |
| Missing / non-numeric id on get | `603 Access denied`, not one of the codes the executor maps to "not found" (`702`, `710`). |
| Timestamps in responses | `"2026-10-30T12:30:07Z+0000"`. This matches the SDK's existing `ObjectMapper` date format `yyyy-MM-dd'T'HH:mm:ss'Z'Z`. |
| List item vs get | List items have no `data`. Get returns `data.html.body`, `data.text.body`, `data.text.syncFromHtml`. |
| URL | Neither response carries an external/web URL for the email. |

## Design

Patterns: **Command** (the SDK's existing request abstraction, reused), **Builder** (`EmailV2Filter`, which has 19
filters: the required `workspaceId` plus 18 optional ones), **Facade** (`MarketoEmailV2Client` hides commands and the executor). No new strategy or factory layers:
there is one variant and one caller.

### Transport changes (shared, backward compatible)

1. `Command` gets `default Map<String, String> getHeaders()` returning an empty map. `JaxRsHttpCommandExecutor`
   applies each entry to the request. Existing commands are unchanged.
2. `JaxRsHttpCommandExecutor.buildWebTarget` expands a `Collection` parameter value into one query parameter per element
   (`status=draft&status=published`). Today a `Collection` passed to Jersey 3.1.10's
   `queryParam(name, Object...)` is one value and is sent via `toString()` (`[draft, published]`) (checked in the
   Jersey jar); a WireMock test pins the new behavior. The expansion runs in the `buildWebTarget` loop, **after** `processParameters`,
   so a value that `processParameters` already turned into a JSON `String` (`HasToBeMappedToJson`, e.g. a list subclass)
   is still sent as one scalar; only a plain `Collection` is expanded. Non-collection values behave exactly as before, including a `null` value, which Jersey
   treats as "remove this parameter" and which existing v1 commands rely on. The error message the
   executor builds includes `command.getParameters()`, so a list appears in that message.

### New types

Package layout: v2 is nested under `v2/<asset>` (`rest/command/v2/email`, `domain/v2/email`), not appended to the asset
name, because the same Asset API v2 will also serve fragments and email templates in follow-up work (`v2/fragment`,
`v2/emailtemplate`). Naming rule: a class inside a `v2/...` package gets its version from the package, so the commands
carry no suffix (`BaseEmailGetCommand`, `FilterEmails`, `GetEmailById`; v1 already has a `LoadEmailById`, hence
`GetEmailById`). The public API surface keeps the `V2` suffix so a consumer can tell it from v1 in imports and
signatures: the domain family `EmailV2`, `EmailV2Page`, `EmailV2Filter` (kept together under one suffix; `EmailV2` itself
is imported next to the v1 `Email`), and the two client classes `MarketoEmailV2Client` and `MarketoEmailV2RestClient`,
which share flat packages with their v1 siblings. `SortOrder` is a generic name: it takes no suffix and lives in `domain/v2/email`.

- `rest/command/v2/email/BaseEmailGetCommand<R>` — abstract, implements `Command<R>` **directly**. It cannot extend the
  existing `BaseGetCommand`: that class is `Command<List<T>>` and builds a `List<T>` result type
  (`BaseMarketoCommand`), but the filter result is a single `EmailV2Page` object. It takes the result `Type` in its
  constructor, returns method `GET`, returns no parameters by default, and defines the `x-app-type: marketo` header in
  `getHeaders()`, the single place that header is defined. `BaseMarketoCommand` and every v1 command are untouched.
- `rest/command/v2/email/FilterEmails` — `BaseEmailGetCommand<EmailV2Page>`, path `/asset/v2/email/filter`. Puts only
  non-null filter values into `getParameters()`, **already converted to strings by the command** (`Instant` →
  `yyyy-MM-dd'T'HH:mm:ss` in UTC with a fixed formatter, because `Instant.toString()` ends in `Z`, which the API rejects, `SortOrder` → `ASC`/`DESC`, `FolderType` → its `@JsonProperty` name `Folder`/`Program`, `Boolean`/`Integer` → `toString()`),
  because the executor sends `toString()` and does not apply the ObjectMapper. The one exception is `status`, which
  stays a `List<String>` so the executor expands it (see Transport changes).
- `rest/command/v2/email/GetEmailById` — `BaseEmailGetCommand<List<EmailV2>>` (the array envelope), path
  `/asset/v2/email/{id}`. The `List<EmailV2>` `Type` is built with Guava's `TypeToken` (Guava is already a dependency).
- `domain/v2/email/EmailV2` — fields: `id` (String), `name`, `description`, `status`, `state`, `appType`, `templateId`,
  `externalId`, `virtualId`, `scriptEngine`, `headers` (`subject`, `fromEmail`, `fromName`, `replyEmail`, all plain `String` in the sandbox response, unlike v1's `{type,value}` fields), `metadata`
  (`createdBy`, `createdAt` as `Date`, `modifiedBy`, `modifiedAt` as `Date`), `appData` (`workspaceId`, `folderId`),
  `data` (`html.body`, `text.body`, `text.syncFromHtml`; null on list items). Unknown JSON fields are ignored by the
  existing mapper. `settings`, `editorContext` and `associatedStates` are not modeled now (not needed for list/get;
  the delivery ticket adds what it needs).
  JSON types, from the sandbox payloads: every scalar is a JSON **string** except `data.text.syncFromHtml`
  (boolean). That includes `id`, `templateId`, `externalId`, `virtualId`, `appData.workspaceId`, `appData.folderId`
  and the two `metadata` timestamps (parsed to `Date`). `headers`, `metadata`, `appData` and `data` are nested objects,
  and `data.html` / `data.text` are nested again; each is a small static nested class of `EmailV2` (`Headers`, `Metadata`, `AppData`, `Data`, `Html`, `Text`). Sanitized
  copies of one list response and one get response are the test fixtures
  `src/test/resources/json/email-v2-filter-response.json` and `email-v2-get-response.json`.
- `domain/v2/email/EmailV2Page` — `items`, `totalItems`, `pageSize`, `currentPage`.
- `domain/v2/email/EmailV2Filter` — immutable, built with a builder; `workspaceId` is required by the builder. Field types:

| Filter | Java type |
|---|---|
| `workspaceId`, `folderId`, `name`, `templateId`, `scriptEngine`, `isValueNonNullable`, `sortKey` | `String` |
| `folderType` | existing `FolderType`, sent as `Folder`/`Program`. The names are not repeated in the command: it reads them from the enum's `@JsonProperty` through the SDK's `ObjectMapper` (`convertValue(folderType, String.class)`), because the executor sends GET parameters with `toString()`, which gives `FOLDER` (v1's `GetFolderByName` sends `type.name()` = `FOLDER`, which the v2 swagger does not list). Unit test pins the strings. |
| `status` | `List<String>` (values are the API's; `Asset.Status` is v1 and would drop `published_with_draft`) |
| `sortOrder` | new enum `SortOrder { ASC, DESC }` |
| `isCreatedByMe`, `isModifiedByMe`, `includeArchived` | `Boolean` |
| `createdAtStart`, `createdAtEnd`, `modifiedAtStart`, `modifiedAtEnd` | `Instant`, sent in UTC as `yyyy-MM-dd'T'HH:mm:ss` (no zone suffix, whole seconds; the API reads it as UTC, see the Dates row). An `Instant` has no local time zone, so a caller cannot send local time by mistake. |
| `pageIndex`, `pageSize` | `Integer` |

- `MarketoEmailV2Client` (interface, in `com.smartling.marketo.sdk` beside `MarketoEmailClient` and
  `MarketoClientManager`) and `rest/MarketoEmailV2RestClient`:
  - `EmailV2 loadEmailById(String id)` — throws `ObjectNotFoundException` if the result is null or the array is empty,
    mirroring v1 (`MarketoEmailRestClient` null-checks before `isEmpty()`), and rethrows error `603` as
    `ObjectNotFoundException` (see Error handling). On the sandbox a missing id returns `603`, not an empty array,
    so the empty-array branch is kept for parity with v1 and has a unit test. The API types the id as a string and returns it as a JSON string (`"6885"`). The client rejects a
    null/blank id, or one that is not all digits (`[0-9]+`), with `IllegalArgumentException` **before** building the
    command, because the id becomes a URL path segment: Jersey treats `{...}` as a template (unchecked exception), `/`
    or `?` would change the route, `null` would become `.../email/null`, and a word such as `filter` would hit the list
    endpoint (whose object result cannot be read as the array this call expects). Every email id seen is numeric
    (the sandbox ids, e.g. `6873` and `6886`). A later fragment client can use its own rule. A unit test covers `filter`,
    `1/2`, `{a}`, `` and null. The Javadoc says that a get returns the version the id identifies (`EmailV2.getState()` is `published` or
    `draft`) and that the swagger gives get-by-id no `status` parameter.
  - `EmailV2Page filterEmails(EmailV2Filter filter)` — a null `result` with `success:true` yields an empty
    `EmailV2Page` (no items, zero totals), like v1's null-to-empty-list normalization, so callers never get an NPE.
    Callers must paginate with the returned `pageSize` and `totalItems`, not the size they requested, because the API
    clamps `pageSize` to 50; the Javadoc says so, and lists the accepted `sortKey` values, the UTC and inclusive/exclusive date rules,
    sub-folder matching and the accepted `scriptEngine` values from the observed facts above. An empty page from null normalization has no items and zero totals; callers stop on empty `items`,
    not on `pageSize`.
- `MarketoClientManager.getMarketoEmailV2Client()` (abstract interface method) and its implementation in
  `MarketoRestClientManager`. In this repo `MarketoRestClientManager` is the only implementer (checked with grep over
  `src`); an implementer outside this repo must add the new method, which is why the version bump is minor and called out below.
  `HttpCommandExecutor` needs no change: its only implementers are `JaxRsHttpCommandExecutor` and the delegating
  `RetryingHttpCommandExecutor`. `x-app-type` is added to the request built in `execute`, so the token call is unaffected.

### Error handling

v2 errors reuse the executor's existing mapping. This works because v2 reports errors as HTTP 200 with `success:false`
(see the observed table); the executor's `method(..., GenericType)` call would throw `WebApplicationException` for a
non-2xx status before the mapping runs, which is the same exposure v1 already has. Rate-limit codes (`606`, `607`, `615`)
map to `RequestLimitExceededException` and are retried by `RetryingHttpCommandExecutor` exactly as for v1; a v2
rate-limit response was not observed. Three v2 facts matter to callers:

- `611` (bad `sortKey`, date format, `scriptEngine`, or `sortOrder` without `sortKey`) surfaces as `MarketoApiException`
  with code `611`.
- A missing id returns `603 Access denied`. `MarketoEmailV2RestClient.loadEmailById` rethrows it as
  `ObjectNotFoundException` with the same error code and message, so that callers do not retry a missing email:
  the provider wraps each SDK client in a retry proxy whose classifier retries a plain `MarketoApiException` and
  does not retry `ObjectNotFoundException`. The mapping is in the client, not in the shared executor, so the v1
  commands are unaffected.
- Marketo's `60019` and similar generic codes stay `MarketoApiException`.

### Versioning

Minor bump in `pom.xml` (3.1.45 → 3.2.0), as its own commit, matching how earlier releases were done. The bump is minor because it
adds a method to `MarketoClientManager`, so any other implementer of that interface must add it.

## Testing

JUnit 4, Mockito and AssertJ as in the existing tests; WireMock for transport.

- `JaxRsHttpCommandExecutorTest`: a command's headers reach the wire; a collection parameter becomes repeated query
  parameters; existing behavior for single values is unchanged.
- `FilterEmails` / `GetEmailById` tests: path, method, header, and that only set filters appear in the parameters
  (every filter covered, including dates as `yyyy-MM-dd'T'HH:mm:ss` (an `Instant` with zero seconds still sends `:00`, and an instant with a non-UTC offset is converted to UTC), `status` list, and `sortOrder`).
- One end-to-end WireMock test through `MarketoRestClientManager` with the real commands: both requests carry
  `x-app-type: marketo`; an HTTP 200 `{"success":false,"errors":[{"code":"611",...}]}` becomes `MarketoApiException`
  with code `611`; a `606` becomes `RequestLimitExceededException`.
- `MarketoRestClientManagerTest`: `shouldReturnEmailV2RestClient`, matching the existing per-client tests.
- v1 regression: a v1 command sends no `x-app-type` header (assert absence in `JaxRsHttpCommandExecutorTest`).
- `EmailV2Filter`: builder rejects a null/blank `workspaceId`; `status` is defensively copied (mutating the caller's
  list after `build()` changes nothing); an empty `status` list sends no `status` parameter; a null element is rejected
  with a clear message rather than an NPE inside Jersey.
- End-to-end test setup: it lives in package `com.smartling.marketo.sdk.rest` (the `MarketoRestClientManager`
  constructor is package-private) and builds its own `JaxRsHttpCommandExecutor` with a fresh public `BasicTokenProvider`, not
  `MarketoRestClientManagerFactory`, whose static token cache is keyed by `clientId` and shared by every manager the
  factory creates in the JVM. A stub `TokenProvider` cannot be written from that package (`Token` and `ClientConnectionData` are
  package-private in `rest.transport`), so WireMock also stubs `GET /oauth/token` (on the identity URL) to return an
  `access_token` and a positive `expires_in`.
- Query-string values: the encoding rule is "the server decodes each value to exactly the original string". The test
  sends `a+b&c=d#e %41 {x} é` in `name` and in a `status` element and asserts WireMock sees the originals. Jersey
  keeps existing `%XX` as written and treats `{...}` as a template, so `FilterEmails` percent-encodes its string filters
  and each `status` element (UTF-8, `%` as `%25`, `+` as `%2B`, space as `%20`) before `queryParam`; `FilterEmailsTest`
  pins the encoded form and this end-to-end test pins the round trip.
- A `603` on get by id end to end is an `ObjectNotFoundException` with code `603`; the client unit tests also cover that
  another API error (`611`) is rethrown unchanged.
- `MarketoEmailV2RestClientTest`: delegates to the executor with the right command; empty array → `ObjectNotFoundException`.
- Deserialization tests using the fixtures `src/test/resources/json/email-v2-*.json` (sanitized
  real sandbox payloads, HTML body shortened): `EmailV2` and
  `EmailV2Page` including `Date` parsing of `2026-10-30T12:30:07Z+0000`.
- Not added: a live integration test. The existing `*IntegrationTest` classes need real credentials; the sandbox probes
  above are the live evidence for this change.

## Known limitations carried over from v1

- `JsonClientLoggingFilter` skips request/response logging for any URL containing "auth" (for example
  `name=Author...`). Existing behavior, not changed here.
- Adding `getMarketoEmailV2Client()` to `MarketoClientManager` is source-breaking for external implementers; accepted,
  hence the minor version bump.

## Test mechanics

- Query-parameter order is not deterministic (`HashMap`), so WireMock tests use `withQueryParam` matchers, not exact URLs.
- Deserialization tests build the mapper with `new ObjectMapperProvider().getContext(null)`; a plain `ObjectMapper` lacks
  the date format, field visibility and case-insensitive matching.
- A test pins that a `HasToBeMappedToJson` collection is still sent as one JSON string.
