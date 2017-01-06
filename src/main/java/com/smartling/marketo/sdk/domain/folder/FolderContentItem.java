package com.smartling.marketo.sdk.domain.folder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FolderContentItem {
    private Integer id;
    private Type type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        @JsonProperty("Email")
        EMAIL,
        @JsonProperty("Email Template")
        EMAIL_TEMPLATE,
        @JsonProperty("Form")
        FORM,
        @JsonProperty("Landing Page")
        LANDING_PAGE,
        @JsonProperty("Landing Page Template")
        LANDING_PAGE_TEMPLATE,
        @JsonProperty("Snippet")
        SNIPPET,
        @JsonProperty("File")
        FILE,
        @JsonProperty("Program")
        PROGRAM,
        @JsonProperty("Campaign")
        CAMPAIGN,
        @JsonProperty("Folder")
        FOLDER,
        @JsonProperty("List")
        LIST,
        @JsonProperty("Mobile Push Notification")
        MOBILE_PUSH_NOTIFICATION,
        @JsonProperty("Report")
        REPORT,
        @JsonProperty("Social App")
        SOCIAL_APP,
        UNKNOWN;
    }
}
