package com.smartling.marketo.sdk.domain.email;

public class EmailSnippetContentItem extends EmailContentItem {
    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "EmailSnippetContentItem{" + "value='" + value + '\'' + "} " + super.toString();
    }
}
