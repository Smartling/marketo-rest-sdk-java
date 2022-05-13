package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.emailtemplate.EmailTemplate;
import com.smartling.marketo.sdk.domain.emailtemplate.EmailTemplateContent;
import com.smartling.marketo.sdk.domain.folder.FolderId;

import java.util.List;

public interface MarketoEmailTemplateClient {
    List<EmailTemplate> getEmailTemplates(Integer offset, Integer limit, FolderId folder, Status status) throws MarketoApiException;

    EmailTemplate getEmailTemplateById(int id, Status status) throws MarketoApiException;

    EmailTemplate getEmailTemplateById(int id) throws MarketoApiException;

    List<EmailTemplateContent> getEmailTemplateContent(int id) throws MarketoApiException;

    List<EmailTemplateContent> getEmailTemplateContent(int id, Status status) throws MarketoApiException;
}
