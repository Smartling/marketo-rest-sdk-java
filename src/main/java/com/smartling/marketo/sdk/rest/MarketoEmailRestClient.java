package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoEmailClient;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.email.DynamicContent;
import com.smartling.marketo.sdk.domain.email.DynamicContentItem;
import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.domain.email.EmailContentItem;
import com.smartling.marketo.sdk.domain.email.EmailFullContent;
import com.smartling.marketo.sdk.domain.email.EmailSnippetContentItem;
import com.smartling.marketo.sdk.domain.email.EmailTextContentItem;
import com.smartling.marketo.sdk.domain.email.EmailVariable;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.rest.command.email.ApproveEmailDraft;
import com.smartling.marketo.sdk.rest.command.email.CloneEmail;
import com.smartling.marketo.sdk.rest.command.email.CreateEmail;
import com.smartling.marketo.sdk.rest.command.email.DeleteEmail;
import com.smartling.marketo.sdk.rest.command.email.GetEmailDynamicContent;
import com.smartling.marketo.sdk.rest.command.email.GetEmailFullContent;
import com.smartling.marketo.sdk.rest.command.email.GetEmailVariables;
import com.smartling.marketo.sdk.rest.command.email.GetEmailsByName;
import com.smartling.marketo.sdk.rest.command.email.GetEmailsCommand;
import com.smartling.marketo.sdk.rest.command.email.LoadEmailById;
import com.smartling.marketo.sdk.rest.command.email.LoadEmailContent;
import com.smartling.marketo.sdk.rest.command.email.SendSample;
import com.smartling.marketo.sdk.rest.command.email.UnapproveEmail;
import com.smartling.marketo.sdk.rest.command.email.UpdateEmailContent;
import com.smartling.marketo.sdk.rest.command.email.UpdateEmailDynamicContent;
import com.smartling.marketo.sdk.rest.command.email.UpdateEmailEditableSection;
import com.smartling.marketo.sdk.rest.command.email.UpdateEmailFullContent;
import com.smartling.marketo.sdk.rest.command.email.UpdateEmailMetadata;
import com.smartling.marketo.sdk.rest.command.email.UpdateEmailSnippetContent;
import com.smartling.marketo.sdk.rest.command.email.UpdateEmailVariable;

import java.util.Collections;
import java.util.List;

public class MarketoEmailRestClient implements MarketoEmailClient {

    private final HttpCommandExecutor httpCommandExecutor;

    MarketoEmailRestClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    @Override
    public List<Email> listEmails(Integer offset, Integer limit) throws MarketoApiException {
        return listEmails(offset, limit, null, null);
    }

    @Override
    public List<Email> listEmails(Integer offset, Integer limit, FolderId folder, Status status) throws MarketoApiException {
        final List<Email> emails = httpCommandExecutor.execute(new GetEmailsCommand(offset, limit, folder, status));
        return emails != null ? emails : Collections.emptyList();
    }

    @Override
    public Email loadEmailById(int id, Status status) throws MarketoApiException {
        List<Email> email = httpCommandExecutor.execute(new LoadEmailById(id, status));
        if (email != null && !email.isEmpty()) {
            return email.get(0);
        } else {
            throw new ObjectNotFoundException(String.format("Email[id = %d] not found", id));
        }
    }

    @Override
    public Email loadEmailById(int id) throws MarketoApiException {
        return loadEmailById(id, null);
    }

    @Override
    public List<Email> getEmailsByName(String name, FolderId folder, Status status) throws MarketoApiException {
        return getEmailsByName(null, null, name, folder, status);
    }

    @Override
    public List<Email> getEmailsByName(Integer offset, Integer limit, String name, FolderId folder, Status status) throws MarketoApiException {
        final List<Email> emails = httpCommandExecutor.execute(new GetEmailsByName(offset, limit, name, folder, status));
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
    public Email updateEmailMetadata(Email email) throws MarketoApiException {
        List<Email> updated = httpCommandExecutor.execute(new UpdateEmailMetadata(email));
        if (updated != null && !updated.isEmpty()) {
            return updated.get(0);
        } else {
            throw new MarketoApiException(String.format("Couldn't fetch updated Email: id=%d", email.getId()));
        }
    }

    @Override
    public void updateEmailContentItem(int id, EmailTextContentItem contentItem) throws MarketoApiException {
        httpCommandExecutor.execute(new UpdateEmailEditableSection(id, contentItem));
    }

    @Override
    public void updateEmailSnippetContentItem(int id, EmailSnippetContentItem contentItem) throws MarketoApiException {
        httpCommandExecutor.execute(new UpdateEmailSnippetContent(id, contentItem));
    }

    @Override
    public void updateEmail(Email email) throws MarketoApiException {
        httpCommandExecutor.execute(new UpdateEmailContent(email));
    }

    @Override
    public void sendSample(int emailId, String emailAddress, boolean textOnly) throws MarketoApiException {
        httpCommandExecutor.execute(new SendSample(emailId, emailAddress, textOnly));
    }

    @Override
    public List<EmailVariable> getEmailVariables(int id) throws MarketoApiException {
        List<EmailVariable> variables = httpCommandExecutor.execute(new GetEmailVariables(id));
        return variables != null ? variables : Collections.emptyList();
    }

    @Override
    public EmailVariable updateEmailVariable(int emailId, EmailVariable emailVariable) throws MarketoApiException {
        List<EmailVariable> updated = httpCommandExecutor.execute(new UpdateEmailVariable(emailId, emailVariable));
        if (updated != null && !updated.isEmpty()) {
            return updated.get(0);
        } else {
            throw new MarketoApiException(String.format("Couldn't fetch updated EmailVariable for emailId=%d: %s", emailId, emailVariable.toString()));
        }
    }

    @Override
    public EmailFullContent getEmailFullContent(int id, Status status) throws MarketoApiException {
        List<EmailFullContent> fullContent = httpCommandExecutor.execute(new GetEmailFullContent(id, status));
        if (fullContent != null && !fullContent.isEmpty()) {
            return fullContent.get(0);
        } else {
            throw new ObjectNotFoundException(String.format("No full content for Email[id = %d, status = %s] found", id, status));
        }
    }

    @Override
    public void updateEmailFullContent(int id, String content) throws MarketoApiException {
        httpCommandExecutor.execute(new UpdateEmailFullContent(id, content));
    }

    @Override
    public void approveDraft(int id) throws MarketoApiException {
        httpCommandExecutor.execute(new ApproveEmailDraft(id));
    }

    @Override
    public void unapprove(int id) throws MarketoApiException {
        httpCommandExecutor.execute(new UnapproveEmail(id));
    }

    @Override
    public Email createEmail(String name, FolderId folder, Integer template) throws MarketoApiException {
        List<Email> emails = httpCommandExecutor.execute(new CreateEmail(name, folder, template));
        return emails != null && !emails.isEmpty()? emails.get(0) : null;
    }

    @Override
    public DynamicContent loadDynamicContentById(int emailId, String dynamicContentId) throws MarketoApiException {
        List<DynamicContent> dynamicContents = httpCommandExecutor.execute(new GetEmailDynamicContent(emailId, dynamicContentId));
        return dynamicContents != null && !dynamicContents.isEmpty() ? dynamicContents.get(0) : null;
    }

    @Override
    public void updateDynamicContent(int emailId, String dynamicContentId, List<DynamicContentItem> dynamicContentItems)  throws MarketoApiException {
        for (DynamicContentItem dynamicContentItem: dynamicContentItems) {
            httpCommandExecutor.execute(new UpdateEmailDynamicContent(emailId, dynamicContentId, dynamicContentItem));
        }
    }

    @Override
    public void delete(int emailId) throws MarketoApiException {
        httpCommandExecutor.execute(new DeleteEmail(emailId));
    }
}
