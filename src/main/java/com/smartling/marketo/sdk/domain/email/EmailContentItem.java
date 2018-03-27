package com.smartling.marketo.sdk.domain.email;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="contentType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(name="Text", value=EmailTextContentItem.class),
        @JsonSubTypes.Type(name="Snippet", value=EmailSnippetContentItem.class),
        @JsonSubTypes.Type(name="DynamicContent", value=EmailDynamicContentItem.class)})

public abstract class EmailContentItem {
    private String htmlId;
    private String contentType;

    public String getHtmlId() {
        return htmlId;
    }

    public void setHtmlId(String htmlId) {
        this.htmlId = htmlId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString()
    {
        return "EmailContentItem{" + "htmlId='" + htmlId + '\'' + ", contentType='" + contentType + '\'' + '}';
    }
}
