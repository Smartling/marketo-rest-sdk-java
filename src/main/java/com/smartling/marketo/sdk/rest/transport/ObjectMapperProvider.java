package com.smartling.marketo.sdk.rest.transport;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.smartling.marketo.sdk.domain.landingpage.LandingPageContentItem;
import com.smartling.marketo.sdk.domain.landingpage.LandingPageContentItemDeserializer;

import jakarta.ws.rs.ext.ContextResolver;
import java.text.SimpleDateFormat;

public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
    @Override
    public ObjectMapper getContext(Class<?> type) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'Z"));

        objectMapper.registerModule(new SimpleModule()
                .addDeserializer(Void.TYPE, new VoidDeserializer())
                .addDeserializer(LandingPageContentItem.class, new LandingPageContentItemDeserializer()));

        return objectMapper;
    }

    private static final class VoidDeserializer extends JsonDeserializer<Void> {
        @Override
        public Void deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
            return null;
        }
    }
}
