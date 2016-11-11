package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoTokenClient;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.token.Token;
import com.smartling.marketo.sdk.domain.token.TokenResult;
import com.smartling.marketo.sdk.rest.command.token.CreateToken;
import com.smartling.marketo.sdk.rest.command.token.DeleteToken;
import com.smartling.marketo.sdk.rest.command.token.GetTokens;
import com.smartling.marketo.sdk.rest.transport.HttpCommandExecutor;

import java.util.Collections;
import java.util.List;

public class MarketoTokenRestClient implements MarketoTokenClient {

    private final HttpCommandExecutor httpCommandExecutor;

    public MarketoTokenRestClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    @Override
    public List<Token> getTokens(FolderId folder) throws MarketoApiException {
        List<TokenResult> tokenResults = httpCommandExecutor.execute(new GetTokens(folder));
        List<Token> tokens = tokenResults.get(0).getTokens();
        return (tokens != null) ? tokens : Collections.emptyList();
    }

    @Override
    public void deleteToken(FolderId folder, String name, Token.Type type) throws MarketoApiException {
        httpCommandExecutor.execute(new DeleteToken(folder, name, type));
    }

    @Override
    public Token createToken(FolderId folder, String name, Token.Type type, String value) throws MarketoApiException {
        List<TokenResult> tokenResults = httpCommandExecutor.execute(new CreateToken(folder, name, type, value));

        return tokenResults.get(0).getTokens().get(0);
    }
}
