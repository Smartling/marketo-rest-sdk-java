package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoTokenClient;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.token.Token;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class TokenIntegrationTest extends BaseIntegrationTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private MarketoTokenClient marketoTokenClient;

    @Before
    public void setUp() {
        marketoTokenClient = marketoClientManager.getMarketoTokenClient();
    }

    @Test
    public void shouldGetTokens() throws Exception {
        List<Token> tokens = marketoTokenClient.getTokens(new FolderId(1038, FolderType.PROGRAM));

        assertThat(tokens).hasSize(3);
    }

    @Test
    public void shouldReturnEmptyList() throws Exception {
        List<Token> tokens = marketoTokenClient.getTokens(new FolderId(1039, FolderType.PROGRAM));

        assertThat(tokens).isEmpty();
    }

    @Test
    public void shouldThrowMarketoApiExceptionIfFolderNotFound() throws Exception {
        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("10390 Folder  not found");

        marketoTokenClient.getTokens(new FolderId(10390, FolderType.FOLDER));
    }

    @Test
    public void shouldCreateToken() throws Exception {
        List<Token> tokens = marketoTokenClient.getTokens(new FolderId(1039, FolderType.PROGRAM));

        assertThat(tokens).isEmpty();
    }

    @Test
    public void shouldDeleteToken() throws Exception {
        List<Token> tokens = marketoTokenClient.getTokens(new FolderId(1039, FolderType.PROGRAM));

        assertThat(tokens).isEmpty();
    }
}
