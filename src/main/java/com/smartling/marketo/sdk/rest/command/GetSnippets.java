package com.smartling.marketo.sdk.rest.command;

import com.smartling.marketo.sdk.Snippet;

public class GetSnippets extends PagedGetCommand<Snippet> {
    public GetSnippets(int offset, int limit, Snippet.Status status) {
        super(Snippet.class, offset, limit);

        super.getParameters().put("status", status);
    }

    @Override
    public String getPath() {
        return "/asset/v1/snippets.json";
    }
}
