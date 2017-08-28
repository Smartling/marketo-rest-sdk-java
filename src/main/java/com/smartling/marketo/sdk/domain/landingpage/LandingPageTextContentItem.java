package com.smartling.marketo.sdk.domain.landingpage;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class LandingPageTextContentItem extends LandingPageContentItem {
    @JsonDeserialize(using = ContentDeserializer.class)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
