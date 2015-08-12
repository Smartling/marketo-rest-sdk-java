package com.smartling.marketo.sdk.rest.command;

import com.smartling.marketo.sdk.Snippet;

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
