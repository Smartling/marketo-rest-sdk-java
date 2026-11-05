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
     * Paginate with the returned {@code pageSize} and {@code totalItems}: the API clamps
     * {@code pageSize} to 50, so do not rely on the requested size, and stop on an empty
     * {@code items}. Passing {@code sortKey=name} is recommended: it is the only sort key known
     * to work, and whether unsorted results keep a stable order across pages is unverified.
     */
    EmailV2Page filterEmails(EmailV2Filter filter) throws MarketoApiException;

    /**
     * Loads an email by its id, which must be all digits; anything else throws
     * {@link IllegalArgumentException} before any request is made. A bare get has no
     * {@code status} parameter, so which version a {@code published_with_draft} email returns
     * is unverified, and {@link EmailV2#getStatus()} is the caller's only signal of which
     * version came back.
     * <p>
     * A missing id and a permission problem both come back from the API as error {@code 603},
     * surfaced as {@link MarketoApiException} with code {@code 603}, not as
     * {@link com.smartling.marketo.sdk.rest.ObjectNotFoundException}. That exception is only thrown for a null or empty result
     * array, which the API has not been seen to return.
     *
     * @throws IllegalArgumentException if the id is not a number
     */
    EmailV2 loadEmailById(String id) throws MarketoApiException;
}
