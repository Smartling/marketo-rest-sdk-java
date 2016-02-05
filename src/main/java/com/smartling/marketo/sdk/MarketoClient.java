package com.smartling.marketo.sdk;

import java.util.List;

public interface MarketoClient {
    List<Email> listEmails(int offset, int limit) throws MarketoApiException;

    List<Email> listEmails(int offset, int limit, FolderId folder, Email.Status status) throws MarketoApiException;

    Email loadEmailById(int id) throws MarketoApiException;

    List<Email> getEmailsByName(String name, FolderId folderId, Email.Status status) throws MarketoApiException;

    List<EmailContentItem> loadEmailContent(int emailId) throws MarketoApiException;

    Email cloneEmail(int sourceEmailId, String newEmailName, FolderId folderId) throws MarketoApiException;

    Email cloneEmail(Email existingEmail, String newEmailName) throws MarketoApiException;

    void updateEmailContent(int id, List<EmailTextContentItem> contentItems) throws MarketoApiException;

    void updateEmailContentItem(int id, EmailTextContentItem contentItem) throws MarketoApiException;

    void updateEmail(Email email) throws MarketoApiException;

    List<FolderDetails> getFolders(FolderId root, int offset, int maxDepth, int limit, String workspace) throws MarketoApiException;

    List<Snippet> listSnippets(int offset, int limit, Asset.Status status) throws MarketoApiException;

    Snippet loadSnippetById(int id) throws MarketoApiException;

    List<SnippetContentItem> loadSnippetContent(int id) throws MarketoApiException;

    Snippet cloneSnippet(int sourceId, String newName, FolderId folderId) throws MarketoApiException;

    void updateSnippetContent(int snippetId, SnippetContentItem contentItem) throws MarketoApiException;
}
