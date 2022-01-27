package com.smartling.marketo.sdk.rest.command.folder;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.folder.FolderDetails;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.Map;

public class CreateFolder extends BaseMarketoCommand<FolderDetails> {
    private FolderId parent;
    private String name;
    private String description;

    public CreateFolder(String name, FolderId parent, String description) {
        super(FolderDetails.class);
        this.parent = parent;
        this.name = name;
        this.description = description;
    }

    @Override
    public String getPath() {
        return "/asset/v1/folders.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
                .put("parent", parent)
                .put("name", name)
                .put("description", description);
        return builder.build();
    }
}
