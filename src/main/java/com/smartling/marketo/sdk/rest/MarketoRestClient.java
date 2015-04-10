package com.smartling.marketo.sdk.rest;

import com.google.common.base.Preconditions;
import com.smartling.marketo.sdk.Email;
import com.smartling.marketo.sdk.EmailContentItem;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoClient;
import com.smartling.marketo.sdk.rest.command.*;
import com.smartling.marketo.sdk.rest.transport.HttpCommandExecutor;

import java.util.Collections;
import java.util.List;

public class MarketoRestClient implements MarketoClient {
    private static final String LIST_END_REACHED_CODE = "702";

    private final HttpCommandExecutor httpCommandExecutor;

    private MarketoRestClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    public static Builder create(String identityUrl, String restUrl) {
        Preconditions.checkNotNull(identityUrl, "Identity URL is empty");
        Preconditions.checkNotNull(restUrl, "REST endpoint URL is empty");

        return new Builder(identityUrl, restUrl);
    }

    @Override
    public List<Email> listEmails(int offset, int limit) throws MarketoApiException {
        try {
            return httpCommandExecutor.execute(new GetEmailsCommand(offset, limit));
        } catch (MarketoApiException e) {
            if (LIST_END_REACHED_CODE.equalsIgnoreCase(e.getErrorCode())) {
                return Collections.emptyList();
            } else {
                throw e;
            }
        }
    }

    @Override
    public Email loadEmailById(int id) throws MarketoApiException {
        List<Email> execute = httpCommandExecutor.execute(new LoadEmailById(id));
        return execute.get(0);
    }

    @Override
    public Email loadEmailByName(final String name) throws MarketoApiException
    {
        List<Email> execute = httpCommandExecutor.execute(new LoadEmailByName(name));

        return execute.get(0);
    }

    @Override
    public List<EmailContentItem> loadEmailContent(int emailId) throws MarketoApiException {
        return httpCommandExecutor.execute(new LoadEmailContent(emailId));
    }

    @Override
    public Email cloneEmail(int sourceEmailId, String newEmailName, int folderId) throws MarketoApiException {
        List<Email> cloned = httpCommandExecutor.execute(new CloneEmail(sourceEmailId, newEmailName, folderId));
        return cloned.get(0);
    }

    @Override
    public Email cloneEmail(Email existingEmail, String newEmailName) throws MarketoApiException {
        return cloneEmail(existingEmail.getId(), newEmailName, existingEmail.getFolder().getValue());
    }

    @Override
    public void updateEmailContent(int id, List<EmailContentItem> contentItems) throws MarketoApiException {
        for (EmailContentItem contentItem : contentItems) {
            httpCommandExecutor.execute(new UpdateEmailEditableSection(id, contentItem));
        }
    }

    public static class Builder {
        private final String identityUrl;
        private final String restUrl;

        private Builder(String identityUrl, String restUrl) {
            this.identityUrl = identityUrl;
            this.restUrl = restUrl;
        }

        public MarketoRestClient withCredentials(String clientId, String clientSecret) {
            Preconditions.checkNotNull(clientId, "Client ID is empty");
            Preconditions.checkNotNull(clientSecret, "Client secret is empty");

            return new MarketoRestClient(new HttpCommandExecutor(identityUrl, restUrl, clientId, clientSecret));
        }
    }
}
