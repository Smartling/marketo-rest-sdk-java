package com.smartling.marketo.sdk.rest.command.program;

import com.smartling.marketo.sdk.domain.program.Program;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

public class GetProgramById extends BaseGetCommand<Program> {
    private final int id;

    public GetProgramById(int id) {
        super(Program.class);
        this.id = id;
    }

    @Override
    public String getPath() {
        return "/asset/v1/program/" + id + ".json";
    }
}
