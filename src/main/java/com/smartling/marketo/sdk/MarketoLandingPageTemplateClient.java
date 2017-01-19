package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.landingpagetemplate.LandingPageTemplate;


public interface MarketoLandingPageTemplateClient {
    LandingPageTemplate getLandingPageTemplateById(int id, Status status) throws MarketoApiException;

    LandingPageTemplate getLandingPageTemplateById(int id) throws MarketoApiException;
}
