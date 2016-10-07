package com.smartling.marketo.sdk.rest.command.token;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.token.Token;
import com.smartling.marketo.sdk.rest.command.BaseMarketoCommand;

import java.util.Map;

public class CreateToken extends BaseMarketoCommand<Token> {
    private final FolderId folder;
    private final String name;
    private final Token.Type type;
    private final String value;

    public CreateToken(FolderId folder, String name, Token.Type type, String value) {
        super(Token.class);
        this.folder = folder;
        this.name = name;
        this.type = type;
        this.value = value;
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public String getPath() {
        return "/asset/v1/folder/" + folder.getId() + "/tokens.json";
    }

    @Override
    public Map<String, Object> getParameters() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
                .put("folderType", folder.getType())
                .put("name", name)
                .put("type", type)
                .put("value", value);

        return builder.build();
    }
}
