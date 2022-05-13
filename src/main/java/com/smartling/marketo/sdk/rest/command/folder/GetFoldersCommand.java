package com.smartling.marketo.sdk.rest.command.folder;

import com.smartling.marketo.sdk.domain.folder.FolderDetails;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.rest.command.BasePagedGetCommand;

public class GetFoldersCommand extends BasePagedGetCommand<FolderDetails> {

    public GetFoldersCommand(FolderId root, Integer offset, int maxDepth, Integer limit, String workspace) {
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
