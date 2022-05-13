package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoLandingPageTemplateClient;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.landingpagetemplate.LandingPageTemplate;
import com.smartling.marketo.sdk.domain.landingpagetemplate.LandingPageTemplateContent;
import com.smartling.marketo.sdk.rest.command.landingpagetemplate.GetLandingPageTemplates;
import com.smartling.marketo.sdk.rest.command.landingpagetemplate.GetLandingPageTemplateById;
import com.smartling.marketo.sdk.rest.command.landingpagetemplate.GetLandingPageTemplateContent;

import java.util.Collections;
import java.util.List;

public class MarketoLandingPageTemplateRestClient implements MarketoLandingPageTemplateClient {

    private final HttpCommandExecutor httpCommandExecutor;

    MarketoLandingPageTemplateRestClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    @Override
    public List<LandingPageTemplate> getLandingPageTemplates(Integer offset, Integer limit, FolderId folder, Status status) throws MarketoApiException {
        final List<LandingPageTemplate> landingPageTemplates = httpCommandExecutor.execute(new GetLandingPageTemplates(offset, limit, folder, status));
        return landingPageTemplates != null ? landingPageTemplates : Collections.emptyList();
    }

    @Override
    public LandingPageTemplate getLandingPageTemplateById(int id, Status status) throws MarketoApiException {
        List<LandingPageTemplate> execute = httpCommandExecutor.execute(new GetLandingPageTemplateById(id, status));
        if (execute != null && !execute.isEmpty()) {
            return execute.get(0);
        } else {
            throw new ObjectNotFoundException(String.format("LandingPageTemplate[id = %d] not found", id));
        }
    }

    @Override
    public LandingPageTemplate getLandingPageTemplateById(int id) throws MarketoApiException {
        return getLandingPageTemplateById(id, null);
    }

    @Override
    public List<LandingPageTemplateContent> getLandingPageTemplateContent(int id) throws MarketoApiException {
        return getLandingPageTemplateContent(id, null);
    }

    @Override
    public List<LandingPageTemplateContent> getLandingPageTemplateContent(int id, Status status) throws MarketoApiException {
        final List<LandingPageTemplateContent> contentItems = httpCommandExecutor.execute(new GetLandingPageTemplateContent(id, status));
        return contentItems != null ? contentItems : Collections.emptyList();
    }
}
