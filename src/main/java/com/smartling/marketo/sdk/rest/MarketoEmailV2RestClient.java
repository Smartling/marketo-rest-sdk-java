package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoEmailV2Client;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Filter;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Page;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Update;
import com.smartling.marketo.sdk.rest.command.v2.email.CloneEmail;
import com.smartling.marketo.sdk.rest.command.v2.email.FilterEmails;
import com.smartling.marketo.sdk.rest.command.v2.email.GetEmailById;
import com.smartling.marketo.sdk.rest.command.v2.email.TransitionEmailState;
import com.smartling.marketo.sdk.rest.command.v2.email.UpdateEmail;

import java.util.List;
import java.util.Objects;

public class MarketoEmailV2RestClient implements MarketoEmailV2Client
{
    private static final String MISSING_EMAIL_ERROR_CODE = "603";

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
        requireNumericId(id);

        // Load the email, reporting Marketo's "missing email" error as not found
        List<EmailV2> emails = executeReportingMissingEmail(new GetEmailById(id));

        if (emails == null || emails.isEmpty())
        {
            throw new ObjectNotFoundException(String.format("Email[id = %s] not found", id));
        }

        return emails.get(0);
    }

    @Override
    public EmailV2 cloneEmail(String id, String name) throws MarketoApiException
    {
        requireNumericId(id);
        Objects.requireNonNull(name, "name must not be null");

        // Clone the email and take the copy from the answer
        List<EmailV2> emails = executeReportingMissingEmail(new CloneEmail(id, name));

        return firstEmailWithId(emails, String.format("Clone of email[id = %s] returned no email", id));
    }

    @Override
    public void updateEmail(String id, EmailV2Update update) throws MarketoApiException
    {
        requireNumericId(id);
        Objects.requireNonNull(update, "update must not be null");

        // Update the email; the answer carries nothing the caller needs
        executeReportingMissingEmail(new UpdateEmail(id, update));
    }

    @Override
    public EmailV2 createDraft(String id) throws MarketoApiException
    {
        requireNumericId(id);

        // Create the draft and take it from the answer
        List<EmailV2> emails = executeReportingMissingEmail(TransitionEmailState.createDraft(id));

        return firstEmailWithId(emails, String.format("Create draft of email[id = %s] returned no email", id));
    }

    @Override
    public void approveEmail(String id) throws MarketoApiException
    {
        requireNumericId(id);

        // Approve the email; the answer carries nothing the caller needs
        executeReportingMissingEmail(TransitionEmailState.approve(id));
    }

    private void requireNumericId(String id)
    {
        // The id goes into the URL path or the body, so only plain numbers are allowed
        if (id == null || !id.matches("[0-9]+"))
        {
            throw new IllegalArgumentException("id must be a number, but was: " + id);
        }
    }

    private <R> R executeReportingMissingEmail(Command<R> command) throws MarketoApiException
    {
        try
        {
            return httpCommandExecutor.execute(command);
        }
        catch (MarketoApiException e)
        {
            // Marketo answers a missing email with error 603; report it as not found so that callers do not retry it
            if (MISSING_EMAIL_ERROR_CODE.equals(e.getErrorCode()))
            {
                throw new ObjectNotFoundException(e.getErrorCode(), e.getMessage());
            }

            throw e;
        }
    }

    private EmailV2 firstEmailWithId(List<EmailV2> emails, String noEmailMessage) throws ObjectNotFoundException
    {
        if (emails == null || emails.isEmpty() || emails.get(0).getId() == null)
        {
            throw new ObjectNotFoundException(noEmailMessage);
        }

        return emails.get(0);
    }
}
