package com.smartling.marketo.sdk.domain.email;

public class DynamicContentField extends ContentField {

    public DynamicContentField() {
        setType("DynamicContent");
    }

    @Override
    public String toString() {
        return "DynamicContentField{type='" + getType() + "', value='" + getValue() + "'}";
    }
}
