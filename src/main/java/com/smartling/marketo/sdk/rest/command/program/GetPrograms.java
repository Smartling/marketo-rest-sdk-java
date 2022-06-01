package com.smartling.marketo.sdk.rest.command.program;

import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.program.Program;
import com.smartling.marketo.sdk.rest.command.BasePagedGetCommand;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GetPrograms extends BasePagedGetCommand<Program> {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    static {
        FORMATTER.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
    }

    public GetPrograms(Integer offset, Integer limit, FolderId folder, Date earliestUpdatedAt, Date latestUpdatedAt) {
        super(Program.class, offset, limit);

        if (folder != null) {
            super.getParameters().put("filterType", determineFilterTypeByFolderType(folder.getType()));
            super.getParameters().put("filterValues", folder.getId());
        }

        if (earliestUpdatedAt != null) {
            super.getParameters().put("earliestUpdatedAt", FORMATTER.format(earliestUpdatedAt));
        }

        if (latestUpdatedAt != null) {
            super.getParameters().put("latestUpdatedAt", FORMATTER.format(latestUpdatedAt));
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
