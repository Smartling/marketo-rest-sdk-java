package com.smartling.marketo.sdk.rest.command.program;

import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.program.Program;
import com.smartling.marketo.sdk.rest.command.BasePagedGetCommand;

public class GetPrograms extends BasePagedGetCommand<Program> {
    public GetPrograms(Integer offset, Integer limit, FolderId folder) {
        super(Program.class, offset, limit);

        if (folder != null) {
            super.getParameters().put("filterType", determineFilterTypeByFolderType(folder.getType()));
            super.getParameters().put("filterValues", folder.getId());
        }
    }

    private String determineFilterTypeByFolderType(FolderType folderType) {
        if (folderType == FolderType.FOLDER) {
            return "folderId";
        } else {
            return "programId";
        }
    }

    @Override
    public String getPath() {
        return "/asset/v1/programs.json";
    }
}
