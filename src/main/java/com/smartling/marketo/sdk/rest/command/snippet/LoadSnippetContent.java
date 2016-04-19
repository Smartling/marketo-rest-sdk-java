package com.smartling.marketo.sdk.rest.command.snippet;

import com.smartling.marketo.sdk.domain.snippet.SnippetContentItem;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

public class LoadSnippetContent extends BaseGetCommand<SnippetContentItem> {
    private final int id;

    public LoadSnippetContent(int id) {
        super(SnippetContentItem.class);
        this.id = id;
    }

    @Override
    public String getPath() {
        return "/asset/v1/snippet/" + id + "/content.json";
    }
}
