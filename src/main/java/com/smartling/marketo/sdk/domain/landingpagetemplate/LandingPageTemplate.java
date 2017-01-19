package com.smartling.marketo.sdk.domain.landingpagetemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartling.marketo.sdk.domain.Asset;

public class LandingPageTemplate extends Asset {
    private TemplateType templateType;

    public TemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    public enum TemplateType {
        @JsonProperty("guided")
        GUIDED,
        @JsonProperty("freeForm")
        FREEFORM
    }
}
