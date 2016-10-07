package com.smartling.marketo.sdk.rest.command.program;

import com.smartling.marketo.sdk.domain.program.Program;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;
import jersey.repackaged.com.google.common.collect.ImmutableMap;

import java.util.Map;

public class CloneProgram extends BaseMarketoCommand<Program> {
    private final int sourceProgramId;
    private final String newProgramName;
    private final FolderId folderId;

    public CloneProgram(int sourceProgramId, String newProgramName, FolderId folderId) {
        super(Program.class);
        this.sourceProgramId = sourceProgramId;
        this.newProgramName = newProgramName;
        this.folderId = folderId;
    }

    @Override
    public String getPath() {
        return "/asset/v1/program/" + sourceProgramId + "/clone.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        return ImmutableMap.of("name", newProgramName, "folder", folderId);
    }
}
