package com.smartling.marketo.sdk.rest.command.folder;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.folder.FolderDetails;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

import java.util.Collections;
import java.util.Map;

public class GetFolderByName extends BaseGetCommand<FolderDetails> {
    private final String name;
    private final FolderType type;
    private final FolderId root;

    public GetFolderByName(String name, FolderType type, FolderId root) {
        super(FolderDetails.class);
        this.name = name;
        this.type = type;
        this.root = root;
    }

    @Override
    public String getPath() {
        return "/asset/v1/folder/byName.json";
    }

    @Override
    public Map<String, Object> getParameters() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
                .put("name", name);

        if (type != null) {
            builder.put("type", type.name());
        }

        if (root != null) {
            builder.put("root", root);
        }

        return builder.build();
    }
}
