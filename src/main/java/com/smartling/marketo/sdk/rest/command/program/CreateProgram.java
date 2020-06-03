package com.smartling.marketo.sdk.rest.command.program;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.program.Program;
import com.smartling.marketo.sdk.domain.program.Tag;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.List;
import java.util.Map;

public class CreateProgram extends BaseMarketoCommand<Program> {
    private FolderId parent;
    private List<Tag> tags;
    private String name;
    private String channel;
    private String status;
    private String description;
    private String type;

    public CreateProgram(Program program) {
        super(Program.class);
        this.name = program.getName();
        this.parent = new FolderId(program.getFolder());
        this.description = program.getDescription();
        this.type = program.getType();
        this.channel = program.getChannel();
        this.status = program.getStatus();
        this.tags = program.getTags();
    }

    @Override
    public String getPath() {
        return "/asset/v1/programs.json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, Object> getParameters() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
                .put("name", name)
                .put("folder", parent)
                .put("description", description)
                .put("type", type)
                .put("channel", channel)
                .put("status", status)
                .put("tags", tags);
        return builder.build();
    }
}
