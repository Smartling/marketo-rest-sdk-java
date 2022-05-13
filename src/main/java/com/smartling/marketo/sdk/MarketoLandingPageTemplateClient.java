package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.landingpagetemplate.LandingPageTemplate;
import com.smartling.marketo.sdk.domain.landingpagetemplate.LandingPageTemplateContent;

import java.util.List;

public interface MarketoLandingPageTemplateClient {
    List<LandingPageTemplate> getLandingPageTemplates(Integer offset, Integer limit, FolderId folder, Status status) throws MarketoApiException;

    LandingPageTemplate getLandingPageTemplateById(int id, Status status) throws MarketoApiException;

    LandingPageTemplate getLandingPageTemplateById(int id) throws MarketoApiException;

    List<LandingPageTemplateContent> getLandingPageTemplateContent(int id) throws MarketoApiException;

    List<LandingPageTemplateContent> getLandingPageTemplateContent(int id, Status status) throws MarketoApiException;
}
