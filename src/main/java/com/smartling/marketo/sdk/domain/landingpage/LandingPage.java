package com.smartling.marketo.sdk.domain.landingpage;

import com.smartling.marketo.sdk.domain.Asset;

public class LandingPage extends Asset {
    private int template;
    private String styleOverRide;
    private boolean mobileEnabled;
    private String computedUrl;

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
}
