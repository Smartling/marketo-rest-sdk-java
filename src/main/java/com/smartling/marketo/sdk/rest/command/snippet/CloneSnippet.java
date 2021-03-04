package com.smartling.marketo.sdk.rest.command.snippet;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.snippet.Snippet;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.Map;

public class CloneSnippet extends BaseMarketoCommand<Snippet> {
    private final int sourceId;
    private final String newName;
    private final FolderId folderId;

    public CloneSnippet(int sourceId, String newName, FolderId folderId) {
        super(Snippet.class);
        this.sourceId = sourceId;
        this.newName = newName;
        this.folderId = folderId;
    }

    @Override
    public String getPath() {
        return "/asset/v1/snippet/" + sourceId + "/clone.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        return ImmutableMap.of("name", newName, "folder", folderId);
    }
}
