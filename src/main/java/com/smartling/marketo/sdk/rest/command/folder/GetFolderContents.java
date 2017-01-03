package com.smartling.marketo.sdk.rest.command.folder;

import com.smartling.marketo.sdk.domain.folder.FolderContentItem;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.rest.command.BasePagedGetCommand;

public class GetFolderContents extends BasePagedGetCommand<FolderContentItem> {
    private final int id;

    public GetFolderContents(FolderId folder, Integer offset, Integer maxReturn) {
        super(FolderContentItem.class, offset, maxReturn);

        super.getParameters().put("type", folder.getType().name());
        this.id = folder.getId();
    }

    @Override
    public String getPath() {
        return "/asset/v1/folder/" + this.id + "/content.json";
    }
}
