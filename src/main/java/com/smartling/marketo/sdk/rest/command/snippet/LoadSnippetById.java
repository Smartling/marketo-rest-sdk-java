package com.smartling.marketo.sdk.rest.command.snippet;

import com.smartling.marketo.sdk.domain.snippet.Snippet;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

public class LoadSnippetById extends BaseGetCommand<Snippet> {
    private final int id;

    public LoadSnippetById(int id) {
        super(Snippet.class);
        this.id = id;
    }

    @Override
    public String getPath() {
        return "/asset/v1/snippet/" + id + ".json";
    }
}
