package com.smartling.marketo.sdk.domain.form;

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
}
