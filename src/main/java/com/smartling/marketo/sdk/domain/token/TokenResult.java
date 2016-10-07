package com.smartling.marketo.sdk.domain.token;

import com.smartling.marketo.sdk.domain.folder.Folder;

import java.util.List;

public class TokenResult {
    private List<Token> tokens;
    private Folder folder;

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }
}
