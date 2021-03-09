package com.smartling.marketo.sdk.domain.form;

public enum FormFieldType {
    STRING("string"),
    TEXTAREA("textArea"),
    TEXT("text"),
    PICKLIST("picklist"),
    HTMLTEXT("htmltext"),
    CHECKBOX("checkbox"),
    INT("int"),
    RADIO("radio"),
    SELECT("select"),
    EMAIL("email"),
    DATE("date"),
    NUMBER("number"),
    DOUBLE("double"),
    PHONE("phone"),
    URL("url"),
    CURRENCY("currency"),
    SINGLE_CHECKBOX("single_checkbox"),
    RANGE("range"),
    PROFILING("profiling"),
    HIDDEN("hidden"),
    FIELDSET("fieldset");

    private final String code;

    FormFieldType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
