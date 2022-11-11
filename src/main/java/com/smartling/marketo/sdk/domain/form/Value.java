package com.smartling.marketo.sdk.domain.form;

public class Value {
    private String label;
    private String value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "Value{" + "label='" + label + '\'' + ", value='" + value + '\'' + '}';
    }
}
