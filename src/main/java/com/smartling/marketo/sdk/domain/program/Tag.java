package com.smartling.marketo.sdk.domain.program;

public class Tag {
    private String tagType;
    private String tagValue;

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    @Override
    public String toString()
    {
        return "Tag{" + "tagType='" + tagType + '\'' + ", tagValue='" + tagValue + '\'' + '}';
    }
}
