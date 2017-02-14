package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoEmailTemplateClient;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.emailtemplate.EmailTemplate;
import com.smartling.marketo.sdk.domain.emailtemplate.EmailTemplateContent;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.rest.command.emailtemplate.GetEmailTemplateById;
import com.smartling.marketo.sdk.rest.command.emailtemplate.GetEmailTemplateContent;
import com.smartling.marketo.sdk.rest.command.emailtemplate.GetEmailTemplates;
import com.smartling.marketo.sdk.rest.transport.HttpCommandExecutor;

import java.util.Collections;
import java.util.List;

public class MarketoEmailTemplateRestClient implements MarketoEmailTemplateClient {

    private final HttpCommandExecutor httpCommandExecutor;

    public MarketoEmailTemplateRestClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    @Override
    public List<EmailTemplate> getEmailTemplates(int offset, int limit, FolderId folder, Status status) throws MarketoApiException {
        final List<EmailTemplate> emailTemplates = httpCommandExecutor.execute(new GetEmailTemplates(offset, limit, folder, status));
        return emailTemplates != null ? emailTemplates : Collections.emptyList();
    }

    @Override
    public EmailTemplate getEmailTemplateById(int id, Status status) throws MarketoApiException {
        List<EmailTemplate> execute = httpCommandExecutor.execute(new GetEmailTemplateById(id, status));
        if (execute != null && !execute.isEmpty()) {
            return execute.get(0);
        } else {
            throw new MarketoApiException(String.format("EmailTemplate[id = %d] not found", id));
        }
    }

    @Override
    public EmailTemplate getEmailTemplateById(int id) throws MarketoApiException {
        return getEmailTemplateById(id, null);
    }

    @Override
    public List<EmailTemplateContent> getEmailTemplateContent(int id) throws MarketoApiException {
        return getEmailTemplateContent(id, null);
    }

    @Override
    public List<EmailTemplateContent> getEmailTemplateContent(int id, Status status) throws MarketoApiException {
        final List<EmailTemplateContent> contentItems = httpCommandExecutor.execute(new GetEmailTemplateContent(id, status));
        return contentItems != null ? contentItems : Collections.emptyList();
    }
}
