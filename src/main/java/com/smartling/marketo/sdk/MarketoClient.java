package com.smartling.marketo.sdk;

import java.util.List;

public interface MarketoClient {
    List<Email> listEmails(int offset, int limit) throws MarketoApiException;

    Email loadEmailById(int id) throws MarketoApiException;

    Email loadEmailByName(String name) throws MarketoApiException;

    List<EmailContentItem> loadEmailContent(int emailId) throws MarketoApiException;

    Email cloneEmail(int sourceEmailId, String newEmailName, int folderId) throws MarketoApiException;

    Email cloneEmail(Email existingEmail, String newEmailName) throws MarketoApiException;

    void updateEmailContent(int id, List<EmailContentItem> contentItems) throws MarketoApiException;

    void updateEmail(Email email) throws MarketoApiException;
}
