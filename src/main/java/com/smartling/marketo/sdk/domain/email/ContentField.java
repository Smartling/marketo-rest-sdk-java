package com.smartling.marketo.sdk.domain.email;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "Text", value = TextField.class),
        @JsonSubTypes.Type(name = "DynamicContent", value = DynamicContentField.class)})
public abstract class ContentField {
    private String type;
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ContentField{type='" + type + "', value='" + value + "'}";
    }
}
