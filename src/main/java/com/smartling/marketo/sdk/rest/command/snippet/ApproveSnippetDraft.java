package com.smartling.marketo.sdk.rest.command.snippet;

import com.smartling.marketo.sdk.domain.snippet.Snippet;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

public class ApproveSnippetDraft extends BaseMarketoCommand<Snippet> {
    private final int id;

    public ApproveSnippetDraft(int id) {
        super(Snippet.class);
        this.id = id;
    }

    @Override
    public String getPath() {
        return "/asset/v1/snippet/" + id + "/approveDraft.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }
}
