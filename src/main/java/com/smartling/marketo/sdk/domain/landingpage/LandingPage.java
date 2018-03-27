package com.smartling.marketo.sdk.domain.landingpage;

import com.smartling.marketo.sdk.domain.Asset;

public class LandingPage extends Asset {
    private int template;
    private String styleOverRide;
    private boolean mobileEnabled;
    private String computedUrl;
    private String title;
    private String keywords;
    private String robots;
    private String facebookOgTags;
    private String customHeadHTML;
    private boolean formPrefill;


    public int getTemplate() {
        return template;
    }

    public void setTemplate(int template) {
        this.template = template;
    }

    public String getStyleOverRide() {
        return styleOverRide;
    }

    public void setStyleOverRide(String styleOverRide) {
        this.styleOverRide = styleOverRide;
    }

    public boolean isMobileEnabled() {
        return mobileEnabled;
    }

    public void setMobileEnabled(boolean mobileEnabled) {
        this.mobileEnabled = mobileEnabled;
    }

    public String getComputedUrl() {
        return computedUrl;
    }

    public void setComputedUrl(String computedUrl) {
        this.computedUrl = computedUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getRobots() {
        return robots;
    }

    public void setRobots(String robots) {
        this.robots = robots;
    }

    public String getFacebookOgTags() {
        return facebookOgTags;
    }

    public void setFacebookOgTags(String facebookOgTags) {
        this.facebookOgTags = facebookOgTags;
    }

    public String getCustomHeadHTML() {
        return customHeadHTML;
    }

    public void setCustomHeadHTML(String customHeadHTML) {
        this.customHeadHTML = customHeadHTML;
    }

    public boolean isFormPrefill() {
        return formPrefill;
    }

    public void setFormPrefill(boolean formPrefill) {
        this.formPrefill = formPrefill;
    }

    @Override
    public String toString()
    {
        return "LandingPage{" + "template=" + template + ", styleOverRide='" + styleOverRide + '\'' + ", mobileEnabled=" + mobileEnabled
                + ", computedUrl='" + computedUrl + '\'' + ", title='" + title + '\'' + ", keywords='" + keywords + '\'' + ", robots='"
                + robots + '\'' + ", facebookOgTags='" + facebookOgTags + '\'' + ", customHeadHTML='" + customHeadHTML + '\''
                + ", formPrefill=" + formPrefill + "} " + super.toString();
    }
}
