package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoTokenClient;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.token.Token;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TokenIntegrationTest extends BaseIntegrationTest {

    private static final String TOKEN_NAME = "IntegrationTestToken";
    private static final String TEXT_TOKEN_VALUE = "new text token value";
    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private MarketoTokenClient marketoTokenClient;

    @Before
    public void setUp() {
        marketoTokenClient = marketoClientManager.getMarketoTokenClient();
    }

    @Test
    public void shouldGetTokens() throws Exception {
        List<Token> tokens = marketoTokenClient.getTokens(new FolderId(1008, FolderType.PROGRAM));

        assertThat(tokens).isNotEmpty();
    }

    @Test
    public void shouldReturnEmptyList() throws Exception {
        List<Token> tokens = marketoTokenClient.getTokens(new FolderId(1039, FolderType.PROGRAM));

        assertThat(tokens).isEmpty();
    }

    @Test
    public void shouldThrowMarketoApiExceptionIfFolderNotFound() throws Exception {
        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("Folder not found");
        thrown.expectMessage("10390");

        marketoTokenClient.getTokens(new FolderId(10390, FolderType.FOLDER));
    }

    @Test
    public void shouldCreateToken() throws Exception {
        Token token = marketoTokenClient.createToken(new FolderId(1008, FolderType.PROGRAM), TOKEN_NAME, Token.Type.TEXT, TEXT_TOKEN_VALUE);

        assertThat(token.getValue()).isEqualTo(TEXT_TOKEN_VALUE);
    }

    @Test
    public void shouldDeleteToken() throws Exception {
        marketoTokenClient.deleteToken(new FolderId(1008, FolderType.PROGRAM), TOKEN_NAME, Token.Type.TEXT);
    }
}
