package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Filter;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Page;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Update;

/**
 * Client for the Marketo Asset API v2 fragment endpoints: filter and load, and the write calls clone, update, create draft and approve.
 */
public interface MarketoFragmentV2Client
{
    /**
     * Filters fragments. {@code workspaceId} is required (the filter builder enforces it).
     * <ul>
     * <li>Paging: {@code pageSize} has the maximum of 50 declared by the API, and larger values are answered
     * with 50. Page with the returned {@code pageSize} and {@code totalItems}, and stop on an empty
     * {@code items}.</li>
     * <li>Sorting: {@code sortOrder} ({@code ASC} or {@code DESC}) needs a {@code sortKey}. The API accepts
     * the keys {@code name}, {@code metadata.createdAt}, {@code metadata.modifiedAt},
     * {@code metadata.createdBy} and {@code metadata.modifiedBy}; the other keys tried ({@code createdAt},
     * {@code id}, {@code status}) were rejected with error {@code 611}.</li>
     * <li>Dates: an {@code Instant} is sent in UTC as whole seconds, so fractional seconds are truncated. A
     * {@code *Start} date is inclusive and a {@code *End} date is exclusive. As a result an exclusive
     * {@code *End} of {@code T.5} is sent as {@code T} and excludes a fragment modified at {@code T.3}, and
     * an inclusive {@code *Start} of {@code T.5} is sent as {@code T} and also includes a fragment modified
     * at {@code T.3}. A caller that needs exact bounds filters the fragments locally.</li>
     * <li>Folders: a {@code folderId} also matches fragments in its sub-folders.</li>
     * </ul>
     */
    FragmentV2Page filterFragments(FragmentV2Filter filter) throws MarketoApiException;

    /**
     * Loads a fragment by its id, which is the id in a fragment reference of an email without the
     * {@code ajo:} prefix. An id that is not alphanumeric throws {@link IllegalArgumentException} before any
     * request is made.
     * <p>
     * A missing id comes back from the API as error {@code 603} ("Access denied"). This client reports it
     * as {@link com.smartling.marketo.sdk.rest.ObjectNotFoundException} with Marketo's error code and
     * message, so that callers do not retry it. A {@code 603} can also mean a missing permission (observed for
     * emails). The same exception is thrown when the result array is null or empty, and when its first element
     * has no id. Other API errors are thrown as the executor maps them (all of them
     * {@link MarketoApiException}s).
     *
     * @throws IllegalArgumentException if the id is null, blank or not alphanumeric
     */
    FragmentV2 loadFragmentById(String id) throws MarketoApiException;

    /**
     * Clones a fragment under a new name and returns the copy. The copy lands in the source's folder as a
     * draft.
     * <p>
     * Error {@code 603} is reported as {@link com.smartling.marketo.sdk.rest.ObjectNotFoundException} with
     * Marketo's error code and message. A {@code 603} can also be a permission refusal (the API clone answered
     * it to the sandbox user), so a caller must not read it as proof that the source fragment is missing. The
     * same exception is thrown when the result array is null or empty, and when its first element has no id.
     * A duplicate or too long name was answered with {@code 60000} (observed); it stays a plain
     * {@link MarketoApiException}.
     *
     * @throws IllegalArgumentException if the id is null, blank or not alphanumeric
     * @throws NullPointerException if the name is null
     */
    FragmentV2 cloneFragment(String id, String name) throws MarketoApiException;

    /**
     * Updates the parts of a draft fragment that are set in the update.
     * <p>
     * Marketo does not regenerate the text body from the HTML body, so a caller that changes the HTML sends the
     * text body too; {@code syncFromHtml} is not sent. Update the id of the draft: Marketo answers {@code 60019}
     * (thrown as {@link com.smartling.marketo.sdk.rest.ContentRejectedException}, not retried) also for an update
     * of the published id, with or without a draft. Error {@code 603} is reported as
     * {@link com.smartling.marketo.sdk.rest.ObjectNotFoundException}. {@code 60051} (a draft id after its approve,
     * observed once) stays a plain {@link MarketoApiException}.
     *
     * @throws IllegalArgumentException if the id is null, blank or not alphanumeric
     * @throws NullPointerException if the update is null
     */
    void updateFragment(String id, FragmentV2Update update) throws MarketoApiException;

    /**
     * Creates a draft of a published fragment and returns the draft.
     * <p>
     * Takes the id of a published fragment. The draft has its own id, which is the id to update and to approve.
     * A published fragment that already has a draft answers {@code 60000} (observed), which stays a plain
     * {@link MarketoApiException}. The draft of a {@code published_with_draft} fragment is found through
     * {@link FragmentV2#getAssociatedStates()}. Error {@code 603} is reported as
     * {@link com.smartling.marketo.sdk.rest.ObjectNotFoundException}, which is also thrown when the result array
     * is null or empty, and when its first element has no id.
     *
     * @throws IllegalArgumentException if the id is null, blank or not alphanumeric
     */
    FragmentV2 createDraft(String id) throws MarketoApiException;

    /**
     * Approves a draft fragment, which makes its content the published one.
     * <p>
     * Takes the id of the draft. Approving the published id while a draft exists answers {@code 60000} and changes
     * nothing (observed). The id of a draft created from a published fragment stops existing after the approve: a
     * get or update of it answers {@code 60051} and an approve of it {@code 60000} (one observation each). A
     * never-published draft keeps its id, which becomes the published one (one observation). {@code 60000} and
     * {@code 60051} stay plain {@link MarketoApiException}s. Error {@code 603} is reported as
     * {@link com.smartling.marketo.sdk.rest.ObjectNotFoundException}.
     *
     * @throws IllegalArgumentException if the id is null, blank or not alphanumeric
     */
    void approveFragment(String id) throws MarketoApiException;
}
