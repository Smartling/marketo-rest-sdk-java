package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.domain.v2.email.EmailV2;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Filter;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Page;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Update;

/**
 * Client for the Marketo Asset API v2 email endpoints.
 */
public interface MarketoEmailV2Client
{
    /**
     * Filters emails. {@code workspaceId} is required (the filter builder enforces it).
     * <ul>
     * <li>Paging: the API clamps {@code pageSize} to 50. Page with the returned {@code pageSize} and
     * {@code totalItems}, and stop on an empty {@code items}.</li>
     * <li>Sorting: {@code sortOrder} ({@code ASC} or {@code DESC}) needs a {@code sortKey}. The API accepts
     * the keys {@code name}, {@code metadata.createdAt}, {@code metadata.modifiedAt},
     * {@code metadata.createdBy} and {@code metadata.modifiedBy}; the other keys tried were rejected with
     * error {@code 611}.</li>
     * <li>Dates: an {@code Instant} is sent in UTC as whole seconds. A {@code *Start} date is inclusive and a
     * {@code *End} date is exclusive.</li>
     * <li>Folders: a {@code folderId} also matches emails in its sub-folders.</li>
     * <li>{@code scriptEngine}: the API accepts {@code VELOCITY} and {@code AJO}; the other values tried were
     * rejected, and the match is case-sensitive.</li>
     * </ul>
     */
    EmailV2Page filterEmails(EmailV2Filter filter) throws MarketoApiException;

    /**
     * Loads an email by its id, which must be all digits; anything else throws
     * {@link IllegalArgumentException} before any request is made.
     * <p>
     * An email that has both a published and a draft version has {@code status}
     * {@code published_with_draft}. Each version has its own id, and a get returns the version the id
     * identifies: {@link EmailV2#getState()} is {@code published} or {@code draft}. The API's get-by-id
     * call has no {@code status} parameter, so the id alone selects the version.
     * <p>
     * A missing id comes back from the API as error {@code 603} ("Access denied"). This client reports it
     * as {@link com.smartling.marketo.sdk.rest.ObjectNotFoundException} with the error code {@code 603}, so
     * that callers do not retry it. That exception is also thrown when the result array is null or empty;
     * the transport maps error code {@code 710} to it, and error code {@code 702} unless its message is an
     * "update content not allowed" message. Every other API error stays a {@link MarketoApiException}.
     *
     * @throws IllegalArgumentException if the id is not all digits
     */
    EmailV2 loadEmailById(String id) throws MarketoApiException;

    /**
     * Clones the email {@code id} into a copy called {@code name} and returns the copy. The copy lands in the
     * source's workspace and folder.
     * <p>
     * A missing id is reported like in {@link #loadEmailById(String)} ({@code 603} becomes
     * {@link com.smartling.marketo.sdk.rest.ObjectNotFoundException} with Marketo's code and message). The
     * same exception, with a null error code, is thrown when the answer carries no email or an email without
     * an id. Marketo also answers {@code 603} for a permission failure, so that reaches the caller as not
     * found too.
     *
     * @throws IllegalArgumentException if the id is not all digits
     * @throws NullPointerException     if the name is null
     */
    EmailV2 cloneEmail(String id, String name) throws MarketoApiException;

    /**
     * Updates the parts of the email {@code id} that are set in {@code update}.
     * <p>
     * The API stores a sent text body as sent and does NOT regenerate the text part from the HTML, so a
     * caller that changes the HTML sends the text body too. {@code syncFromHtml} is not sent.
     * <p>
     * An answer with error {@code 60019} is thrown as
     * {@link com.smartling.marketo.sdk.rest.EmailContentRejectedException}; it also happens when a published
     * (non-draft) email is updated directly, so update the draft. A missing id is reported as in
     * {@link #cloneEmail(String, String)}.
     *
     * @throws IllegalArgumentException if the id is not all digits
     * @throws NullPointerException     if the update is null
     */
    void updateEmail(String id, EmailV2Update update) throws MarketoApiException;

    /**
     * Creates a draft of the approved email {@code id} and returns it. The returned email is the draft with
     * its own id, which is the id to update and approve.
     * <p>
     * It fails with a plain {@link MarketoApiException} (Marketo error {@code 60000}) when the email already
     * has a draft. The draft of a {@code published_with_draft} email is found by reading the published email
     * with {@link #loadEmailById(String)} and looking at {@link EmailV2#getAssociatedStates()}: the entry
     * with state {@code draft} has the draft's id as its {@code contentId}.
     * <p>
     * A missing id and an answer without an email are reported as in {@link #cloneEmail(String, String)}.
     *
     * @throws IllegalArgumentException if the id is not all digits
     */
    EmailV2 createDraft(String id) throws MarketoApiException;

    /**
     * Approves the draft {@code id}. An approved email becomes {@code published}.
     * <p>
     * A missing id is reported as in {@link #cloneEmail(String, String)}.
     *
     * @throws IllegalArgumentException if the id is not all digits
     */
    void approveEmail(String id) throws MarketoApiException;
}
