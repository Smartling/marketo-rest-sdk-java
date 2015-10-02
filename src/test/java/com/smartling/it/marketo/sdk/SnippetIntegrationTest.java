package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.Asset;
import com.smartling.marketo.sdk.SnippetContentItem;
import com.smartling.marketo.sdk.Snippet;
import com.smartling.marketo.sdk.FolderId;
import com.smartling.marketo.sdk.FolderType;
import com.smartling.marketo.sdk.MarketoApiException;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.fest.assertions.api.Assertions.assertThat;

public class SnippetIntegrationTest extends BaseIntegrationTest {

    private static final int TEST_SNIPPET_ID = 4;
    private static final FolderId TEST_FOLDER_ID = new FolderId(93, FolderType.FOLDER);

    @Test
    public void shouldListSnippet() throws Exception {
        List<Snippet> snippets = marketoClient.listSnippets(0, 1, Asset.Status.APPROVED);

        assertThat(snippets).hasSize(1);
        assertThat(snippets.get(0).getId()).isPositive();
        assertThat(snippets.get(0).getName()).isNotEmpty();
        assertThat(snippets.get(0).getUpdatedAt()).isNotNull();
        assertThat(snippets.get(0).getStatus()).isNotNull();
        assertThat(snippets.get(0).getUrl()).isNotEmpty();
        assertThat(snippets.get(0).getFolder()).isNotNull();
    }

    @Test
    public void shouldReturnEmptyList() throws Exception {
        List<Snippet> snippets = marketoClient.listSnippets(10000, 1, Asset.Status.APPROVED);

        assertThat(snippets).hasSize(0);
    }

    @Test(expected = MarketoApiException.class)
    public void shouldThrowLogicException() throws Exception {
        marketoClient.listSnippets(-5, 5, null);
    }

    @Test
    public void shouldLoadSnippetById() throws Exception {
        Snippet snippet = marketoClient.loadSnippetById(TEST_SNIPPET_ID);

        assertThat(snippet).isNotNull();
        assertThat(snippet.getId()).isEqualTo(TEST_SNIPPET_ID);
        assertThat(snippet.getName()).isNotEmpty();
        assertThat(snippet.getUrl()).isNotEmpty();
        assertThat(snippet.getFolder()).isNotNull();
    }

    @Test
    public void shouldReadSnippetContent() throws Exception {
        List<SnippetContentItem> contentItems = marketoClient.loadSnippetContent(TEST_SNIPPET_ID);

        assertThat(contentItems).hasSize(2);
        assertThat(contentItems.get(0).getType()).isEqualTo("HTML");
        assertThat(contentItems.get(0).getContent()).isNotEmpty();
        assertThat(contentItems.get(1).getType()).isEqualTo("Text");
        assertThat(contentItems.get(1).getContent()).isNotEmpty();
    }

    @Test
    public void shouldCloneSnippet() throws Exception {
        String newSnippetName = "integration-test-clone-" + UUID.randomUUID().toString();

        Snippet clone = marketoClient.cloneSnippet(TEST_SNIPPET_ID, newSnippetName, TEST_FOLDER_ID);

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newSnippetName);
        assertThat(new FolderId(clone.getFolder())).isEqualTo(TEST_FOLDER_ID);
    }

    @Test
    public void shouldUpdateSnippetHtmlContent() throws Exception {
        SnippetContentItem newItem = new SnippetContentItem();
        newItem.setType("HTML");
        newItem.setContent("<strong>" + UUID.randomUUID() + "<strong>");

        marketoClient.updateSnippetContent(TEST_SNIPPET_ID, newItem);

        // Can not verify - no way to fetch not approved content
    }

    @Test
    public void shouldUpdateSnippetTextContent() throws Exception {
        SnippetContentItem newItem = new SnippetContentItem();
        newItem.setType("Text");
        newItem.setContent(UUID.randomUUID().toString());

        marketoClient.updateSnippetContent(TEST_SNIPPET_ID, newItem);

        // Can not verify - no way to fetch not approved content
    }
}
