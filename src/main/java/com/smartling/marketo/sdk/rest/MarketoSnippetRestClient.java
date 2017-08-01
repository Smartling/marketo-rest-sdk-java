package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.domain.Asset;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoSnippetClient;
import com.smartling.marketo.sdk.domain.snippet.Snippet;
import com.smartling.marketo.sdk.domain.snippet.SnippetContentItem;
import com.smartling.marketo.sdk.rest.command.snippet.CloneSnippet;
import com.smartling.marketo.sdk.rest.command.snippet.GetSnippets;
import com.smartling.marketo.sdk.rest.command.snippet.LoadSnippetById;
import com.smartling.marketo.sdk.rest.command.snippet.LoadSnippetContent;
import com.smartling.marketo.sdk.rest.command.snippet.UpdateSnippetContent;

import java.util.Collections;
import java.util.List;

public class MarketoSnippetRestClient implements MarketoSnippetClient {
    private final HttpCommandExecutor httpCommandExecutor;

    MarketoSnippetRestClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    @Override
    public List<Snippet> listSnippets(int offset, int limit, Asset.Status status) throws MarketoApiException {
        List<Snippet> snippets = httpCommandExecutor.execute(new GetSnippets(offset, limit, status));
        return snippets != null ? snippets : Collections.emptyList();
    }

    @Override
    public Snippet loadSnippetById(int id) throws MarketoApiException {
        List<Snippet> execute = httpCommandExecutor.execute(new LoadSnippetById(id));
        return execute.get(0);
    }

    @Override
    public List<SnippetContentItem> loadSnippetContent(int id) throws MarketoApiException {
        return httpCommandExecutor.execute(new LoadSnippetContent(id));
    }

    @Override
    public Snippet cloneSnippet(int sourceId, String newName, FolderId folderId) throws MarketoApiException {
        List<Snippet> cloned = httpCommandExecutor.execute(new CloneSnippet(sourceId, newName, folderId));
        return cloned.get(0);
    }

    @Override
    public void updateSnippetContent(int snippetId, SnippetContentItem contentItem) throws MarketoApiException {
        httpCommandExecutor.execute(new UpdateSnippetContent(snippetId, contentItem));
    }
}
