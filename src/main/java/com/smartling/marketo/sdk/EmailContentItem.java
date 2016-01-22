package com.smartling.marketo.sdk;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="contentType")
@JsonSubTypes({
        @JsonSubTypes.Type(name="Text", value=EmailTextContentItem.class),
        @JsonSubTypes.Type(name="Snippet", value=EmailSnippetContentItem.class)})

public abstract class EmailContentItem {
    private String htmlId;
    private ContentType contentType;

    public String getHtmlId() {
        return htmlId;
    }

    public void setHtmlId(String htmlId) {
        this.htmlId = htmlId;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public enum ContentType {
        TEXT("Text"),
        SNIPPET("Snippet"),
        DYNAMIC_CONTENT("DynamicContent");

        private String name;

        ContentType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
