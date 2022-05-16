package com.smartling.marketo.sdk.rest.command.program;

import com.smartling.marketo.sdk.domain.program.Program;
import com.smartling.marketo.sdk.rest.command.BasePagedGetCommand;

public class GetProgramsByName extends BasePagedGetCommand<Program> {

    public GetProgramsByName(Integer offset, Integer limit, String name) {
        super(Program.class, offset, limit);

        super.getParameters().put("name", name);
    }

    @Override
    public String getPath() {
        return "/asset/v1/program/byName.json";
    }
}
