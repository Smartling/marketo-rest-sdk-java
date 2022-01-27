package com.smartling.marketo.sdk.domain.landingpage;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="type", visible=true, defaultImpl=LandingPageExternalContentItem.class)
@JsonSubTypes({
        @JsonSubTypes.Type(name="RichText", value=LandingPageTextContentItem.class),
        @JsonSubTypes.Type(name="HTML",     value=LandingPageTextContentItem.class),
        @JsonSubTypes.Type(name="Form",     value=LandingPageFormContentItem.class)})

public abstract class LandingPageContentItem {
    private String id;
    private String type;
    private int index;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString()
    {
        return "LandingPageContentItem{" + "id='" + id + '\'' + ", type='" + type + '\'' + ", index=" + index + '}';
    }
}
