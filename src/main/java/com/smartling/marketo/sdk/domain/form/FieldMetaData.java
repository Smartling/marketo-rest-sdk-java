package com.smartling.marketo.sdk.domain.form;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

public class FieldMetaData {
    private List<Value> values;

    @JsonValue
    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public static class Value {
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
    }
}
