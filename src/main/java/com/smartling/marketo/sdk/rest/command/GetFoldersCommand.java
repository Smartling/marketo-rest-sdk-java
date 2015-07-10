package com.smartling.marketo.sdk.rest.command;

import com.smartling.marketo.sdk.FolderDetails;
import com.smartling.marketo.sdk.FolderId;

public class GetFoldersCommand extends PagedGetCommand<FolderDetails> {

    public GetFoldersCommand(FolderId root, int offset, int maxDepth, int limit, String workspace) {
        super(FolderDetails.class, offset, limit);

        super.getParameters().put("root", root);
        super.getParameters().put("maxDepth", maxDepth);
        super.getParameters().put("workspace", workspace);
    }

    @Override
    public String getPath() {
        return "/asset/v1/folders.json";
    }
}
