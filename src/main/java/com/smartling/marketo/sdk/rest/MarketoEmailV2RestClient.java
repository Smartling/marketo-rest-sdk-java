package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoEmailV2Client;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Filter;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Page;
import com.smartling.marketo.sdk.rest.command.v2.email.FilterEmails;
import com.smartling.marketo.sdk.rest.command.v2.email.GetEmailById;

import java.util.List;
import java.util.Objects;

public class MarketoEmailV2RestClient implements MarketoEmailV2Client
{
    private final HttpCommandExecutor httpCommandExecutor;

    MarketoEmailV2RestClient(HttpCommandExecutor httpCommandExecutor)
    {
        this.httpCommandExecutor = Objects.requireNonNull(httpCommandExecutor, "httpCommandExecutor must not be null");
    }

    @Override
    public EmailV2Page filterEmails(EmailV2Filter filter) throws MarketoApiException
    {
        Objects.requireNonNull(filter, "filter must not be null");

        EmailV2Page page = httpCommandExecutor.execute(new FilterEmails(filter));
        return page != null ? page : EmailV2Page.empty();
    }

    @Override
    public EmailV2 loadEmailById(String id) throws MarketoApiException
    {
        // The id goes into the URL path, so only plain numbers are allowed
        if (id == null || !id.matches("[0-9]+"))
        {
            throw new IllegalArgumentException("id must be a number, but was: " + id);
        }

        List<EmailV2> emails = httpCommandExecutor.execute(new GetEmailById(id));
        if (emails == null || emails.isEmpty())
        {
            throw new ObjectNotFoundException(String.format("Email[id = %s] not found", id));
        }

        return emails.get(0);
    }
}
