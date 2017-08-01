package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.token.Token;
import com.smartling.marketo.sdk.domain.token.TokenResult;
import com.smartling.marketo.sdk.rest.command.token.CreateToken;
import com.smartling.marketo.sdk.rest.command.token.GetTokens;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;

@RunWith(MockitoJUnitRunner.class)
public class MarketoTokenRestClientTest {

    @Mock
    private HttpCommandExecutor executor;

    @InjectMocks
    private MarketoTokenRestClient testedInstance;

    @Test
    public void shouldRequestTokensList() throws Exception {
        Token token = new Token();
        given(executor.execute(isA(GetTokens.class))).willReturn(Collections.singletonList(tokenResult(token)));

        List<Token> tokens = testedInstance.getTokens(new FolderId(1, FolderType.FOLDER));

        assertThat(tokens).contains(token);
    }

    private TokenResult tokenResult(Token token) {
        TokenResult tokenResult = new TokenResult();
        tokenResult.setTokens(Collections.singletonList(token));
        return tokenResult;
    }

    @Test
    public void shouldCreateToken() throws Exception {
        Token token = new Token();
        given(executor.execute(isA(CreateToken.class))).willReturn(Collections.singletonList(tokenResult(token)));

        Token result = testedInstance.createToken(new FolderId(999, FolderType.FOLDER), "name", Token.Type.TEXT, "value");

        assertThat(result).isSameAs(token);
    }

    @Test
    public void shouldDeleteToken() throws Exception {
        testedInstance.deleteToken(new FolderId(999, FolderType.FOLDER), "name", Token.Type.TEXT);
    }
}