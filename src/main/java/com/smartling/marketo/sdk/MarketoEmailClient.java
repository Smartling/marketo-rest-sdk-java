package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.email.DynamicContent;
import com.smartling.marketo.sdk.domain.email.DynamicContentItem;
import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.domain.email.EmailContentItem;
import com.smartling.marketo.sdk.domain.email.EmailFullContent;
import com.smartling.marketo.sdk.domain.email.EmailTextContentItem;
import com.smartling.marketo.sdk.domain.email.EmailVariable;
import com.smartling.marketo.sdk.domain.folder.FolderId;

import java.util.List;

public interface MarketoEmailClient {
    List<Email> listEmails(int offset, int limit) throws MarketoApiException;

    List<Email> listEmails(int offset, int limit, FolderId folder, Status status) throws MarketoApiException;

    Email loadEmailById(int id, Status status) throws MarketoApiException;

    Email loadEmailById(int id) throws MarketoApiException;

    List<Email> getEmailsByName(String name, FolderId folderId, Status status) throws MarketoApiException;

    List<EmailContentItem> loadEmailContent(int emailId) throws MarketoApiException;

    Email cloneEmail(int sourceEmailId, String newEmailName, FolderId folderId) throws MarketoApiException;

    Email cloneEmail(Email existingEmail, String newEmailName) throws MarketoApiException;

    void updateEmailContent(int id, List<EmailTextContentItem> contentItems) throws MarketoApiException;

    Email updateEmailMetadata(Email email) throws MarketoApiException;

    void updateEmailContentItem(int id, EmailTextContentItem contentItem) throws MarketoApiException;

    void updateEmail(Email email) throws MarketoApiException;

    void sendSample(int emailId, String emailAddress, boolean textOnly) throws MarketoApiException;

    List<EmailVariable> getEmailVariables(int id) throws MarketoApiException;

    EmailVariable updateEmailVariable(int emailId, EmailVariable emailVariable) throws MarketoApiException;

    EmailFullContent getEmailFullContent(int id, Status status) throws MarketoApiException;

    void updateEmailFullContent(int id, String content) throws MarketoApiException;

    void approveDraft(int id) throws MarketoApiException;

    void unapprove(int id) throws MarketoApiException;

    Email createEmail(String name, FolderId folder, Integer template) throws MarketoApiException;

    DynamicContent loadDynamicContentById(int emailId, String dynamicContentId) throws MarketoApiException;

    void updateDynamicContent(int emailId, String dynamicContentId, List<DynamicContentItem> dynamicContentItems)  throws MarketoApiException;
}
