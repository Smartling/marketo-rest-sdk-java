package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.token.Token;

import java.util.List;

public interface MarketoTokenClient {
    List<Token> getTokens(FolderId folder) throws MarketoApiException;

    void deleteToken(FolderId folder, String name, Token.Type type) throws MarketoApiException;

    Token createToken(FolderId folder, String name, Token.Type type, String value) throws MarketoApiException;
}
