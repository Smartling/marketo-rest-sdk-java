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
     * Filters emails. Paginate with the returned {@code pageSize} and {@code totalItems}.
     * {@code sortKey=name} is the only sort key known to work and gives stable paging.
     */
    EmailV2Page filterEmails(EmailV2Filter filter) throws MarketoApiException;

    /**
     * Loads an email by its numeric id. A bare get has no {@code status} parameter, so which
     * version a {@code published_with_draft} email returns is unverified.
     *
     * @throws IllegalArgumentException if the id is not a number
     */
    EmailV2 loadEmailById(String id) throws MarketoApiException;
}
