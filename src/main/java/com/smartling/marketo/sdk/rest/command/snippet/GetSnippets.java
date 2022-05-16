package com.smartling.marketo.sdk.rest.command.snippet;

import com.smartling.marketo.sdk.domain.snippet.Snippet;
import com.smartling.marketo.sdk.rest.command.BasePagedGetCommand;

public class GetSnippets extends BasePagedGetCommand<Snippet> {
    public GetSnippets(Integer offset, Integer limit, Snippet.Status status) {
        super(Snippet.class, offset, limit);

        super.getParameters().put("status", status);
    }

    @Override
    public String getPath() {
        return "/asset/v1/snippets.json";
    }
}
