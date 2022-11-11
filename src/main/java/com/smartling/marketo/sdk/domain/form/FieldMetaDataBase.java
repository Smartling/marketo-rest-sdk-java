package com.smartling.marketo.sdk.domain.form;

import com.fasterxml.jackson.annotation.JsonValue;
import com.smartling.marketo.sdk.HasToBeMappedToJson;

import java.util.List;

public class FieldMetaDataBase<T> implements HasToBeMappedToJson {
    private List<T> values;

    @JsonValue
    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    @Override
    public String toString()
    {
        return "FieldMetaData{" + "values=" + values + '}';
    }
}
