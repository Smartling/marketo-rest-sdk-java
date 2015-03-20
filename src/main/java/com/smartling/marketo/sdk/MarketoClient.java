package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.command.GetEmailsCommand;
import com.smartling.marketo.sdk.command.LoadEmailById;
import com.smartling.marketo.sdk.command.LoadEmailContent;
import com.smartling.marketo.sdk.transport.HttpCommandExecutor;

import java.util.List;

public class MarketoClient {
    private final HttpCommandExecutor httpCommandExecutor;

    private MarketoClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    public static Builder create(String identityUrl, String restUrl) {
        return new Builder(identityUrl, restUrl);
    }

    public List<Email> listEmails(int offset, int limit) throws MarketoApiException {
        return httpCommandExecutor.execute(new GetEmailsCommand(offset, limit));
    }

    public Email loadEmailById(int id) throws MarketoApiException {
        List<Email> execute = httpCommandExecutor.execute(new LoadEmailById(id));
        return execute.get(0);
    }

    public List<EmailContentItem> loadEmailContent(int emailId) throws MarketoApiException {
        return httpCommandExecutor.execute(new LoadEmailContent(emailId));
    }

    public static class Builder {
        private final String identityUrl;
        private final String restUrl;

        private Builder(String identityUrl, String restUrl) {
            this.identityUrl = identityUrl;
            this.restUrl = restUrl;
        }

        public MarketoClient withCredentials(String clientId, String clientSecret) {
            return new MarketoClient(new HttpCommandExecutor(identityUrl, restUrl, clientId, clientSecret));
        }
    }
}
