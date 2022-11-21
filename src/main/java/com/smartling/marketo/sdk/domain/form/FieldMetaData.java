package com.smartling.marketo.sdk.domain.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.smartling.marketo.sdk.HasToBeMappedToJson;

import java.util.List;

public class FieldMetaData implements HasToBeMappedToJson {
    private List<Value> values;

    @JsonValue
    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    @Override
    public String toString()
    {
        return "FieldMetaData{" + "values=" + values + '}';
    }

    public static class Value {
        private String label;
        private String value;
        private boolean isDefault;
        private boolean selected;

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

        @JsonProperty("isDefault")
        public boolean isDefault() {
            return isDefault;
        }

        public void setDefault(boolean isDefault) {
            this.isDefault = isDefault;
        }

        @JsonProperty("selected")
        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        @Override
        public String toString()
        {
            return "Value{" + "label='" + this.getLabel() + '\'' + ", value='" + this.getValue() + '\'' +
                    ", isDefault=" + isDefault + ", selected=" + selected + '}';
        }
    }
}
