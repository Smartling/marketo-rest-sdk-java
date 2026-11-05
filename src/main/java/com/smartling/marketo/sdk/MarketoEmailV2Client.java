package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.domain.v2.email.EmailV2;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Filter;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Page;

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
     * {@code metadata.createdBy} and {@code metadata.modifiedBy}; any other key is an error.</li>
     * <li>Dates: a {@code LocalDateTime} is sent as whole seconds and read as UTC. A {@code *Start} date is
     * inclusive and a {@code *End} date is exclusive.</li>
     * <li>Folders: a {@code folderId} also matches emails in its sub-folders.</li>
     * <li>{@code scriptEngine}: the API accepts {@code VELOCITY} and {@code AJO} (upper case).</li>
     * </ul>
     */
    EmailV2Page filterEmails(EmailV2Filter filter) throws MarketoApiException;

    /**
     * Loads an email by its id, which must be all digits; anything else throws
     * {@link IllegalArgumentException} before any request is made.
     * <p>
     * An email that has both a published and a draft version has {@code status}
     * {@code published_with_draft}. Each version has its own id, and a get returns the version the id
     * identifies: {@link EmailV2#getState()} is {@code published} or {@code draft}.
     * <p>
     * A missing id and a permission problem both come back from the API as error {@code 603},
     * surfaced as {@link MarketoApiException} with code {@code 603}, not as
     * {@link com.smartling.marketo.sdk.rest.ObjectNotFoundException}. That exception is only thrown
     * for a null or empty result array.
     *
     * @throws IllegalArgumentException if the id is not all digits
     */
    EmailV2 loadEmailById(String id) throws MarketoApiException;
}
