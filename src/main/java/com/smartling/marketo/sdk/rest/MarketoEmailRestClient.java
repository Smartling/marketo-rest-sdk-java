package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.domain.email.EmailContentItem;
import com.smartling.marketo.sdk.domain.email.EmailTextContentItem;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoEmailClient;
import com.smartling.marketo.sdk.rest.command.email.*;

import java.util.Collections;
import java.util.List;

public class MarketoEmailRestClient implements MarketoEmailClient {

    private final HttpCommandExecutor httpCommandExecutor;

    MarketoEmailRestClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    @Override
    public List<Email> listEmails(int offset, int limit) throws MarketoApiException {
        return listEmails(offset, limit, null, null);
    }

    @Override
    public List<Email> listEmails(int offset, int limit, FolderId folder, Email.Status status) throws MarketoApiException {
        final List<Email> emails = httpCommandExecutor.execute(new GetEmailsCommand(offset, limit, folder, status));
        return emails != null ? emails : Collections.emptyList();
    }

    @Override
    public Email loadEmailById(int id) throws MarketoApiException {
        List<Email> execute = httpCommandExecutor.execute(new LoadEmailById(id));
        if (execute != null && !execute.isEmpty()) {
            return execute.get(0);
        } else {
            throw new MarketoApiException(String.format("Email[id = %d] not found", id));
        }
    }

    @Override
    public List<Email> getEmailsByName(final String name, final FolderId folder, Email.Status status) throws MarketoApiException {
        final List<Email> emails = httpCommandExecutor.execute(new GetEmailsByName(name, folder, status));
        return emails != null ? emails : Collections.emptyList();
    }

    @Override
    public List<EmailContentItem> loadEmailContent(int emailId) throws MarketoApiException {
        return httpCommandExecutor.execute(new LoadEmailContent(emailId));
    }

    @Override
    public Email cloneEmail(int sourceEmailId, String newEmailName, FolderId folderId) throws MarketoApiException {
        List<Email> cloned = httpCommandExecutor.execute(new CloneEmail(sourceEmailId, newEmailName, folderId));
        return cloned.get(0);
    }

    @Override
    public Email cloneEmail(Email existingEmail, String newEmailName) throws MarketoApiException {
        return cloneEmail(existingEmail.getId(), newEmailName, new FolderId(existingEmail.getFolder()));
    }

    @Override
    public void updateEmailContent(int id, List<EmailTextContentItem> contentItems) throws MarketoApiException {
        for (EmailTextContentItem contentItem : contentItems) {
            httpCommandExecutor.execute(new UpdateEmailEditableSection(id, contentItem));
        }
    }

    @Override
    public void updateEmailContentItem(int id, EmailTextContentItem contentItem) throws MarketoApiException {
        httpCommandExecutor.execute(new UpdateEmailEditableSection(id, contentItem));
    }

    @Override
    public void updateEmail(Email email) throws MarketoApiException {
        httpCommandExecutor.execute(new UpdateEmailContent(email));
    }

    @Override
    public void sendSample(int emailId, String emailAddress, boolean textOnly) throws MarketoApiException {
        httpCommandExecutor.execute(new SendSample(emailId, emailAddress, textOnly));
    }
}
