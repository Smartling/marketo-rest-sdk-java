package com.smartling.marketo.sdk.rest.command.folder;

import com.smartling.marketo.sdk.domain.folder.FolderDetails;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

import java.util.Collections;
import java.util.Map;

public class GetFolderById extends BaseGetCommand<FolderDetails> {
    private final FolderId folderId;

    public GetFolderById(FolderId folderId) {
        super(FolderDetails.class);
        this.folderId = folderId;
    }

    @Override
    public String getPath() {
        return "/asset/v1/folder/" + folderId.getId() + ".json";
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.singletonMap("type", folderId.getType().name());
    }

}
