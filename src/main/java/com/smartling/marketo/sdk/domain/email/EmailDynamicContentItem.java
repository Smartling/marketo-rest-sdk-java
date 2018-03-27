package com.smartling.marketo.sdk.domain.email;

public class EmailDynamicContentItem extends EmailContentItem {
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
        return "EmailDynamicContentItem{" + "value='" + value + '\'' + "} " + super.toString();
    }
}
