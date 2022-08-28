package com.smartling.marketo.sdk.rest.command.snippet;

import com.smartling.marketo.sdk.domain.snippet.SnippetContentItem;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

public class LoadSnippetDynamicContent extends BaseGetCommand<String> {
    private final int id;

    public LoadSnippetDynamicContent(int id) {
        super(String.class);
        this.id = id;
    }

    @Override
    public String getPath() {
        return "/asset/v1/snippet/" + id + "/dynamicContent.json";
    }
}
