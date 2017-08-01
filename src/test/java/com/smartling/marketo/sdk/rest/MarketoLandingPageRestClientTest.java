package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.landingpage.LandingPage;
import com.smartling.marketo.sdk.domain.landingpage.LandingPageContentItem;
import com.smartling.marketo.sdk.domain.landingpage.LandingPageTextContentItem;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.rest.command.landingpage.CloneLandingPage;
import com.smartling.marketo.sdk.rest.command.landingpage.DiscardLandingPageDraft;
import com.smartling.marketo.sdk.rest.command.landingpage.GetLandingPagesByName;
import com.smartling.marketo.sdk.rest.command.landingpage.GetLandingPages;
import com.smartling.marketo.sdk.rest.command.landingpage.GetLandingPageById;
import com.smartling.marketo.sdk.rest.command.landingpage.GetLandingPageContent;
import com.smartling.marketo.sdk.rest.command.landingpage.UpdateLandingPageEditableSection;
import com.smartling.marketo.sdk.rest.command.landingpage.UpdateLandingPageMetadata;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MarketoLandingPageRestClientTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Mock
    private HttpCommandExecutor executor;

    @InjectMocks
    private MarketoLandingPageRestClient testedInstance;

    @Test
    public void shouldRequestLandingPageList() throws Exception {
        LandingPage landingPage = new LandingPage();
        given(executor.execute(isA(GetLandingPages.class))).willReturn(Collections.singletonList(landingPage));

        List<LandingPage> landingPages = testedInstance.getLandingPages(0, 10, null, null);

        assertThat(landingPages).contains(landingPage);
    }

    @Test
    public void shouldRequestLandingPageListWithFilter() throws Exception {
        LandingPage landingPage = new LandingPage();
        given(executor.execute(isA(GetLandingPages.class))).willReturn(Collections.singletonList(landingPage));

        List<LandingPage> landingPages = testedInstance.getLandingPages(0, 10, new FolderId(1, FolderType.FOLDER), Status.APPROVED);

        assertThat(landingPages).contains(landingPage);
    }

    @Test
    public void shouldReturnEmptyLandingPageListOnNullResult() throws Exception {
        given(executor.execute(any(Command.class))).willReturn(null);

        List<LandingPage> landingPages = testedInstance.getLandingPages(0, 10, new FolderId(1, FolderType.FOLDER), Status.APPROVED);

        assertThat(landingPages).isEmpty();
    }

    @Test
    public void shouldReturnEmptyLandingPageListByNameOnNullResult() throws Exception {
        given(executor.execute(any(Command.class))).willReturn(null);

        List<LandingPage> landingPages = testedInstance.getLandingPagesByName("name", new FolderId(1, FolderType.FOLDER), Status.APPROVED);

        assertThat(landingPages).isEmpty();
    }

    @Test(expected = MarketoApiException.class)
    public void shouldRethrowApiErrorsDifferentFromPaginationIssues() throws Exception {
        given(executor.execute(any(Command.class))).willThrow(new MarketoApiException("100", ""));

        testedInstance.getLandingPages(0, 10, new FolderId(1, FolderType.FOLDER), Status.APPROVED);
    }

    @Test
    public void shouldGetLandingPageById() throws Exception {
        LandingPage landingPage = new LandingPage();
        given(executor.execute(isA(GetLandingPageById.class))).willReturn(Collections.singletonList(landingPage));

        LandingPage result = testedInstance.getLandingPageById(42);

        assertThat(result).isEqualTo(landingPage);
    }

    @Test
    public void shouldGetLandingPageByIdAndStatus() throws Exception {
        LandingPage landingPage = new LandingPage();
        given(executor.execute(isA(GetLandingPageById.class))).willReturn(Collections.singletonList(landingPage));

        LandingPage result = testedInstance.getLandingPageById(42, Status.DRAFT);

        assertThat(result).isEqualTo(landingPage);
    }

    @Test
    public void shouldThrowMarketoApiExceptionIfLandingPageNotFound() throws Exception
    {
        int nonExistingId = 42;

        given(executor.execute(isA(GetLandingPageById.class))).willReturn(null);

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("LandingPage[id = 42] not found");

        testedInstance.getLandingPageById(nonExistingId);
    }

    @Test
    public void shouldGetLandingPagesByName() throws Exception {
        LandingPage expected = new LandingPage();
        given(executor.execute(isA(GetLandingPagesByName.class))).willReturn(Collections.singletonList(expected));

        List<LandingPage> result = testedInstance.getLandingPagesByName("name", new FolderId(42, FolderType.FOLDER), Status.APPROVED);

        assertThat(result.get(0)).isEqualTo(expected);
    }

    @Test
    public void shouldGetLandingPageContent() throws Exception {
        LandingPageContentItem contentItem = new LandingPageTextContentItem();
        given(executor.execute(isA(GetLandingPageContent.class))).willReturn(Collections.singletonList(contentItem));

        List<LandingPageContentItem> result = testedInstance.getLandingPageContent(42);

        assertThat(result).contains(contentItem);
    }

    @Test
    public void shouldCloneLandingPage() throws Exception {
        LandingPage clone = new LandingPage();
        given(executor.execute(isA(CloneLandingPage.class))).willReturn(Collections.singletonList(clone));

        LandingPage result = testedInstance.cloneLandingPage(42, "blah", new FolderId(999, FolderType.FOLDER), 1);

        assertThat(result).isSameAs(clone);
    }

    @Test
    public void shouldCloneExistingLandingPage() throws Exception {
        LandingPage clone = new LandingPage();
        given(executor.execute(isA(CloneLandingPage.class))).willReturn(Collections.singletonList(clone));

        LandingPage result = testedInstance.cloneLandingPage(new LandingPage(), "blah");

        assertThat(result).isSameAs(clone);
    }

    @Test
    public void shouldUpdateLandingPageContent() throws Exception {
        testedInstance.updateLandingPageContent(42, Arrays.asList(new LandingPageTextContentItem(), new LandingPageTextContentItem()));

        verify(executor, times(2)).execute(isA(UpdateLandingPageEditableSection.class));
    }

    @Test
    public void shouldDiscardLandingPage() throws Exception {
        testedInstance.discardLandingPageDraft(42);

        verify(executor).execute(isA(DiscardLandingPageDraft.class));
    }

    @Test
    public void shouldUpdateLandingPageMetadata() throws Exception {
        testedInstance.updateLandingPageMetadata(42, "title");

        verify(executor).execute(isA(UpdateLandingPageMetadata.class));
    }
}