# Marketo Asset API v2 email write calls (clone, update, create draft, approve): SDK design

Scope: `marketo-rest-sdk-java` only. The service that consumes this SDK (the provider) has its own spec (MRKT-508). This spec extends `2026-11-05-email-v2-client-design.md` (the read calls, released as 3.2.1).

## Intent

Delivering a translation of a New Email Designer email needs four write calls that the SDK's `MarketoEmailV2Client` does not have: clone the source email into a language copy, update the copy, create a draft of a published copy, and approve. MRKT-506 ran all of them by hand on a Marketo sandbox instance; this change adds them to the SDK.

## Goals

- `MarketoEmailV2Client` gets `cloneEmail`, `updateEmail`, `createDraft` and `approveEmail`.
- A JSON request body is supported by the transport, because these calls take `application/json` bodies and the executor today sends POST only as a form or multipart.
- Marketo's `60019` is reported as a dedicated exception.
- No behaviour change for any existing v1 or v2 read command.

## Non-goals / anti-goals

- No fragment or email-template calls (separate tickets), no delete, no unapprove, no other state transitions.
- No client-side validation of values the API decides (names, HTML, subject). The only checks are structural, on the SDK's own inputs: ids must be all digits (they become path segments or body ids, same rule as `loadEmailById`), a required argument must not be null, and an update must set at least one part.
- Anti-goal: a v2 write that silently omits `x-app-type: marketo` or sends a form body instead of JSON.
- No provider code and no Smartling concepts.

## Facts from MRKT-506 (sandbox, not re-verified for this spec)

| Call | Observation |
|---|---|
| Clone | `POST /rest/asset/v2/email/clone`, body `{"assetId": "6873", "newAsset": {"name": "...", "description": "..."}}`, `x-app-type: marketo`, `Content-Type: application/json`. `success: true`, new id `6874`. The copy is a `draft` in the source's workspace and folder; there is no folder field. `description` was ignored in one call. A duplicate name returned `60000` for fragments and `60002` for templates (not observed for emails). |
| Update | `POST /rest/asset/v2/email/{id}/update`, body `{"headers": {"subject": "..."}, "data": {"html": {"body": "..."}}}`. Accepts `data.html.body` and `headers.subject` on a draft. `fromName` and `data.text.body` are modelled in the v2 email (`headers.fromName`, `data.text.body`) but were not sent in the findings. |
| Update with damaged template markers | `60019 contentWaveAssetUpdationFailed`, no further detail. The same HTML with the markers restored is accepted. The cause is inferred from before/after pairs. |
| Approve | `POST /rest/asset/v2/email/state/transition`, body `{"contentId": "6874", "action": "approve"}`. HTTP 200; the email is then `published` (not `approved`). |
| Create draft | Seen for fragments and templates only: `POST /fragment/state/transition` and `/emailtemplate/state/transition` with `action: create_draft`, status becomes `published_with_draft`, and the draft has its own id; the published id stays the same and its content changes only after approval. Not observed for emails. |

Declared by Adobe's `swagger-asset.json` (the untracked file in the repository root; it is only read, not committed): the three calls (`/email/clone`, `/email/{id}/update`, `/email/state/transition`) all answer `EmailNewResponse`, whose `result` is an **array** of `EmailDetailDTO`, the same shape as get-by-id; the transition `action` enum is `approve`, `unapprove`, `discard`, `create_draft`; `UpdateEmailRequest` has `headers.fromName` and `data.text.body`/`syncFromHtml`; `CloneNewAsset` has a required `name` and an optional `description`. This is the declared schema, not an observed response: only success and the new id were observed for clone. Plan task 1 compares the declared schema with real responses from the sandbox, and the design below changes if they differ.

## Design

Patterns: **Command** (the SDK's request abstraction, reused), **Facade** (`MarketoEmailV2Client`). No new layers: one variant, one caller.

### Transport change (shared, backward compatible)

`JaxRsHttpCommandExecutor.execute` handles `GET`, `POST` (form) and `MULTIPART`; any other method name falls through to `invocationBuilder.method(name, ...)`, which would send the name as an HTTP verb. A new method name `JSON_POST` (the executor's own convention for names that are not HTTP verbs, like `MULTIPART`) gets its own branch before that fallback: it sends the command's body as `application/json; charset=UTF-8` with `invocationBuilder.post(entity, typeToken)`.

- **Serialization:** the body is a `Map<String, Object>` built by the command, passed to `Entity.entity(body, MediaType.APPLICATION_JSON_TYPE.withCharset("UTF-8"))`. Jersey serializes it with the Jersey-registered `ObjectMapperProvider` mapper (not the executor's static plain `OBJECT_MAPPER`, which is used only for `HasToBeMappedToJson` parameters). That mapper uses field visibility with getters off and `NON_NULL` inclusion, so the body is a `Map` (a POJO would need fields, not getters) and the commands put plain strings in it (the command puts the `action` as a String, taken from the enum's API value, never the enum itself: under this mapper an enum would serialize as its constant name, `APPROVE`). The client passed to the executor must register `JacksonFeature` and `ObjectMapperProvider` (the latter supplies the field-visibility and `NON_NULL` settings; a client without it would use Jackson's default mapper). The manager factory, `MarketoEmailV2EndToEndTest` and `JaxRsHttpCommandExecutorTest` register both, and the change adds a short Javadoc on the executor constructor saying so (it has none today).
- `Command` gets `default Object getBody()` returning `null`; only `JSON_POST` commands set it. A `JSON_POST` command with a null body is rejected with `IllegalStateException` before the request is built. Tests that mock `Command` must stub `getBody()` and `getMethod()` (a mock returns `null` from the default).
- Query parameters of a `JSON_POST` command are not used: `buildWebTarget` adds query parameters for GET only, so `getParameters()` returns an empty map.
- Existing commands are unchanged. A regression test pins that a form `POST` still sends a form body.
- **Error message:** `description(...)` is not changed: it prints the method, the path and `getParameters()`, so for `JSON_POST` it reads `(JSON_POST:/path, parameters={})` and never includes the body (an email body can be large). For `update` the path carries the email id; for clone and approve the ids are in the body and not in the message, so callers (the provider) add their own context. Request logging is not changed. The factory logs request entities through Jersey's `LoggingFeature` (`PAYLOAD_ANY`) or, when a logging filter is supplied, through `JsonClientLoggingFilter`, so the JSON body of a write, an email's HTML included, can appear in the logs like any other request entity, depending on the caller's logging setup. How much of a large body is logged was not checked. Changing the logging is out of scope.

### New commands

In `rest/command/v2/email`, next to the read commands; they share the `x-app-type: marketo` header with `BaseEmailGetCommand`. The header and the result type are currently in `BaseEmailGetCommand`; they move to a small shared base `BaseEmailCommand<R>` (the `x-app-type` header, the result type and an empty `getParameters()`), which `BaseEmailGetCommand` and the new `BaseEmailPostCommand<R>` (method `JSON_POST`; the subclass passes the result type and the body `Map` to its constructor, as `BaseEmailGetCommand` takes the result type; each command builds its own map, `UpdateEmail` from the `EmailV2Update`, and `getBody()` returns it, so the executor's null-body check is a guard against a mocked or custom command) extend. Only `GetEmailById` and `FilterEmails` extend the read base today; no behaviour change for them.

| Command | Path | Body |
|---|---|---|
| `CloneEmail` | `/asset/v2/email/clone` | `{"assetId", "newAsset": {"name"}}` |
| `UpdateEmail` | `/asset/v2/email/{id}/update` | built from `EmailV2Update`: `headers.subject`, `headers.fromName`, `data.html.body`, `data.text.body`, each omitted when unset; the whole `headers` or `data` object is omitted when it would be empty |
| `TransitionEmailState` | `/asset/v2/email/state/transition` | `{"contentId", "action"}`; `action` comes from the enum `EmailV2StateAction { APPROVE("approve"), CREATE_DRAFT("create_draft") }`; the command puts its API value (a String) in the body map |

`approveEmail` and `createDraft` both use `TransitionEmailState` (Command pattern, one command and two actions rather than two near-identical classes).

### Client methods

On `MarketoEmailV2Client` and `MarketoEmailV2RestClient`:

All id arguments are named `id`. The inline check in `loadEmailById` (null or not all digits: `IllegalArgumentException("id must be a number, but was: ...")`) is extracted into one private helper in `MarketoEmailV2RestClient` and used by every method, so a null or non-digit `id` is an `IllegalArgumentException` everywhere and the existing `loadEmailById` tests keep passing. A null `name` or null `update` is a `NullPointerException` naming the argument. A blank `name` is not checked: the API decides what a valid name is.

- `EmailV2 cloneEmail(String id, String name)`: clones the email `id` into a copy called `name` and returns the new email. The request carries no description, because it was ignored. The command's result type is `List<EmailV2>` (the declared `result` array, built with Guava's `TypeToken` like `GetEmailById`) and the client returns its first element; a null or empty `result` throws `ObjectNotFoundException` as `loadEmailById` does, and so does a first element without an `id` (a new check, `loadEmailById` does not make it), with a message that names the operation and the source id (for example `Clone of email[id = %s] returned no email`), using the one-argument constructor, so `getErrorCode()` is null there, as in `loadEmailById`. If the sandbox shows that the response carries less than a full email (for example only an id), the client loads the email with `loadEmailById` instead (plan task 1).
- `void updateEmail(String id, EmailV2Update update)`: `EmailV2Update` is an immutable final class with a private constructor, getters and a static `EmailV2Update.builder()` (no required argument), shaped like `EmailV2Filter` (`domain/v2/email`), with the optional parts `subject`, `fromName`, `htmlBody` and `textBody`; a `null` part is unset and is not sent, and an empty string counts as set and is sent as is (the API decides). A builder instead of four positional strings, because three of them are easy to swap. `build()` rejects an update with no part set (`IllegalArgumentException`: an empty update is a caller bug).
- `EmailV2 createDraft(String id)`: returns the draft email (its own id). Result handling as for clone.
- `void approveEmail(String id)`.

The `updateEmail` and `approveEmail` commands use the result type `Void.TYPE`, like the SDK's other commands whose result is not used (`UpdateForm`): `ObjectMapperProvider` registers a `VoidDeserializer` that skips the `result` content, so a response of any shape succeeds. This is deliberate: a write that Marketo applied must not fail afterwards in the SDK's deserialization (such a failure would most likely come from Jersey rather than as a `MarketoApiException`, which is what callers retry on; the executor has no handling for it today), and the client has no use for the result of these two calls. `cloneEmail` and `createDraft` need the result and use `List<EmailV2>`. In the update body, `data.html` and `data.text` are omitted when their `body` is unset, in addition to the empty-`headers`/`data` rule. When several arguments are invalid, the checks run in argument order (`id` first).

`60019` handling: the shared executor maps error codes to exceptions. It maps `60019` to a new `EmailContentRejectedException extends MarketoApiException` in `com.smartling.marketo.sdk.rest` (next to `ObjectNotFoundException`, with the same two constructors). The name avoids `UpdateContentNotAllowedException`, which is the 702 "update content not allowed" case. Other codes are unchanged. It is not retried: `RetryingHttpCommandExecutor` retries only `RequestLimitExceededException`. No code in `src` references `60019` today (checked with grep), so existing commands are unaffected.

A missing email on write: the executor already maps `710`, and `702` unless its message is the "update content not allowed" one, to `ObjectNotFoundException`. The `603` mapping that `loadEmailById` adds was observed on a GET only, so it is **not** added to the write calls here; what a write to a missing id returns is checked in plan task 1, and a client-level mapping is added then if it is `603`.

Rate limits: `606`, `607` and `615` are retried for these POSTs as for any command. A retried `cloneEmail` could create a second copy if the first request was applied but answered with a rate-limit error; this was not observed and is accepted. The provider looks for the copy by name before it clones.

### Versioning

Minor bump in `pom.xml` (3.2.1 to 3.3.0): the interface gains methods, so an external implementer must add them, as with the 3.2.0 bump. Its own commit.

## Testing

JUnit 4, Mockito, AssertJ and WireMock as in the existing tests.

- `JaxRsHttpCommandExecutorTest` (its commands are Mockito mocks, so the `JSON_POST` tests stub `getMethod()`, `getPath()`, `getBody()` and, for the header check, `getHeaders()`): `JSON_POST` sends the body as JSON with `Content-Type: application/json; charset=UTF-8` (like the existing form-charset test), the stubbed header and the bearer token; it sends no query parameters; a body with non-ASCII text, quotes and HTML round-trips unchanged (the test declares its own constant with quotes, HTML tags, non-ASCII text and `&`, since the end-to-end test's `SPECIAL_VALUE` is private and has no quotes or HTML); a form `POST` is unchanged; a `JSON_POST` command with a null body is rejected (the check sits in the private `execute` branch, after the result type and the path have been read, so the strict stubs of the existing `setUp` are all used); the error message for a `JSON_POST` failure has the method and path and does not contain the body text (the format is unchanged); the `60019` code becomes the new exception and other codes are unchanged.
- Command tests: path, method, headers and the exact JSON body of each command, including omitted `null` parts and the empty-`headers`/`data` rule.
- `MarketoEmailV2RestClientTest`: each method delegates with the right command; digit and null checks (`1/2`, `{a}`, empty, null) on every `id` (all `IllegalArgumentException`), a null `name` and a null `update` (`NullPointerException`); `EmailV2Update.build()` with no part set is rejected; `60019` propagates as the new exception.
- `MarketoEmailV2EndToEndTest` (existing class): one WireMock test per call through the real manager, asserting the request path, headers and JSON body, and a `60019` response for update. The update and approve tests answer with an array `result` (the declared shape) and with no `result`, and both succeed, which pins the `Void.TYPE` behaviour.
- A sanitized fixture of the clone and create-draft responses captured in plan task 1.
- Not added: a live integration test; the sandbox runs in plan task 1 and in the provider's manual check are the live evidence.

## Open questions

- Do the real responses of clone and create draft match the declared `result` array of emails (plan task 1)? Update and approve do not depend on it (their result is ignored).
- Whether `create_draft` and `approve` accept the same body for emails as for fragments (plan task 1).
- Whether the update call accepts `headers.fromName` and `data.text.body` for emails: they are in the declared request schema, but not observed (plan task 1).
- What the API does with `data.text.body` when the copy has `data.text.syncFromHtml` true (ignored, overwrites, or an error), and whether the text part is regenerated when only `htmlBody` is sent (plan task 1).
- What `update` does on a `published` email id (the provider only updates drafts) (plan task 1).
- A `published_with_draft` email: which id its draft has, whether `create_draft` on it fails or returns the existing draft, and whether the SDK `EmailV2` must model `associatedStates` (listed in the get response, MRKT-507) so the provider can find the draft (plan task 1). The provider spec carries the same question.
- What a write to a missing email id returns (`603`, `702` or `710`) (plan task 1). Task 1 settles this before the release: the provider's classifier retries a plain `MarketoApiException`, so if the code is `603` the SDK must map it to `ObjectNotFoundException` for the writes as `loadEmailById` does.
- Tagging the release: the repository has no `3.2.x` tags, so tagging is left to the release process.
