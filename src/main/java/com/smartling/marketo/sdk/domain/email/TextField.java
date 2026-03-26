package com.smartling.marketo.sdk.domain.email;

public class TextField extends ContentField {

    public TextField() {
        setType("Text");
    }

    @Override
    public String toString() {
        return "TextField{type='" + getType() + "', value='" + getValue() + "'}";
    }
}
