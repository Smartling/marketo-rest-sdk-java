package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.landingpagetemplate.LandingPageTemplate;
import com.smartling.marketo.sdk.domain.landingpagetemplate.LandingPageTemplateContentItem;

import java.util.List;

public interface MarketoLandingPageTemplateClient {
    LandingPageTemplate getLandingPageTemplateById(int id, Status status) throws MarketoApiException;

    LandingPageTemplate getLandingPageTemplateById(int id) throws MarketoApiException;

    List<LandingPageTemplateContentItem> getLandingPageTemplateContent(int id) throws MarketoApiException;

    List<LandingPageTemplateContentItem> getLandingPageTemplateContent(int id, Status status) throws MarketoApiException;
}
