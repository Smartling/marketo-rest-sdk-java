package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoLandingPageClient;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.landingpage.LandingPage;
import com.smartling.marketo.sdk.domain.landingpage.LandingPageContentItem;
import com.smartling.marketo.sdk.domain.landingpage.LandingPageTextContentItem;
import com.smartling.marketo.sdk.domain.landingpage.LandingPageVariable;
import com.smartling.marketo.sdk.rest.command.landingpage.CloneLandingPage;
import com.smartling.marketo.sdk.rest.command.landingpage.DiscardLandingPageDraft;
import com.smartling.marketo.sdk.rest.command.landingpage.GetLandingPageById;
import com.smartling.marketo.sdk.rest.command.landingpage.GetLandingPageContent;
import com.smartling.marketo.sdk.rest.command.landingpage.GetLandingPageVariables;
import com.smartling.marketo.sdk.rest.command.landingpage.GetLandingPages;
import com.smartling.marketo.sdk.rest.command.landingpage.GetLandingPagesByName;
import com.smartling.marketo.sdk.rest.command.landingpage.UpdateLandingPageEditableSection;
import com.smartling.marketo.sdk.rest.command.landingpage.UpdateLandingPageMetadata;
import com.smartling.marketo.sdk.rest.command.landingpage.UpdateLandingPageVariable;

import java.util.Collections;
import java.util.List;

public class MarketoLandingPageRestClient implements MarketoLandingPageClient {

    private final HttpCommandExecutor httpCommandExecutor;

    MarketoLandingPageRestClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    @Override
    public List<LandingPage> getLandingPages(int offset, int limit, FolderId folder, Status status) throws MarketoApiException {
        final List<LandingPage> landingPages = httpCommandExecutor.execute(new GetLandingPages(offset, limit, folder, status));
        return landingPages != null ? landingPages : Collections.emptyList();
    }

    @Override
    public LandingPage getLandingPageById(int id, Status status) throws MarketoApiException {
        List<LandingPage> execute = httpCommandExecutor.execute(new GetLandingPageById(id, status));
        if (execute != null && !execute.isEmpty()) {
            return execute.get(0);
        } else {
            throw new MarketoApiException(String.format("LandingPage[id = %d] not found", id));
        }
    }

    @Override
    public LandingPage getLandingPageById(int id) throws MarketoApiException {
        return getLandingPageById(id, null);
    }

    @Override
    public List<LandingPage> getLandingPagesByName(String name, FolderId folder, Status status) throws MarketoApiException {
        final List<LandingPage> emails = httpCommandExecutor.execute(new GetLandingPagesByName(name, folder, status));
        return emails != null ? emails : Collections.emptyList();
    }

    @Override
    public LandingPage cloneLandingPage(int sourceId, String newName, FolderId folder, int templateId) throws MarketoApiException {
        List<LandingPage> cloned = httpCommandExecutor.execute(new CloneLandingPage(sourceId, newName, folder, templateId));
        return cloned.get(0);
    }

    @Override
    public LandingPage cloneLandingPage(LandingPage sourceLandingPage, String newName) throws MarketoApiException {
        return cloneLandingPage(sourceLandingPage.getId(), newName, new FolderId(sourceLandingPage.getFolder()), sourceLandingPage.getTemplate());
    }

    @Override
    public List<LandingPageContentItem> getLandingPageContent(int id) throws MarketoApiException {
        return getLandingPageContent(id, null);
    }

    @Override
    public List<LandingPageContentItem> getLandingPageContent(int id, Status status) throws MarketoApiException {
        final List<LandingPageContentItem> contentItems = httpCommandExecutor.execute(new GetLandingPageContent(id, status));
        return contentItems != null ? contentItems : Collections.emptyList();
    }

    @Override
    public void updateLandingPageContent(int id, List<LandingPageTextContentItem> contentItems) throws MarketoApiException {
        for (LandingPageTextContentItem item : contentItems) {
            httpCommandExecutor.execute(new UpdateLandingPageEditableSection(id, item));
        }
    }

    @Override
    public void discardLandingPageDraft(int id) throws MarketoApiException {
        httpCommandExecutor.execute(new DiscardLandingPageDraft(id));
    }

    @Override
    public void updateLandingPageMetadata(int id, String title) throws MarketoApiException {
        httpCommandExecutor.execute(new UpdateLandingPageMetadata(id, title));
    }

    @Override
    public List<LandingPageVariable> getlandingPageVariables(int id) throws MarketoApiException {
        return this.getlandingPageVariables(id, null);
    }

    @Override
    public List<LandingPageVariable> getlandingPageVariables(int id, Status status) throws MarketoApiException {
        List<LandingPageVariable> variables = httpCommandExecutor.execute(new GetLandingPageVariables(id, status));
        return variables != null ? variables : Collections.emptyList();
    }

    @Override
    public LandingPageVariable updateLandingPageVariable(int pageId, LandingPageVariable variable) throws MarketoApiException {
        List<LandingPageVariable> updated = httpCommandExecutor.execute(new UpdateLandingPageVariable(pageId, variable));
        if (updated != null && !updated.isEmpty()) {
            return updated.get(0);
        } else {
            throw new MarketoApiException(String.format("Couldn't fetch updated LandingPageVariable for landingPageId=%d: %s", pageId, variable.toString()));
        }

    }
}
