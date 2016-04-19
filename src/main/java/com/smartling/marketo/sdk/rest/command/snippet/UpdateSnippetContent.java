package com.smartling.marketo.sdk.rest.command.snippet;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.snippet.SnippetContentItem;
import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.Map;

public class UpdateSnippetContent implements Command<Void> {
    private final int snippetId;
    private final SnippetContentItem contentItem;

    public UpdateSnippetContent(int snippetId, SnippetContentItem contentItem) {
        this.snippetId = snippetId;
        this.contentItem = contentItem;
    }

    @Override
    public String getPath() {
        return "/asset/v1/snippet/" + snippetId + "/content.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Type getResultType() {
        return Void.TYPE;
    }


    @Override
    public Map<String, Object> getParameters() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
                .put("type", contentItem.getType()).put("content", contentItem.getContent());

        return builder.build();
    }
}
