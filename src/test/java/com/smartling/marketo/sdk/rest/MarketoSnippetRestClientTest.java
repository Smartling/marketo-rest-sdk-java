package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.domain.Asset;
import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.snippet.Snippet;
import com.smartling.marketo.sdk.domain.snippet.SnippetContentItem;
import com.smartling.marketo.sdk.rest.command.snippet.CloneSnippet;
import com.smartling.marketo.sdk.rest.command.snippet.GetSnippets;
import com.smartling.marketo.sdk.rest.command.snippet.LoadSnippetById;
import com.smartling.marketo.sdk.rest.command.snippet.LoadSnippetContent;
import com.smartling.marketo.sdk.rest.command.snippet.UpdateSnippetContent;
import com.smartling.marketo.sdk.rest.transport.HttpCommandExecutor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MarketoSnippetRestClientTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Mock
    private HttpCommandExecutor executor;

    @InjectMocks
    private MarketoSnippetRestClient testedInstance;

    @Test
    public void shouldRequestSnippetListWithFilter() throws Exception {
        Snippet snippet= new Snippet();
        given(executor.execute(isA(GetSnippets.class))).willReturn(Collections.singletonList(snippet));

        List<Snippet> snippets = testedInstance.listSnippets(0, 10, Asset.Status.APPROVED);

        assertThat(snippets).contains(snippet);
    }

    @Test
    public void shouldLoadSnippetById() throws Exception {
        Snippet snippet = new Snippet();
        given(executor.execute(isA(LoadSnippetById.class))).willReturn(Collections.singletonList(snippet));

        Snippet result = testedInstance.loadSnippetById(42);

        assertThat(result).isEqualTo(snippet);
    }

    @Test
    public void shouldLoadSnippetContent() throws Exception {
        SnippetContentItem contentItem = new SnippetContentItem();
        given(executor.execute(isA(LoadSnippetContent.class))).willReturn(Collections.singletonList(contentItem));

        List<SnippetContentItem> result = testedInstance.loadSnippetContent(42);

        assertThat(result).contains(contentItem);
    }

    @Test
    public void shouldCloneSnippet() throws Exception {
        Snippet clone = new Snippet();
        given(executor.execute(isA(CloneSnippet.class))).willReturn(Collections.singletonList(clone));

        Snippet result = testedInstance.cloneSnippet(42, "blah", new FolderId(999, FolderType.FOLDER));

        assertThat(result).isSameAs(clone);
    }

    @Test
    public void shouldUpdateSnippetContent() throws Exception {
        testedInstance.updateSnippetContent(42, new SnippetContentItem());

        verify(executor).execute(isA(UpdateSnippetContent.class));
    }

    @Test
    public void shouldReturnEmptySnippetListOnNullResult() throws Exception {
        given(executor.execute(any(Command.class))).willReturn(null);

        List<Snippet> snippets = testedInstance.listSnippets(0, 10, Email.Status.APPROVED);

        assertThat(snippets).isEmpty();
    }
}