package com.smartling.marketo.sdk.domain.landingpagetemplate;

import com.smartling.marketo.sdk.domain.Asset;

public class LandingPageTemplate extends Asset {
    private TemplateType templateType;

    public TemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    @Override
    public String toString()
    {
        return "LandingPageTemplate{" + "templateType=" + templateType + "} " + super.toString();
    }
}
