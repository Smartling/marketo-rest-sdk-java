package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.landingpage.DynamicContent;
import com.smartling.marketo.sdk.domain.landingpage.DynamicContentItem;
import com.smartling.marketo.sdk.domain.landingpage.LandingPage;
import com.smartling.marketo.sdk.domain.landingpage.LandingPageContentItem;
import com.smartling.marketo.sdk.domain.landingpage.LandingPageFullContent;
import com.smartling.marketo.sdk.domain.landingpage.LandingPageTextContentItem;
import com.smartling.marketo.sdk.domain.landingpage.LandingPageVariable;

import java.util.List;
import java.util.Map;

public interface MarketoLandingPageClient {
    List<LandingPage> getLandingPages(Integer offset, Integer limit, FolderId folder, Status status) throws MarketoApiException;

    LandingPage getLandingPageById(int id, Status status) throws MarketoApiException;

    LandingPage getLandingPageById(int id) throws MarketoApiException;

    List<LandingPage> getLandingPagesByName(String name, FolderId folder, Status status) throws MarketoApiException;

    List<LandingPage> getLandingPagesByName(Integer offset, Integer limit, String name, FolderId folder, Status status) throws MarketoApiException;

    LandingPage cloneLandingPage(int sourceId, String newName, FolderId folder, int templateId) throws MarketoApiException;

    LandingPage cloneLandingPage(LandingPage sourceLandingPage, String newName) throws MarketoApiException;

    List<LandingPageContentItem> getLandingPageContent(int id) throws MarketoApiException;

    List<LandingPageContentItem> getLandingPageContent(int id, Status status) throws MarketoApiException;

    void updateLandingPageContent(int id, List<LandingPageTextContentItem> contentItems) throws MarketoApiException;

    LandingPageFullContent getLandingPageFullContent(int id) throws MarketoApiException;

    void discardLandingPageDraft(int id) throws MarketoApiException;

    void updateLandingPageMetadata(int id, String title) throws MarketoApiException;

    List<LandingPageVariable> getLandingPageVariables(int id) throws MarketoApiException;

    void updateLandingPageMetadata(int id, Map<String, String> tagMap) throws MarketoApiException;

    List<LandingPageVariable> getLandingPageVariables(int id, Status status) throws MarketoApiException;

    LandingPageVariable updateLandingPageVariable(int pageId, LandingPageVariable variable) throws MarketoApiException;

    void approveDraft(int id) throws MarketoApiException;

    void unapprove(int id) throws MarketoApiException;

    LandingPage createLandingPage(String name, FolderId folder, Integer template) throws MarketoApiException;

    DynamicContent loadDynamicContentById(int landingPageId, String dynamicContentId) throws MarketoApiException;

    void updateDynamicContent(int landingPageId, String dynamicContentId, List<DynamicContentItem> dynamicContentItems)  throws MarketoApiException;

    void delete(int id) throws MarketoApiException;
}
