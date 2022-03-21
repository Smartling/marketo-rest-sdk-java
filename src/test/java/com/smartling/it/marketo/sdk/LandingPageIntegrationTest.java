package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoLandingPageClient;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.landingpage.DynamicContent;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.landingpage.*;
import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static com.smartling.marketo.sdk.domain.landingpage.VariableType.BOOLEAN;
import static com.smartling.marketo.sdk.domain.landingpage.VariableType.STRING;

public class LandingPageIntegrationTest extends BaseIntegrationTest {
    private static final int TEST_LANDING_PAGE_ID = 5620;
    private static final int TEST_LANDING_PAGE_WITH_DYNAMIC_CONTENT_ID = 5631;
    private static final int TEST_GUIDED_LANDING_PAGE_ID = 3749;
    private static final int TEST_PROGRAM_LANDING_PAGE_ID = 1347;
    private static final int EMPTY_TEST_LANDING_PAGE_ID = 1049;
    private static final int TEST_LANDING_PAGE_TO_DESCARD_ID = 1065;
    private static final int TEST_LANDING_PAGE_WITH_DRAFT_ID = 1096;
    private static final String LANDING_PAGE_FOR_INTEGRATION_TESTS = "LP for Integration Tests without Social";
    private static final FolderId TEST_FOLDER_ID = new FolderId(124, FolderType.FOLDER);
    private static final int TEST_PROGRAM_ID = 1008;
    private static final int TEST_TEMPLATE_ID = 1;
    private static final String TEST_CONTENT_ITEM_ID = "28839";
    private static final String TEST_CONTENT_ITEM_TYPE = "RichText";
    private static final String TEST_LANDING_PAGE_DYNAMIC_CONTENT_ID = "RVMtMjk2MA==";

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private MarketoLandingPageClient marketoLandingPageClient;

    @Before
    public void setUp() {
        marketoLandingPageClient = marketoClientManager.getMarketoLandingPageClient();
    }

    @Test
    public void shouldListLandingPageDynamicContent() throws Exception
    {
        List<LandingPageContentItem> landingPageWithDynamicContent = marketoLandingPageClient.getLandingPageContent(TEST_LANDING_PAGE_WITH_DYNAMIC_CONTENT_ID);

        Optional<LandingPageContentItem> landingPageDynamicContentItemOptional = landingPageWithDynamicContent
                .stream()
                .filter(it -> it instanceof LandingPageDynamicContentItem)
                .findFirst();
        assertThat(landingPageDynamicContentItemOptional.isPresent()).isTrue();

        LandingPageDynamicContentItem landingPageDynamicContentItem = (LandingPageDynamicContentItem) landingPageDynamicContentItemOptional.get();
        assertThat(landingPageDynamicContentItem.getContent().getContent()).isEqualTo(TEST_LANDING_PAGE_DYNAMIC_CONTENT_ID);

        DynamicContent dynamicContent = marketoLandingPageClient.loadDynamicContentById(TEST_LANDING_PAGE_WITH_DYNAMIC_CONTENT_ID, TEST_LANDING_PAGE_DYNAMIC_CONTENT_ID);
        assertThat(dynamicContent).isNotNull();
        assertThat(dynamicContent.getSegments()).hasSize(3);
    }

    @Test
    public void shouldUpdateLandingPageDynamicContent() throws Exception
    {
        DynamicContent dynamicContent = marketoLandingPageClient.loadDynamicContentById(TEST_LANDING_PAGE_WITH_DYNAMIC_CONTENT_ID, TEST_LANDING_PAGE_DYNAMIC_CONTENT_ID);
        assertThat(dynamicContent).isNotNull();
        assertThat(dynamicContent.getSegments()).hasSize(3);

        DynamicContentItem segment = dynamicContent.getSegments().get(0);
        segment.setContent("test");

        marketoLandingPageClient.updateDynamicContent(TEST_LANDING_PAGE_WITH_DYNAMIC_CONTENT_ID, TEST_LANDING_PAGE_DYNAMIC_CONTENT_ID, Arrays.asList(segment));
    }

    @Test
    public void shouldListLandingPages() throws Exception {
        List<LandingPage> landingPages = marketoLandingPageClient.getLandingPages(0, 1, null, null);

        assertThat(landingPages).hasSize(1);
        assertThat(landingPages.get(0).getId()).isPositive();
        assertThat(landingPages.get(0).getName()).isNotEmpty();
        assertThat(landingPages.get(0).getUpdatedAt()).isNotNull();
        assertThat(landingPages.get(0).getStatus()).isNotNull();
        assertThat(landingPages.get(0).getUrl()).isNotEmpty();
        assertThat(landingPages.get(0).getFolder()).isNotNull();
    }

    @Test
    public void shouldListLandingPagesWithFilter() throws Exception {
        List<LandingPage> landingPages = marketoLandingPageClient.getLandingPages(0, 1, TEST_FOLDER_ID, Status.APPROVED);

        assertThat(landingPages).hasSize(1);
        assertThat(landingPages.get(0).getId()).isPositive();
        assertThat(landingPages.get(0).getName()).isNotEmpty();
        assertThat(landingPages.get(0).getUpdatedAt()).isNotNull();
        assertThat(landingPages.get(0).getStatus()).isNotNull();
        assertThat(landingPages.get(0).getUrl()).isNotEmpty();
        assertThat(landingPages.get(0).getFolder()).isNotNull();
    }

    @Test
    public void shouldReturnEmptyListWhenEndReached() throws Exception {
        List<LandingPage> landingPages = marketoLandingPageClient.getLandingPages(10000, 1, null, null);

        assertThat(landingPages).isEmpty();
    }

    @Test(expected = MarketoApiException.class)
    public void shouldThrowLogicException() throws Exception {
        marketoLandingPageClient.getLandingPages(-5, 5, null, null);
    }

    @Test
    public void shouldGetLandingPageById() throws Exception {
        LandingPage landingPage = marketoLandingPageClient.getLandingPageById(TEST_LANDING_PAGE_ID);

        assertThat(landingPage).isNotNull();
        assertThat(landingPage.getId()).isEqualTo(TEST_LANDING_PAGE_ID);
        assertThat(landingPage.getName()).isNotEmpty();
        assertThat(landingPage.getUrl()).isNotEmpty();
        assertThat(landingPage.getFolder()).isNotNull();
    }

    @Test
    public void shouldGetLandingPageByIdAndStatus() throws Exception {
        LandingPage landingPage = marketoLandingPageClient.getLandingPageById(TEST_LANDING_PAGE_WITH_DRAFT_ID, Status.DRAFT);

        assertThat(landingPage).isNotNull();
        assertThat(landingPage.getId()).isEqualTo(TEST_LANDING_PAGE_WITH_DRAFT_ID);
        assertThat(landingPage.getStatus()).isEqualTo(Status.DRAFT);
    }

    @Test
    public void shouldThrowMarketoApiExceptionWhenCouldNotFindLandingPageById() throws Exception {

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("LandingPage[id = 42] not found");

        marketoLandingPageClient.getLandingPageById(42);
    }

    @Test
    public void shouldGetLandingPagesByName() throws Exception {
        List<LandingPage> landingPages = marketoLandingPageClient.getLandingPagesByName(LANDING_PAGE_FOR_INTEGRATION_TESTS, null, null);

        assertThat(landingPages).haveAtLeast(1, new EntityWithName(LANDING_PAGE_FOR_INTEGRATION_TESTS));
    }

    @Test
    public void shouldGetLandingPagesByNameWithFolder() throws Exception {
        List<LandingPage> landingPages = marketoLandingPageClient
                .getLandingPagesByName(LANDING_PAGE_FOR_INTEGRATION_TESTS, TEST_FOLDER_ID, null);

        assertThat(landingPages).hasSize(1);
        assertThat(landingPages).haveExactly(1, new EntityWithNameAndFolderId(LANDING_PAGE_FOR_INTEGRATION_TESTS, TEST_FOLDER_ID));
    }

    @Test
    public void shouldGetLandingPagesByNameWithStatus() throws Exception {
        List<LandingPage> landingPages = marketoLandingPageClient
                .getLandingPagesByName(LANDING_PAGE_FOR_INTEGRATION_TESTS, null, Status.APPROVED);

        assertThat(landingPages).haveAtLeast(1, new AssetWithNameAndStatus(LANDING_PAGE_FOR_INTEGRATION_TESTS, Status.APPROVED));
    }

    @Test
    public void shouldReadLandingPageContent() throws Exception {
        List<LandingPageContentItem> contentItems = marketoLandingPageClient.getLandingPageContent(TEST_LANDING_PAGE_ID);

        assertThat(contentItems).hasSize(6);
        assertThat(contentItems.get(5)).isInstanceOf(LandingPageTextContentItem.class);
        LandingPageTextContentItem textContentItem = (LandingPageTextContentItem) contentItems.get(5);
        assertThat(textContentItem.getId()).isEqualTo(TEST_CONTENT_ITEM_ID);
        assertThat(textContentItem.getContent()).contains("Rich Text content for Integration Tests");
        assertThat(textContentItem.getType()).isEqualTo("RichText");
    }

    @Test
    public void shouldReadEmptyLandingPageContent() throws Exception {
        List<LandingPageContentItem> contentItems = marketoLandingPageClient.getLandingPageContent(EMPTY_TEST_LANDING_PAGE_ID);

        assertThat(contentItems).hasSize(0);
    }

    @Test
    public void shouldCloneLandingPage() throws Exception {
        String newLandingPageName = "integration-test-clone-" + UUID.randomUUID().toString();

        LandingPage clone = marketoLandingPageClient
                .cloneLandingPage(TEST_LANDING_PAGE_ID, newLandingPageName, TEST_FOLDER_ID, TEST_TEMPLATE_ID);

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newLandingPageName);
        assertThat(new FolderId(clone.getFolder())).isEqualTo(TEST_FOLDER_ID);
    }

    @Test
    public void shouldCloneLandingPageInProgram() throws Exception {
        String newLandingPageName = "integration-test-clone-" + UUID.randomUUID().toString();

        LandingPage clone = marketoLandingPageClient
                .cloneLandingPage(TEST_PROGRAM_LANDING_PAGE_ID, newLandingPageName, new FolderId(TEST_PROGRAM_ID, FolderType.PROGRAM),
                        TEST_TEMPLATE_ID);

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newLandingPageName);
        assertThat(clone.getFolder().getValue()).isEqualTo(TEST_PROGRAM_ID);
    }

    @Test
    public void shouldCloneLandingPageViaShorthandMethod() throws Exception {
        String newLandingPageName = "integration-test-clone-" + UUID.randomUUID().toString();
        LandingPage existingLandingPage = marketoLandingPageClient.getLandingPageById(TEST_LANDING_PAGE_ID);

        LandingPage clone = marketoLandingPageClient.cloneLandingPage(existingLandingPage, newLandingPageName);

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newLandingPageName);
        assertThat(clone.getFolder().getValue()).isEqualTo(existingLandingPage.getFolder().getValue());
    }

    @Test
    public void shouldUpdateLandingPageContent() throws Exception {
        discardOldDraft(TEST_LANDING_PAGE_ID);
        marketoLandingPageClient.updateLandingPageMetadata(TEST_LANDING_PAGE_ID, "title");
        LandingPageTextContentItem newItem = (LandingPageTextContentItem) getFirstItemFromDraft(TEST_LANDING_PAGE_ID, TEST_CONTENT_ITEM_TYPE);
        newItem.setContent("New content: " + UUID.randomUUID());

        marketoLandingPageClient.updateLandingPageContent(TEST_LANDING_PAGE_ID, Collections.singletonList(newItem));

        List<LandingPageContentItem> content = marketoLandingPageClient.getLandingPageContent(TEST_LANDING_PAGE_ID, Status.DRAFT);
        assertThat(content).haveExactly(1, new ContentItemWithIdAndText(newItem.getId(), newItem.getContent()));
    }

    @Test
    public void shouldDiscardLandingPageDraft() throws Exception {
        marketoLandingPageClient.updateLandingPageMetadata(TEST_LANDING_PAGE_TO_DESCARD_ID, "title");

        marketoLandingPageClient.discardLandingPageDraft(TEST_LANDING_PAGE_TO_DESCARD_ID);

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("LandingPage[id = " + TEST_LANDING_PAGE_TO_DESCARD_ID + "] not found");
        marketoLandingPageClient.getLandingPageById(TEST_LANDING_PAGE_TO_DESCARD_ID, Status.DRAFT);

    }

    @Test
    public void shouldUpdateLandingPageMetadata() throws Exception {
        String title = "title" + UUID.randomUUID();

        marketoLandingPageClient.updateLandingPageMetadata(TEST_LANDING_PAGE_ID, title);

        LandingPage page = marketoLandingPageClient.getLandingPageById(TEST_LANDING_PAGE_ID, Status.DRAFT);

        assertThat(page.getTitle()).isEqualTo(title);
    }

    @Test
    public void shouldGetLandingPageVariables() throws Exception {
        List<LandingPageVariable> variables = marketoLandingPageClient.getLandingPageVariables(TEST_GUIDED_LANDING_PAGE_ID);

        assertThat(variables).hasSize(11);
        assertThat(variables.get(1).getId()).isEqualTo("ShowWhiteLogo");
        assertThat(variables.get(1).getType()).isEqualTo(BOOLEAN);
        assertThat(variables.get(1).getValue()).isEqualTo("false");
    }

    @Test
    public void shouldGetDraftLandingPageVariables() throws Exception {
        List<LandingPageVariable> variables = marketoLandingPageClient.getLandingPageVariables(TEST_GUIDED_LANDING_PAGE_ID, Status.DRAFT);

        assertThat(variables).hasSize(11);
        assertThat(variables.get(4).getId()).isEqualTo("headerBg");
        assertThat(variables.get(4).getType()).isEqualTo(STRING);
        assertThat(variables.get(4).getValue()).isEqualTo("draft content");
    }

    @Test
    public void shouldUpdateLandingPageVariable() throws Exception {
        LandingPageVariable variable = new LandingPageVariable();
        variable.setId("MainBg");
        variable.setType(STRING);
        variable.setValue(UUID.randomUUID().toString());

        marketoLandingPageClient.updateLandingPageVariable(TEST_GUIDED_LANDING_PAGE_ID, variable);
    }

    private LandingPageContentItem getFirstItemFromDraft(int testLandingPageId, String testContentItemType) throws MarketoApiException {
        List<LandingPageContentItem> contentItems = marketoLandingPageClient.getLandingPageContent(testLandingPageId, Status.DRAFT);
        return contentItems.stream().filter(i -> i.getType().equals(testContentItemType)).findFirst().get();
    }

    private void discardOldDraft(int landingPageId) {
        try {
            marketoLandingPageClient.discardLandingPageDraft(landingPageId);
        } catch (MarketoApiException e) {
            // LP may not have a draft
        }
    }

    private class ContentItemWithIdAndText extends Condition<LandingPageContentItem> {
        private final String id;
        private final String text;

        ContentItemWithIdAndText(String id, String text) {
            this.id = id;
            this.text = text;
        }

        @Override
        public boolean matches(LandingPageContentItem item) {
            if (!(item instanceof LandingPageTextContentItem)) {
                return false;
            }
            LandingPageTextContentItem textItem = (LandingPageTextContentItem) item;

            return textItem.getId().equals(id) && textItem.getContent().equals(text);
        }
    }
}
