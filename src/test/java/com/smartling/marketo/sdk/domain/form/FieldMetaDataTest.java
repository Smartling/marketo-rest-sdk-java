package com.smartling.marketo.sdk.domain.form;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.HasToBeMappedToJson;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.rest.command.form.UpdateFormFieldBase;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

public class FieldMetaDataTest
{
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void metadataShouldBePostProcessable() throws Exception {
        SelectFieldMetaData fieldMetaData = new SelectFieldMetaData();
        List<SelectItemValue> values = new ArrayList<>();
        values.add(getValue("label1", "value1", true, true));
        values.add(getValue("label2", "value2", false, false));
        values.add(getValue("label3", "value3", false, false));
        fieldMetaData.setValues(values);

        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put("values", fieldMetaData);

        Map<String, Object> params = processParameters(builder.build(), false);

        assertThat(params.get("values")).isEqualTo("[{\"label\":\"label1\",\"value\":\"value1\",\"selected\":true,\"isDefault\":true},{\"label\":\"label2\",\"value\":\"value2\",\"selected\":false,\"isDefault\":false},{\"label\":\"label3\",\"value\":\"value3\",\"selected\":false,\"isDefault\":false}]");
    }

    private SelectItemValue getValue(String label, String value, boolean isDefault, boolean selected) {
        SelectItemValue val = new SelectItemValue();
        val.setLabel(label);
        val.setValue(value);
        val.setDefault(isDefault);
        val.setSelected(selected);
        return val;
    }

    private Map<String, Object> processParameters(Map<String, Object> parameters, boolean needUrlEncode) throws MarketoApiException
    {

        try {
            Map<String, Object> processedParameters = new HashMap<>(parameters);
            for (Map.Entry<String, Object> entry : processedParameters.entrySet()) {
                Object value = entry.getValue();
                Object newValue = value;
                if (value instanceof HasToBeMappedToJson) {
                    newValue = OBJECT_MAPPER.writeValueAsString(newValue);
                    if (needUrlEncode) {
                        newValue = URLEncoder.encode((String) newValue, "UTF-8");
                    }
                }
                entry.setValue(newValue);
            }
            return processedParameters;
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            throw new MarketoApiException(e.getMessage());
        }
    }
}
