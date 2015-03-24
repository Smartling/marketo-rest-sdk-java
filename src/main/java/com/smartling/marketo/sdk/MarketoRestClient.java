package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.command.CloneEmail;
import com.smartling.marketo.sdk.command.GetEmailsCommand;
import com.smartling.marketo.sdk.command.LoadEmailById;
import com.smartling.marketo.sdk.command.LoadEmailContent;
import com.smartling.marketo.sdk.command.UpdateEmailContent;
import com.smartling.marketo.sdk.transport.HttpCommandExecutor;

import java.util.List;

public class MarketoRestClient implements MarketoClient {
    private final HttpCommandExecutor httpCommandExecutor;

    private MarketoRestClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    public static Builder create(String identityUrl, String restUrl) {
        return new Builder(identityUrl, restUrl);
    }

    @Override
    public List<Email> listEmails(int offset, int limit) throws MarketoApiException {
        return httpCommandExecutor.execute(new GetEmailsCommand(offset, limit));
    }

    @Override
    public Email loadEmailById(int id) throws MarketoApiException {
        List<Email> execute = httpCommandExecutor.execute(new LoadEmailById(id));
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
    public void updateEmailContent(int id, String subject, List<EmailContentItem> contentItems) throws MarketoApiException {
        String format = "<div class='mktEditable' id='%s'>%s</div>";

        StringBuilder htmlContent = new StringBuilder();
        StringBuilder textContent = new StringBuilder();
        for (EmailContentItem contentItem : contentItems) {
            htmlContent.append(String.format(format, contentItem.getHtmlId(), contentItem.getHtmlContent()));
            textContent.append(String.format(format, contentItem.getHtmlId(), contentItem.getTextContent()));
        }

        httpCommandExecutor.execute(new UpdateEmailContent(id, subject, htmlContent.toString(), textContent.toString()));
    }

    public static class Builder {
        private final String identityUrl;
        private final String restUrl;

        private Builder(String identityUrl, String restUrl) {
            this.identityUrl = identityUrl;
            this.restUrl = restUrl;
        }

        public MarketoRestClient withCredentials(String clientId, String clientSecret) {
            return new MarketoRestClient(new HttpCommandExecutor(identityUrl, restUrl, clientId, clientSecret));
        }
    }
}
