package com.smartling.marketo.sdk.domain.landingpage;

public class LandingPageTextContentItem extends LandingPageContentItem {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString()
    {
        return "LandingPageTextContentItem{" + "content='" + content + '\'' + "} " + super.toString();
    }
}
