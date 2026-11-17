package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Filter;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Page;

/**
 * Client for the Marketo Asset API v2 fragment endpoints (read-only).
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
}
