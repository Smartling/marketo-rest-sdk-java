package com.smartling.marketo.sdk.domain.emailtemplate;

import com.smartling.marketo.sdk.domain.Asset;
import com.smartling.marketo.sdk.domain.landingpagetemplate.TemplateType;

public class EmailTemplateContent {
    private int id;
    private Asset.Status status;
    private String content;

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
}
