package com.smartling.marketo.sdk.rest.command.token;

import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.token.TokenResult;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

import java.util.Collections;
import java.util.Map;

public class GetTokens extends BaseGetCommand<TokenResult> {

    private FolderId folder;

    public GetTokens(FolderId folder) {
        super(TokenResult.class);
        this.folder = folder;
    }

    @Override
    public String getPath() {
        return "/asset/v1/folder/" + folder.getId() + "/tokens.json";
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.singletonMap("folderType", folder.getType());
    }
}
