package com.smartling.marketo.sdk.rest.command.program;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.program.Program;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

import java.util.Map;

public class GetProgramsByName extends BaseGetCommand<Program> {
    private final String name;

    public GetProgramsByName(String name) {
        super(Program.class);
        this.name = name;
    }

    @Override
    public String getPath() {
        return "/asset/v1/program/byName.json";
    }

    @Override
    public Map<String, Object> getParameters() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
                .put("name", name);
        return builder.build();
    }
}
