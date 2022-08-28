package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.domain.Asset;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.snippet.Snippet;
import com.smartling.marketo.sdk.domain.snippet.SnippetContentItem;

import java.util.List;

public interface MarketoSnippetClient {
    List<Snippet> listSnippets(Integer offset, Integer limit, Asset.Status status) throws MarketoApiException;

    Snippet loadSnippetById(int id) throws MarketoApiException;

    List<SnippetContentItem> loadSnippetContent(int id) throws MarketoApiException;

    List<String> loadSnippetDynamicContent(int id) throws MarketoApiException;

    Snippet cloneSnippet(int sourceId, String newName, FolderId folderId) throws MarketoApiException;

    void updateSnippetContent(int snippetId, SnippetContentItem contentItem) throws MarketoApiException;

    void approveDraft(int id) throws MarketoApiException;
}
