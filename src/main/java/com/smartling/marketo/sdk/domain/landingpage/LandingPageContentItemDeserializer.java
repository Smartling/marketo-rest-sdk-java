package com.smartling.marketo.sdk.domain.landingpage;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LandingPageContentItemDeserializer extends StdDeserializer<LandingPageContentItem> {

    private static final String TYPE_PROPERTY = "type";
    private static final String CONTENT_PROPERTY = "content";
    private static final String CONTENT_SUB_TYPE_PROPERTY = "contentType";
    private static final Class<? extends LandingPageContentItem> DEFAULT_TYPE = LandingPageExternalContentItem.class;

    private static final Map<String, Class<? extends LandingPageContentItem>> TYPE_TO_CLASS = new HashMap<>();
    private static final Map<String, Class<? extends LandingPageContentItem>> SUB_TYPE_TO_CLASS = new HashMap<>();

    static {
        TYPE_TO_CLASS.put("RichText", LandingPageTextContentItem.class);
        TYPE_TO_CLASS.put("HTML", LandingPageTextContentItem.class);
        TYPE_TO_CLASS.put("Form", LandingPageFormContentItem.class);

        SUB_TYPE_TO_CLASS.put("DynamicContent", LandingPageDynamicContentItem.class);
    }

    private final ObjectMapper om = new ObjectMapper();

    public LandingPageContentItemDeserializer() {
        this(null);
    }

    public LandingPageContentItemDeserializer(Class<?> clazz) {
        super(clazz);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        om.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        om.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    @Override
    public LandingPageContentItem deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        Class<? extends LandingPageContentItem> type = getType(node);
        Class<? extends LandingPageContentItem> subType = getSubType(node);
        Class<? extends LandingPageContentItem> finalType = subType == null ? type : subType;

        if (finalType == null) {
            finalType = DEFAULT_TYPE;
        }

        return om.treeToValue(node, finalType);
    }

    private Class<? extends LandingPageContentItem> getType(JsonNode node) {
        JsonNode typeProperty = node.get(TYPE_PROPERTY);
        return typeProperty == null ? null : TYPE_TO_CLASS.get(typeProperty.asText());
    }

    private Class<? extends LandingPageContentItem> getSubType(JsonNode node) {
        JsonNode contentProperty = node.get(CONTENT_PROPERTY);
        if (contentProperty == null) {
            return null;
        }
        JsonNode subTypeProperty = contentProperty.get(CONTENT_SUB_TYPE_PROPERTY);
        return subTypeProperty == null ? null : SUB_TYPE_TO_CLASS.get(subTypeProperty.asText());
    }
}
