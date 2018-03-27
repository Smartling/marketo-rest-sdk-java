package com.smartling.marketo.sdk.domain.landingpagetemplate;

import com.smartling.marketo.sdk.domain.Asset;

public class LandingPageTemplateContent {
    private int id;
    private Asset.Status status;
    private String content;
    private TemplateType templateType;
    private Boolean enableMunchkin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Asset.Status getStatus() {
        return status;
    }

    public void setStatus(Asset.Status status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    public Boolean getEnableMunchkin() {
        return enableMunchkin;
    }

    public void setEnableMunchkin(Boolean enableMunchkin) {
        this.enableMunchkin = enableMunchkin;
    }

    @Override
    public String toString()
    {
        return "LandingPageTemplateContent{" + "id=" + id + ", status=" + status + ", content='" + content + '\'' + ", templateType="
                + templateType + ", enableMunchkin=" + enableMunchkin + '}';
    }
}
